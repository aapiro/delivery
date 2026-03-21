import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { ArrowLeft } from 'lucide-react';
import { adminDishes } from '../../services/admin';
import { AdminDishWritePayload } from '../../types';
import { ROUTES } from '../../constants';
import { AdminPageHeader } from '../../components/admin/common';
import AdminDishForm from './AdminDishForm';
import { useNotify } from '../../hooks/useNotify';

const DishCreate: React.FC = () => {
    const navigate = useNavigate();
    const queryClient = useQueryClient();
    const notify = useNotify();

    const mutation = useMutation({
        mutationFn: (payload: AdminDishWritePayload) => adminDishes.create(payload),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['admin-dishes'] });
            notify.success('Plato creado');
            navigate(ROUTES.ADMIN.DISHES);
        },
        onError: () => notify.error('No se pudo crear el plato'),
    });

    return (
        <div className="space-y-6">
            <Link
                to={ROUTES.ADMIN.DISHES}
                className="mb-2 inline-flex items-center text-gray-500 hover:text-indigo-600"
            >
                <ArrowLeft className="mr-1 h-4 w-4" /> Volver al listado
            </Link>
            <AdminPageHeader title="Nuevo plato" description="Se guarda vía API admin (JSON)." />
            <AdminDishForm
                submitLabel="Crear plato"
                isSubmitting={mutation.isPending}
                onSubmit={(payload) => mutation.mutate(payload)}
                onCancel={() => navigate(-1)}
            />
        </div>
    );
};

export default DishCreate;
