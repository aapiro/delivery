import { Dish, Restaurant } from '../types';

export function mapRestaurantFromApi(raw: Record<string, unknown>): Restaurant {
    const r = raw ?? {};
    return {
        id: Number(r.id),
        name: String(r.name ?? ''),
        description: String(r.description ?? ''),
        address: r.address != null ? String(r.address) : undefined,
        phone: r.phone != null ? String(r.phone) : undefined,
        email: r.email != null ? String(r.email) : undefined,
        rating: Number(r.rating ?? 0),
        reviewCount: Number(r.reviewCount ?? 0),
        deliveryTime: `${r.deliveryTimeMin ?? 30}-${r.deliveryTimeMax ?? 45} min`,
        deliveryFee: Number(r.deliveryFee ?? 0),
        minimumOrder: Number(r.minimumOrder ?? 0),
        deliveryTimeMin: Number(r.deliveryTimeMin ?? 30),
        deliveryTimeMax: Number(r.deliveryTimeMax ?? 45),
        isActive: Boolean(r.isOpen ?? true),
        isOpen: Boolean(r.isOpen ?? true),
        open: Boolean(r.isOpen ?? true),
        imageUrl: r.imageUrl != null ? String(r.imageUrl) : undefined,
        coverImage: String(r.imageUrl ?? r.coverImage ?? ''),
        cuisine: r.cuisine != null ? String(r.cuisine) : undefined,
        createdAt: r.createdAt != null ? String(r.createdAt) : undefined,
        updatedAt: r.updatedAt != null ? String(r.updatedAt) : undefined,
    };
}

export function mapDishFromApi(raw: Record<string, unknown>, fallbackRestaurantId = 0): Dish {
    const restaurantObj = raw.restaurant as Record<string, unknown> | undefined;
    const restaurantId = Number(raw.restaurantId ?? restaurantObj?.id ?? fallbackRestaurantId);
    const cat = raw.category as Record<string, unknown> | undefined;
    const categoryId = Number(raw.categoryId ?? cat?.id ?? 0);
    const price = Number(raw.price ?? 0);
    const img = String(raw.imageUrl ?? raw.image ?? '');

    const dishCategory: Dish['category'] = cat
        ? {
              id: Number(cat.id ?? categoryId),
              restaurantId: Number(cat.restaurantId ?? (cat.restaurant as Record<string, unknown> | undefined)?.id ?? restaurantId),
              name: String(cat.name ?? 'Menú'),
              slug: String(cat.slug ?? 'menu'),
              displayOrder: Number(cat.displayOrder ?? 0),
              isActive: cat.isActive !== false,
          }
        : {
              id: categoryId || 0,
              restaurantId,
              name: 'General',
              slug: 'general',
              displayOrder: 0,
              isActive: true,
          };

    return {
        id: Number(raw.id),
        restaurantId,
        categoryId: categoryId || dishCategory.id,
        name: String(raw.name ?? ''),
        description: String(raw.description ?? ''),
        price,
        originalPrice: raw.originalPrice != null ? Number(raw.originalPrice) : undefined,
        image: img,
        isAvailable: raw.isAvailable !== false,
        isPopular: Boolean(raw.isPopular),
        preparationTime: Number(raw.preparationTime ?? 15),
        ingredients: Array.isArray(raw.ingredients) ? (raw.ingredients as string[]) : [],
        allergens: Array.isArray(raw.allergens) ? (raw.allergens as string[]) : [],
        category: dishCategory,
        createdAt: String(raw.createdAt ?? ''),
        updatedAt: String(raw.updatedAt ?? ''),
    };
}
