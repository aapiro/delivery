import React from 'react';
import { useNavigate } from 'react-router-dom';
import { ArrowLeft } from 'lucide-react';
import {
    useCreateRestaurant,
    getListRestaurantsQueryKey,
} from '../../api/generated/administrative-api/administrative-api';
import { CreateRestaurantRequest } from '../../api/generated/model';

/** El formulario envía campos extra (p. ej. isOpen); el API solo persiste los del contrato OpenAPI. */
type RestaurantFormPayload = CreateRestaurantRequest & { isOpen?: boolean };
import RestaurantForm from './RestaurantForm';
import { ROUTES } from '../../constants';
import { useQueryClient } from '@tanstack/react-query';
import { useNotify } from '../../hooks/useNotify';
import { AdminPageHeader } from '../../components/admin/common';

const RestaurantCreate: React.FC = () => {
    const navigate = useNavigate();
    const createMutation = useCreateRestaurant();
    const queryClient = useQueryClient();
    const notify = useNotify();

    const handleCreate = (values: RestaurantFormPayload, meta?: { stayOnPage?: boolean }) => {
        const { isOpen: _omit, ...apiBody } = values;
        createMutation.mutate(
            { data: apiBody as CreateRestaurantRequest },
            {
                onSuccess: () => {
                    queryClient.invalidateQueries({ queryKey: getListRestaurantsQueryKey() });
                    notify.success('¡Restaurante creado con éxito!');

                    if (!meta?.stayOnPage) {
                        navigate(ROUTES.ADMIN.RESTAURANTS);
                    }
                },
                onError: () => {
                    notify.error('Hubo un error al crear el restaurante.');
                },
            }
        );
    };

    return (
        <div className="space-y-6">
            <button
                type="button"
                onClick={() => navigate(-1)}
                className="mb-2 flex items-center text-gray-500 hover:text-indigo-600"
            >
                <ArrowLeft className="mr-1 h-4 w-4" /> Volver
            </button>
            <AdminPageHeader
                title="Nuevo restaurante"
                description="Completa los datos del local. El backend validará los campos requeridos."
            />
            <RestaurantForm
                onSubmit={(values, meta) => handleCreate(values, meta)}
                isLoading={createMutation.isPending}
                onCancel={() => navigate(-1)}
            />
        </div>
    );
};

export default RestaurantCreate;
