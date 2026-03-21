import React, { useMemo, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { ArrowLeft, Star, Clock, MapPin, Plus } from 'lucide-react';
import Layout from '../components/layout/Layout';
import Card from '../components/ui/Card';
import Button from '../components/ui/Button';
import { useCart } from '../hooks/useCart';
import { ROUTES } from '../constants';
import { Dish } from '../types';
import { useRestaurant, useRestaurantDishes } from '../services/restaurants';
import { isMockEnabled } from '../mocks/config';

const RestaurantDetailPage: React.FC = () => {
    const { id } = useParams<{ id: string }>();
    const numericId = Number(id);
    const { addToCart } = useCart();
    const [selectedCategory, setSelectedCategory] = useState<number | null>(null);

    const restaurantQuery = useRestaurant(numericId);
    const dishesQuery = useRestaurantDishes(numericId);

    const restaurant = restaurantQuery.data;
    const dishes = dishesQuery.data ?? [];

    const dishCategories = useMemo(() => {
        const map = new Map<number, { id: number; name: string; count: number }>();
        dishes.forEach((d) => {
            const cid = d.categoryId || d.category?.id || 0;
            const name = d.category?.name || 'Otros';
            const prev = map.get(cid);
            if (prev) prev.count += 1;
            else map.set(cid, { id: cid, name, count: 1 });
        });
        return Array.from(map.values()).sort((a, b) => a.name.localeCompare(b.name));
    }, [dishes]);

    const filteredDishes =
        selectedCategory != null
            ? dishes.filter((dish) => (dish.categoryId || dish.category?.id) === selectedCategory)
            : dishes;

    const handleAddToCart = (dish: Dish) => {
        addToCart(dish, 1);
    };

    const loading = restaurantQuery.isLoading || dishesQuery.isLoading;

    if (!Number.isFinite(numericId) || numericId <= 0) {
        return (
            <Layout>
                <div className="max-w-7xl mx-auto px-4 py-8 text-center">
                    <h1 className="text-2xl font-bold text-gray-900 mb-4">Restaurante no encontrado</h1>
                    <Link to={ROUTES.RESTAURANTS}>
                        <Button variant="primary">Volver a restaurantes</Button>
                    </Link>
                </div>
            </Layout>
        );
    }

    if (!loading && (restaurantQuery.isError || !restaurant)) {
        return (
            <Layout>
                <div className="max-w-7xl mx-auto px-4 py-8 text-center">
                    <h1 className="text-2xl font-bold text-gray-900 mb-4">Restaurante no encontrado</h1>
                    <p className="text-gray-600 mb-4">{restaurantQuery.error?.message}</p>
                    <Link to={ROUTES.RESTAURANTS}>
                        <Button variant="primary">Volver a restaurantes</Button>
                    </Link>
                </div>
            </Layout>
        );
    }

    const headerImage = restaurant ? restaurant.coverImage || restaurant.imageUrl || '' : '';
    const headerAddress = restaurant?.address || 'Dirección no disponible';
    const openingHours = 'Consultar horario';

    return (
        <Layout>
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                {process.env.NODE_ENV === 'development' && (
                    <div className="mb-4 rounded-lg border border-amber-200 bg-amber-50 px-3 py-2 text-xs text-amber-900">
                        <strong>Dev:</strong> {isMockEnabled() ? 'MOCK' : 'API'} ·{' '}
                        <code className="rounded bg-white/80 px-1">{process.env.REACT_APP_API_URL}</code>
                    </div>
                )}

                <Link
                    to={ROUTES.RESTAURANTS}
                    className="inline-flex items-center gap-2 text-primary-500 hover:text-primary-600 mb-6 transition-colors"
                >
                    <ArrowLeft className="w-4 h-4" />
                    Volver a restaurantes
                </Link>

                {loading && (
                    <div className="space-y-6">
                        <div className="h-72 animate-pulse rounded-xl bg-gray-200" />
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                            <div className="h-48 animate-pulse rounded-xl bg-gray-200" />
                            <div className="h-48 animate-pulse rounded-xl bg-gray-200" />
                        </div>
                    </div>
                )}

                {!loading && restaurant && (
                    <>
                        <div className="bg-white rounded-xl shadow-md overflow-hidden mb-8">
                            <div className="h-64 md:h-80 relative">
                                <img src={headerImage} alt={restaurant.name} className="w-full h-full object-cover" />
                                {!restaurant.isOpen && (
                                    <div className="absolute inset-0 bg-black/50 flex items-center justify-center">
                                        <span className="bg-white px-4 py-2 rounded-full font-medium text-gray-900">Restaurante Cerrado</span>
                                    </div>
                                )}
                            </div>

                            <div className="p-6">
                                <div className="flex flex-col md:flex-row md:items-start md:justify-between gap-4">
                                    <div className="flex-1">
                                        <h1 className="text-3xl font-bold text-gray-900 mb-2">{restaurant.name}</h1>
                                        <p className="text-gray-600 mb-4">{restaurant.description}</p>

                                        <div className="flex flex-wrap items-center gap-6 text-sm text-gray-600">
                                            <div className="flex items-center gap-1">
                                                <Star className="w-4 h-4 text-yellow-400 fill-current" />
                                                <span className="font-medium">{restaurant.rating}</span>
                                                <span>({restaurant.reviewCount} reseñas)</span>
                                            </div>
                                            <div className="flex items-center gap-1">
                                                <Clock className="w-4 h-4" />
                                                <span>{restaurant.deliveryTime}</span>
                                            </div>
                                            <div className="flex items-center gap-1">
                                                <MapPin className="w-4 h-4" />
                                                <span>{headerAddress}</span>
                                            </div>
                                        </div>
                                    </div>

                                    <div className="text-right">
                                        <div className="bg-green-50 border border-green-200 rounded-lg p-3 mb-2">
                                            <div className="text-green-800 font-medium">Delivery €{restaurant.deliveryFee}</div>
                                            <div className="text-green-600 text-sm">Mínimo €{restaurant.minimumOrder}</div>
                                        </div>
                                        <div className="text-sm text-gray-600">Horario: {openingHours}</div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div className="flex flex-col lg:flex-row gap-8">
                            <div className="lg:w-64">
                                <div className="bg-white rounded-xl shadow-md p-4 sticky top-24">
                                    <h3 className="font-bold text-gray-900 mb-4">Categorías</h3>
                                    <div className="space-y-2">
                                        <button
                                            type="button"
                                            onClick={() => setSelectedCategory(null)}
                                            className={`w-full text-left px-3 py-2 rounded-lg transition-colors ${
                                                selectedCategory === null ? 'bg-primary-100 text-primary-700' : 'hover:bg-gray-100 text-gray-700'
                                            }`}
                                        >
                                            <div className="flex justify-between">
                                                <span>Todos los platos</span>
                                                <span className="text-sm">({dishes.length})</span>
                                            </div>
                                        </button>
                                        {dishCategories.map((category) => (
                                            <button
                                                type="button"
                                                key={category.id}
                                                onClick={() => setSelectedCategory(category.id)}
                                                className={`w-full text-left px-3 py-2 rounded-lg transition-colors ${
                                                    selectedCategory === category.id ? 'bg-primary-100 text-primary-700' : 'hover:bg-gray-100 text-gray-700'
                                                }`}
                                            >
                                                <div className="flex justify-between">
                                                    <span>{category.name}</span>
                                                    <span className="text-sm">({category.count})</span>
                                                </div>
                                            </button>
                                        ))}
                                    </div>
                                </div>
                            </div>

                            <div className="flex-1">
                                {dishesQuery.isError && (
                                    <div className="mb-4 rounded-lg border border-amber-200 bg-amber-50 p-4 text-sm text-amber-900">
                                        No se pudieron cargar los platos: {dishesQuery.error?.message}
                                    </div>
                                )}

                                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                                    {filteredDishes.map((dish) => (
                                        <Card key={dish.id} className="flex flex-col">
                                            <div className="aspect-video relative overflow-hidden">
                                                <img src={dish.image} alt={dish.name} className="w-full h-full object-cover" />
                                                {dish.isPopular && (
                                                    <div className="absolute top-3 left-3 bg-primary-500 text-white px-2 py-1 rounded-full text-xs font-medium">
                                                        Popular
                                                    </div>
                                                )}
                                                {dish.originalPrice != null && (
                                                    <div className="absolute top-3 right-3 bg-red-500 text-white px-2 py-1 rounded-full text-xs font-medium">
                                                        Oferta
                                                    </div>
                                                )}
                                            </div>

                                            <div className="p-4 flex-1 flex flex-col">
                                                <h3 className="text-lg font-bold text-gray-900 mb-2">{dish.name}</h3>
                                                <p className="text-gray-600 text-sm mb-4 flex-1">{dish.description}</p>

                                                {dish.ingredients?.length > 0 && (
                                                    <div className="mb-4">
                                                        <div className="text-xs text-gray-500 mb-1">Ingredientes:</div>
                                                        <div className="text-xs text-gray-600">
                                                            {dish.ingredients.slice(0, 3).join(', ')}
                                                            {dish.ingredients.length > 3 && '...'}
                                                        </div>
                                                    </div>
                                                )}

                                                <div className="flex items-center justify-between">
                                                    <div className="flex items-center gap-2">
                                                        {dish.originalPrice != null && (
                                                            <span className="text-gray-400 line-through text-sm">€{dish.originalPrice}</span>
                                                        )}
                                                        <span className="text-2xl font-bold text-primary-500">€{dish.price}</span>
                                                    </div>

                                                    <Button
                                                        size="sm"
                                                        variant="primary"
                                                        icon={<Plus className="w-4 h-4" />}
                                                        disabled={!dish.isAvailable || !restaurant.isOpen}
                                                        onClick={() => handleAddToCart(dish)}
                                                    >
                                                        {dish.isAvailable ? 'Agregar' : 'No disponible'}
                                                    </Button>
                                                </div>

                                                {dish.preparationTime != null && (
                                                    <div className="flex items-center gap-1 mt-2 text-xs text-gray-500">
                                                        <Clock className="w-3 h-3" />
                                                        <span>{dish.preparationTime} min</span>
                                                    </div>
                                                )}
                                            </div>
                                        </Card>
                                    ))}
                                </div>

                                {filteredDishes.length === 0 && selectedCategory != null && (
                                    <div className="text-center py-12">
                                        <div className="text-6xl mb-4">🍽️</div>
                                        <h3 className="text-xl font-bold text-gray-900 mb-2">No hay platos en esta categoría</h3>
                                        <p className="text-gray-600 mb-6">Explora otras categorías del menú</p>
                                        <Button variant="primary" onClick={() => setSelectedCategory(null)}>
                                            Ver todos los platos
                                        </Button>
                                    </div>
                                )}
                            </div>
                        </div>
                    </>
                )}
            </div>
        </Layout>
    );
};

export default RestaurantDetailPage;
