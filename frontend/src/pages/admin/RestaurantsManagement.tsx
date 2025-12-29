import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import {
    Plus, Search, Eye, Edit2, Power,
    Star, Clock, Utensils, AlertCircle
} from 'lucide-react';

// 1. Hooks generados por Orval
import { useListRestaurants, useUpdateRestaurant } from '../../api/generated/administrative-api/administrative-api';
import { RestaurantSummary } from '../../api/generated/model';

// 2. Componentes y Constantes
import { ROUTES } from '../../constants';
import { AdminPermission } from '../../types';
import { useAdminStore } from '../../store/adminStore';
import Button from '../../components/ui/Button';
import Input from '../../components/ui/Input';
import Card from '../../components/ui/Card';
import Pagination from '../../components/ui/Pagination';
import RestaurantDetailModal from "./RestaurantDetailModal";
// ... (imports iguales)
import { useQueryClient } from '@tanstack/react-query'; // Añade este import
import { getListRestaurantsQueryKey } from '../../api/generated/administrative-api/administrative-api'; // Y este

const RestaurantsManagement: React.FC = () => {
    const queryClient = useQueryClient();
    const [page, setPage] = useState(0);
    const [searchTerm, setSearchTerm] = useState('');
    const { hasPermission } = useAdminStore();

    // 1. MEJORA: Pasamos el searchTerm a la API (asumiendo que tu YAML lo soporta)
    // Si tu YAML no tiene 'search', Orval lo ignorará, pero es la forma correcta.
    const { data: response, isLoading, isFetching, error } = useListRestaurants({
        page: page,
        size: 10,
        // search: searchTerm // Descomenta si tu backend acepta búsqueda
    });

    const updateMutation = useUpdateRestaurant();

    const pagedData = response;
    const restaurants = pagedData?.content || [];

    const [selectedId, setSelectedId] = useState<number | null>(null);
    const [isModalOpen, setIsModalOpen] = useState(false);

    const handleViewDetail = (id: number) => {
        setSelectedId(id);
        setIsModalOpen(true);
    };

    // 2. MEJORA: Invalidar la caché después de cambiar el estado (Toggle)
    const handleToggleStatus = (restaurant: RestaurantSummary) => {
        if (window.confirm(`¿Cambiar estado de "${restaurant.name}"?`)) {
            updateMutation.mutate({
                id: restaurant.id,
                data: {
                    ...restaurant,
                    isOpen: !restaurant.isOpen
                }
            }, {
                // Al tener éxito, forzamos a la lista a refrescarse
                onSuccess: () => {
                    queryClient.invalidateQueries({
                        queryKey: getListRestaurantsQueryKey()
                    });
                }
            });
        }
    };

    // ... (Manejo de loading y error igual)

    return (
        <div className="p-6 space-y-6 bg-gray-50 min-h-screen">
            {/* Cabecera */}
            <div className="flex justify-between items-center">
                <div>
                    <h1 className="text-2xl font-bold text-gray-900">Panel de Restaurantes</h1>
                    <p className="text-sm text-gray-500">
                        Total: <span className="font-bold">{pagedData?.totalElements || 0}</span> locales
                    </p>
                </div>
                {hasPermission(AdminPermission.CREATE_RESTAURANTS) && (
                    <Link to={ROUTES.ADMIN.RESTAURANT_CREATE}>
                        <Button className="bg-indigo-600 hover:bg-indigo-700 shadow-sm">
                            <Plus className="w-4 h-4 mr-2" /> Nuevo Restaurante
                        </Button>
                    </Link>
                )}
            </div>

            {/* Barra de Búsqueda */}
            <Card className="p-4">
                <form onSubmit={(e) => { e.preventDefault(); setPage(0); }} className="relative max-w-md">
                    <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400 w-4 h-4" />
                    <Input
                        placeholder="Filtrar por nombre..."
                        className="pl-10"
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                    />
                </form>
            </Card>

            {/* Lista de Restaurantes */}
            <div className="space-y-4">
                {restaurants.length > 0 ? (
                    restaurants.map((restaurant: RestaurantSummary) => (
                        <Card key={restaurant.id} className="p-4 hover:shadow-md transition-shadow bg-white border-gray-100">
                            <div className="flex flex-col md:flex-row items-center gap-6">
                                <div className="w-16 h-16 rounded-lg bg-gray-100 overflow-hidden flex-shrink-0">
                                    <img
                                        src={restaurant.imageUrl || 'https://via.placeholder.com/64'}
                                        alt=""
                                        className="w-full h-full object-cover"
                                    />
                                </div>

                                <div className="flex-1 min-w-0">
                                    <div className="flex items-center gap-3 mb-1">
                                        <h3 className="text-lg font-bold text-gray-900 truncate">
                                            {restaurant.name}
                                        </h3>
                                        <span className={`px-2 py-0.5 rounded-full text-[10px] font-bold uppercase ${
                                            restaurant.isOpen ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'
                                        }`}>
                                            {restaurant.isOpen ? 'Abierto' : 'Cerrado'}
                                        </span>
                                    </div>

                                    <div className="flex flex-wrap gap-x-4 gap-y-1 text-sm text-gray-500">
                                        <div className="flex items-center gap-1">
                                            <Utensils className="w-3.5 h-3.5" /> {restaurant.cuisine}
                                        </div>
                                        <div className="flex items-center gap-1 text-yellow-600 font-medium">
                                            <Star className="w-3.5 h-3.5 fill-current" /> {restaurant.rating}
                                        </div>
                                        <div className="flex items-center gap-1">
                                            <Clock className="w-3.5 h-3.5" /> {restaurant.deliveryTimeMin}-{restaurant.deliveryTimeMax} min
                                        </div>
                                    </div>
                                </div>

                                <div className="flex items-center gap-2 md:border-l md:pl-6 border-gray-100 w-full md:w-auto justify-end">
                                    {/* DETALLE (MODAL) */}
                                    <button
                                        type="button"
                                        onClick={() => handleViewDetail(restaurant.id)}
                                        className="p-2 text-gray-400 hover:text-indigo-600 hover:bg-indigo-50 rounded-lg transition-colors"
                                    >
                                        <Eye className="w-5 h-5" />
                                    </button>

                                    {/* EDITAR (PÁGINA) */}
                                    {hasPermission(AdminPermission.EDIT_RESTAURANTS) && (
                                        <Link to={ROUTES.ADMIN.RESTAURANT_EDIT(restaurant.id)}>
                                            <button className="p-2 text-gray-400 hover:text-blue-600 hover:bg-blue-50 rounded-lg transition-colors">
                                                <Edit2 className="w-5 h-5" />
                                            </button>
                                        </Link>
                                    )}

                                    {/* STATUS (TOGGLE) */}
                                    {hasPermission(AdminPermission.MANAGE_RESTAURANT_STATUS) && (
                                        <button
                                            onClick={() => handleToggleStatus(restaurant)}
                                            disabled={updateMutation.isPending}
                                            className={`p-2 rounded-lg transition-colors ${
                                                restaurant.isOpen ? 'text-green-500 hover:bg-green-50' : 'text-gray-300 hover:bg-gray-100'
                                            }`}
                                        >
                                            <Power className="w-5 h-5" />
                                        </button>
                                    )}
                                </div>
                            </div>
                        </Card>
                    ))
                ) : (
                    <div className="py-20 text-center bg-white rounded-xl border border-dashed border-gray-300 font-medium text-gray-400">
                        No se encontraron restaurantes.
                    </div>
                )}
            </div>

            <Pagination
                currentPage={page}
                totalPages={pagedData?.totalPages || 0}
                totalElements={pagedData?.totalElements || 0}
                pageSize={pagedData?.size || 10}
                onPageChange={(newPage) => setPage(newPage)}
                isLoading={isFetching}
            />

            <RestaurantDetailModal
                restaurantId={selectedId}
                isOpen={isModalOpen}
                onClose={() => {
                    setIsModalOpen(false);
                    setSelectedId(null);
                }}
            />
        </div>
    );
};

export default RestaurantsManagement;