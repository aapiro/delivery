import React from 'react';
import { useGetRestaurantById } from '../../api/generated/administrative-api/administrative-api';
import { X, Star, Clock, MapPin, Utensils, Euro, CheckCircle2 } from 'lucide-react';
import Card from '../../components/ui/Card';
import { Restaurant, MenuCategory } from '../../api/generated/model';

interface RestaurantDetailModalProps {
    restaurantId: number | null;
    isOpen: boolean;
    onClose: () => void;
}

const RestaurantDetailModal: React.FC<RestaurantDetailModalProps> = ({ restaurantId, isOpen, onClose }) => {
    // Solo pedimos los datos si el modal está abierto y tenemos un ID
    const { data: restaurant, isLoading, error } = useGetRestaurantById(restaurantId as number, {
        query: {
                enabled: isOpen && !!restaurantId,
                // Refresca los datos cada vez que el componente se monta (se abre el modal)
                refetchOnWindowFocus: true,
                staleTime: 0 // Considera los datos viejos inmediatamente
            }
    });


    if (!isOpen) return null;

    return (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/50 backdrop-blur-sm">
            <div className="bg-white w-full max-w-2xl max-h-[90vh] rounded-2xl shadow-2xl overflow-hidden flex flex-col animate-in fade-in zoom-in duration-200">

                {/* Header con Imagen */}
                <div className="relative h-48 bg-gray-200 flex-shrink-0">
                    {restaurant?.imageUrl ? (
                        <img src={restaurant.imageUrl} alt="" className="w-full h-full object-cover" />
                    ) : (
                        <div className="w-full h-full flex items-center justify-center text-gray-400">
                            {restaurant?.name?.charAt(0)}
                        </div>
                    )}
                    <button
                        onClick={onClose}
                        className="absolute top-4 right-4 p-2 bg-white/90 hover:bg-white rounded-full shadow-lg transition-colors"
                    >
                        <X className="w-5 h-5 text-gray-700" />
                    </button>
                    <div className="absolute bottom-4 left-4">
                        <span className={`px-3 py-1 rounded-full text-xs font-bold uppercase shadow-sm ${
                            restaurant?.isOpen ? 'bg-green-500 text-white' : 'bg-red-500 text-white'
                        }`}>
                            {restaurant?.isOpen ? 'Abierto' : 'Cerrado'}
                        </span>
                    </div>
                </div>

                {/* Contenido con Scroll */}
                <div className="p-6 overflow-y-auto space-y-6">
                    {isLoading ? (
                        <div className="space-y-4 animate-pulse">
                            <div className="h-8 bg-gray-200 rounded w-3/4"></div>
                            <div className="h-20 bg-gray-200 rounded"></div>
                        </div>
                    ) : error ? (
                        <div className="text-center text-red-500">
                            {/* ✅ Manejo de error seguro */}
                            {(error as any)?.message || "Error al cargar"}
                        </div>
                    ) : (
                        <>
                            <div>
                                <h2 className="text-2xl font-bold text-gray-900">{restaurant?.name}</h2>
                                <p className="text-gray-600 mt-2">{restaurant?.description}</p>
                            </div>

                            <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                                <InfoItem icon={<Utensils className="w-4 h-4" />} label="Cocina" value={restaurant?.cuisine} />
                                <InfoItem icon={<Star className="w-4 h-4 text-yellow-500" />} label="Rating" value={`${restaurant?.rating} (${restaurant?.reviewCount})`} />
                                <InfoItem icon={<Clock className="w-4 h-4" />} label="Entrega" value={`${restaurant?.deliveryTimeMin}-${restaurant?.deliveryTimeMax}m`} />
                                <InfoItem icon={<Euro className="w-4 h-4" />} label="Envío" value={`€${restaurant?.deliveryFee}`} />
                            </div>

                            {/* Categorías de Menú */}
                            {restaurant?.categories && restaurant.categories.length > 0 && (
                                <div className="space-y-3">
                                    <h3 className="font-bold text-gray-800 border-b pb-2">Menú / Categorías</h3>
                                    <div className="flex flex-wrap gap-2">
                                        {/* 2. AQUÍ: Añadimos el tipo : MenuCategory al parámetro cat */}
                                        {restaurant.categories.map((cat: MenuCategory) => (
                                            <span
                                                key={cat.id}
                                                className="bg-indigo-50 text-indigo-700 px-3 py-1 rounded-lg text-sm font-medium border border-indigo-100"
                                            >
                    {cat.name}
                </span>
                                        ))}
                                    </div>
                                </div>
                            )}
                        </>
                    )}
                </div>

                {/* Footer */}
                <div className="p-4 border-t bg-gray-50 flex justify-end">
                    <button
                        onClick={onClose}
                        className="px-6 py-2 bg-gray-900 text-white rounded-xl font-bold hover:bg-gray-800 transition-colors"
                    >
                        Cerrar Vista
                    </button>
                </div>
            </div>
        </div>
    );
};

// Subcomponente de Info para limpieza
const InfoItem = ({ icon, label, value }: { icon: any, label: string, value: any }) => (
    <div className="bg-gray-50 p-3 rounded-xl border border-gray-100">
        <div className="flex items-center gap-1.5 text-gray-400 text-xs mb-1">
            {icon} <span>{label}</span>
        </div>
        <div className="text-sm font-bold text-gray-800">{value || 'N/A'}</div>
    </div>
);

export default RestaurantDetailModal;