import React from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useQueryClient } from '@tanstack/react-query'; // 1. Importa el QueryClient
import { useGetRestaurantById,
    useUpdateRestaurant,
    getListRestaurantsQueryKey  } from '../../api/generated/administrative-api/administrative-api';
import { Restaurant } from '../../api/generated/model';
import RestaurantForm from './RestaurantForm';
import { ROUTES } from '../../constants';
import { AlertCircle, ArrowLeft, Loader2 } from 'lucide-react';
import Button from '../../components/ui/Button';
import {getGetRestaurantByIdQueryKey} from "../../api/generated/default/default";

// ... (imports iguales)

// ... (imports iguales)

const RestaurantEdit: React.FC = () => {
    const queryClient = useQueryClient();
    const { id } = useParams<{ id: string }>();
    const navigate = useNavigate();
    const restaurantId = Number(id);

    // 1. Hook para obtener datos (GET)
    const {
        data: restaurant,
        isLoading,
        error,
        refetch
    } = useGetRestaurantById(restaurantId);

    // 2. CORRECCIÓN TS2559:
    // Llamamos al hook SIN el ID. El ID se pasará en el momento del envío.
    const updateMutation = useUpdateRestaurant();

    if (isLoading) return <div className="p-20 text-center">Cargando...</div>;

    if (error) {
        return (
            <div className="p-10 text-center">
                <AlertCircle className="w-12 h-12 text-red-500 mx-auto mb-4" />
                <h2 className="text-xl font-bold">Error al cargar</h2>
                <div className="flex gap-3 justify-center mt-4">
                    <Button variant="outline" onClick={() => navigate(-1)}>Volver</Button>
                    <Button onClick={() => (refetch as any)()}>Reintentar</Button>
                </div>
            </div>
        );
    }

    return (
        <div className="p-6 max-w-6xl mx-auto space-y-6">
            <header>
                <button onClick={() => navigate(-1)} className="text-gray-500 hover:text-indigo-600 flex items-center mb-2">
                    <ArrowLeft className="w-4 h-4 mr-1" /> Gestión de Restaurantes
                </button>
                <h1 className="text-3xl font-bold text-gray-900">Editar Restaurante</h1>
            </header>

            <RestaurantForm
                initialData={restaurant as Restaurant}
                onSubmit={(values) => {
                    // ✅ SOLUCIÓN FINAL AL ERROR TS2345:
                    // Orval espera el ID y el DATA al mismo nivel en el objeto

                    updateMutation.mutate({
                        id: restaurantId, // Propiedad 1: Para la URL {id}
                        data: {           // Propiedad 2: Para el Body del PUT
                            ...values,
                            id: restaurantId // Mantenemos el ID aquí también si tu YAML lo exige
                        }
                    }, {
                        onSuccess: async () => {
                            try {
                                await queryClient.invalidateQueries({
                                    queryKey: getListRestaurantsQueryKey()
                                });

                                // Esto obliga al Modal a pedir los datos nuevos a Quarkus
                                await queryClient.invalidateQueries({
                                    queryKey: getGetRestaurantByIdQueryKey(restaurantId)
                                });

                                alert("¡Restaurante actualizado con éxito!");
                                navigate(ROUTES.ADMIN.RESTAURANTS);
                            } catch (err) {
                                console.error("Error al invalidar caché:", err);
                                navigate(ROUTES.ADMIN.RESTAURANTS);
                            }
                        },
                        onError: (err) => {
                            console.error("Error en el PUT:", err);
                            alert("No se pudo guardar la información.");
                        }
                    });
                }}
                isLoading={updateMutation.isPending}
                onCancel={() => navigate(-1)}
            />
        </div>
    );
};

export default RestaurantEdit;