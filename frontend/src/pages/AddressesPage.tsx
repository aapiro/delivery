import React from 'react';
import { Link } from 'react-router-dom';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import Layout from '../components/layout/Layout';
import Card from '../components/ui/Card';
import Button from '../components/ui/Button';
import Input from '../components/ui/Input';
import { api } from '../services/api';
import { useAuthStore, useNotificationStore } from '../store';
import { ROUTES } from '../constants';

type BackendAddress = {
    id: number;
    street: string;
    city: string;
    state?: string;
    zipCode: string;
    country?: string;
    isDefault?: boolean;
    instructions?: string;
    addressType?: string;
};

type AddressFormState = {
    street: string;
    city: string;
    state: string;
    zipCode: string;
    country: string;
    instructions: string;
    addressType: string;
    isDefault: boolean;
};

const defaultForm: AddressFormState = {
    street: '',
    city: 'Madrid',
    state: '',
    zipCode: '',
    country: 'Spain',
    instructions: '',
    addressType: 'HOME',
    isDefault: false,
};

const AddressesPage: React.FC = () => {
    const queryClient = useQueryClient();
    const { isAuthenticated } = useAuthStore();
    const { addNotification } = useNotificationStore();

    const [form, setForm] = React.useState<AddressFormState>(defaultForm);

    const { data, isLoading, error } = useQuery({
        queryKey: ['addresses'],
        enabled: isAuthenticated,
        queryFn: async () => {
            const res = (await api.get<any>('/users/addresses')) as any;
            return res?.data?.addresses ?? res?.addresses ?? [];
        },
    });

    const addresses: BackendAddress[] = data ?? [];

    const addAddressMutation = useMutation({
        mutationFn: async () => {
            return api.post<any>('/users/addresses', {
                street: form.street,
                city: form.city,
                state: form.state,
                zipCode: form.zipCode,
                country: form.country,
                instructions: form.instructions,
                addressType: form.addressType,
                isDefault: form.isDefault,
            });
        },
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['addresses'] });
            setForm(defaultForm);
            addNotification({
                type: 'success',
                title: 'Dirección guardada',
                message: 'Se agregó tu dirección correctamente.',
                duration: 3000,
            });
        },
        onError: () => {
            addNotification({
                type: 'error',
                title: 'Error al guardar',
                message: 'No se pudo guardar la dirección.',
                duration: 4000,
            });
        },
    });

    const deleteAddressMutation = useMutation({
        mutationFn: async (id: number) => api.delete<any>(`/users/addresses/${id}`),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['addresses'] });
            addNotification({
                type: 'info',
                title: 'Dirección eliminada',
                message: 'Se eliminó correctamente.',
                duration: 3000,
            });
        },
    });

    const setDefaultMutation = useMutation({
        mutationFn: async (id: number) => api.put<any>(`/users/addresses/${id}/default`, {}),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['addresses'] });
            addNotification({
                type: 'success',
                title: 'Dirección por defecto',
                message: 'Se actualizó tu dirección principal.',
                duration: 3000,
            });
        },
    });

    if (!isAuthenticated) {
        return (
            <Layout>
                <div className="max-w-2xl mx-auto px-4 py-12 text-center">
                    <h1 className="text-2xl font-bold text-gray-900 mb-4">Inicia sesión</h1>
                    <p className="text-gray-600 mb-6">Para gestionar direcciones.</p>
                    <Link to={ROUTES.LOGIN}>
                        <Button variant="primary">Ir a login</Button>
                    </Link>
                </div>
            </Layout>
        );
    }

    return (
        <Layout>
            <div className="max-w-4xl mx-auto px-4 py-8">
                <div className="mb-6">
                    <h1 className="text-3xl font-bold text-gray-900">Direcciones</h1>
                    <p className="text-gray-600 mt-1">Guarda y elige tu dirección de entrega.</p>
                </div>

                <div className="grid grid-cols-1 lg:grid-cols-2 gap-8 items-start">
                    <div className="space-y-4">
                        <Card padding="lg">
                            <h2 className="text-lg font-bold text-gray-900 mb-4">Tus direcciones</h2>

                            {isLoading ? <p className="text-gray-600">Cargando...</p> : null}
                            {error ? <p className="text-red-600">No se pudieron cargar las direcciones.</p> : null}

                            {!isLoading && addresses.length === 0 ? (
                                <p className="text-gray-600">Aún no tienes direcciones guardadas.</p>
                            ) : null}

                            {!isLoading &&
                                addresses.map((a) => (
                                    <div key={a.id} className="border-t border-gray-100 pt-4 mt-4">
                                        <div className="flex items-start justify-between gap-4">
                                            <div>
                                                <p className="font-medium text-gray-900">
                                                    {a.isDefault ? 'Dirección principal' : 'Dirección'}
                                                </p>
                                                <p className="text-sm text-gray-600 mt-1">
                                                    {a.street}, {a.city} {a.zipCode}
                                                </p>
                                                {a.instructions ? (
                                                    <p className="text-xs text-gray-500 mt-1">Notas: {a.instructions}</p>
                                                ) : null}
                                            </div>

                                            <div className="flex flex-col gap-2">
                                                {!a.isDefault ? (
                                                    <Button size="sm" variant="primary" onClick={() => setDefaultMutation.mutate(a.id)}>
                                                        Usar
                                                    </Button>
                                                ) : (
                                                    <Button size="sm" variant="outline" disabled>
                                                        Por defecto
                                                    </Button>
                                                )}

                                                <Button
                                                    size="sm"
                                                    variant="ghost"
                                                    onClick={() => deleteAddressMutation.mutate(a.id)}
                                                >
                                                    Eliminar
                                                </Button>
                                            </div>
                                        </div>
                                    </div>
                                ))}
                        </Card>
                    </div>

                    <div className="space-y-4">
                        <Card padding="lg">
                            <h2 className="text-lg font-bold text-gray-900 mb-4">Agregar dirección</h2>

                            <form
                                className="space-y-4"
                                onSubmit={(e) => {
                                    e.preventDefault();
                                    addAddressMutation.mutate();
                                }}
                            >
                                <Input
                                    label="Dirección"
                                    placeholder="Calle, número..."
                                    value={form.street}
                                    onChange={(e) => setForm((p) => ({ ...p, street: e.target.value }))}
                                    fullWidth
                                    required
                                />

                                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                                    <Input
                                        label="Ciudad"
                                        value={form.city}
                                        onChange={(e) => setForm((p) => ({ ...p, city: e.target.value }))}
                                        fullWidth
                                    />
                                    <Input
                                        label="Código Postal"
                                        placeholder="28001"
                                        value={form.zipCode}
                                        onChange={(e) => setForm((p) => ({ ...p, zipCode: e.target.value }))}
                                        fullWidth
                                        required
                                    />
                                </div>

                                <Input
                                    label="Provincia/Estado (opcional)"
                                    placeholder="Madrid"
                                    value={form.state}
                                    onChange={(e) => setForm((p) => ({ ...p, state: e.target.value }))}
                                    fullWidth
                                />

                                <Input
                                    label="País (opcional)"
                                    value={form.country}
                                    onChange={(e) => setForm((p) => ({ ...p, country: e.target.value }))}
                                    fullWidth
                                />

                                <Input
                                    label="Instrucciones (opcional)"
                                    placeholder="Ej: sin timbre"
                                    value={form.instructions}
                                    onChange={(e) => setForm((p) => ({ ...p, instructions: e.target.value }))}
                                    fullWidth
                                />

                                <div className="flex items-center gap-3">
                                    <input
                                        type="checkbox"
                                        checked={form.isDefault}
                                        onChange={(e) => setForm((p) => ({ ...p, isDefault: e.target.checked }))}
                                    />
                                    <span className="text-sm text-gray-700">Marcar como principal</span>
                                </div>

                                <Button type="submit" variant="primary" fullWidth disabled={addAddressMutation.isPending}>
                                    Guardar dirección
                                </Button>
                            </form>
                        </Card>
                    </div>
                </div>
            </div>
        </Layout>
    );
};

export default AddressesPage;

