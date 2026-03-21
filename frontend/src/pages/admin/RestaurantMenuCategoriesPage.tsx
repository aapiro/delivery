import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { Pencil, Plus, Trash2 } from 'lucide-react';
import { adminRestaurants, adminRestaurantMenuCategories } from '../../services/admin';
import { AdminMenuCategoryRow, AdminPermission } from '../../types';
import { useAdminStore } from '../../store/adminStore';
import Button from '../../components/ui/Button';
import Input from '../../components/ui/Input';
import Card from '../../components/ui/Card';
import {
    AdminPageHeader,
    AdminSectionCard,
    AdminEmptyState,
    getAdminErrorMessage,
} from '../../components/admin/common';

const queryKey = (restaurantId: number) => ['admin-restaurant-menu-categories', restaurantId] as const;

const RestaurantMenuCategoriesPage: React.FC = () => {
    const queryClient = useQueryClient();
    const { hasPermission } = useAdminStore();
    const [restaurantId, setRestaurantId] = useState<number | ''>('');

    const { data: restaurantsPage } = useQuery({
        queryKey: ['admin-restaurants', 'menu-cat-page'],
        queryFn: () => adminRestaurants.getAll(undefined, 1, 200),
    });

    const rid = typeof restaurantId === 'number' ? restaurantId : 0;

    const {
        data: categories = [],
        isLoading,
        error,
        refetch,
    } = useQuery({
        queryKey: queryKey(rid),
        queryFn: () => adminRestaurantMenuCategories.list(rid),
        enabled: rid > 0,
    });

    const [createName, setCreateName] = useState('');
    const [createSlug, setCreateSlug] = useState('');
    const [createOrder, setCreateOrder] = useState('0');

    const createMutation = useMutation({
        mutationFn: () =>
            adminRestaurantMenuCategories.create(rid, {
                name: createName.trim(),
                slug: createSlug.trim() || undefined,
                displayOrder: parseInt(createOrder, 10) || 0,
            }),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: queryKey(rid) });
            queryClient.invalidateQueries({ queryKey: ['admin-menu-categories'] });
            setCreateName('');
            setCreateSlug('');
            setCreateOrder('0');
        },
    });

    const [editing, setEditing] = useState<AdminMenuCategoryRow | null>(null);
    const [editName, setEditName] = useState('');
    const [editSlug, setEditSlug] = useState('');
    const [editOrder, setEditOrder] = useState('0');

    const startEdit = (row: AdminMenuCategoryRow) => {
        setEditing(row);
        setEditName(row.name);
        setEditSlug(row.slug);
        setEditOrder(String(row.displayOrder));
    };

    const cancelEdit = () => {
        setEditing(null);
    };

    const updateMutation = useMutation({
        mutationFn: () =>
            adminRestaurantMenuCategories.update(rid, editing!.id, {
                name: editName.trim(),
                slug: editSlug.trim() || undefined,
                displayOrder: parseInt(editOrder, 10) || 0,
            }),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: queryKey(rid) });
            queryClient.invalidateQueries({ queryKey: ['admin-menu-categories'] });
            cancelEdit();
        },
    });

    const deleteMutation = useMutation({
        mutationFn: (categoryId: number) => adminRestaurantMenuCategories.delete(rid, categoryId),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: queryKey(rid) });
            queryClient.invalidateQueries({ queryKey: ['admin-menu-categories'] });
        },
    });

    const restaurants = restaurantsPage?.data ?? [];

    const canCreate = hasPermission(AdminPermission.CREATE_CATEGORIES);
    const canEdit = hasPermission(AdminPermission.EDIT_CATEGORIES);
    const canDelete = hasPermission(AdminPermission.DELETE_CATEGORIES);

    return (
        <div className="space-y-6">
            <AdminPageHeader
                title="Categorías de menú por restaurante"
                description="Gestiona las categorías del menú (tabla menu_categories). Los platos se asignan a estas categorías."
            />

            <AdminSectionCard title="Restaurante" description="Selecciona un local para ver y editar sus categorías de menú.">
                <select
                    value={restaurantId === '' ? '' : restaurantId}
                    onChange={(e) => {
                        const v = e.target.value;
                        setRestaurantId(v ? Number(v) : '');
                        cancelEdit();
                    }}
                    className="max-w-md w-full rounded-lg border border-gray-300 px-3 py-2 text-sm shadow-sm focus:border-indigo-500 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                >
                    <option value="">— Elija restaurante —</option>
                    {restaurants.map((r) => (
                        <option key={r.id} value={r.id}>
                            {r.name}
                        </option>
                    ))}
                </select>
            </AdminSectionCard>

            {rid <= 0 ? (
                <AdminEmptyState title="Sin selección" message="Elige un restaurante para continuar." />
            ) : error ? (
                <Card className="border-red-200 bg-red-50/50 p-6">
                    <p className="text-sm text-red-800">{getAdminErrorMessage(error, 'Error al cargar categorías')}</p>
                    <Button type="button" className="mt-4" onClick={() => refetch()}>
                        Reintentar
                    </Button>
                </Card>
            ) : (
                <>
                    {canCreate && (
                        <AdminSectionCard title="Nueva categoría de menú">
                            <div className="grid grid-cols-1 gap-4 md:grid-cols-4">
                                <div className="md:col-span-2">
                                    <label className="mb-1 block text-xs font-medium text-gray-600">Nombre *</label>
                                    <Input
                                        value={createName}
                                        onChange={(e) => setCreateName(e.target.value)}
                                        placeholder="Ej. Entrantes"
                                    />
                                </div>
                                <div>
                                    <label className="mb-1 block text-xs font-medium text-gray-600">Slug (opcional)</label>
                                    <Input
                                        value={createSlug}
                                        onChange={(e) => setCreateSlug(e.target.value)}
                                        placeholder="entrantes"
                                    />
                                </div>
                                <div>
                                    <label className="mb-1 block text-xs font-medium text-gray-600">Orden</label>
                                    <Input
                                        type="number"
                                        value={createOrder}
                                        onChange={(e) => setCreateOrder(e.target.value)}
                                    />
                                </div>
                            </div>
                            <Button
                                type="button"
                                className="mt-4 bg-indigo-600 hover:bg-indigo-700"
                                disabled={!createName.trim() || createMutation.isPending}
                                onClick={() => createMutation.mutate()}
                            >
                                <Plus className="mr-2 inline h-4 w-4" />
                                {createMutation.isPending ? 'Creando…' : 'Añadir'}
                            </Button>
                            {createMutation.isError && (
                                <p className="mt-2 text-sm text-red-600">
                                    {getAdminErrorMessage(createMutation.error, 'No se pudo crear')}
                                </p>
                            )}
                        </AdminSectionCard>
                    )}

                    <AdminSectionCard title="Listado" description={`${categories.length} categoría(s)`}>
                        {isLoading ? (
                            <p className="text-sm text-gray-500">Cargando…</p>
                        ) : categories.length === 0 ? (
                            <AdminEmptyState message="Este restaurante aún no tiene categorías de menú. Crea la primera arriba." />
                        ) : (
                            <div className="overflow-x-auto">
                                <table className="min-w-full divide-y divide-gray-200 text-sm">
                                    <thead className="bg-gray-50">
                                        <tr>
                                            <th className="px-4 py-2 text-left font-medium text-gray-600">Orden</th>
                                            <th className="px-4 py-2 text-left font-medium text-gray-600">Nombre</th>
                                            <th className="px-4 py-2 text-left font-medium text-gray-600">Slug</th>
                                            <th className="px-4 py-2 text-right font-medium text-gray-600">Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody className="divide-y divide-gray-100 bg-white">
                                        {categories.map((row) => (
                                            <tr key={row.id} className="hover:bg-gray-50">
                                                {editing?.id === row.id ? (
                                                    <>
                                                        <td className="px-4 py-2">
                                                            <Input
                                                                type="number"
                                                                className="w-20"
                                                                value={editOrder}
                                                                onChange={(e) => setEditOrder(e.target.value)}
                                                            />
                                                        </td>
                                                        <td className="px-4 py-2">
                                                            <Input
                                                                value={editName}
                                                                onChange={(e) => setEditName(e.target.value)}
                                                            />
                                                        </td>
                                                        <td className="px-4 py-2">
                                                            <Input
                                                                value={editSlug}
                                                                onChange={(e) => setEditSlug(e.target.value)}
                                                            />
                                                        </td>
                                                        <td className="px-4 py-2 text-right">
                                                            <Button
                                                                type="button"
                                                                size="sm"
                                                                variant="outline"
                                                                className="mr-2"
                                                                onClick={cancelEdit}
                                                            >
                                                                Cancelar
                                                            </Button>
                                                            <Button
                                                                type="button"
                                                                size="sm"
                                                                disabled={!editName.trim() || updateMutation.isPending}
                                                                onClick={() => updateMutation.mutate()}
                                                            >
                                                                Guardar
                                                            </Button>
                                                            {updateMutation.isError && (
                                                                <p className="mt-1 text-xs text-red-600">
                                                                    {getAdminErrorMessage(updateMutation.error, 'Error')}
                                                                </p>
                                                            )}
                                                        </td>
                                                    </>
                                                ) : (
                                                    <>
                                                        <td className="px-4 py-2 text-gray-700">{row.displayOrder}</td>
                                                        <td className="px-4 py-2 font-medium text-gray-900">{row.name}</td>
                                                        <td className="px-4 py-2 text-gray-600">{row.slug}</td>
                                                        <td className="px-4 py-2 text-right">
                                                            {canEdit && (
                                                                <button
                                                                    type="button"
                                                                    onClick={() => startEdit(row)}
                                                                    className="mr-2 inline-flex rounded p-1.5 text-indigo-600 hover:bg-indigo-50"
                                                                    title="Editar"
                                                                >
                                                                    <Pencil className="h-4 w-4" />
                                                                </button>
                                                            )}
                                                            {canDelete && (
                                                                <button
                                                                    type="button"
                                                                    onClick={() => {
                                                                        if (
                                                                            window.confirm(
                                                                                `¿Eliminar la categoría "${row.name}"? No debe tener platos asignados.`
                                                                            )
                                                                        ) {
                                                                            deleteMutation.mutate(row.id);
                                                                        }
                                                                    }}
                                                                    disabled={deleteMutation.isPending}
                                                                    className="inline-flex rounded p-1.5 text-red-600 hover:bg-red-50 disabled:opacity-50"
                                                                    title="Eliminar"
                                                                >
                                                                    <Trash2 className="h-4 w-4" />
                                                                </button>
                                                            )}
                                                        </td>
                                                    </>
                                                )}
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            </div>
                        )}
                        {deleteMutation.isError && (
                            <p className="mt-3 text-sm text-red-600">
                                {getAdminErrorMessage(deleteMutation.error, 'No se pudo eliminar (¿tiene platos?)')}
                            </p>
                        )}
                    </AdminSectionCard>
                </>
            )}
        </div>
    );
};

export default RestaurantMenuCategoriesPage;
