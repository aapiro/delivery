import React from 'react';
import Header from './Header';
import CartSidebar from '../common/CartSidebar';
import { ToastContainer } from '../ui/Toast';
import { useCartStore } from '../../store';
import { cartService } from '../../services/cart';

interface LayoutProps {
    children: React.ReactNode;
    className?: string;
}

const Layout: React.FC<LayoutProps> = ({ children, className }) => {
    const setCartFromBackend = useCartStore((s) => s.setCartFromBackend);
    const clearCart = useCartStore((s) => s.clearCart);

    React.useEffect(() => {
        let mounted = true;
        cartService.getCart()
            .then((items) => {
                if (mounted) {
                    setCartFromBackend(items);
                }
            })
            .catch(() => {
                if (mounted) {
                    clearCart();
                }
            });
        return () => {
            mounted = false;
        };
    }, [setCartFromBackend, clearCart]);

    return (
        <div className="min-h-screen bg-gray-50 flex flex-col">
            <Header />

            <main className={`flex-1 ${className || ''}`}>
                {children}
            </main>

            {/* Carrito lateral */}
            <CartSidebar />

            {/* Sistema de notificaciones */}
            <ToastContainer />
        </div>
    );
};

export default Layout;