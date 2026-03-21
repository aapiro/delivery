import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { Trash2, Edit3, Plus, Search } from 'lucide-react';
import { adminDishes, adminRestaurants, adminMenuCategories } from '../../services/admin';
import { Dish, AdminPermission } from '../../types';
import { ROUTES } from '../../constants';
import { useAdminStore } from '../../store/adminStore';
import Button from '../../components/ui/Button';
import Input from '../../components/ui/Input';
import Card from '../../components/ui/Card';
import {
    AdminPageHeader,
    AdminSectionCard,
    AdminQueryBoundary,
    AdminEmptyState,
} from '../../components/admin/common';

const PAGE_SIZE = 20;

const DishesManagement: React.FC = () => {
    const queryClient = useQueryClient();
    const { hasPermission } = useAdminStore();
    const [page, setPage] = useState(1);
    const [searchTerm, setSearchTerm] = useState('');
    const [selectedRestaurant, setSelectedRestaurant] = useState<number | undefined>();
    const [selectedCategory, setSelectedCategory] = useState<number | undefined>();

    const { data: restaurantsPage } = useQuery({
        queryKey: ['admin-restaurants', 'options'],
        queryFn: () => adminRestaurants.getAll(undefined, 1, 100),
    });

    const { data: menuCategories = [], isLoading: loadingMenuCats } = useQuery({
        queryKey: ['admin-menu-categories', 'filter', selectedRestaurant],
        queryFn: () => adminMenuCategories.getByRestaurant(selectedRestaurant!),
        enabled: selectedRestaurant != null && selectedRestaurant > 0,
    });

    useEffect(() => {
        setSelectedCategory(undefined);
    }, [selectedRestaurant]);

    const {
        data,
        isLoading,
        error,
        refetch,
    } = useQuery({
        queryKey: ['admin-dishes', searchTerm, selectedRestaurant, selectedCategory, page],
        queryFn: () =>
            adminDishes.getAll(
                {
                    ...(searchTerm.trim() && { search: searchTerm.trim() }),
                    ...(selectedRestaurant != null && { restaurantId: selectedRestaurant }),
                    ...(selectedCategory != null && { categoryId: selectedCategory }),
                },
                page,
                PAGE_SIZE
            ),
    });

    const deleteMutation = useMutation({
        mutationFn: (id: number) => adminDishes.delete(id),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['admin-dishes'] });
        },
    });

    const handleDelete = (dish: Dish) => {
        if (!window.confirm(`¿Eliminar el plato "${dish.name}"?`)) return;
        deleteMutation.mutate(dish.id);
    };

    const dishes = data?.data ?? [];
    const pagination = data?.pagination;
    const total = pagination?.total ?? 0;

    const restaurantOptions = restaurantsPage?.data ?? [];
    const categoryOptions = menuCategories;

    return (
        <AdminQueryBoundary
            isLoading={isLoading}
            error={error}
            errorTitle="Error al cargar platos"
            onRetry={() => refetch()}
        >
            <div className="space-y-6">
                <AdminPageHeader
                    title="Gestión de platos"
                    description={`${total} plato${total === 1 ? '' : 's'} en esta vista`}
                    actions={
                        hasPermission(AdminPermission.CREATE_DISHES) ? (
                            <Link to={ROUTES.ADMIN.DISH_CREATE}>
                                <Button type="button" className="bg-indigo-600 hover:bg-indigo-700">
                                    <Plus className="mr-2 h-4 w-4" />
                                    Nuevo plato
                                </Button>
                            </Link>
                        ) : undefined
                    }
                />

                <AdminSectionCard title="Filtros">
                    <div className="grid grid-cols-1 gap-4 md:grid-cols-3">
                        <div>
                            <label className="mb-2 block text-sm font-medium text-gray-700">Buscar</label>
                            <div className="relative">
                                <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-gray-400" />
                                <Input
                                    type="text"
                                    placeholder="Nombre del plato..."
                                    value={searchTerm}
                                    onChange={(e) => {
                                        setSearchTerm(e.target.value);
                                        setPage(1);
                                    }}
                                    className="pl-10"
                                />
                            </div>
                        </div>
                        <div>
                            <label className="mb-2 block text-sm font-medium text-gray-700">Restaurante</label>
                            <select
                                value={selectedRestaurant ?? ''}
                                onChange={(e) => {
                                    setSelectedRestaurant(e.target.value ? Number(e.target.value) : undefined);
                                    setPage(1);
                                }}
                                className="block w-full rounded-lg border border-gray-300 px-3 py-2 text-sm shadow-sm focus:border-blue-500 focus:outline-none focus:ring-2 focus:ring-blue-500"
                            >
                                <option value="">Todos los restaurantes</option>
                                {restaurantOptions.map((r) => (
                                    <option key={r.id} value={r.id}>
                                        {r.name}
                                    </option>
                                ))}
                            </select>
                        </div>
                        <div>
                            <label className="mb-2 block text-sm font-medium text-gray-700">
                                Categoría de menú
                            </label>
                            <select
                                value={selectedCategory ?? ''}
                                onChange={(e) => {
                                    setSelectedCategory(e.target.value ? Number(e.target.value) : undefined);
                                    setPage(1);
                                }}
                                disabled={!selectedRestaurant || loadingMenuCats}
                                className="block w-full rounded-lg border border-gray-300 px-3 py-2 text-sm shadow-sm focus:border-blue-500 focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:bg-gray-100"
                            >
                                <option value="">
                                    {selectedRestaurant ? 'Todas las categorías' : 'Elija restaurante'}
                                </option>
                                {categoryOptions.map((c) => (
                                    <option key={c.id} value={c.id}>
                                        {c.name}
                                    </option>
                                ))}
                            </select>
                        </div>
                    </div>
                </AdminSectionCard>

                {dishes.length === 0 ? (
                    <AdminEmptyState
                        title="Sin platos"
                        message="No hay platos con estos filtros o aún no hay datos en el catálogo."
                    />
                ) : (
                    <Card className="overflow-hidden p-0">
                        <div className="overflow-x-auto">
                            <table className="min-w-full divide-y divide-gray-200">
                                <thead className="bg-gray-50">
                                    <tr>
                                        <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500">
                                            Nombre
                                        </th>
                                        <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500">
                                            Categoría
                                        </th>
                                        <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500">
                                            Precio
                                        </th>
                                        <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500">
                                            Estado
                                        </th>
                                        <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500">
                                            Acciones
                                        </th>
                                    </tr>
                                </thead>
                                <tbody className="divide-y divide-gray-200 bg-white">
                                    {dishes.map((dish) => (
                                        <tr key={dish.id} className="hover:bg-gray-50">
                                            <td className="whitespace-nowrap px-6 py-4">
                                                <div className="text-sm font-medium text-gray-900">{dish.name}</div>
                                                {dish.restaurant?.name != null && (
                                                    <div className="text-xs text-gray-500">{dish.restaurant.name}</div>
                                                )}
                                            </td>
                                            <td className="whitespace-nowrap px-6 py-4 text-sm text-gray-700">
                                                {dish.category?.name ?? '—'}
                                            </td>
                                            <td className="whitespace-nowrap px-6 py-4 text-sm text-gray-900">
                                                €{Number(dish.price).toFixed(2)}
                                            </td>
                                            <td className="whitespace-nowrap px-6 py-4">
                                                <span
                                                    className={`inline-flex rounded-full px-2 py-1 text-xs font-semibold ${
                                                        dish.isAvailable
                                                            ? 'bg-green-100 text-green-800'
                                                            : 'bg-red-100 text-red-800'
                                                    }`}
                                                >
                                                    {dish.isAvailable ? 'Disponible' : 'No disponible'}
                                                </span>
                                            </td>
                                            <td className="whitespace-nowrap px-6 py-4 text-sm font-medium">
                                                <div className="flex space-x-2">
                                                    {hasPermission(AdminPermission.EDIT_DISHES) ? (
                                                        <Link
                                                            to={ROUTES.ADMIN.DISH_EDIT(dish.id)}
                                                            className="text-indigo-600 hover:text-indigo-900"
                                                            title="Editar"
                                                        >
                                                            <Edit3 className="h-4 w-4" />
                                                        </Link>
                                                    ) : (
                                                        <span className="text-gray-300">
                                                            <Edit3 className="h-4 w-4" />
                                                        </span>
                                                    )}
                                                    {hasPermission(AdminPermission.DELETE_DISHES) ? (
                                                        <button
                                                            type="button"
                                                            onClick={() => handleDelete(dish)}
                                                            disabled={deleteMutation.isPending}
                                                            className="text-red-600 hover:text-red-900"
                                                        >
                                                            <Trash2 className="h-4 w-4" />
                                                        </button>
                                                    ) : (
                                                        <span className="text-gray-300">
                                                            <Trash2 className="h-4 w-4" />
                                                        </span>
                                                    )}
                                                </div>
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        </div>
                    </Card>
                )}

                {pagination && pagination.totalPages > 1 && (
                    <div className="flex flex-col items-center justify-between gap-4 border-t border-gray-200 pt-4 sm:flex-row">
                        <p className="text-sm text-gray-700">
                            Página{' '}
                            <span className="font-medium">
                                {pagination.page} de {pagination.totalPages}
                            </span>
                            {' · '}
                            {pagination.total} resultados
                        </p>
                        <div className="flex gap-2">
                            <Button
                                type="button"
                                variant="outline"
                                disabled={page <= 1}
                                onClick={() => setPage((p) => Math.max(1, p - 1))}
                            >
                                Anterior
                            </Button>
                            <Button
                                type="button"
                                variant="outline"
                                disabled={page >= pagination.totalPages}
                                onClick={() => setPage((p) => Math.min(pagination.totalPages, p + 1))}
                            >
                                Siguiente
                            </Button>
                        </div>
                    </div>
                )}
            </div>
        </AdminQueryBoundary>
    );
};

export default DishesManagement;
