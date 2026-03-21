import { adminHttp, pickData } from './adminHttp';
import { API_ENDPOINTS } from '../constants';
import {
    Admin,
    AdminLoginForm,
    AdminLoginResponse,
    DashboardStats,
    AdminRestaurantFilters,
    AdminOrderFilters,
    AdminUserFilters,
    Restaurant,
    Order,
    User,
    RestaurantForm,
    AdminDishWritePayload,
    CategoryForm,
    SalesReport,
    UserReport,
    PaginatedResponse,
    Dish,
    RestaurantCategory,
    DishCategory
} from '../types';

// --- Normalización respuestas Quarkus (listas planas) → contratos del admin SPA ---

function paginateLocally<T>(items: T[], page: number, limit: number): PaginatedResponse<T> {
    const total = items.length;
    const totalPages = Math.max(1, Math.ceil(total / limit) || 1);
    const safePage = Math.min(Math.max(1, page), totalPages);
    const start = (safePage - 1) * limit;
    const data = items.slice(start, start + limit);
    return {
        data,
        pagination: {
            page: safePage,
            limit,
            total,
            totalPages,
            hasNext: safePage < totalPages,
            hasPrev: safePage > 1,
        },
    };
}

function mapBackendCategory(raw: Record<string, unknown>): DishCategory {
    const id = Number(raw.id);
    const name = String(raw.name ?? '');
    const active =
        raw.active !== undefined
            ? Boolean(raw.active)
            : raw.isActive !== undefined
              ? Boolean(raw.isActive)
              : true;
    return {
        id,
        name,
        slug: String(raw.slug ?? ''),
        displayOrder: Number(raw.displayOrder ?? 0),
        isActive: active,
        restaurantId: Number(raw.restaurantId ?? 0),
    };
}

function mapBackendDish(raw: Record<string, unknown>): Dish {
    const restaurant = raw.restaurant as Record<string, unknown> | undefined;
    const category = raw.category as Record<string, unknown> | undefined;
    const price = raw.price;
    const available = raw.available !== undefined ? raw.available : raw.isAvailable;
    const emptyCategory: DishCategory = {
        id: 0,
        name: '',
        slug: '',
        displayOrder: 0,
        isActive: true,
        restaurantId: 0,
    };
    return {
        id: Number(raw.id),
        restaurantId: Number(restaurant?.id ?? raw.restaurantId ?? 0),
        categoryId: Number(category?.id ?? raw.categoryId ?? 0),
        name: String(raw.name ?? ''),
        description: String(raw.description ?? ''),
        price: typeof price === 'number' ? price : Number(price ?? 0),
        image: String(raw.imageUrl ?? raw.image ?? ''),
        isAvailable: Boolean(available ?? true),
        isPopular: Boolean(raw.isPopular ?? false),
        preparationTime: Number(raw.preparationTime ?? 0),
        ingredients: Array.isArray(raw.ingredients) ? (raw.ingredients as string[]) : [],
        allergens: Array.isArray(raw.allergens) ? (raw.allergens as string[]) : [],
        category: category
            ? {
                  id: Number(category.id),
                  name: String(category.name ?? ''),
                  slug: String(category.slug ?? ''),
                  displayOrder: Number(category.displayOrder ?? 0),
                  isActive: true,
                  restaurantId: Number(
                      (category.restaurant as Record<string, unknown> | undefined)?.id ??
                          restaurant?.id ??
                          0
                  ),
              }
            : emptyCategory,
        restaurant: restaurant as Dish['restaurant'],
        createdAt: String(raw.createdAt ?? ''),
        updatedAt: String(raw.updatedAt ?? ''),
    };
}

function mapMenuCategoryRow(raw: Record<string, unknown>): DishCategory {
    const rest = raw.restaurant as Record<string, unknown> | undefined;
    return {
        id: Number(raw.id),
        name: String(raw.name ?? ''),
        slug: String(raw.slug ?? ''),
        displayOrder: Number(raw.displayOrder ?? 0),
        isActive: true,
        restaurantId: Number(rest?.id ?? 0),
    };
}

// ============= AUTHENTICATION =============

export const adminAuth = {
    login: async (credentials: AdminLoginForm): Promise<AdminLoginResponse> => {
        const body = await adminHttp.post<unknown>(API_ENDPOINTS.ADMIN.LOGIN, credentials);
        return pickData<AdminLoginResponse>(body) ?? (body as AdminLoginResponse);
    },

    logout: async (): Promise<void> => {
        await adminHttp.post(API_ENDPOINTS.ADMIN.LOGOUT);
    },

    refreshToken: async (): Promise<AdminLoginResponse> => {
        const body = await adminHttp.post<unknown>(API_ENDPOINTS.ADMIN.REFRESH);
        return pickData<AdminLoginResponse>(body) ?? (body as AdminLoginResponse);
    },

    getProfile: async (): Promise<Admin> => {
        const body = await adminHttp.get<unknown>(API_ENDPOINTS.ADMIN.PROFILE);
        return pickData<Admin>(body) as Admin;
    },

    updateProfile: async (data: Partial<Admin>): Promise<Admin> => {
        const body = await adminHttp.put<unknown>(API_ENDPOINTS.ADMIN.PROFILE, data);
        return pickData<Admin>(body) as Admin;
    },
};

// ============= DASHBOARD =============

export const adminDashboard = {
    getStats: async (): Promise<DashboardStats> => {
        const body = await adminHttp.get<unknown>(API_ENDPOINTS.ADMIN.STATS);
        const stats = pickData<DashboardStats>(body);
        return stats && typeof stats === 'object' ? stats : ({} as DashboardStats);
    },
};

// ============= RESTAURANTS MANAGEMENT =============

export const adminRestaurants = {
    getAll: async (filters?: AdminRestaurantFilters, page = 1, limit = 20): Promise<PaginatedResponse<Restaurant>> => {
        try {
            const response = await adminHttp.get<any>(API_ENDPOINTS.ADMIN.RESTAURANTS, { params: { ...filters, page, limit } });

            console.debug('adminRestaurants.getAll body:', response);

            const payload: any = response;
            const list = Array.isArray(payload) ? payload : (payload?.data ?? payload?.result ?? payload);

            if (!Array.isArray(list)) {
                console.error('Respuesta inesperada al obtener restaurantes:', payload);
                return {
                    data: [],
                    pagination: {
                        total: 0,
                        totalPages: 0,
                        page,
                        limit,
                        hasNext: false,
                        hasPrev: false
                    }
                };
            }

            const mappedData: Restaurant[] = list.map((raw: any) => ({
                id: Number(raw.id),
                name: raw.name ?? '',
                description: raw.description ?? '',
                address: raw.address ?? 'Dirección no disponible',
                phone: raw.phone ?? '',
                email: raw.email ?? '',
                imageUrl: raw.imageUrl ?? raw.coverImage ?? '',
                coverImage: raw.imageUrl ?? raw.coverImage ?? '',
                logo: raw.logo ?? '',
                rating: Number(raw.rating ?? 0),
                reviewCount: Number(raw.reviewCount ?? 0),
                deliveryFee: Number(raw.deliveryFee ?? 0),
                minimumOrder: Number(raw.minimumOrder ?? 0),
                deliveryTimeMin: Number(raw.deliveryTimeMin ?? raw.delivery_min ?? 30),
                deliveryTimeMax: Number(raw.deliveryTimeMax ?? raw.delivery_max ?? 45),
                deliveryTime: (raw.deliveryTimeMin && raw.deliveryTimeMax)
                    ? `${raw.deliveryTimeMin}-${raw.deliveryTimeMax} min`
                    : (raw.deliveryTime ?? '30 min'),
                isActive: Boolean(raw.isActive ?? raw.open ?? true),
                isOpen: Boolean(raw.isOpen ?? raw.open ?? true),
                open: Boolean(raw.open ?? raw.isOpen ?? raw.isActive ?? true),
                cuisine: raw.cuisine ?? '',
                createdAt: raw.createdAt ?? raw.created_at ?? '',
                updatedAt: raw.updatedAt ?? raw.updated_at ?? '',
            }));

            const pagination = payload?.pagination ?? {
                total: mappedData.length,
                totalPages: 1,
                page,
                limit,
                hasNext: false,
                hasPrev: false
            };

            return { data: mappedData, pagination };
        } catch (err: any) {
            // Error de red / Axios: mostrar status y body si están disponibles
            console.error('Error al solicitar restaurantes:', {
                message: err?.message,
                status: err?.response?.status,
                responseData: err?.response?.data,
                stack: err?.stack
            });

            return {
                data: [],
                pagination: {
                    total: 0,
                    totalPages: 0,
                    page,
                    limit,
                    hasNext: false,
                    hasPrev: false
                }
            };
        }
    },

    getById: async (id: number): Promise<Restaurant> => {
        const body = await adminHttp.get<unknown>(API_ENDPOINTS.ADMIN.RESTAURANT_DETAIL(id));
        return pickData<Restaurant>(body) as Restaurant;
    },

    create: async (data: RestaurantForm): Promise<Restaurant> => {
        const formData = new FormData();

        Object.entries(data).forEach(([key, value]) => {
            if (value instanceof File) {
                formData.append(key, value);
            } else if (value !== undefined && value !== null) {
                formData.append(key, value.toString());
            }
        });

        const body = await adminHttp.post<unknown>(API_ENDPOINTS.ADMIN.RESTAURANT_CREATE, formData, {
            headers: { 'Content-Type': 'multipart/form-data' },
        });
        return pickData<Restaurant>(body) as Restaurant;
    },

    update: async (id: number, data: Partial<RestaurantForm>): Promise<Restaurant> => {
        const formData = new FormData();
        
        Object.entries(data).forEach(([key, value]) => {
            if (value instanceof File) {
                formData.append(key, value);
            } else if (value !== undefined && value !== null) {
                formData.append(key, value.toString());
            }
        });

        const body = await adminHttp.put<unknown>(API_ENDPOINTS.ADMIN.RESTAURANT_UPDATE(id), formData, {
            headers: { 'Content-Type': 'multipart/form-data' },
        });
        return pickData<Restaurant>(body) as Restaurant;
    },

    delete: async (id: number): Promise<void> => {
        await adminHttp.delete(API_ENDPOINTS.ADMIN.RESTAURANT_DELETE(id));
    },

    toggleStatus: async (id: number): Promise<Restaurant> => {
        const body = await adminHttp.patch<unknown>(API_ENDPOINTS.ADMIN.RESTAURANT_TOGGLE_STATUS(id));
        return pickData<Restaurant>(body) as Restaurant;
    },
};

// ============= DISHES MANAGEMENT =============

export const adminDishes = {
    /**
     * Quarkus devuelve un array; paginamos en cliente. Query params: name, categoryId, restaurantId, isAvailable.
     */
    getAll: async (
        filters?: { restaurantId?: number; search?: string; categoryId?: number },
        page = 1,
        limit = 20
    ): Promise<PaginatedResponse<Dish>> => {
        const params = new URLSearchParams();
        if (filters?.search?.trim()) params.append('name', filters.search.trim());
        if (filters?.categoryId != null) params.append('categoryId', String(filters.categoryId));
        if (filters?.restaurantId != null) params.append('restaurantId', String(filters.restaurantId));
        const qs = params.toString();
        const body = await adminHttp.get<unknown>(`${API_ENDPOINTS.ADMIN.DISHES}${qs ? `?${qs}` : ''}`);
        const rawList = Array.isArray(body) ? body : [];
        const mapped = rawList.map((x) => mapBackendDish(x as Record<string, unknown>));
        return paginateLocally(mapped, page, limit);
    },

    getById: async (id: number): Promise<Dish> => {
        const body = await adminHttp.get<unknown>(API_ENDPOINTS.ADMIN.DISH_DETAIL(id));
        return mapBackendDish(pickData(body) as Record<string, unknown>);
    },

    /** POST JSON (AdminDishWriteDto en backend). */
    create: async (data: AdminDishWritePayload): Promise<Dish> => {
        const payload = {
            name: data.name,
            description: data.description ?? '',
            price: data.price,
            imageUrl: data.imageUrl ?? '',
            isAvailable: data.isAvailable ?? true,
            restaurantId: data.restaurantId,
            categoryId: data.categoryId ?? undefined,
        };
        const body = await adminHttp.post<unknown>(API_ENDPOINTS.ADMIN.DISH_CREATE, payload);
        return mapBackendDish(pickData(body) as Record<string, unknown>);
    },

    /** PUT JSON; envía todos los campos editables. */
    update: async (id: number, data: AdminDishWritePayload): Promise<Dish> => {
        const payload = {
            name: data.name,
            description: data.description ?? '',
            price: data.price,
            imageUrl: data.imageUrl ?? '',
            isAvailable: data.isAvailable ?? true,
            restaurantId: data.restaurantId,
            categoryId: data.categoryId ?? undefined,
        };
        const body = await adminHttp.put<unknown>(API_ENDPOINTS.ADMIN.DISH_UPDATE(id), payload);
        return mapBackendDish(pickData(body) as Record<string, unknown>);
    },

    delete: async (id: number): Promise<void> => {
        await adminHttp.delete(API_ENDPOINTS.ADMIN.DISH_DELETE(id));
    },

    toggleAvailability: async (id: number): Promise<Dish> => {
        const body = await adminHttp.patch<unknown>(API_ENDPOINTS.ADMIN.DISH_TOGGLE_AVAILABILITY(id));
        return mapBackendDish(pickData(body) as Record<string, unknown>);
    },
};

/** Categorías de menú por restaurante (MenuCategory), GET público bajo `/dishes/...`. */
export const adminMenuCategories = {
    getByRestaurant: async (restaurantId: number): Promise<DishCategory[]> => {
        const body = await adminHttp.get<unknown>(API_ENDPOINTS.DISHES.CATEGORIES(restaurantId));
        const rawList = Array.isArray(body) ? body : [];
        return rawList.map((x) => mapMenuCategoryRow(x as Record<string, unknown>));
    },
};

// ============= DISH CATEGORIES MANAGEMENT =============

export const adminDishCategories = {
    /** Quarkus: lista plana; query `name`, `isActive`. Paginación en cliente. */
    getAll: async (
        filters?: { search?: string },
        page = 1,
        limit = 20
    ): Promise<PaginatedResponse<DishCategory>> => {
        const params = new URLSearchParams();
        if (filters?.search?.trim()) params.append('name', filters.search.trim());
        const qs = params.toString();
        const body = await adminHttp.get<unknown>(`${API_ENDPOINTS.ADMIN.CATEGORIES}${qs ? `?${qs}` : ''}`);
        const rawList = Array.isArray(body) ? body : [];
        const mapped = rawList.map((x) => mapBackendCategory(x as Record<string, unknown>));
        return paginateLocally(mapped, page, limit);
    },

    getById: async (id: number): Promise<DishCategory> => {
        const body = await adminHttp.get<unknown>(API_ENDPOINTS.ADMIN.CATEGORY_DETAIL(id));
        return mapBackendCategory(pickData(body) as Record<string, unknown>);
    },

    /** El backend solo persiste `name` (entidad Category). */
    create: async (data: Pick<CategoryForm, 'name'> | { name: string }): Promise<DishCategory> => {
        const body = await adminHttp.post<unknown>(API_ENDPOINTS.ADMIN.CATEGORY_CREATE, { name: data.name });
        return mapBackendCategory(pickData(body) as Record<string, unknown>);
    },

    update: async (id: number, data: Pick<CategoryForm, 'name'> | { name: string }): Promise<DishCategory> => {
        const body = await adminHttp.put<unknown>(API_ENDPOINTS.ADMIN.CATEGORY_UPDATE(id), { name: data.name });
        return mapBackendCategory(pickData(body) as Record<string, unknown>);
    },

    delete: async (id: number): Promise<void> => {
        await adminHttp.delete(API_ENDPOINTS.ADMIN.CATEGORY_DELETE(id));
    },

    toggleStatus: async (id: number): Promise<DishCategory> => {
        const body = await adminHttp.patch<unknown>(API_ENDPOINTS.ADMIN.CATEGORY_TOGGLE_STATUS(id));
        return mapBackendCategory(pickData(body) as Record<string, unknown>);
    },
};

// ============= ORDERS MANAGEMENT =============

export const adminOrders = {
    getAll: async (
        filters?: AdminOrderFilters,
        page = 1,
        limit = 20
    ): Promise<PaginatedResponse<Order>> => {
        const params = new URLSearchParams();
        params.append('page', page.toString());
        params.append('limit', limit.toString());
        
        if (filters) {
            Object.entries(filters).forEach(([key, value]) => {
                if (value !== undefined && value !== null && value !== '') {
                    params.append(key, value.toString());
                }
            });
        }

        const body = await adminHttp.get<PaginatedResponse<Order>>(`${API_ENDPOINTS.ADMIN.ORDERS}?${params}`);
        return body;
    },

    getById: async (id: number): Promise<Order> => {
        const body = await adminHttp.get<unknown>(API_ENDPOINTS.ADMIN.ORDER_DETAIL(id));
        return pickData<Order>(body) as Order;
    },

    updateStatus: async (id: number, status: string): Promise<Order> => {
        const body = await adminHttp.patch<unknown>(API_ENDPOINTS.ADMIN.ORDER_UPDATE_STATUS(id), { status });
        return pickData<Order>(body) as Order;
    },

    cancel: async (id: number, reason?: string): Promise<Order> => {
        const body = await adminHttp.patch<unknown>(API_ENDPOINTS.ADMIN.ORDER_CANCEL(id), { reason });
        return pickData<Order>(body) as Order;
    },

    refund: async (id: number, amount?: number, reason?: string): Promise<Order> => {
        const body = await adminHttp.patch<unknown>(API_ENDPOINTS.ADMIN.ORDER_REFUND(id), { amount, reason });
        return pickData<Order>(body) as Order;
    },
};

// ============= USERS MANAGEMENT =============

export const adminUsers = {
    getAll: async (
        filters?: AdminUserFilters,
        page = 1,
        limit = 20
    ): Promise<PaginatedResponse<User>> => {
        const params = new URLSearchParams();
        params.append('page', page.toString());
        params.append('limit', limit.toString());
        
        if (filters) {
            Object.entries(filters).forEach(([key, value]) => {
                if (value !== undefined && value !== null && value !== '') {
                    params.append(key, value.toString());
                }
            });
        }

        const body = await adminHttp.get<PaginatedResponse<User>>(`${API_ENDPOINTS.ADMIN.USERS}?${params}`);
        return body;
    },

    getById: async (id: number): Promise<User> => {
        const body = await adminHttp.get<unknown>(API_ENDPOINTS.ADMIN.USER_DETAIL(id));
        return pickData<User>(body) as User;
    },

    update: async (id: number, data: Partial<User>): Promise<User> => {
        const body = await adminHttp.put<unknown>(API_ENDPOINTS.ADMIN.USER_UPDATE(id), data);
        return pickData<User>(body) as User;
    },

    toggleStatus: async (id: number): Promise<User> => {
        const body = await adminHttp.patch<unknown>(API_ENDPOINTS.ADMIN.USER_TOGGLE_STATUS(id));
        return pickData<User>(body) as User;
    },

    delete: async (id: number): Promise<void> => {
        await adminHttp.delete(API_ENDPOINTS.ADMIN.USER_DELETE(id));
    },
};

// ============= CATEGORIES MANAGEMENT =============

export const adminCategories = {
    getRestaurantCategories: async (): Promise<RestaurantCategory[]> => {
        const body = await adminHttp.get<unknown>(API_ENDPOINTS.ADMIN.CATEGORIES);
        return pickData<RestaurantCategory[]>(body) as RestaurantCategory[];
    },

    getDishCategories: async (restaurantId?: number): Promise<DishCategory[]> => {
        const params = restaurantId ? `?restaurantId=${restaurantId}` : '';
        const body = await adminHttp.get<unknown>(`${API_ENDPOINTS.ADMIN.CATEGORIES}${params}`);
        return pickData<DishCategory[]>(body) as DishCategory[];
    },

    create: async (data: CategoryForm): Promise<RestaurantCategory | DishCategory> => {
        const body = await adminHttp.post<unknown>(API_ENDPOINTS.ADMIN.CATEGORY_CREATE, data);
        return pickData<RestaurantCategory | DishCategory>(body) as RestaurantCategory | DishCategory;
    },

    update: async (
        id: number,
        data: Partial<CategoryForm>
    ): Promise<RestaurantCategory | DishCategory> => {
        const body = await adminHttp.put<unknown>(API_ENDPOINTS.ADMIN.CATEGORY_UPDATE(id), data);
        return pickData<RestaurantCategory | DishCategory>(body) as RestaurantCategory | DishCategory;
    },

    delete: async (id: number): Promise<void> => {
        await adminHttp.delete(API_ENDPOINTS.ADMIN.CATEGORY_DELETE(id));
    },
};

// ============= REPORTS =============

export const adminReports = {
    getSalesReport: async (
        period: 'day' | 'week' | 'month' | 'year',
        startDate?: string,
        endDate?: string
    ): Promise<SalesReport> => {
        const params = new URLSearchParams();
        params.append('period', period);
        if (startDate) params.append('startDate', startDate);
        if (endDate) params.append('endDate', endDate);

        const body = await adminHttp.get<unknown>(`${API_ENDPOINTS.ADMIN.SALES_REPORT}?${params}`);
        return pickData<SalesReport>(body) as SalesReport;
    },

    getUsersReport: async (): Promise<UserReport> => {
        const body = await adminHttp.get<unknown>(API_ENDPOINTS.ADMIN.USERS_REPORT);
        return pickData<UserReport>(body) as UserReport;
    },

    exportReport: async (
        type: 'sales' | 'users',
        format: 'csv' | 'excel' | 'pdf',
        filters?: any
    ): Promise<Blob> => {
        const params = new URLSearchParams();
        params.append('type', type);
        params.append('format', format);

        if (filters) {
            Object.entries(filters).forEach(([key, value]) => {
                if (value !== undefined && value !== null && value !== '') {
                    params.append(key, value.toString());
                }
            });
        }

        return adminHttp.get<Blob>(`${API_ENDPOINTS.ADMIN.EXPORT_REPORT}?${params}`, {
            responseType: 'blob',
        });
    },
};