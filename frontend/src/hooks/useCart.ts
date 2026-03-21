import { useMemo } from 'react';
import { useCartStore, useNotificationStore } from '../store';
import { Dish } from '../types';
import { SUCCESS_MESSAGES, ERROR_MESSAGES, ORDER_CONFIG } from '../constants';
import { cartService } from '../services/cart';

export const useCart = () => {
    const cartStore = useCartStore();
    const { addNotification } = useNotificationStore();

    // Calculaciones memoizadas para optimizar performance
    const cartSummary = useMemo(() => {
        const subtotal = cartStore.getSubtotal();
        const deliveryFee = cartStore.getDeliveryFee();
        const tax = cartStore.getTax();
        const total = cartStore.getTotal();
        const itemCount = cartStore.getItemCount();

        return {
            subtotal,
            deliveryFee,
            tax,
            total,
            itemCount,
            hasItems: itemCount > 0,
            meetsMinimum: subtotal >= ORDER_CONFIG.MINIMUM_ORDER_AMOUNT,
            isFreeDelivery: subtotal >= ORDER_CONFIG.FREE_DELIVERY_THRESHOLD,
        };
    }, [cartStore.items]);

    // Función mejorada para agregar al carrito con validaciones
    const addToCart = async (dish: Dish, quantity: number = 1, specialInstructions?: string) => {
        try {
            // Validaciones
            if (!dish.isAvailable) {
                addNotification({
                    type: 'error',
                    title: 'Plato no disponible',
                    message: ERROR_MESSAGES.DISH_UNAVAILABLE,
                    duration: 4000,
                });
                return false;
            }

            if (quantity > ORDER_CONFIG.MAX_ITEMS_PER_DISH) {
                addNotification({
                    type: 'warning',
                    title: 'Cantidad máxima excedida',
                    message: `Máximo ${ORDER_CONFIG.MAX_ITEMS_PER_DISH} unidades por plato`,
                    duration: 4000,
                });
                return false;
            }

            // Verificar si se puede agregar (mismo restaurante)
            if (!cartStore.canAddItem(dish.restaurantId)) {
                addNotification({
                    type: 'warning',
                    title: 'Restaurante diferente',
                    message: 'Solo puedes ordenar de un restaurante a la vez. ¿Quieres limpiar tu carrito?',
                    duration: 6000,
                    action: {
                        label: 'Limpiar carrito',
                        onClick: () => {
                            cartStore.clearCart();
                            // Reintentar agregar después de limpiar
                            setTimeout(() => addToCart(dish, quantity, specialInstructions), 100);
                        }
                    }
                });
                return false;
            }

            const backendItems = await cartService.addToCart(dish.id, quantity, specialInstructions);
            cartStore.setCartFromBackend(backendItems);

            // Notificación de éxito
            addNotification({
                type: 'success',
                title: SUCCESS_MESSAGES.ITEM_ADDED_TO_CART,
                message: `${dish.name} agregado al carrito`,
                duration: 3000,
            });

            return true;
        } catch (error) {
            addNotification({
                type: 'error',
                title: 'Error',
                message: 'No se pudo agregar el producto al carrito',
                duration: 4000,
            });
            return false;
        }
    };

    // Función para actualizar cantidad con validaciones
    const updateQuantity = async (itemId: string, newQuantity: number) => {
        if (newQuantity < 1) {
            await removeFromCart(itemId);
            return;
        }

        if (newQuantity > ORDER_CONFIG.MAX_ITEMS_PER_DISH) {
            addNotification({
                type: 'warning',
                title: 'Cantidad máxima excedida',
                message: `Máximo ${ORDER_CONFIG.MAX_ITEMS_PER_DISH} unidades por plato`,
                duration: 4000,
            });
            return;
        }

        try {
            const backendItems = await cartService.updateCartItem(itemId, newQuantity);
            cartStore.setCartFromBackend(backendItems);
        } catch {
            addNotification({
                type: 'error',
                title: 'Error',
                message: 'No se pudo actualizar el carrito',
                duration: 4000,
            });
        }
    };

    // Función para remover del carrito
    const removeFromCart = async (itemId: string) => {
        try {
            const backendItems = await cartService.removeFromCart(itemId);
            cartStore.setCartFromBackend(backendItems);
            addNotification({
                type: 'info',
                title: 'Producto eliminado',
                message: 'El producto fue eliminado del carrito',
                duration: 3000,
            });
        } catch {
            addNotification({
                type: 'error',
                title: 'Error',
                message: 'No se pudo eliminar el producto',
                duration: 3000,
            });
        }
    };

    // Función para limpiar carrito completo
    const clearCart = async () => {
        try {
            await cartService.clearCart();
            cartStore.clearCart();
            addNotification({
                type: 'info',
                title: 'Carrito vaciado',
                message: 'Se eliminaron todos los productos del carrito',
                duration: 3000,
            });
        } catch {
            addNotification({
                type: 'error',
                title: 'Error',
                message: 'No se pudo vaciar el carrito',
                duration: 3000,
            });
        }
    };

    // Función para validar antes del checkout
    const validateCheckout = () => {
        if (!cartSummary.hasItems) {
            addNotification({
                type: 'error',
                title: 'Carrito vacío',
                message: ERROR_MESSAGES.CART_EMPTY,
                duration: 4000,
            });
            return false;
        }

        if (!cartSummary.meetsMinimum) {
            addNotification({
                type: 'warning',
                title: 'Pedido mínimo no alcanzado',
                message: `${ERROR_MESSAGES.MINIMUM_ORDER} Mínimo: $${ORDER_CONFIG.MINIMUM_ORDER_AMOUNT}`,
                duration: 5000,
            });
            return false;
        }

        return true;
    };

    return {
        // Estado del carrito
        items: cartStore.items,
        restaurantId: cartStore.restaurantId,
        isOpen: cartStore.isOpen,

        // Resumen calculado
        ...cartSummary,

        // Acciones básicas
        addToCart,
        removeFromCart,
        updateQuantity,
        clearCart,

        // Acciones UI
        openCart: cartStore.openCart,
        closeCart: cartStore.closeCart,

        // Validaciones
        canAddItem: cartStore.canAddItem,
        validateCheckout,
    };
};