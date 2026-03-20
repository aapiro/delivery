import { api } from './api';
import { API_ENDPOINTS } from '../constants';
import { Restaurant, Dish, RestaurantFilters, PaginatedResponse } from '../types';

export const restaurantsService = {
    // ============= RESTAURANTS =============

    async getRestaurants(filters?: RestaurantFilters, page = 1, limit = 12): Promise<PaginatedResponse<Restaurant>> {
        const queryPage = Math.max(page - 1, 0);
        const params = new URLSearchParams({
            page: queryPage.toString(),
            size: limit.toString(),
        });

        const response = await api.get<any>(`${API_ENDPOINTS.RESTAURANTS.LIST}?${params}`);
        const payload = response?.data ?? {};
        const content: any[] = Array.isArray(payload.content) ? payload.content : [];

        const mapped = content.map((raw: any) => ({
            id: Number(raw.id),
            name: raw.name ?? '',
            description: raw.description ?? '',
            address: raw.address ?? '',
            phone: raw.phone ?? '',
            email: raw.email ?? '',
            rating: Number(raw.rating ?? 0),
            reviewCount: Number(raw.reviewCount ?? 0),
            deliveryTime: `${raw.deliveryTimeMin ?? 30}-${raw.deliveryTimeMax ?? 45} min`,
            deliveryFee: Number(raw.deliveryFee ?? 0),
            minimumOrder: Number(raw.minimumOrder ?? 0),
            deliveryTimeMin: Number(raw.deliveryTimeMin ?? 30),
            deliveryTimeMax: Number(raw.deliveryTimeMax ?? 45),
            isActive: Boolean(raw.isOpen ?? true),
            isOpen: Boolean(raw.isOpen ?? true),
            open: Boolean(raw.isOpen ?? true),
            imageUrl: raw.imageUrl ?? '',
            coverImage: raw.imageUrl ?? '',
            cuisine: raw.cuisine ?? '',
            createdAt: raw.createdAt ?? '',
            updatedAt: raw.updatedAt ?? '',
        })) as Restaurant[];

        const filtered = mapped.filter((r) => {
            if (filters?.search && !r.name.toLowerCase().includes(filters.search.toLowerCase())) return false;
            if (filters?.isOpen !== undefined && r.isOpen !== filters.isOpen) return false;
            return true;
        });

        const total = Number(payload.totalElements ?? filtered.length);
        const totalPages = Number(payload.totalPages ?? 1);

        return {
            data: filtered,
            pagination: {
                page,
                limit,
                total,
                totalPages,
                hasNext: page < totalPages,
                hasPrev: page > 1,
            },
        };
    },

    async getRestaurant(id: number): Promise<Restaurant> {
        const response = await api.get<any>(API_ENDPOINTS.RESTAURANTS.DETAIL(id));
        const raw = response?.data ?? {};
        return {
            id: Number(raw.id),
            name: raw.name ?? '',
            description: raw.description ?? '',
            address: raw.address ?? '',
            phone: raw.phone ?? '',
            email: raw.email ?? '',
            rating: Number(raw.rating ?? 0),
            reviewCount: Number(raw.reviewCount ?? 0),
            deliveryTime: `${raw.deliveryTimeMin ?? 30}-${raw.deliveryTimeMax ?? 45} min`,
            deliveryFee: Number(raw.deliveryFee ?? 0),
            minimumOrder: Number(raw.minimumOrder ?? 0),
            deliveryTimeMin: Number(raw.deliveryTimeMin ?? 30),
            deliveryTimeMax: Number(raw.deliveryTimeMax ?? 45),
            isActive: Boolean(raw.isOpen ?? true),
            isOpen: Boolean(raw.isOpen ?? true),
            open: Boolean(raw.isOpen ?? true),
            imageUrl: raw.imageUrl ?? '',
            coverImage: raw.imageUrl ?? '',
            cuisine: raw.cuisine ?? '',
            createdAt: raw.createdAt ?? '',
            updatedAt: raw.updatedAt ?? '',
        };
    },

    async getRestaurantDishes(id: number): Promise<Dish[]> {
        // Quarkus no expone /restaurants/{id}/dishes; usamos /dishes y filtramos por restaurante.
        const response = await api.get<any[]>(API_ENDPOINTS.DISHES.LIST);
        const all = Array.isArray(response?.data) ? response.data : [];
        return all.filter((dish: any) => {
            const restaurantId = dish?.restaurantId ?? dish?.restaurant?.id;
            return Number(restaurantId) === id;
        });
    },

    async searchRestaurants(query: string, filters?: RestaurantFilters): Promise<Restaurant[]> {
        const params = new URLSearchParams({
            name: query,
        });

        const response = await api.get<any[]>(`${API_ENDPOINTS.RESTAURANTS.SEARCH}?${params}`);
        const list = Array.isArray(response?.data) ? response.data : [];
        return list
            .map((raw: any) => ({
                id: Number(raw.id),
                name: raw.name ?? '',
                description: raw.description ?? '',
                address: raw.address ?? '',
                phone: raw.phone ?? '',
                email: raw.email ?? '',
                rating: Number(raw.rating ?? 0),
                reviewCount: Number(raw.reviewCount ?? 0),
                deliveryTime: `${raw.deliveryTimeMin ?? 30}-${raw.deliveryTimeMax ?? 45} min`,
                deliveryFee: Number(raw.deliveryFee ?? 0),
                minimumOrder: Number(raw.minimumOrder ?? 0),
                deliveryTimeMin: Number(raw.deliveryTimeMin ?? 30),
                deliveryTimeMax: Number(raw.deliveryTimeMax ?? 45),
                isActive: Boolean(raw.isOpen ?? true),
                isOpen: Boolean(raw.isOpen ?? true),
                open: Boolean(raw.isOpen ?? true),
                imageUrl: raw.imageUrl ?? '',
                coverImage: raw.imageUrl ?? '',
                cuisine: raw.cuisine ?? '',
                createdAt: raw.createdAt ?? '',
                updatedAt: raw.updatedAt ?? '',
            }))
            .filter((r: Restaurant) => {
                if (filters?.isOpen !== undefined && r.isOpen !== filters.isOpen) return false;
                return true;
            });
    },

    // ============= DISHES =============

    async getDish(id: number): Promise<Dish> {
        const response = await api.get<Dish>(API_ENDPOINTS.DISHES.DETAIL(id));
        return response.data;
    },

    async getDishCategories(restaurantId: number) {
        const response = await api.get(API_ENDPOINTS.DISHES.CATEGORIES(restaurantId));
        return response.data;
    },

    async searchDishes(query: string): Promise<Dish[]> {
        const params = new URLSearchParams({ name: query });
        const response = await api.get<Dish[]>(`${API_ENDPOINTS.DISHES.SEARCH}?${params}`);
        return response.data;
    },
};

// ============= REACT QUERY HOOKS =============

import { useQuery, UseQueryResult } from '@tanstack/react-query';
import { QUERY_KEYS } from './queryClient';

export const useRestaurants = (
    filters?: RestaurantFilters,
    page = 1,
    limit = 12
): UseQueryResult<PaginatedResponse<Restaurant>, Error> => {
    return useQuery({
        queryKey: [...QUERY_KEYS.RESTAURANTS, filters, page, limit],
        queryFn: () => restaurantsService.getRestaurants(filters, page, limit),
        staleTime: 5 * 60 * 1000, // 5 minutos
    });
};

export const useRestaurant = (id: number): UseQueryResult<Restaurant, Error> => {
    return useQuery({
        queryKey: QUERY_KEYS.RESTAURANT(id),
        queryFn: () => restaurantsService.getRestaurant(id),
        enabled: !!id && id > 0,
        staleTime: 5 * 60 * 1000,
    });
};

export const useRestaurantDishes = (restaurantId: number): UseQueryResult<Dish[], Error> => {
    return useQuery({
        queryKey: QUERY_KEYS.RESTAURANT_DISHES(restaurantId),
        queryFn: () => restaurantsService.getRestaurantDishes(restaurantId),
        enabled: !!restaurantId && restaurantId > 0,
        staleTime: 2 * 60 * 1000, // 2 minutos para platos
    });
};

export const useDish = (id: number): UseQueryResult<Dish, Error> => {
    return useQuery({
        queryKey: QUERY_KEYS.DISH(id),
        queryFn: () => restaurantsService.getDish(id),
        enabled: !!id && id > 0,
        staleTime: 5 * 60 * 1000,
    });
};

export const useSearchRestaurants = (
    query: string,
    filters?: RestaurantFilters
): UseQueryResult<Restaurant[], Error> => {
    return useQuery({
        queryKey: QUERY_KEYS.SEARCH_RESTAURANTS(query, filters),
        queryFn: () => restaurantsService.searchRestaurants(query, filters),
        enabled: !!query && query.trim().length > 0,
        staleTime: 2 * 60 * 1000,
    });
};