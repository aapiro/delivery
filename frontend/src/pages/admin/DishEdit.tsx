import React from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { ArrowLeft } from 'lucide-react';
import { adminDishes } from '../../services/admin';
import { AdminDishWritePayload } from '../../types';
import { ROUTES } from '../../constants';
import { AdminPageHeader, AdminErrorState, AdminListPageSkeleton } from '../../components/admin/common';
import AdminDishForm from './AdminDishForm';
import { useNotify } from '../../hooks/useNotify';

const DishEdit: React.FC = () => {
    const { id } = useParams<{ id: string }>();
    const dishId = Number(id);
    const navigate = useNavigate();
    const queryClient = useQueryClient();
    const notify = useNotify();

    const { data, isLoading, error, refetch } = useQuery({
        queryKey: ['admin-dish', dishId],
        queryFn: () => adminDishes.getById(dishId),
        enabled: Number.isFinite(dishId) && dishId > 0,
    });

    const mutation = useMutation({
        mutationFn: (payload: AdminDishWritePayload) => adminDishes.update(dishId, payload),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['admin-dishes'] });
            queryClient.invalidateQueries({ queryKey: ['admin-dish', dishId] });
            notify.success('Plato actualizado');
            navigate(ROUTES.ADMIN.DISHES);
        },
        onError: () => notify.error('No se pudo guardar'),
    });

    if (!Number.isFinite(dishId) || dishId <= 0) {
        return <AdminErrorState title="ID inválido" onRetry={() => navigate(ROUTES.ADMIN.DISHES)} />;
    }

    if (isLoading) {
        return (
            <div className="space-y-6">
                <AdminPageHeader title="Editar plato" description="Cargando…" />
                <AdminListPageSkeleton />
            </div>
        );
    }

    if (error || !data) {
        return (
            <div className="space-y-6">
                <Link to={ROUTES.ADMIN.DISHES} className="inline-flex items-center text-gray-500 hover:text-indigo-600">
                    <ArrowLeft className="mr-1 h-4 w-4" /> Volver
                </Link>
                <AdminErrorState error={error} onRetry={() => refetch()} />
            </div>
        );
    }

    const initial: Partial<AdminDishWritePayload> = {
        name: data.name,
        description: data.description,
        price: data.price,
        imageUrl: data.image,
        isAvailable: data.isAvailable,
        restaurantId: data.restaurantId,
        categoryId: data.categoryId > 0 ? data.categoryId : undefined,
    };

    return (
        <div className="space-y-6">
            <Link
                to={ROUTES.ADMIN.DISHES}
                className="mb-2 inline-flex items-center text-gray-500 hover:text-indigo-600"
            >
                <ArrowLeft className="mr-1 h-4 w-4" /> Volver al listado
            </Link>
            <AdminPageHeader title="Editar plato" description={data.name} />
            <AdminDishForm
                key={dishId}
                initial={initial}
                submitLabel="Guardar cambios"
                isSubmitting={mutation.isPending}
                onSubmit={(payload) => mutation.mutate(payload)}
                onCancel={() => navigate(-1)}
            />
        </div>
    );
};

export default DishEdit;
