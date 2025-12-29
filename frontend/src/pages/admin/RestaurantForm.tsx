import React, { useEffect, useState } from 'react';
import { Restaurant } from '../../api/generated/model';
import Button from '../../components/ui/Button';
import Input from '../../components/ui/Input';
import Card from '../../components/ui/Card';
import { Save, X, Utensils, Clock, Image as ImageIcon } from 'lucide-react';

interface RestaurantFormProps {
    initialData?: Restaurant;
    onSubmit: (data: any) => void;
    isLoading: boolean;
    onCancel: () => void;
}

const RestaurantForm: React.FC<RestaurantFormProps> = ({ initialData, onSubmit, isLoading, onCancel }) => {
    // 1. Estado simplificado: todo coincide con el YAML
    const [formData, setFormData] = useState({
        name: '',
        description: '',
        cuisine: '',
        deliveryFee: 0,
        deliveryTimeMin: 15,
        deliveryTimeMax: 45,
        minimumOrder: 10,
        isOpen: true,
        imageUrl: '', // Ahora es puramente un string
    });

    useEffect(() => {
        if (initialData) {
            setFormData({
                name: initialData.name || '',
                description: initialData.description || '',
                cuisine: initialData.cuisine || '',
                deliveryFee: Number(initialData.deliveryFee) || 0,
                deliveryTimeMin: initialData.deliveryTimeMin || 15,
                deliveryTimeMax: initialData.deliveryTimeMax || 45,
                minimumOrder: Number(initialData.minimumOrder) || 10,
                isOpen: initialData.isOpen ?? true,
                imageUrl: initialData.imageUrl || '',
            });
        }
    }, [initialData]);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value, type } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: type === 'number' ? Number(value) : value
        }));
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        onSubmit(formData); // Enviamos el objeto JSON tal cual
    };
    const isEditMode = !!initialData;

    return (
        <form onSubmit={handleSubmit} className="space-y-6">
            <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
                <div className="lg:col-span-2 space-y-6">
                    {/* Información General */}
                    <Card className="p-6 bg-white border border-gray-100">
                        <h3 className="text-lg font-bold mb-4 flex items-center gap-2">
                            <Utensils className="w-5 h-5 text-indigo-500" /> Datos del Restaurante
                        </h3>
                        <div className="space-y-4">
                            <Input label="Nombre del restaurante" name="name" value={formData.name} onChange={handleChange} required />
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">Descripción</label>
                                <textarea
                                    name="description"
                                    value={formData.description}
                                    onChange={handleChange}
                                    className="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none min-h-[100px]"
                                />
                            </div>
                            <Input label="Tipo de cocina" name="cuisine" value={formData.cuisine} onChange={handleChange} required
                                // todo deshabilitado este campo hasta ver que haremos con el
                                   disabled={isEditMode}
                                   helperText={isEditMode ? "El tipo de cocina no se puede modificar temporalmente" : ""}
                                   className={isEditMode ? "bg-gray-100 cursor-not-allowed" : ""} />
                        </div>
                    </Card>

                    {/* Logística */}
                    <Card className="p-6 bg-white border border-gray-100">
                        <h3 className="text-lg font-bold mb-4 flex items-center gap-2">
                            <Clock className="w-5 h-5 text-orange-500" /> Logística y Tarifas
                        </h3>
                        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                            <Input label="Tarifa Envío (€)" type="number" name="deliveryFee" value={formData.deliveryFee} onChange={handleChange} step="0.01" />
                            <Input label="Tiempo Mín (min)" type="number" name="deliveryTimeMin" value={formData.deliveryTimeMin} onChange={handleChange} />
                            <Input label="Tiempo Máx (min)" type="number" name="deliveryTimeMax" value={formData.deliveryTimeMax} onChange={handleChange} />
                        </div>
                    </Card>
                </div>

                {/* Columna Lateral: Imagen y Estado */}
                <div className="space-y-6">
                    <Card className="p-6 bg-white border border-gray-100">
                        <h3 className="text-lg font-bold mb-4 flex items-center gap-2">
                            <ImageIcon className="w-5 h-5 text-blue-500" /> Imagen del Banner
                        </h3>
                        <div className="aspect-video bg-gray-100 rounded-lg overflow-hidden border mb-4">
                            {formData.imageUrl ? (
                                <img src={formData.imageUrl} alt="Preview" className="w-full h-full object-cover" />
                            ) : (
                                <div className="flex items-center justify-center h-full text-gray-400 text-xs text-center p-4">
                                    Pega una URL para ver la previsualización
                                </div>
                            )}
                        </div>
                        <Input
                            label="URL de la imagen"
                            name="imageUrl"
                            value={formData.imageUrl}
                            onChange={handleChange}
                            placeholder="https://ejemplo.com/foto.jpg"
                        />
                    </Card>

                    <div className="flex flex-col gap-3">
                        <Button type="submit" disabled={isLoading} className="w-full justify-center">
                            <Save className="w-4 h-4 mr-2" />
                            {initialData ? 'Guardar Cambios' : 'Crear Restaurante'}
                        </Button>
                        <Button type="button" variant="outline" onClick={onCancel} className="w-full justify-center">
                            <X className="w-4 h-4 mr-2" /> Cancelar
                        </Button>
                    </div>
                </div>
            </div>
        </form>
    );
};

export default RestaurantForm;
