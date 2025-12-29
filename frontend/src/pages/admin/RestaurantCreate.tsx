import React, {useState} from 'react';
import { useNavigate } from 'react-router-dom';
import { useCreateRestaurant , getListRestaurantsQueryKey} from '../../api/generated/administrative-api/administrative-api'; // Tu hook de Orval
import RestaurantForm from './RestaurantForm';
import { ROUTES } from '../../constants';
import { useQueryClient } from '@tanstack/react-query';
import {toast} from "sonner";

const RestaurantCreate: React.FC = () => {
    const navigate = useNavigate();

    // 1. Inicializamos la mutación de Orval
    const createMutation = useCreateRestaurant();

    const queryClient = useQueryClient();

    const [createAnother, setCreateAnother] = useState(false);

    // 2. Definimos la función que se ejecutará al dar click en "Guardar"
    const handleCreate = (values: any) => {
        // 'mutate' es la función que dispara la petición POST a Quarkus
        createMutation.mutate({
            data: values // Orval siempre pide el body dentro de una propiedad 'data'
        }, {
            onSuccess: () => {
                queryClient.invalidateQueries({ queryKey: getListRestaurantsQueryKey() });
                // Opcional: navegar de vuelta al panel
                if (createAnother) {
                    // 2. Si quiere crear otro: solo resetea el formulario y muestra éxito
                    toast.success("¡Restaurante creado con éxito!. Puedes añadir el siguiente.");
                    document.dispatchEvent(new Event('reset-restaurant-form'));
                    // Aquí deberías llamar a form.reset() si usas react-hook-form
                } else {
                    // 3. Si no: vuelve al panel administrativo
                    toast.success("¡Restaurante creado con éxito!. Redirigiendo...");
                    navigate('/admin/restaurants');
                }
                // navigate('/admin/restaurants');
                // alert("¡Restaurante creado con éxito!");
                // navigate(ROUTES.ADMIN.RESTAURANTS); // Volvemos a la lista
            },
            onError: (error) => {
                console.error("Error al crear:", error);
                alert("Hubo un error al crear el restaurante.");
            }
        });
    };

    return (
        <div className="p-6 space-y-6">
            <h1 className="text-2xl font-bold">Nuevo Restaurante</h1>

            {/* 3. AQUÍ es donde usas la constante. Al pasarla como prop, el error desaparece */}
            <RestaurantForm
                onSubmit={handleCreate}
                isLoading={createMutation.isPending} // Orval usa isPending para el estado de carga
                onCancel={() => navigate(-1)}
            />

            <div className="mt-6 flex items-center gap-2 p-4 bg-gray-50 rounded-lg border border-gray-200">
                <input
                    type="checkbox"
                    id="another"
                    className="w-4 h-4 text-blue-600 rounded"
                    checked={createAnother}
                    onChange={(e) => setCreateAnother(e.target.checked)}
                />
                <label htmlFor="another" className="text-sm text-gray-700 cursor-pointer">
                    Mantenerse aquí para crear otro restaurante adicional
                </label>
            </div>
        </div>
    );
};

export default RestaurantCreate;