import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { ArrowLeft } from 'lucide-react';
import { adminDishCategories } from '../../services/admin';
import { ROUTES } from '../../constants';
import Button from '../../components/ui/Button';
import Input from '../../components/ui/Input';
import Card from '../../components/ui/Card';
import { AdminPageHeader } from '../../components/admin/common';
import { useNotify } from '../../hooks/useNotify';

const CategoryCreate: React.FC = () => {
    const navigate = useNavigate();
    const queryClient = useQueryClient();
    const notify = useNotify();
    const [name, setName] = useState('');

    const mutation = useMutation({
        mutationFn: () => adminDishCategories.create({ name: name.trim() }),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['admin-dish-categories'] });
            notify.success('Categoría creada');
            navigate(ROUTES.ADMIN.CATEGORIES);
        },
        onError: () => notify.error('No se pudo crear la categoría'),
    });

    return (
        <div className="space-y-6">
            <Link
                to={ROUTES.ADMIN.CATEGORIES}
                className="mb-2 inline-flex items-center text-gray-500 hover:text-indigo-600"
            >
                <ArrowLeft className="mr-1 h-4 w-4" /> Volver al listado
            </Link>
            <AdminPageHeader
                title="Nueva categoría"
                description="Categoría del catálogo global (tabla categories en backend)."
            />
            <Card className="p-6">
                <label className="mb-2 block text-sm font-medium text-gray-700">Nombre *</label>
                <Input
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    placeholder="Ej. Pizzas"
                    className="max-w-md"
                />
                <div className="mt-6 flex flex-wrap gap-2">
                    <Button type="button" variant="outline" onClick={() => navigate(-1)}>
                        Cancelar
                    </Button>
                    <Button
                        type="button"
                        onClick={() => mutation.mutate()}
                        disabled={!name.trim() || mutation.isPending}
                    >
                        {mutation.isPending ? 'Guardando…' : 'Guardar'}
                    </Button>
                </div>
            </Card>
        </div>
    );
};

export default CategoryCreate;
