import { api } from './api';
import { CartItem, Dish } from '../types';

type BackendDish = {
    id: number;
    name?: string;
    description?: string;
    price?: number;
    imageUrl?: string;
    isAvailable?: boolean;
    restaurantId?: number;
    restaurant?: {
        id: number;
        name?: string;
        deliveryFee?: number;
        deliveryTimeMin?: number;
        deliveryTimeMax?: number;
        isOpen?: boolean;
    };
};

type BackendCartItem = {
    id: number;
    dishId: number;
    quantity: number;
    unitPrice: number;
    totalPrice: number;
    specialInstructions?: string;
    dish: BackendDish;
};

type CartPayload = {
    items: BackendCartItem[];
    total: number;
    restaurantId?: number | null;
};

const asDish = (d: BackendDish): Dish => {
    const rest = d.restaurant;
    const restaurantId = Number(d.restaurantId ?? rest?.id ?? 0);
    const min = Number(rest?.deliveryTimeMin ?? 30);
    const max = Number(rest?.deliveryTimeMax ?? 45);

    return {
        id: Number(d.id),
        restaurantId,
        categoryId: 0,
        name: d.name ?? '',
        description: d.description ?? '',
        price: Number(d.price ?? 0),
        image: d.imageUrl ?? '',
        isAvailable: Boolean(d.isAvailable ?? true),
        isPopular: false,
        preparationTime: 0,
        ingredients: [],
        allergens: [],
        category: {
            id: 0,
            restaurantId,
            name: '',
            slug: '',
            displayOrder: 0,
            isActive: true,
        },
        restaurant: {
            id: restaurantId,
            name: rest?.name ?? 'Restaurante',
            description: '',
            rating: 0,
            reviewCount: 0,
            deliveryTime: `${min}-${max} min`,
            deliveryFee: Number(rest?.deliveryFee ?? 0),
            minimumOrder: 0,
            deliveryTimeMin: min,
            deliveryTimeMax: max,
            isActive: Boolean(rest?.isOpen ?? true),
            isOpen: Boolean(rest?.isOpen ?? true),
            open: Boolean(rest?.isOpen ?? true),
            coverImage: '',
        },
        createdAt: '',
        updatedAt: '',
    };
};

const toCartItem = (row: BackendCartItem): CartItem => ({
    id: String(row.id),
    dishId: Number(row.dishId),
    quantity: Number(row.quantity),
    unitPrice: Number(row.unitPrice),
    totalPrice: Number(row.totalPrice),
    specialInstructions: row.specialInstructions,
    dish: asDish(row.dish),
    restaurantId: Number(row.dish?.restaurantId ?? row.dish?.restaurant?.id ?? 0),
});

const unwrap = (response: any): CartPayload => (response?.data ?? response) as CartPayload;

export const cartService = {
    async getCart(): Promise<CartItem[]> {
        const res = await api.get<any>('/cart');
        return (unwrap(res).items ?? []).map(toCartItem);
    },

    async addToCart(dishId: number, quantity: number, specialInstructions?: string): Promise<CartItem[]> {
        const res = await api.post<any>('/cart/add', { dishId, quantity, specialInstructions });
        return (unwrap(res).items ?? []).map(toCartItem);
    },

    async updateCartItem(itemId: string, quantity: number): Promise<CartItem[]> {
        const res = await api.put<any>('/cart/update', { itemId: Number(itemId), quantity });
        return (unwrap(res).items ?? []).map(toCartItem);
    },

    async removeFromCart(itemId: string): Promise<CartItem[]> {
        const res = await api.delete<any>('/cart/remove', { data: { itemId: Number(itemId) } as any });
        return (unwrap(res).items ?? []).map(toCartItem);
    },

    async clearCart(): Promise<CartItem[]> {
        const res = await api.delete<any>('/cart/clear');
        return (unwrap(res).items ?? []).map(toCartItem);
    },
};

