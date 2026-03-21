import React, { useState } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import { ArrowLeft, MapPin, Clock, CheckCircle, Truck, ChefHat } from 'lucide-react';
import Layout from '../components/layout/Layout';
import Card from '../components/ui/Card';
import Button from '../components/ui/Button';
import { APP_CONFIG, ROUTES } from '../constants';
import { OrderStatus } from '../types';
import { api } from '../services/api';
import { unwrapEntity } from '../utils/apiPayload';
import { useNotificationStore } from '../store';

type BackendOrder = {
    id: number;
    status: string;
    restaurant?: {
        id: number;
        name: string;
        address?: string;
        phone?: string;
    };
    items?: Array<{
        id: number;
        quantity: number;
        price: number | string;
        dish?: {
            id: number;
            name?: string;
            imageUrl?: string;
            image?: string;
        };
    }>;
    deliveryAddress?: string;
    totalAmount?: number | string;
    orderDate?: string;
    estimatedDeliveryTime?: string;
};

const mapBackendStatusToUi = (backendStatus?: string): OrderStatus => {
    switch ((backendStatus || '').toUpperCase()) {
        case 'PENDING':
            return OrderStatus.PENDING;
        case 'CONFIRMED':
            return OrderStatus.CONFIRMED;
        case 'PREPARING':
            return OrderStatus.PREPARING;
        case 'READY_FOR_PICKUP':
            return OrderStatus.READY;
        case 'ON_THE_WAY':
            return OrderStatus.OUT_FOR_DELIVERY;
        case 'DELIVERED':
            return OrderStatus.DELIVERED;
        case 'CANCELLED':
            return OrderStatus.CANCELLED;
        default:
            return OrderStatus.PENDING;
    }
};

const statusConfig = {
    [OrderStatus.PENDING]: {
        icon: Clock,
        title: 'Pedido Recibido',
        description: 'Hemos recibido tu pedido y lo estamos procesando',
        color: 'text-yellow-600',
        bgColor: 'bg-yellow-50',
        borderColor: 'border-yellow-200'
    },
    [OrderStatus.CONFIRMED]: {
        icon: CheckCircle,
        title: 'Pedido Confirmado',
        description: 'El restaurante ha confirmado tu pedido',
        color: 'text-blue-600',
        bgColor: 'bg-blue-50',
        borderColor: 'border-blue-200'
    },
    [OrderStatus.PREPARING]: {
        icon: ChefHat,
        title: 'Preparando tu Pedido',
        description: 'El chef está preparando tu deliciosa comida',
        color: 'text-orange-600',
        bgColor: 'bg-orange-50',
        borderColor: 'border-orange-200'
    },
    [OrderStatus.READY]: {
        icon: CheckCircle,
        title: 'Pedido Listo',
        description: 'Tu pedido está listo para ser entregado',
        color: 'text-green-600',
        bgColor: 'bg-green-50',
        borderColor: 'border-green-200'
    },
    [OrderStatus.OUT_FOR_DELIVERY]: {
        icon: Truck,
        title: 'En Camino',
        description: 'Tu pedido está en camino a tu dirección',
        color: 'text-purple-600',
        bgColor: 'bg-purple-50',
        borderColor: 'border-purple-200'
    },
    [OrderStatus.DELIVERED]: {
        icon: CheckCircle,
        title: 'Entregado',
        description: '¡Tu pedido ha sido entregado! ¡Disfrútalo!',
        color: 'text-green-600',
        bgColor: 'bg-green-50',
        borderColor: 'border-green-200'
    },
    [OrderStatus.CANCELLED]: {
        icon: Clock,
        title: 'Pedido Cancelado',
        description: 'Tu pedido ha sido cancelado',
        color: 'text-red-600',
        bgColor: 'bg-red-50',
        borderColor: 'border-red-200'
    }
};

const OrderDetailPage: React.FC = () => {
    const { id } = useParams<{ id: string }>();
    const navigate = useNavigate();
    const { addNotification } = useNotificationStore();
    const [isCancelling, setIsCancelling] = useState(false);

    const orderId = Number(id);

    const {
        data: rawOrder,
        isLoading,
        error,
    } = useQuery({
        queryKey: ['order-detail', orderId],
        enabled: Number.isFinite(orderId) && orderId > 0,
        queryFn: async () => {
            const res = await api.get<any>(`/orders/${orderId}`);
            return unwrapEntity<BackendOrder>(res) as BackendOrder;
        },
    });

    const order = rawOrder;

    if (isLoading) {
        return (
            <Layout>
                <div className="max-w-4xl mx-auto px-4 py-8">
                    <div className="animate-pulse rounded-lg bg-gray-100 h-24" />
                </div>
            </Layout>
        );
    }

    if (!order || error) {
        return (
            <Layout>
                <div className="max-w-4xl mx-auto px-4 py-8 text-center">
                    <h1 className="text-2xl font-bold text-gray-900 mb-2">Pedido no encontrado</h1>
                    <p className="text-gray-600 mb-6">Revisa el id del pedido o vuelve a tu lista.</p>
                    <Link to={ROUTES.ORDERS}>
                        <Button variant="primary">Mis pedidos</Button>
                    </Link>
                </div>
            </Layout>
        );
    }

    const uiStatus = mapBackendStatusToUi(order.status);
    const currentStatus = statusConfig[uiStatus];
    const StatusIcon = currentStatus.icon;

    const formatTime = (timeString: string) => {
        return new Date(timeString).toLocaleString('es-ES', {
            day: 'numeric',
            month: 'short',
            hour: '2-digit',
            minute: '2-digit'
        });
    };

    const restaurant = order.restaurant;
    const items = order.items ?? [];
    const itemsTotal = items.reduce((sum, item) => {
        const qty = Number(item.quantity ?? 0);
        const unit = Number(item.price ?? 0);
        return sum + qty * unit;
    }, 0);
    const totalAmount = Number(order.totalAmount ?? itemsTotal);

    const timelineSequence: OrderStatus[] = [
        OrderStatus.PENDING,
        OrderStatus.CONFIRMED,
        OrderStatus.PREPARING,
        OrderStatus.READY,
        OrderStatus.OUT_FOR_DELIVERY,
        OrderStatus.DELIVERED,
    ];
    const progressIndex = timelineSequence.indexOf(uiStatus);
    const progress = progressIndex >= 0 ? progressIndex : 2; // CANCELLED -> marcar hasta PREPARING

    return (
        <Layout>
            <div className="max-w-4xl mx-auto px-4 py-8">
                {/* Header */}
                <Link
                    to={ROUTES.ORDERS}
                    className="inline-flex items-center gap-2 text-primary-500 hover:text-primary-600 mb-6 transition-colors"
                >
                    <ArrowLeft className="w-4 h-4" />
                    Mis pedidos
                </Link>

                <div className="mb-6">
                    <h1 className="text-3xl font-bold text-gray-900 mb-2">
                        Pedido #{order.id}
                    </h1>
                    <p className="text-gray-600">
                        Realizado el {order.orderDate ? formatTime(order.orderDate) : '—'}
                    </p>
                </div>

                <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                    {/* Estado del pedido y detalles */}
                    <div className="lg:col-span-2 space-y-6">

                        {/* Estado actual */}
                        <Card padding="lg" className={`${currentStatus.bgColor} ${currentStatus.borderColor} border-2`}>
                            <div className="flex items-center gap-4">
                                <div className={`w-12 h-12 rounded-full ${currentStatus.bgColor} flex items-center justify-center`}>
                                    <StatusIcon className={`w-6 h-6 ${currentStatus.color}`} />
                                </div>
                                <div>
                                    <h2 className={`text-xl font-bold ${currentStatus.color}`}>
                                        {currentStatus.title}
                                    </h2>
                                    <p className={`${currentStatus.color} opacity-80`}>
                                        {currentStatus.description}
                                    </p>
                                    {uiStatus === OrderStatus.OUT_FOR_DELIVERY && (
                                        <p className="text-sm mt-2 font-medium">
                                            Llegada estimada: {order.estimatedDeliveryTime}
                                        </p>
                                    )}
                                </div>
                            </div>
                        </Card>

                        {/* Timeline del pedido */}
                        <Card padding="lg">
                            <h3 className="font-bold text-gray-900 mb-4">Estado del Pedido</h3>
                            <div className="space-y-4">
                                {[
                                    { status: OrderStatus.PENDING, time: '18:45' },
                                    { status: OrderStatus.CONFIRMED, time: '18:47' },
                                    { status: OrderStatus.PREPARING, time: '18:50' },
                                    { status: OrderStatus.READY, time: '19:15' },
                                    { status: OrderStatus.OUT_FOR_DELIVERY, time: '19:20' },
                                    { status: OrderStatus.DELIVERED, time: '19:30' }
                                ].map((step, index) => {
                                    const config = statusConfig[step.status];
                                    const StepIcon = config.icon;
                                    const active = index <= progress;

                                    return (
                                        <div key={step.status} className="flex items-center gap-3">
                                            <div className={`w-8 h-8 rounded-full flex items-center justify-center ${
                                                active
                                                    ? `${config.bgColor} ${config.borderColor} border-2`
                                                    : 'bg-gray-100 border-2 border-gray-200'
                                            }`}>
                                                <StepIcon className={`w-4 h-4 ${active ? config.color : 'text-gray-400'}`} />
                                            </div>
                                            <div className="flex-1">
                                                <div className={`font-medium ${active ? 'text-gray-900' : 'text-gray-500'}`}>
                                                    {config.title}
                                                </div>
                                                <div className="text-sm text-gray-500">{step.time}</div>
                                            </div>
                                        </div>
                                    );
                                })}
                            </div>
                        </Card>

                        {/* Información del restaurante */}
                        <Card padding="lg">
                            <h3 className="font-bold text-gray-900 mb-4">Restaurante</h3>
                            <div className="flex items-center gap-4">
                                <div className="w-12 h-12 bg-primary-100 rounded-lg flex items-center justify-center">
                                    <span className="text-primary-600 font-bold">🍴</span>
                                </div>
                                <div>
                                    <h4 className="font-medium text-gray-900">{restaurant?.name ?? 'Restaurante'}</h4>
                                    <p className="text-gray-600 text-sm">{restaurant?.address ?? ''}</p>
                                    <p className="text-gray-600 text-sm">{restaurant?.phone ?? ''}</p>
                                </div>
                            </div>
                        </Card>

                        {/* Dirección de entrega */}
                        <Card padding="lg">
                            <div className="flex items-center gap-2 mb-3">
                                <MapPin className="w-5 h-5 text-primary-500" />
                                <h3 className="font-bold text-gray-900">Dirección de Entrega</h3>
                            </div>
                            <div className="text-gray-700">
                                <p className="whitespace-pre-line">{order.deliveryAddress ?? ''}</p>
                            </div>
                        </Card>
                    </div>

                    {/* Resumen y acciones */}
                    <div className="lg:col-span-1">
                        <Card padding="lg" className="sticky top-24">
                            <h3 className="font-bold text-gray-900 mb-4">Resumen</h3>

                            {/* Items */}
                            <div className="space-y-3 mb-6">
                                {items.map((item) => {
                                    const dish = item.dish;
                                    const name = dish?.name ?? 'Plato';
                                    const image = dish?.imageUrl ?? dish?.image ?? '';
                                    const qty = Number(item.quantity ?? 0);
                                    const unit = Number(item.price ?? 0);
                                    const lineTotal = qty * unit;

                                    return (
                                        <div key={item.id} className="flex gap-3">
                                            <img
                                                src={image}
                                                alt={name}
                                                className="w-12 h-12 object-cover rounded-lg"
                                            />
                                            <div className="flex-1">
                                                <h4 className="font-medium text-gray-900 text-sm">{name}</h4>
                                                <div className="flex items-center justify-between text-sm">
                                                    <span className="text-gray-600">x{qty}</span>
                                                    <span className="font-medium">${lineTotal.toFixed(2)}</span>
                                                </div>
                                            </div>
                                        </div>
                                    );
                                })}
                            </div>

                            {/* Totales */}
                            <div className="space-y-2 text-sm border-t pt-4 mb-6">
                                <div className="flex justify-between">
                                    <span className="text-gray-600">Subtotal</span>
                                    <span className="font-medium">${itemsTotal.toFixed(2)}</span>
                                </div>
                                <hr />
                                <div className="flex justify-between text-lg font-bold">
                                    <span>Total</span>
                                    <span className="text-primary-500">${totalAmount.toFixed(2)}</span>
                                </div>
                            </div>

                            {/* Método de pago */}
                            <div className="text-sm text-gray-600 mb-6">
                                <strong>Pago:</strong> Pendiente de integración
                            </div>

                            {/* Acciones */}
                            <div className="space-y-3">
                                {uiStatus !== OrderStatus.DELIVERED && uiStatus !== OrderStatus.CANCELLED && (
                                    <Button
                                        variant="outline"
                                        fullWidth
                                        disabled={isCancelling}
                                        onClick={async () => {
                                            if (!window.confirm('¿Cancelar este pedido?')) return;
                                            try {
                                                setIsCancelling(true);
                                                await api.delete<any>(`/orders/${order.id}`);
                                                addNotification({
                                                    type: 'success',
                                                    title: 'Pedido cancelado',
                                                    message: `Tu pedido #${order.id} fue cancelado.`,
                                                    duration: 4000,
                                                });
                                                navigate(ROUTES.ORDERS);
                                            } catch (e) {
                                                addNotification({
                                                    type: 'error',
                                                    title: 'No se pudo cancelar',
                                                    message: 'Intenta nuevamente más tarde.',
                                                    duration: 4000,
                                                });
                                            } finally {
                                                setIsCancelling(false);
                                            }
                                        }}
                                    >
                                        Cancelar Pedido
                                    </Button>
                                )}

                                <Link to={ROUTES.RESTAURANTS}>
                                    <Button variant="primary" fullWidth>
                                        Hacer Nuevo Pedido
                                    </Button>
                                </Link>

                                <Button
                                    variant="ghost"
                                    fullWidth
                                    onClick={() => {
                                        window.location.href = `mailto:${APP_CONFIG.CONTACT_EMAIL}?subject=${encodeURIComponent(
                                            `Soporte - Pedido #${order.id}`
                                        )}`;
                                    }}
                                >
                                    Contactar Soporte
                                </Button>
                            </div>
                        </Card>
                    </div>
                </div>
            </div>
        </Layout>
    );
};

export default OrderDetailPage;