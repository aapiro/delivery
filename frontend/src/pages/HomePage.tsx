import React from 'react';
import { Link } from 'react-router-dom';
import { Star, Clock, Truck } from 'lucide-react';
import Layout from '../components/layout/Layout';
import Card from '../components/ui/Card';
import Button from '../components/ui/Button';
import { ROUTES } from '../constants';
import { useRestaurants } from '../services/restaurants';
import { isMockEnabled } from '../mocks/config';

const categories = [
    { name: 'Pizza', emoji: '🍕', count: 12 },
    { name: 'Hamburguesas', emoji: '🍔', count: 8 },
    { name: 'Italiana', emoji: '🍝', count: 15 },
    { name: 'Japonesa', emoji: '🍱', count: 6 },
    { name: 'Mexicana', emoji: '🌮', count: 9 },
    { name: 'Parrilla', emoji: '🥩', count: 7 },
    { name: 'Postres', emoji: '🍰', count: 11 },
    { name: 'Saludable', emoji: '🥗', count: 5 },
];

const HomePage: React.FC = () => {
    const { data, isLoading, isError, error, refetch } = useRestaurants(undefined, 1, 6);
    const restaurants = data?.data ?? [];

    return (
        <Layout>
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                {process.env.NODE_ENV === 'development' && (
                    <div
                        className="mb-4 rounded-lg border border-amber-200 bg-amber-50 px-3 py-2 text-xs text-amber-900"
                        title="Origen de datos según .env.development"
                    >
                        <strong>Dev:</strong> {isMockEnabled() ? 'MOCK (fixtures locales)' : 'API'}{' '}
                        <code className="ml-1 rounded bg-white/80 px-1">{process.env.REACT_APP_API_URL}</code>
                    </div>
                )}

                <section className="text-center mb-12">
                    <h1 className="text-4xl md:text-5xl font-bold text-gray-900 mb-4">
                        Tu comida favorita,
                        <span className="text-primary-500"> entregada rápido</span>
                    </h1>
                    <p className="text-xl text-gray-600 mb-8 max-w-2xl mx-auto">
                        Descubre los mejores restaurantes de Madrid y disfruta de una experiencia de delivery única
                    </p>
                    <div className="flex flex-col sm:flex-row gap-4 justify-center items-center">
                        <div className="flex items-center gap-2 text-gray-600">
                            <Truck className="w-5 h-5" />
                            <span>Delivery gratis en pedidos +25€</span>
                        </div>
                        <div className="flex items-center gap-2 text-gray-600">
                            <Clock className="w-5 h-5" />
                            <span>Entrega en 30 min promedio</span>
                        </div>
                    </div>
                </section>

                <section className="mb-12">
                    <h2 className="text-2xl font-bold text-gray-900 mb-6">Explora por categorías</h2>
                    <div className="grid grid-cols-2 md:grid-cols-4 lg:grid-cols-8 gap-4">
                        {categories.map((category, index) => (
                            <Link
                                key={index}
                                to={`${ROUTES.RESTAURANTS}?category=${encodeURIComponent(category.name.toLowerCase())}`}
                                className="group"
                            >
                                <div className="bg-white rounded-xl p-4 text-center shadow-sm border border-gray-100 hover:shadow-md hover:border-primary-200 transition-all duration-200 group-hover:scale-105">
                                    <div className="text-3xl mb-2">{category.emoji}</div>
                                    <div className="text-sm font-medium text-gray-900 mb-1">{category.name}</div>
                                    <div className="text-xs text-gray-500">{category.count} restaurantes</div>
                                </div>
                            </Link>
                        ))}
                    </div>
                </section>

                <section className="mb-12">
                    <div className="flex items-center justify-between mb-6">
                        <h2 className="text-2xl font-bold text-gray-900">Restaurantes populares</h2>
                        <Link to={ROUTES.RESTAURANTS}>
                            <Button variant="outline" size="sm">
                                Ver todos
                            </Button>
                        </Link>
                    </div>

                    {isLoading && (
                        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                            {[1, 2, 3].map((i) => (
                                <div key={i} className="h-72 animate-pulse rounded-xl bg-gray-200" />
                            ))}
                        </div>
                    )}

                    {isError && (
                        <div className="rounded-xl border border-red-200 bg-red-50 p-6 text-center text-red-800">
                            <p className="mb-2 font-medium">No se pudieron cargar los restaurantes</p>
                            <p className="text-sm mb-4">{error?.message}</p>
                            <Button variant="outline" size="sm" onClick={() => refetch()}>
                                Reintentar
                            </Button>
                        </div>
                    )}

                    {!isLoading && !isError && (
                        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                            {restaurants.map((restaurant) => {
                                const img = restaurant.coverImage || restaurant.imageUrl || '';
                                const badge = restaurant.cuisine || 'Restaurante';
                                return (
                                    <Link key={restaurant.id} to={ROUTES.RESTAURANT_DETAIL(restaurant.id)}>
                                        <Card className="h-full group">
                                            <div className="aspect-video relative overflow-hidden">
                                                <img
                                                    src={img}
                                                    alt={restaurant.name}
                                                    className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
                                                />
                                                <div className="absolute top-3 right-3 bg-white/90 backdrop-blur-sm px-2 py-1 rounded-full text-xs font-medium">
                                                    {badge}
                                                </div>
                                            </div>

                                            <div className="p-4">
                                                <h3 className="text-lg font-bold text-gray-900 mb-2">{restaurant.name}</h3>
                                                <p className="text-sm text-gray-600 mb-4 line-clamp-2">{restaurant.description}</p>

                                                <div className="flex items-center justify-between text-sm">
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
                                            </div>

                                            <div className="px-4 pb-4">
                                                <div className="flex items-center justify-between w-full">
                                                    <span className="text-green-600 font-medium text-sm">
                                                        Delivery €{restaurant.deliveryFee}
                                                    </span>
                                                    <Button size="sm" variant="primary">
                                                        Ver Menú
                                                    </Button>
                                                </div>
                                            </div>
                                        </Card>
                                    </Link>
                                );
                            })}
                        </div>
                    )}
                </section>

                <section className="bg-gradient-to-r from-primary-500 to-primary-600 rounded-2xl p-8 md:p-12 text-center text-white">
                    <h2 className="text-3xl font-bold mb-4">¿Tienes un restaurante?</h2>
                    <p className="text-primary-100 mb-6 max-w-2xl mx-auto">
                        Únete a nuestra plataforma y llega a miles de clientes en Madrid. Gestiona tus pedidos fácilmente y aumenta
                        tus ventas.
                    </p>
                    <Button variant="secondary" size="lg">
                        Registrar mi restaurante
                    </Button>
                </section>
            </div>
        </Layout>
    );
};

export default HomePage;
