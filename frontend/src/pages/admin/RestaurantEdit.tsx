import React from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useQueryClient } from '@tanstack/react-query';
import { ArrowLeft } from 'lucide-react';
import {
    useGetRestaurantById,
    useUpdateRestaurant,
    getListRestaurantsQueryKey,
    getGetRestaurantByIdQueryKey,
} from '../../api/generated/administrative-api/administrative-api';
import { Restaurant } from '../../api/generated/model';
import RestaurantForm from './RestaurantForm';
import { ROUTES } from '../../constants';
import Button from '../../components/ui/Button';
import { AdminPageHeader, AdminErrorState, AdminListPageSkeleton, getAdminErrorMessage } from '../../components/admin/common';
import { useNotify } from '../../hooks/useNotify';

const RestaurantEdit: React.FC = () => {
    const notify = useNotify();
    const queryClient = useQueryClient();
    const { id } = useParams<{ id: string }>();
    const navigate = useNavigate();
    const restaurantId = Number(id);

    const { data: restaurant, isLoading, error } = useGetRestaurantById(restaurantId);

    const updateMutation = useUpdateRestaurant();

    if (isLoading) {
        return (
            <div className="mx-auto max-w-6xl space-y-6">
                <button
                    type="button"
                    onClick={() => navigate(-1)}
                    className="mb-2 flex items-center text-gray-500 hover:text-indigo-600"
                >
                    <ArrowLeft className="mr-1 h-4 w-4" /> Volver
                </button>
                <AdminPageHeader title="Editar restaurante" description="Cargando datos…" />
                <AdminListPageSkeleton />
            </div>
        );
    }

    if (error) {
        return (
            <div className="mx-auto max-w-6xl space-y-6">
                <button
                    type="button"
                    onClick={() => navigate(-1)}
                    className="mb-2 flex items-center text-gray-500 hover:text-indigo-600"
                >
                    <ArrowLeft className="mr-1 h-4 w-4" /> Volver
                </button>
                <AdminErrorState
                    title="No se pudo cargar el restaurante"
                    error={error}
                    onRetry={() => {
                        void queryClient.invalidateQueries({
                            queryKey: getGetRestaurantByIdQueryKey(restaurantId),
                        });
                    }}
                />
                <div className="flex justify-center">
                    <Button variant="outline" onClick={() => navigate(ROUTES.ADMIN.RESTAURANTS)}>
                        Ir al listado
                    </Button>
                </div>
            </div>
        );
    }

    if (restaurant == null) {
        return (
            <div className="mx-auto max-w-6xl space-y-6">
                <button
                    type="button"
                    onClick={() => navigate(-1)}
                    className="mb-2 flex items-center text-gray-500 hover:text-indigo-600"
                >
                    <ArrowLeft className="mr-1 h-4 w-4" /> Volver
                </button>
                <AdminErrorState
                    title="Sin datos"
                    message="No se recibió el restaurante."
                    onRetry={() =>
                        queryClient.invalidateQueries({
                            queryKey: getGetRestaurantByIdQueryKey(restaurantId),
                        })
                    }
                />
            </div>
        );
    }

    const restaurantData = restaurant as Restaurant;

    return (
        <div className="mx-auto max-w-6xl space-y-6">
            <button
                type="button"
                onClick={() => navigate(-1)}
                className="mb-2 flex items-center text-gray-500 hover:text-indigo-600"
            >
                <ArrowLeft className="mr-1 h-4 w-4" /> Volver
            </button>
            <AdminPageHeader
                title="Editar restaurante"
                description={restaurantData.name ?? `ID ${restaurantId}`}
            />

            <RestaurantForm
                initialData={restaurantData}
                onSubmit={(values) => {
                    updateMutation.mutate(
                        {
                            id: restaurantId,
                            data: {
                                ...values,
                                id: restaurantId,
                            },
                        },
                        {
                            onSuccess: async () => {
                                try {
                                    await queryClient.invalidateQueries({
                                        queryKey: getListRestaurantsQueryKey(),
                                    });
                                    await queryClient.invalidateQueries({
                                        queryKey: getGetRestaurantByIdQueryKey(restaurantId),
                                    });
                                    notify.success('Restaurante actualizado', 'Los cambios se guardaron correctamente.');
                                    navigate(ROUTES.ADMIN.RESTAURANTS);
                                } catch (err) {
                                    console.error('Error al invalidar caché:', err);
                                    notify.error('Actualizado con avisos', 'No se pudo refrescar la caché; la lista puede estar desactualizada.');
                                    navigate(ROUTES.ADMIN.RESTAURANTS);
                                }
                            },
                            onError: (err) => {
                                console.error('Error en el PUT:', err);
                                notify.error(
                                    'No se pudo guardar',
                                    getAdminErrorMessage(err, 'Revisa los datos o tu conexión.')
                                );
                            },
                        }
                    );
                }}
                isLoading={updateMutation.isPending}
                onCancel={() => navigate(-1)}
            />
        </div>
    );
};

export default RestaurantEdit;
