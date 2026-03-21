import React, { useEffect, useMemo, useState } from 'react';
import { Link, useSearchParams } from 'react-router-dom';
import { Filter, Star, Clock } from 'lucide-react';
import Layout from '../components/layout/Layout';
import Card, { CardContent } from '../components/ui/Card';
import Button from '../components/ui/Button';
import Input from '../components/ui/Input';
import { ROUTES } from '../constants';
import { useRestaurants } from '../services/restaurants';
import { Restaurant } from '../types';
import { isMockEnabled } from '../mocks/config';

const RestaurantsPage: React.FC = () => {
    const [searchParams, setSearchParams] = useSearchParams();
    const [searchQuery, setSearchQuery] = useState('');
    const [selectedCategory, setSelectedCategory] = useState('');
    const [sortBy, setSortBy] = useState('rating');
    const { data, isLoading, isError, error, refetch } = useRestaurants(undefined, 1, 100);
    const list = data?.data ?? [];

    useEffect(() => {
        const raw = searchParams.get('category');
        if (!raw || !list.length) return;
        const slug = raw.toLowerCase();
        const cuisines = [...new Set(list.map((r) => r.cuisine || 'Otros'))];
        const found = cuisines.find(
            (c) => c.toLowerCase() === slug || c.toLowerCase().includes(slug) || slug.includes(c.toLowerCase())
        );
        if (found) setSelectedCategory(found);
    }, [list, searchParams]);

    const cuisineChips = useMemo(() => {
        const map = new Map<string, number>();
        list.forEach((r) => {
            const c = (r.cuisine || 'Otros').trim();
            map.set(c, (map.get(c) || 0) + 1);
        });
        const sorted = Array.from(map.entries()).sort((a, b) => a[0].localeCompare(b[0]));
        return [{ name: 'Todos', value: '', count: list.length }, ...sorted.map(([name, count]) => ({ name, value: name, count }))];
    }, [list]);

    const filteredRestaurants = list.filter((restaurant) => {
        const matchesSearch =
            !searchQuery ||
            restaurant.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
            restaurant.description.toLowerCase().includes(searchQuery.toLowerCase()) ||
            (restaurant.cuisine && restaurant.cuisine.toLowerCase().includes(searchQuery.toLowerCase()));

        const matchesCategory = !selectedCategory || (restaurant.cuisine || 'Otros') === selectedCategory;

        return matchesSearch && matchesCategory;
    });

    const sortedRestaurants = [...filteredRestaurants].sort((a, b) => {
        switch (sortBy) {
            case 'rating':
                return b.rating - a.rating;
            case 'deliveryTime':
                return (a.deliveryTimeMin ?? 0) - (b.deliveryTimeMin ?? 0);
            case 'deliveryFee':
                return a.deliveryFee - b.deliveryFee;
            case 'name':
                return a.name.localeCompare(b.name);
            default:
                return 0;
        }
    });

    const handleCategoryChange = (value: string) => {
        setSelectedCategory(value);
        if (value) {
            setSearchParams({ category: value.toLowerCase() });
        } else {
            setSearchParams({});
        }
    };

    return (
        <Layout>
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                {process.env.NODE_ENV === 'development' && (
                    <div className="mb-4 rounded-lg border border-amber-200 bg-amber-50 px-3 py-2 text-xs text-amber-900">
                        <strong>Dev:</strong> {isMockEnabled() ? 'MOCK' : 'API'} ·{' '}
                        <code className="rounded bg-white/80 px-1">{process.env.REACT_APP_API_URL}</code>
                    </div>
                )}

                <div className="mb-8">
                    <h1 className="text-3xl font-bold text-gray-900 mb-2">Restaurantes en Madrid</h1>
                    <p className="text-gray-600">
                        {isLoading ? 'Cargando…' : `${filteredRestaurants.length} restaurantes disponibles`}
                    </p>
                </div>

                <div className="mb-8 space-y-4">
                    <div className="flex flex-col md:flex-row gap-4">
                        <div className="flex-1">
                            <Input
                                type="text"
                                placeholder="Buscar restaurantes..."
                                value={searchQuery}
                                onChange={(e) => setSearchQuery(e.target.value)}
                                fullWidth
                            />
                        </div>
                        <div className="flex gap-2">
                            <select
                                value={sortBy}
                                onChange={(e) => setSortBy(e.target.value)}
                                className="px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500"
                            >
                                <option value="rating">Mejor valorados</option>
                                <option value="deliveryTime">Más rápidos</option>
                                <option value="deliveryFee">Menor costo de envío</option>
                                <option value="name">Nombre A-Z</option>
                            </select>
                            <Button variant="outline" icon={<Filter className="w-4 h-4" />} type="button">
                                Filtros
                            </Button>
                        </div>
                    </div>

                    <div className="flex gap-2 overflow-x-auto scrollbar-hide pb-2">
                        {cuisineChips.map((category) => (
                            <button
                                key={category.value === '' ? 'all' : category.value}
                                onClick={() => handleCategoryChange(category.value)}
                                className={`flex-shrink-0 px-4 py-2 rounded-full text-sm font-medium transition-colors ${
                                    selectedCategory === category.value
                                        ? 'bg-primary-500 text-white'
                                        : 'bg-white text-gray-700 border border-gray-300 hover:bg-gray-50'
                                }`}
                            >
                                {category.name} ({category.count})
                            </button>
                        ))}
                    </div>
                </div>

                {isLoading && (
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                        {[1, 2, 3, 4, 5, 6].map((i) => (
                            <div key={i} className="h-80 animate-pulse rounded-xl bg-gray-200" />
                        ))}
                    </div>
                )}

                {isError && (
                    <div className="rounded-xl border border-red-200 bg-red-50 p-8 text-center text-red-800">
                        <p className="mb-2 font-medium">Error al cargar restaurantes</p>
                        <p className="text-sm mb-4">{error?.message}</p>
                        <Button variant="primary" onClick={() => refetch()}>
                            Reintentar
                        </Button>
                    </div>
                )}

                {!isLoading && !isError && (
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                        {sortedRestaurants.map((restaurant: Restaurant) => {
                            const img = restaurant.coverImage || restaurant.imageUrl || '';
                            const catLabel = (restaurant.cuisine || 'restaurante').toLowerCase();
                            return (
                                <Link key={restaurant.id} to={ROUTES.RESTAURANT_DETAIL(restaurant.id)}>
                                    <Card hover clickable className="h-full">
                                        <div className="aspect-video relative overflow-hidden">
                                            <img src={img} alt={restaurant.name} className="w-full h-full object-cover" />
                                            {!restaurant.isOpen && (
                                                <div className="absolute inset-0 bg-black/50 flex items-center justify-center">
                                                    <span className="bg-white px-3 py-1 rounded-full text-sm font-medium text-gray-900">
                                                        Cerrado
                                                    </span>
                                                </div>
                                            )}
                                            <div className="absolute top-3 right-3 bg-white/90 backdrop-blur-sm px-2 py-1 rounded-full text-xs font-medium capitalize">
                                                {catLabel}
                                            </div>
                                        </div>

                                        <CardContent className="p-4">
                                            <h3 className="text-lg font-bold text-gray-900 mb-2">{restaurant.name}</h3>
                                            <p className="text-sm text-gray-600 mb-4 line-clamp-2">{restaurant.description}</p>

                                            <div className="flex items-center justify-between text-sm mb-3">
                                                <div className="flex items-center gap-4">
                                                    <div className="flex items-center gap-1">
                                                        <Star className="w-4 h-4 text-yellow-400 fill-current" />
                                                        <span className="font-medium">{restaurant.rating}</span>
                                                        <span className="text-gray-500">({restaurant.reviewCount})</span>
                                                    </div>
                                                    <div className="flex items-center gap-1 text-gray-600">
                                                        <Clock className="w-4 h-4" />
                                                        <span>{restaurant.deliveryTime}</span>
                                                    </div>
                                                </div>
                                            </div>

                                            <div className="flex items-center justify-between text-sm">
                                                <div className="space-y-1">
                                                    <div className="text-green-600 font-medium">Delivery €{restaurant.deliveryFee}</div>
                                                    <div className="text-gray-500">Mínimo €{restaurant.minimumOrder}</div>
                                                </div>
                                                <Button size="sm" variant={restaurant.isOpen ? 'primary' : 'secondary'} disabled={!restaurant.isOpen}>
                                                    {restaurant.isOpen ? 'Ver Menú' : 'Cerrado'}
                                                </Button>
                                            </div>
                                        </CardContent>
                                    </Card>
                                </Link>
                            );
                        })}
                    </div>
                )}

                {!isLoading && !isError && filteredRestaurants.length === 0 && (
                    <div className="text-center py-12">
                        <div className="text-6xl mb-4">🔍</div>
                        <h3 className="text-xl font-bold text-gray-900 mb-2">No encontramos restaurantes</h3>
                        <p className="text-gray-600 mb-6">Intenta ajustar tus filtros o búsqueda</p>
                        <Button
                            variant="primary"
                            onClick={() => {
                                setSearchQuery('');
                                setSelectedCategory('');
                                setSearchParams({});
                            }}
                        >
                            Limpiar filtros
                        </Button>
                    </div>
                )}
            </div>
        </Layout>
    );
};

export default RestaurantsPage;
