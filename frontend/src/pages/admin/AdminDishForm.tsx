import React, { useEffect, useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { AdminDishWritePayload } from '../../types';
import { adminRestaurants, adminMenuCategories } from '../../services/admin';
import Button from '../../components/ui/Button';
import Input from '../../components/ui/Input';
import Card from '../../components/ui/Card';

export interface AdminDishFormProps {
    /** Valores iniciales (edición) o vacíos (alta) */
    initial?: Partial<AdminDishWritePayload>;
    submitLabel?: string;
    isSubmitting?: boolean;
    onSubmit: (payload: AdminDishWritePayload) => void;
    onCancel: () => void;
}

const empty: AdminDishWritePayload = {
    name: '',
    description: '',
    price: 0,
    imageUrl: '',
    isAvailable: true,
    restaurantId: 0,
    categoryId: undefined,
};

/**
 * Formulario alineado con `AdminDishWriteDto` (Quarkus). Categoría = menú del restaurante elegido.
 */
const AdminDishForm: React.FC<AdminDishFormProps> = ({
    initial,
    submitLabel = 'Guardar',
    isSubmitting,
    onSubmit,
    onCancel,
}) => {
    const [form, setForm] = useState<AdminDishWritePayload>(() => ({
        ...empty,
        ...initial,
        restaurantId: initial?.restaurantId || 0,
        categoryId: initial?.categoryId ?? undefined,
    }));

    const { data: restaurantsPage } = useQuery({
        queryKey: ['admin-restaurants', 'dish-form-options'],
        queryFn: () => adminRestaurants.getAll(undefined, 1, 200),
    });

    const restaurantIdForMenu = form.restaurantId > 0 ? form.restaurantId : undefined;

    const { data: menuCategories = [], isLoading: loadingMenus } = useQuery({
        queryKey: ['admin-menu-categories', restaurantIdForMenu],
        queryFn: () => adminMenuCategories.getByRestaurant(restaurantIdForMenu!),
        enabled: restaurantIdForMenu != null,
    });

    useEffect(() => {
        if (restaurantIdForMenu == null) {
            setForm((f) => ({ ...f, categoryId: undefined }));
        }
    }, [restaurantIdForMenu]);

    const restaurants = restaurantsPage?.data ?? [];

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        if (!form.name.trim()) return;
        if (!form.restaurantId) return;
        const categoryId =
            form.categoryId != null && form.categoryId > 0 ? form.categoryId : undefined;
        onSubmit({
            ...form,
            name: form.name.trim(),
            description: form.description?.trim() ?? '',
            price: Number(form.price) || 0,
            imageUrl: form.imageUrl?.trim() ?? '',
            restaurantId: form.restaurantId,
            categoryId,
        });
    };

    return (
        <form onSubmit={handleSubmit} className="space-y-6">
            <Card className="p-6">
                <h2 className="mb-4 text-lg font-semibold text-gray-900">Datos del plato</h2>
                <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
                    <div className="md:col-span-2">
                        <label className="mb-1 block text-sm font-medium text-gray-700">Restaurante *</label>
                        <select
                            required
                            value={form.restaurantId || ''}
                            onChange={(e) => {
                                const v = e.target.value ? Number(e.target.value) : 0;
                                setForm((f) => ({ ...f, restaurantId: v, categoryId: undefined }));
                            }}
                            className="w-full rounded-lg border border-gray-300 px-3 py-2 text-sm shadow-sm focus:border-blue-500 focus:outline-none focus:ring-2 focus:ring-blue-500"
                        >
                            <option value="">Seleccione…</option>
                            {restaurants.map((r) => (
                                <option key={r.id} value={r.id}>
                                    {r.name}
                                </option>
                            ))}
                        </select>
                    </div>
                    <div className="md:col-span-2">
                        <label className="mb-1 block text-sm font-medium text-gray-700">Categoría de menú</label>
                        <select
                            value={form.categoryId ?? ''}
                            onChange={(e) => {
                                const v = e.target.value ? Number(e.target.value) : undefined;
                                setForm((f) => ({ ...f, categoryId: v }));
                            }}
                            disabled={!restaurantIdForMenu || loadingMenus}
                            className="w-full rounded-lg border border-gray-300 px-3 py-2 text-sm shadow-sm focus:border-blue-500 focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:bg-gray-100"
                        >
                            <option value="">Sin categoría</option>
                            {menuCategories.map((c) => (
                                <option key={c.id} value={c.id}>
                                    {c.name}
                                </option>
                            ))}
                        </select>
                        {!restaurantIdForMenu && (
                            <p className="mt-1 text-xs text-gray-500">Elija un restaurante para listar categorías de menú.</p>
                        )}
                    </div>
                    <div className="md:col-span-2">
                        <label className="mb-1 block text-sm font-medium text-gray-700">Nombre *</label>
                        <Input
                            required
                            value={form.name}
                            onChange={(e) => setForm((f) => ({ ...f, name: e.target.value }))}
                            placeholder="Nombre del plato"
                        />
                    </div>
                    <div className="md:col-span-2">
                        <label className="mb-1 block text-sm font-medium text-gray-700">Descripción</label>
                        <textarea
                            value={form.description ?? ''}
                            onChange={(e) => setForm((f) => ({ ...f, description: e.target.value }))}
                            rows={3}
                            className="w-full rounded-lg border border-gray-300 px-3 py-2 text-sm shadow-sm focus:border-blue-500 focus:outline-none focus:ring-2 focus:ring-blue-500"
                        />
                    </div>
                    <div>
                        <label className="mb-1 block text-sm font-medium text-gray-700">Precio (€) *</label>
                        <Input
                            type="number"
                            step="0.01"
                            min={0}
                            required
                            value={form.price === 0 ? '' : form.price}
                            onChange={(e) => setForm((f) => ({ ...f, price: Number(e.target.value) || 0 }))}
                        />
                    </div>
                    <div>
                        <label className="mb-1 block text-sm font-medium text-gray-700">URL imagen</label>
                        <Input
                            value={form.imageUrl ?? ''}
                            onChange={(e) => setForm((f) => ({ ...f, imageUrl: e.target.value }))}
                            placeholder="https://…"
                        />
                    </div>
                    <div className="flex items-center gap-2 md:col-span-2">
                        <input
                            id="isAvailable"
                            type="checkbox"
                            checked={form.isAvailable ?? true}
                            onChange={(e) => setForm((f) => ({ ...f, isAvailable: e.target.checked }))}
                            className="h-4 w-4 rounded border-gray-300"
                        />
                        <label htmlFor="isAvailable" className="text-sm text-gray-700">
                            Disponible para pedidos
                        </label>
                    </div>
                </div>
                <div className="mt-6 flex flex-wrap gap-2">
                    <Button type="button" variant="outline" onClick={onCancel}>
                        Cancelar
                    </Button>
                    <Button type="submit" disabled={isSubmitting || !form.restaurantId}>
                        {isSubmitting ? 'Guardando…' : submitLabel}
                    </Button>
                </div>
            </Card>
        </form>
    );
};

export default AdminDishForm;
