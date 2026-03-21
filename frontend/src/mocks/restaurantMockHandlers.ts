import { Dish, Restaurant, RestaurantFilters, PaginatedResponse } from '../types';
import { MOCK_DISHES_BY_RESTAURANT, MOCK_RESTAURANTS } from './restaurantFixtureData';
import { delay } from './config';

function filterRestaurants(list: Restaurant[], filters?: RestaurantFilters): Restaurant[] {
    return list.filter((r) => {
        if (filters?.search && !r.name.toLowerCase().includes(filters.search.toLowerCase())) return false;
        if (filters?.isOpen !== undefined && r.isOpen !== filters.isOpen) return false;
        return true;
    });
}

export async function mockGetRestaurants(
    filters?: RestaurantFilters,
    page = 1,
    limit = 12
): Promise<PaginatedResponse<Restaurant>> {
    await delay();
    const filtered = filterRestaurants([...MOCK_RESTAURANTS], filters);
    const total = filtered.length;
    const totalPages = Math.max(1, Math.ceil(total / limit));
    const start = (page - 1) * limit;
    const data = filtered.slice(start, start + limit);
    return {
        data,
        pagination: {
            page,
            limit,
            total,
            totalPages,
            hasNext: page < totalPages,
            hasPrev: page > 1,
        },
    };
}

export async function mockGetRestaurant(id: number): Promise<Restaurant> {
    await delay();
    const r = MOCK_RESTAURANTS.find((x) => x.id === id);
    if (!r) throw new Error('Restaurante no encontrado');
    return { ...r };
}

export async function mockGetRestaurantDishes(restaurantId: number): Promise<Dish[]> {
    await delay();
    const list = MOCK_DISHES_BY_RESTAURANT[restaurantId];
    return list ? list.map((d) => ({ ...d })) : [];
}

export async function mockSearchRestaurants(query: string, filters?: RestaurantFilters): Promise<Restaurant[]> {
    await delay();
    const q = query.trim().toLowerCase();
    return filterRestaurants(
        MOCK_RESTAURANTS.filter(
            (r) =>
                r.name.toLowerCase().includes(q) ||
                (r.description && r.description.toLowerCase().includes(q)) ||
                (r.cuisine && r.cuisine.toLowerCase().includes(q))
        ),
        filters
    );
}

export async function mockGetDish(id: number): Promise<Dish> {
    await delay();
    for (const dishes of Object.values(MOCK_DISHES_BY_RESTAURANT)) {
        const d = dishes.find((x) => x.id === id);
        if (d) return { ...d };
    }
    throw new Error('Plato no encontrado');
}

export async function mockGetDishCategories(restaurantId: number): Promise<unknown> {
    await delay();
    const dishes = MOCK_DISHES_BY_RESTAURANT[restaurantId] ?? [];
    const map = new Map<number, { id: number; name: string; slug: string }>();
    dishes.forEach((d) => {
        map.set(d.category.id, { id: d.category.id, name: d.category.name, slug: d.category.slug });
    });
    return Array.from(map.values());
}

export async function mockSearchDishes(query: string): Promise<Dish[]> {
    await delay();
    const q = query.trim().toLowerCase();
    const out: Dish[] = [];
    for (const dishes of Object.values(MOCK_DISHES_BY_RESTAURANT)) {
        dishes.forEach((d) => {
            if (d.name.toLowerCase().includes(q)) out.push({ ...d });
        });
    }
    return out;
}
