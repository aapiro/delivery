import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { Plus, Search, Edit, Trash2, Eye, EyeOff } from 'lucide-react';
import { adminDishCategories } from '../../services/admin';
import { DishCategory, AdminPermission } from '../../types';
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
    AdminCardGridSkeleton,
} from '../../components/admin/common';

const PAGE_SIZE = 20;

const CategoriesManagement: React.FC = () => {
    const queryClient = useQueryClient();
    const { hasPermission } = useAdminStore();
    const [searchTerm, setSearchTerm] = useState('');
    const [currentPage, setCurrentPage] = useState(1);

    const { data, isLoading, error, refetch } = useQuery({
        queryKey: ['admin-dish-categories', searchTerm, currentPage],
        queryFn: () =>
            adminDishCategories.getAll(
                searchTerm.trim() ? { search: searchTerm.trim() } : undefined,
                currentPage,
                PAGE_SIZE
            ),
    });

    const toggleMutation = useMutation({
        mutationFn: (id: number) => adminDishCategories.toggleStatus(id),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['admin-dish-categories'] });
        },
    });

    const deleteMutation = useMutation({
        mutationFn: (id: number) => adminDishCategories.delete(id),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['admin-dish-categories'] });
        },
    });

    const handleToggleStatus = (category: DishCategory) => {
        toggleMutation.mutate(category.id);
    };

    const handleDelete = (categoryId: number) => {
        if (window.confirm('¿Estás seguro de que quieres eliminar esta categoría?')) {
            deleteMutation.mutate(categoryId);
        }
    };

    const getStatusBadge = (isActive: boolean) => (
        <span
            className={`rounded-full px-2 py-1 text-xs font-medium ${
                isActive ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
            }`}
        >
            {isActive ? 'Activa' : 'Inactiva'}
        </span>
    );

    const categories = data?.data ?? [];
    const pagination = data?.pagination;

    return (
        <AdminQueryBoundary
            isLoading={isLoading}
            error={error}
            errorTitle="Error al cargar categorías"
            onRetry={() => refetch()}
            loadingFallback={
                <div className="space-y-6">
                    <AdminPageHeader
                        title="Gestión de Categorías"
                        description="Administra las categorías de platos"
                    />
                    <AdminCardGridSkeleton count={8} />
                </div>
            }
        >
            <div className="space-y-6">
                <AdminPageHeader
                    title="Gestión de Categorías"
                    description="Administra las categorías de platos"
                    actions={
                        hasPermission(AdminPermission.CREATE_CATEGORIES) ? (
                            <Link to={ROUTES.ADMIN.CATEGORY_CREATE}>
                                <Button type="button" className="bg-indigo-600 hover:bg-indigo-700">
                                    <Plus className="mr-2 h-4 w-4" />
                                    Nueva categoría
                                </Button>
                            </Link>
                        ) : undefined
                    }
                />

                <AdminSectionCard title="Búsqueda" description="Filtra por nombre">
                    <div className="max-w-md">
                        <label className="mb-2 block text-sm font-medium text-gray-700">Buscar categorías</label>
                        <div className="relative">
                            <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 transform text-gray-400" />
                            <Input
                                type="text"
                                placeholder="Nombre de la categoría..."
                                value={searchTerm}
                                onChange={(e) => {
                                    setSearchTerm(e.target.value);
                                    setCurrentPage(1);
                                }}
                                className="pl-10"
                            />
                        </div>
                    </div>
                </AdminSectionCard>

                <div className="grid grid-cols-1 gap-6 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
                    {categories.length === 0 ? (
                        <div className="col-span-full">
                            <AdminEmptyState
                                title="Sin resultados"
                                message="No hay categorías que coincidan con la búsqueda o la lista está vacía."
                            />
                        </div>
                    ) : (
                        categories.map((category) => (
                            <Card key={category.id} className="p-6 transition-shadow hover:shadow-lg">
                                <div className="mb-4 flex items-start justify-between">
                                    <div className="flex items-center space-x-3">
                                        <div className="flex h-12 w-12 items-center justify-center rounded-lg bg-orange-100">
                                            <div className="h-8 w-8 rounded bg-orange-300" />
                                        </div>
                                        <div>
                                            <h3 className="font-semibold text-gray-900">{category.name}</h3>
                                            <p className="text-sm text-gray-500">Orden: {category.displayOrder}</p>
                                        </div>
                                    </div>
                                    {getStatusBadge(category.isActive)}
                                </div>

                                <p className="mb-4 text-sm text-gray-600">
                                Slug: {category.slug || '—'}
                            </p>

                                <div className="flex items-center justify-between border-t border-gray-100 pt-4">
                                    <div className="text-xs text-gray-500">ID: {category.id}</div>
                                    <div className="flex items-center space-x-2">
                                        <button
                                            type="button"
                                            onClick={() => handleToggleStatus(category)}
                                            disabled={toggleMutation.isPending}
                                            className="rounded-lg p-2 text-blue-600 transition-colors hover:bg-blue-50 hover:text-blue-900"
                                            title={category.isActive ? 'Desactivar' : 'Activar'}
                                        >
                                            {category.isActive ? (
                                                <EyeOff className="h-4 w-4" />
                                            ) : (
                                                <Eye className="h-4 w-4" />
                                            )}
                                        </button>
                                        {hasPermission(AdminPermission.EDIT_CATEGORIES) ? (
                                            <Link
                                                to={ROUTES.ADMIN.CATEGORY_EDIT(category.id)}
                                                className="rounded-lg p-2 text-indigo-600 transition-colors hover:bg-indigo-50 hover:text-indigo-900"
                                                title="Editar nombre"
                                            >
                                                <Edit className="h-4 w-4" />
                                            </Link>
                                        ) : (
                                            <span className="rounded-lg p-2 text-gray-300">
                                                <Edit className="h-4 w-4" />
                                            </span>
                                        )}
                                        <button
                                            type="button"
                                            onClick={() => handleDelete(category.id)}
                                            disabled={deleteMutation.isPending}
                                            className="rounded-lg p-2 text-red-600 transition-colors hover:bg-red-50 hover:text-red-900"
                                            title="Eliminar"
                                        >
                                            <Trash2 className="h-4 w-4" />
                                        </button>
                                    </div>
                                </div>
                            </Card>
                        ))
                    )}
                </div>

                {pagination && pagination.totalPages > 1 && (
                    <Card className="p-4">
                        <div className="flex items-center justify-between">
                            <div className="flex flex-1 justify-between sm:hidden">
                                <Button
                                    type="button"
                                    onClick={() => setCurrentPage((p) => Math.max(1, p - 1))}
                                    disabled={currentPage === 1}
                                    variant="outline"
                                >
                                    Anterior
                                </Button>
                                <Button
                                    type="button"
                                    onClick={() =>
                                        setCurrentPage((p) => Math.min(pagination.totalPages, p + 1))
                                    }
                                    disabled={currentPage === pagination.totalPages}
                                    variant="outline"
                                >
                                    Siguiente
                                </Button>
                            </div>
                            <div className="hidden flex-1 items-center justify-between sm:flex">
                                <p className="text-sm text-gray-700">
                                    Mostrando{' '}
                                    <span className="font-medium">
                                        {(currentPage - 1) * pagination.limit + 1}
                                    </span>{' '}
                                    a{' '}
                                    <span className="font-medium">
                                        {Math.min(currentPage * pagination.limit, pagination.total)}
                                    </span>{' '}
                                    de <span className="font-medium">{pagination.total}</span> categorías
                                </p>
                                <nav className="relative z-0 inline-flex -space-x-px rounded-md shadow-sm">
                                    {Array.from({ length: pagination.totalPages }, (_, i) => i + 1).map((page) => (
                                        <button
                                            key={page}
                                            type="button"
                                            onClick={() => setCurrentPage(page)}
                                            className={`relative inline-flex items-center border px-4 py-2 text-sm font-medium ${
                                                page === currentPage
                                                    ? 'z-10 border-orange-500 bg-orange-50 text-orange-600'
                                                    : 'border-gray-300 bg-white text-gray-500 hover:bg-gray-50'
                                            }`}
                                        >
                                            {page}
                                        </button>
                                    ))}
                                </nav>
                            </div>
                        </div>
                    </Card>
                )}
            </div>
        </AdminQueryBoundary>
    );
};

export default CategoriesManagement;
