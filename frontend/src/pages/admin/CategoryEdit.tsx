import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { ArrowLeft } from 'lucide-react';
import { adminDishCategories } from '../../services/admin';
import { ROUTES } from '../../constants';
import Button from '../../components/ui/Button';
import Input from '../../components/ui/Input';
import Card from '../../components/ui/Card';
import { AdminPageHeader, AdminErrorState, AdminListPageSkeleton } from '../../components/admin/common';
import { useNotify } from '../../hooks/useNotify';

const CategoryEdit: React.FC = () => {
    const { id } = useParams<{ id: string }>();
    const categoryId = Number(id);
    const navigate = useNavigate();
    const queryClient = useQueryClient();
    const notify = useNotify();
    const [name, setName] = useState('');

    const { data, isLoading, error, refetch } = useQuery({
        queryKey: ['admin-dish-category', categoryId],
        queryFn: () => adminDishCategories.getById(categoryId),
        enabled: Number.isFinite(categoryId) && categoryId > 0,
    });

    useEffect(() => {
        if (data?.name) setName(data.name);
    }, [data?.name]);

    const mutation = useMutation({
        mutationFn: () => adminDishCategories.update(categoryId, { name: name.trim() }),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['admin-dish-categories'] });
            queryClient.invalidateQueries({ queryKey: ['admin-dish-category', categoryId] });
            notify.success('Categoría actualizada');
            navigate(ROUTES.ADMIN.CATEGORIES);
        },
        onError: () => notify.error('No se pudo guardar'),
    });

    if (!Number.isFinite(categoryId) || categoryId <= 0) {
        return (
            <AdminErrorState
                title="ID inválido"
                message="La categoría no existe."
                onRetry={() => navigate(ROUTES.ADMIN.CATEGORIES)}
                retryLabel="Ir al listado"
            />
        );
    }

    if (isLoading) {
        return (
            <div className="space-y-6">
                <AdminPageHeader title="Editar categoría" description="Cargando…" />
                <AdminListPageSkeleton />
            </div>
        );
    }

    if (error) {
        return (
            <div className="space-y-6">
                <Link to={ROUTES.ADMIN.CATEGORIES} className="inline-flex items-center text-gray-500 hover:text-indigo-600">
                    <ArrowLeft className="mr-1 h-4 w-4" /> Volver
                </Link>
                <AdminErrorState error={error} onRetry={() => refetch()} />
            </div>
        );
    }

    return (
        <div className="space-y-6">
            <Link
                to={ROUTES.ADMIN.CATEGORIES}
                className="mb-2 inline-flex items-center text-gray-500 hover:text-indigo-600"
            >
                <ArrowLeft className="mr-1 h-4 w-4" /> Volver al listado
            </Link>
            <AdminPageHeader title="Editar categoría" description={`ID ${categoryId}`} />
            <Card className="p-6">
                <p className="mb-4 text-sm text-gray-500">
                    El backend solo permite cambiar el nombre. Activa/desactiva desde el listado con el icono de ojo.
                </p>
                <label className="mb-2 block text-sm font-medium text-gray-700">Nombre *</label>
                <Input value={name} onChange={(e) => setName(e.target.value)} className="max-w-md" />
                <div className="mt-6 flex flex-wrap gap-2">
                    <Button type="button" variant="outline" onClick={() => navigate(-1)}>
                        Cancelar
                    </Button>
                    <Button
                        type="button"
                        onClick={() => mutation.mutate()}
                        disabled={!name.trim() || mutation.isPending}
                    >
                        {mutation.isPending ? 'Guardando…' : 'Guardar cambios'}
                    </Button>
                </div>
            </Card>
        </div>
    );
};

export default CategoryEdit;
