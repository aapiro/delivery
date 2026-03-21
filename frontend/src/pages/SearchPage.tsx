import React from 'react';
import { useSearchParams, Link } from 'react-router-dom';
import { Star, Clock } from 'lucide-react';
import Layout from '../components/layout/Layout';
import Card from '../components/ui/Card';
import Button from '../components/ui/Button';
import { useSearchRestaurants } from '../services/restaurants';
import { ROUTES } from '../constants';

const SearchPage: React.FC = () => {
    const [params] = useSearchParams();
    const q = params.get('q') ?? '';

    const { data, isLoading, isError, error } = useSearchRestaurants(q);
    const restaurants = data ?? [];

    return (
        <Layout>
            <div className="max-w-6xl mx-auto px-4 py-8">
                <div className="mb-6">
                    <h1 className="text-3xl font-bold text-gray-900">Búsqueda</h1>
                    <p className="text-gray-600 mt-1">
                        Resultados para: <span className="font-medium">{q || '—'}</span>
                    </p>
                </div>

                {isLoading ? <p className="text-gray-600">Buscando...</p> : null}

                {isError ? (
                    <div className="rounded-xl border border-red-200 bg-red-50 p-6 text-center text-red-800">
                        <p className="mb-2 font-medium">Error al buscar</p>
                        <p className="text-sm">{(error as Error).message}</p>
                        <Button variant="primary" className="mt-4" onClick={() => window.location.reload()}>
                            Reintentar
                        </Button>
                    </div>
                ) : null}

                {!isLoading && !isError && restaurants.length === 0 ? (
                    <div className="text-center py-12">
                        <h2 className="text-xl font-bold text-gray-900 mb-2">No hay resultados</h2>
                        <p className="text-gray-600 mb-6">Prueba con otro término.</p>
                        <Link to={ROUTES.RESTAURANTS}>
                            <Button variant="primary">Ver restaurantes</Button>
                        </Link>
                    </div>
                ) : null}

                {!isLoading && !isError && restaurants.length > 0 ? (
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                        {restaurants.map((restaurant) => (
                            <Link key={restaurant.id} to={ROUTES.RESTAURANT_DETAIL(restaurant.id)}>
                                <Card hover clickable className="h-full">
                                    <div className="aspect-video relative overflow-hidden">
                                        <img
                                            src={restaurant.coverImage || restaurant.imageUrl || ''}
                                            alt={restaurant.name}
                                            className="w-full h-full object-cover"
                                        />
                                    </div>
                                    <div className="p-4">
                                        <h3 className="text-lg font-bold text-gray-900">{restaurant.name}</h3>
                                        <p className="text-sm text-gray-600 mt-1 line-clamp-2">
                                            {restaurant.description}
                                        </p>
                                        <div className="mt-3 flex items-center justify-between text-sm">
                                            <div className="flex items-center gap-2">
                                                <Star className="w-4 h-4 text-yellow-400 fill-current" />
                                                <span className="font-medium">{restaurant.rating}</span>
                                            </div>
                                            <div className="flex items-center gap-2">
                                                <Clock className="w-4 h-4" />
                                                <span>{restaurant.deliveryTime}</span>
                                            </div>
                                        </div>
                                    </div>
                                </Card>
                            </Link>
                        ))}
                    </div>
                ) : null}
            </div>
        </Layout>
    );
};

export default SearchPage;

