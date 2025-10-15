import { vi, describe, it, expect } from 'vitest';
import { 
  getUserAddresses,
  addUserAddress,
  updateUserAddress,
  deleteUserAddress,
  setDefaultAddress
} from '../../services/userAddressService';

// Mock fetch globally
global.fetch = vi.fn();

describe('User Address API Service', () => {
  beforeEach(() => {
    vi.resetAllMocks();
  });

  it('should get user addresses successfully', async () => {
    const mockResponse = {
      message: 'Addresses retrieved successfully',
      addresses: [
        {
          id: 1,
          street: 'Calle Principal 123',
          city: 'Madrid',
          postalCode: '28001',
          isDefault: true
        }
      ]
    };
    
    (global.fetch as any).mockResolvedValueOnce({
      ok: true,
      json: async () => mockResponse
    });

    const result = await getUserAddresses();
    
    expect(result).toEqual(mockResponse);
    expect(fetch).toHaveBeenCalledWith('/api/users/addresses', {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' }
    });
  });

  it('should add new address successfully', async () => {
    const mockAddress = {
      street: 'Calle Secundaria 456',
      city: 'Barcelona',
      postalCode: '08001'
    };
    
    const mockResponse = {
      message: 'Address added successfully',
      address: {
        id: 2,
        ...mockAddress,
        isDefault: false
      }
    };

    (global.fetch as any).mockResolvedValueOnce({
      ok: true,
      json: async () => mockResponse
    });

    const result = await addUserAddress(mockAddress);
    
    expect(result).toEqual(mockResponse);
    expect(fetch).toHaveBeenCalledWith('/api/users/addresses', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(mockAddress)
    });
  });

  it('should update existing address successfully', async () => {
    const mockUpdate = {
      id: 1,
      street: 'Calle Principal Actualizada 123'
    };
    
    const mockResponse = {
      message: 'Address updated successfully',
      address: {
        ...mockUpdate,
        city: 'Madrid',
        postalCode: '28001',
        isDefault: true
      }
    };

    (global.fetch as any).mockResolvedValueOnce({
      ok: true,
      json: async () => mockResponse
    });

    const result = await updateUserAddress(mockUpdate);
    
    expect(result).toEqual(mockResponse);
    expect(fetch).toHaveBeenCalledWith('/api/users/addresses/1', {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(mockUpdate)
    });
  });

  it('should delete address successfully', async () => {
    const mockAddressId = 1;
    
    const mockResponse = {
      message: 'Address deleted successfully'
    };

    (global.fetch as any).mockResolvedValueOnce({
      ok: true,
      json: async () => mockResponse
    });

    const result = await deleteUserAddress(mockAddressId);
    
    expect(result).toEqual(mockResponse);
    expect(fetch).toHaveBeenCalledWith('/api/users/addresses/1', {
      method: 'DELETE',
      headers: { 'Content-Type': 'application/json' }
    });
  });

  it('should set default address successfully', async () => {
    const mockAddressId = 2;
    
    const mockResponse = {
      message: 'Default address updated successfully'
    };

    (global.fetch as any).mockResolvedValueOnce({
      ok: true,
      json: async () => mockResponse
    });

    const result = await setDefaultAddress(mockAddressId);
    
    expect(result).toEqual(mockResponse);
    expect(fetch).toHaveBeenCalledWith('/api/users/addresses/2/default', {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' }
    });
  });

  it('should handle user address API error', async () => {
    (global.fetch as any).mockResolvedValueOnce({
      ok: false,
      status: 500,
      json: async () => ({ message: 'Internal server error' })
    });

    await expect(getUserAddresses()).rejects.toThrow('Failed to fetch user addresses');
  });
});