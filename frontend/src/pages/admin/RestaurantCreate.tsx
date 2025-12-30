import React, {useState} from 'react';
import { useNavigate } from 'react-router-dom';
import { useCreateRestaurant , getListRestaurantsQueryKey} from '../../api/generated/administrative-api/administrative-api'; // Tu hook de Orval
import RestaurantForm from './RestaurantForm';
import { ROUTES } from '../../constants';
import { useQueryClient } from '@tanstack/react-query';
import {toast} from "sonner";
import {ArrowLeft} from "lucide-react";

const RestaurantCreate: React.FC = () => {
    const navigate = useNavigate();

    // 1. Inicializamos la mutación de Orval
    const createMutation = useCreateRestaurant();

    const queryClient = useQueryClient();

    // 2. Definimos la función que se ejecutará al dar click en "Guardar"
    const handleCreate = (values: any, meta?: { stayOnPage?: boolean }) => {
        createMutation.mutate(
            { data: values },
            {
                onSuccess: () => {
                    queryClient.invalidateQueries({ queryKey: getListRestaurantsQueryKey() });
                    toast.success("¡Restaurante creado con éxito!");

                    if (!meta?.stayOnPage) {
                        navigate(ROUTES.ADMIN.RESTAURANTS);
                    }
                },
                onError: () => {
                    toast.error("Hubo un error al crear el restaurante.");
                }
            }
        );
    };


    return (
        <div className="p-6 space-y-6">
            <button onClick={() => navigate(-1)} className="text-gray-500 hover:text-indigo-600 flex items-center mb-2">
                <ArrowLeft className="w-4 h-4 mr-1" /> Gestión de Restaurantes
            </button>
            <h1 className="text-2xl font-bold">Nuevo Restaurante</h1>

            {/* 3. AQUÍ es donde usas la constante. Al pasarla como prop, el error desaparece */}
            <RestaurantForm
                onSubmit={(values, meta) => handleCreate(values, meta)}
                isLoading={createMutation.isPending}
                onCancel={() => navigate(-1)}
            />



        </div>
    );
};

export default RestaurantCreate;