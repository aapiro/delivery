import { useQuery, UseQueryResult } from '@tanstack/react-query';
import { api } from './api';
import { API_ENDPOINTS } from '../constants';
import { QUERY_KEYS } from './queryClient';
import { Restaurant, Dish, RestaurantFilters, PaginatedResponse } from '../types';
import { isMockEnabled } from '../mocks/config';
import {
    mockGetRestaurants,
    mockGetRestaurant,
    mockGetRestaurantDishes,
    mockSearchRestaurants,
    mockGetDish,
    mockGetDishCategories,
    mockSearchDishes,
} from '../mocks/restaurantMockHandlers';
import { unwrapEntity, unwrapList, unwrapPagedResponse } from '../utils/apiPayload';
import { mapDishFromApi, mapRestaurantFromApi } from '../utils/restaurantMappers';

export const restaurantsService = {
    async getRestaurants(filters?: RestaurantFilters, page = 1, limit = 12): Promise<PaginatedResponse<Restaurant>> {
        if (isMockEnabled()) {
            return mockGetRestaurants(filters, page, limit);
        }

        const queryPage = Math.max(page - 1, 0);
        const params = new URLSearchParams({
            page: queryPage.toString(),
            size: limit.toString(),
        });

        const raw = await api.get<unknown>(`${API_ENDPOINTS.RESTAURANTS.LIST}?${params}`);
        const { content, totalElements, totalPages } = unwrapPagedResponse(raw);

        const mapped = content.map((item) => mapRestaurantFromApi(item as Record<string, unknown>)) as Restaurant[];

        const filtered = mapped.filter((r) => {
            if (filters?.search && !r.name.toLowerCase().includes(filters.search.toLowerCase())) return false;
            if (filters?.isOpen !== undefined && r.isOpen !== filters.isOpen) return false;
            return true;
        });

        const total = Number(totalElements || filtered.length);
        const pages = Number(totalPages || 1);

        return {
            data: filtered,
            pagination: {
                page,
                limit,
                total,
                totalPages: pages,
                hasNext: page < pages,
                hasPrev: page > 1,
            },
        };
    },

    async getRestaurant(id: number): Promise<Restaurant> {
        if (isMockEnabled()) {
            return mockGetRestaurant(id);
        }

        const raw = await api.get<unknown>(API_ENDPOINTS.RESTAURANTS.DETAIL(id));
        const entity = unwrapEntity<Record<string, unknown>>(raw);
        return mapRestaurantFromApi(entity);
    },

    async getRestaurantDishes(id: number): Promise<Dish[]> {
        if (isMockEnabled()) {
            return mockGetRestaurantDishes(id);
        }

        const raw = await api.get<unknown>(API_ENDPOINTS.DISHES.LIST);
        const all = unwrapList<Record<string, unknown>>(raw);
        return all
            .map((row) => mapDishFromApi(row, id))
            .filter((dish) => Number(dish.restaurantId) === id);
    },

    async searchRestaurants(query: string, filters?: RestaurantFilters): Promise<Restaurant[]> {
        if (isMockEnabled()) {
            return mockSearchRestaurants(query, filters);
        }

        const params = new URLSearchParams({ name: query });
        const raw = await api.get<unknown>(`${API_ENDPOINTS.RESTAURANTS.SEARCH}?${params}`);
        const list = unwrapList<Record<string, unknown>>(raw);
        return list
            .map((row) => mapRestaurantFromApi(row))
            .filter((r: Restaurant) => {
                if (filters?.isOpen !== undefined && r.isOpen !== filters.isOpen) return false;
                return true;
            });
    },

    async getDish(id: number): Promise<Dish> {
        if (isMockEnabled()) {
            return mockGetDish(id);
        }

        const raw = await api.get<unknown>(API_ENDPOINTS.DISHES.DETAIL(id));
        const entity = unwrapEntity<Record<string, unknown>>(raw);
        return mapDishFromApi(entity, 0);
    },

    async getDishCategories(restaurantId: number) {
        if (isMockEnabled()) {
            return mockGetDishCategories(restaurantId);
        }

        const raw = await api.get<unknown>(API_ENDPOINTS.DISHES.CATEGORIES(restaurantId));
        return unwrapList(raw);
    },

    async searchDishes(query: string): Promise<Dish[]> {
        if (isMockEnabled()) {
            return mockSearchDishes(query);
        }

        const params = new URLSearchParams({ name: query });
        const raw = await api.get<unknown>(`${API_ENDPOINTS.DISHES.SEARCH}?${params}`);
        return unwrapList<Record<string, unknown>>(raw).map((row) => mapDishFromApi(row, 0));
    },
};

// ============= REACT QUERY HOOKS =============

export const useRestaurants = (
    filters?: RestaurantFilters,
    page = 1,
    limit = 12
): UseQueryResult<PaginatedResponse<Restaurant>, Error> => {
    return useQuery({
        queryKey: [...QUERY_KEYS.RESTAURANTS, filters, page, limit],
        queryFn: () => restaurantsService.getRestaurants(filters, page, limit),
        staleTime: 5 * 60 * 1000,
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
        staleTime: 2 * 60 * 1000,
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
