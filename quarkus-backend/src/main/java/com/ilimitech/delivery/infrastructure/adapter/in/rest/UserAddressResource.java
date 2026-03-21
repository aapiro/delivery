package com.ilimitech.delivery.infrastructure.adapter.in.rest;

import com.ilimitech.delivery.infrastructure.adapter.out.persistence.Address;
import com.ilimitech.delivery.application.port.out.AddressRepository;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Path("/users/addresses")
@Authenticated
public class UserAddressResource {

    /**
     * Get user addresses
     * GET /users/addresses
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserAddresses() {
        Long userId = currentUserId();
        List<Address> addresses = addressRepository.findByUserId(userId);
        return Response.ok(Map.of(
                "message", "User addresses retrieved successfully",
                "addresses", addresses
        )).build();
    }

    /**
     * Add new address for user
     * POST /users/addresses
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response addAddress(Address address) {
        if (address == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Address body is required"))
                    .build();
        }

        Long userId = currentUserId();
        address.setUserId(userId);

        // Ensure exactly one default address per user
        if (Boolean.TRUE.equals(address.getIsDefault())) {
            unsetDefaultsForUser(userId);
        } else if (addressRepository.findDefaultByUserId(userId) == null) {
            address.setIsDefault(true);
        }

        addressRepository.persist(address);

        return Response.ok(Map.of(
                "message", "Address added successfully",
                "address", address
        )).build();
    }

    /**
     * Update existing address
     * PUT /users/addresses/{id}
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateAddress(@PathParam("id") Long id, Address updatedAddress) {
        if (updatedAddress == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Updated address body is required"))
                    .build();
        }

        Long userId = currentUserId();
        Address existing = addressRepository.findByIdOptional(id)
                .orElse(null);

        if (existing == null || existing.getUserId() == null || !existing.getUserId().equals(userId)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Address not found"))
                    .build();
        }

        existing.setStreet(updatedAddress.getStreet());
        existing.setCity(updatedAddress.getCity());
        existing.setState(updatedAddress.getState());
        existing.setZipCode(updatedAddress.getZipCode());
        existing.setCountry(updatedAddress.getCountry());
        existing.setInstructions(updatedAddress.getInstructions());
        existing.setAddressType(updatedAddress.getAddressType());

        boolean willBeDefault = Boolean.TRUE.equals(updatedAddress.getIsDefault());
        existing.setIsDefault(willBeDefault);
        if (willBeDefault) {
            unsetDefaultsForUser(userId);
            existing.setIsDefault(true);
        }

        return Response.ok(Map.of(
                "message", "Address updated successfully",
                "address", existing
        )).build();
    }

    /**
     * Delete address
     * DELETE /users/addresses/{id}
     */
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteAddress(@PathParam("id") Long id) {
        Long userId = currentUserId();

        Address existing = addressRepository.findByIdOptional(id)
                .orElse(null);

        if (existing == null || existing.getUserId() == null || !existing.getUserId().equals(userId)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Address not found"))
                    .build();
        }

        boolean deletedWasDefault = Boolean.TRUE.equals(existing.getIsDefault());
        addressRepository.delete(existing);

        if (deletedWasDefault) {
            // Set first remaining address as default (if any)
            List<Address> remaining = addressRepository.findByUserId(userId);
            if (!remaining.isEmpty()) {
                Address first = remaining.get(0);
                first.setIsDefault(true);
                addressRepository.persist(first);
            }
        }

        return Response.ok(Map.of(
                "message", "Address deleted successfully",
                "addressId", id
        )).build();
    }

    /**
     * Set default address
     * PUT /users/addresses/{id}/default
     */
    @PUT
    @Path("/{id}/default")
    @Transactional
    public Response setDefaultAddress(@PathParam("id") Long id) {
        Long userId = currentUserId();

        Address existing = addressRepository.findByIdOptional(id)
                .orElse(null);

        if (existing == null || existing.getUserId() == null || !existing.getUserId().equals(userId)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Address not found"))
                    .build();
        }

        unsetDefaultsForUser(userId);
        existing.setIsDefault(true);
        addressRepository.persist(existing);

        return Response.ok(Map.of(
                "message", "Default address updated successfully",
                "addressId", id
        )).build();
    }

    @Inject
    AddressRepository addressRepository;

    @Inject
    SecurityIdentity securityIdentity;

    private Long currentUserId() {
        if (securityIdentity == null || securityIdentity.isAnonymous()) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
        try {
            return Long.parseLong(securityIdentity.getPrincipal().getName());
        } catch (NumberFormatException e) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }

    private void unsetDefaultsForUser(Long userId) {
        // Small batch update for "single default per user".
        List<Address> current = addressRepository.findByUserId(userId);
        for (Address a : current) {
            a.setIsDefault(false);
            addressRepository.persist(a);
        }
    }
}