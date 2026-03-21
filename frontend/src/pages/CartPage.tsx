import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import Layout from '../components/layout/Layout';
import Card from '../components/ui/Card';
import Button from '../components/ui/Button';
import Input from '../components/ui/Input';
import { ORDER_CONFIG, ROUTES } from '../constants';
import { useCart } from '../hooks/useCart';

const CartPage: React.FC = () => {
    const navigate = useNavigate();
    const {
        items,
        restaurantId,
        subtotal,
        deliveryFee,
        tax,
        total,
        itemCount,
        meetsMinimum,
        validateCheckout,
        updateQuantity,
        removeFromCart,
        clearCart,
    } = useCart();

    const handleCheckout = () => {
        if (!validateCheckout()) return;
        navigate(ROUTES.CHECKOUT);
    };

    if (items.length === 0) {
        return (
            <Layout>
                <div className="max-w-2xl mx-auto px-4 py-12 text-center">
                    <div className="text-6xl mb-4">🛒</div>
                    <h1 className="text-2xl font-bold text-gray-900 mb-4">Tu carrito está vacío</h1>
                    <p className="text-gray-600 mb-6">Agrega productos antes de continuar.</p>
                    <Link to={ROUTES.RESTAURANTS}>
                        <Button variant="primary">Explorar Restaurantes</Button>
                    </Link>
                </div>
            </Layout>
        );
    }

    return (
        <Layout>
            <div className="max-w-4xl mx-auto px-4 py-8">
                <div className="flex items-start justify-between gap-6 mb-6">
                    <div>
                        <h1 className="text-3xl font-bold text-gray-900">Carrito</h1>
                        <p className="text-gray-600 mt-1">{itemCount} items</p>
                        {restaurantId ? (
                            <p className="text-gray-500 text-sm mt-2">Orden de un solo restaurante por vez.</p>
                        ) : null}
                    </div>

                    <Button variant="outline" onClick={() => clearCart()} disabled={items.length === 0}>
                        Vaciar carrito
                    </Button>
                </div>

                <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                    <div className="lg:col-span-2 space-y-4">
                        {items.map((item) => (
                            <Card key={item.id} padding="lg" className="border border-gray-100">
                                <div className="flex gap-4 items-start">
                                    <img
                                        src={item.dish.image}
                                        alt={item.dish.name}
                                        className="w-20 h-20 rounded-lg object-cover bg-gray-50"
                                    />

                                    <div className="flex-1">
                                        <div className="flex items-start justify-between gap-4">
                                            <div>
                                                <h3 className="font-bold text-gray-900">{item.dish.name}</h3>
                                                <p className="text-sm text-gray-500">
                                                    ${item.unitPrice.toFixed(2)} / unidad
                                                </p>
                                            </div>
                                            <div className="text-right">
                                                <p className="font-bold text-gray-900">
                                                    ${item.totalPrice.toFixed(2)}
                                                </p>
                                                {item.specialInstructions ? (
                                                    <p className="text-xs text-gray-500 mt-1">Notas: {item.specialInstructions}</p>
                                                ) : null}
                                            </div>
                                        </div>

                                        <div className="mt-4 flex items-center gap-3">
                                            <Button
                                                variant="outline"
                                                onClick={() => updateQuantity(item.id, item.quantity - 1)}
                                                disabled={item.quantity <= 1}
                                            >
                                                -
                                            </Button>

                                            <div className="w-28">
                                                <Input
                                                    type="number"
                                                    label="Cantidad"
                                                    value={item.quantity}
                                                    min={1}
                                                    onChange={(e) => {
                                                        const next = Number(e.target.value);
                                                        if (Number.isFinite(next)) updateQuantity(item.id, next);
                                                    }}
                                                    fullWidth
                                                />
                                            </div>

                                            <Button
                                                variant="outline"
                                                onClick={() => updateQuantity(item.id, item.quantity + 1)}
                                            >
                                                +
                                            </Button>

                                            <div className="flex-1" />

                                            <Button variant="ghost" onClick={() => removeFromCart(item.id)}>
                                                Eliminar
                                            </Button>
                                        </div>
                                    </div>
                                </div>
                            </Card>
                        ))}
                    </div>

                    <div className="space-y-4">
                        <Card padding="lg">
                            <h2 className="text-lg font-bold text-gray-900 mb-4">Resumen</h2>
                            <div className="space-y-2 text-sm">
                                <div className="flex justify-between">
                                    <span className="text-gray-600">Subtotal</span>
                                    <span className="font-medium">${subtotal.toFixed(2)}</span>
                                </div>
                                <div className="flex justify-between">
                                    <span className="text-gray-600">Envío</span>
                                    <span className="font-medium">${deliveryFee.toFixed(2)}</span>
                                </div>
                                <div className="flex justify-between">
                                    <span className="text-gray-600">Impuestos</span>
                                    <span className="font-medium">${tax.toFixed(2)}</span>
                                </div>
                                <hr className="my-3" />
                                <div className="flex justify-between text-base font-bold">
                                    <span>Total</span>
                                    <span className="text-primary-500">${total.toFixed(2)}</span>
                                </div>
                            </div>

                            {!meetsMinimum ? (
                                <p className="text-sm text-amber-700 mt-3">
                                    Falta para el pedido mínimo: ${ORDER_CONFIG.MINIMUM_ORDER_AMOUNT.toFixed(2)}
                                </p>
                            ) : null}
                        </Card>

                        <Button variant="primary" fullWidth onClick={handleCheckout}>
                            Ir a checkout
                        </Button>
                    </div>
                </div>
            </div>
        </Layout>
    );
};

export default CartPage;

