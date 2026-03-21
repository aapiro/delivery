import React from 'react';
import { Link } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import Layout from '../components/layout/Layout';
import Card from '../components/ui/Card';
import Button from '../components/ui/Button';
import { ROUTES } from '../constants';
import { api } from '../services/api';
import { unwrapList } from '../utils/apiPayload';
import { useAuthStore } from '../store';

type BackendOrder = {
    id: number;
    status: string;
    restaurant?: { id?: number; name?: string };
    totalAmount?: number | string;
    orderDate?: string;
    estimatedDeliveryTime?: string;
};

const mapStatusLabel = (backendStatus?: string) => {
    switch ((backendStatus || '').toUpperCase()) {
        case 'PENDING':
        case 'CREATED':
            return 'Pendiente';
        case 'CONFIRMED':
            return 'Confirmado';
        case 'PREPARING':
            return 'Preparando';
        case 'READY':
            return 'Listo';
        case 'ON_THE_WAY':
        case 'OUT_FOR_DELIVERY':
            return 'En camino';
        case 'DELIVERED':
            return 'Entregado';
        case 'CANCELLED':
            return 'Cancelado';
        default:
            return backendStatus || '—';
    }
};

const MyOrdersPage: React.FC = () => {
    const { isAuthenticated } = useAuthStore();

    const { data, isLoading, error } = useQuery({
        queryKey: ['orders', 'me'],
        enabled: isAuthenticated,
        queryFn: async () => {
            const res = await api.get<any>('/orders');
            return unwrapList<BackendOrder>(res);
        },
        staleTime: 2 * 60 * 1000,
    });

    if (!isAuthenticated) {
        return (
            <Layout>
                <div className="max-w-2xl mx-auto px-4 py-12 text-center">
                    <h1 className="text-2xl font-bold text-gray-900 mb-4">Inicia sesión</h1>
                    <p className="text-gray-600 mb-6">Para ver tus pedidos.</p>
                    <Link to={ROUTES.LOGIN}>
                        <Button variant="primary">Ir a login</Button>
                    </Link>
                </div>
            </Layout>
        );
    }

    if (isLoading) {
        return (
            <Layout>
                <div className="max-w-4xl mx-auto px-4 py-8">
                    <div className="animate-pulse rounded-lg bg-gray-100 h-24" />
                </div>
            </Layout>
        );
    }

    if (error) {
        return (
            <Layout>
                <div className="max-w-4xl mx-auto px-4 py-8 text-center">
                    <h1 className="text-2xl font-bold text-gray-900 mb-2">No se pudieron cargar los pedidos</h1>
                    <p className="text-gray-600">{(error as Error).message}</p>
                </div>
            </Layout>
        );
    }

    const orders = data ?? [];

    return (
        <Layout>
            <div className="max-w-4xl mx-auto px-4 py-8">
                <div className="flex items-end justify-between gap-6 mb-6">
                    <div>
                        <h1 className="text-3xl font-bold text-gray-900">Mis pedidos</h1>
                        <p className="text-gray-600 mt-1">{orders.length} pedidos</p>
                    </div>
                </div>

                {orders.length === 0 ? (
                    <div className="rounded-xl border border-gray-100 bg-white p-8 text-center">
                        <p className="text-gray-600">Todavía no tienes pedidos.</p>
                        <div className="mt-4">
                            <Link to={ROUTES.RESTAURANTS}>
                                <Button variant="primary">Explorar Restaurantes</Button>
                            </Link>
                        </div>
                    </div>
                ) : (
                    <div className="space-y-4">
                        {orders.map((order) => (
                            <Card key={order.id} padding="lg">
                                <div className="flex items-start justify-between gap-4">
                                    <div>
                                        <h2 className="font-bold text-gray-900">Pedido #{order.id}</h2>
                                        <p className="text-gray-600 mt-1">
                                            {order.restaurant?.name ? `${order.restaurant.name} • ` : ''}
                                            {order.orderDate ? new Date(order.orderDate).toLocaleString('es-ES') : ''}
                                        </p>
                                        <p className="text-sm text-gray-500 mt-2">
                                            Estado: <span className="font-medium text-gray-900">{mapStatusLabel(order.status)}</span>
                                        </p>
                                    </div>

                                    <div className="text-right">
                                        <p className="font-bold text-primary-600">
                                            ${Number(order.totalAmount ?? 0).toFixed(2)}
                                        </p>
                                        <Link to={ROUTES.ORDER_DETAIL(order.id)}>
                                            <Button variant="outline" className="mt-3">
                                                Ver detalle
                                            </Button>
                                        </Link>
                                    </div>
                                </div>
                            </Card>
                        ))}
                    </div>
                )}
            </div>
        </Layout>
    );
};

export default MyOrdersPage;

