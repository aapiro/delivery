import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { ArrowLeft, Mail, Shield, User } from 'lucide-react';
import { useAdminStore } from '../../store/adminStore';
import { ROUTES } from '../../constants';
import Button from '../../components/ui/Button';
import Input from '../../components/ui/Input';
import Card from '../../components/ui/Card';
import { AdminPageHeader, getAdminErrorMessage } from '../../components/admin/common';
import { useNotify } from '../../hooks/useNotify';

/**
 * Perfil del administrador autenticado (lectura + actualización vía API si está disponible).
 */
const AdminProfilePage: React.FC = () => {
    const { admin, updateProfile, isLoading } = useAdminStore();
    const notify = useNotify();
    const [name, setName] = useState(admin?.name ?? '');
    const [email, setEmail] = useState(admin?.email ?? '');

    React.useEffect(() => {
        if (admin) {
            setName(admin.name);
            setEmail(admin.email);
        }
    }, [admin]);

    if (!admin) {
        return (
            <div className="space-y-6">
                <AdminPageHeader title="Mi perfil" description="No hay sesión de administrador cargada." />
                <Link to={ROUTES.ADMIN.LOGIN} className="text-indigo-600 hover:underline">
                    Ir al inicio de sesión
                </Link>
            </div>
        );
    }

    const handleSave = async () => {
        try {
            await updateProfile({
                name: name.trim(),
                email: email.trim(),
            });
            notify.success('Perfil actualizado', 'Los cambios se han guardado.');
        } catch (e) {
            notify.error('No se pudo guardar', getAdminErrorMessage(e, 'Intenta de nuevo más tarde.'));
        }
    };

    const roleLabel = admin.role.replace(/_/g, ' ').toLowerCase();

    return (
        <div className="mx-auto max-w-3xl space-y-6">
            <Link
                to={ROUTES.ADMIN.DASHBOARD}
                className="inline-flex items-center text-sm text-gray-500 hover:text-indigo-600"
            >
                <ArrowLeft className="mr-1 h-4 w-4" /> Volver al panel
            </Link>

            <AdminPageHeader
                title="Mi perfil"
                description="Datos de tu cuenta en el panel de administración."
            />

            <div className="grid gap-6 md:grid-cols-2">
                <Card className="p-6">
                    <div className="mb-4 flex items-center gap-2 text-gray-900">
                        <Shield className="h-5 w-5 text-indigo-600" />
                        <h2 className="text-lg font-semibold">Rol y estado</h2>
                    </div>
                    <dl className="space-y-3 text-sm">
                        <div>
                            <dt className="text-gray-500">Rol</dt>
                            <dd className="font-medium capitalize text-gray-900">{roleLabel}</dd>
                        </div>
                        <div>
                            <dt className="text-gray-500">Estado</dt>
                            <dd>
                                <span
                                    className={`inline-flex rounded-full px-2 py-0.5 text-xs font-medium ${
                                        admin.isActive
                                            ? 'bg-green-100 text-green-800'
                                            : 'bg-red-100 text-red-800'
                                    }`}
                                >
                                    {admin.isActive ? 'Activa' : 'Inactiva'}
                                </span>
                            </dd>
                        </div>
                        {admin.lastLogin && (
                            <div>
                                <dt className="text-gray-500">Último acceso</dt>
                                <dd className="text-gray-800">{admin.lastLogin}</dd>
                            </div>
                        )}
                    </dl>
                </Card>

                <Card className="p-6 md:col-span-2">
                    <div className="mb-4 flex items-center gap-2 text-gray-900">
                        <User className="h-5 w-5 text-indigo-600" />
                        <h2 className="text-lg font-semibold">Datos editables</h2>
                    </div>
                    <p className="mb-4 text-sm text-gray-600">
                        Los cambios dependen de lo que permita el backend en{' '}
                        <code className="rounded bg-gray-100 px-1 text-xs">/admin/auth/profile</code>.
                    </p>
                    <div className="grid max-w-lg gap-4">
                        <div>
                            <label className="mb-1 flex items-center gap-1 text-sm font-medium text-gray-700">
                                <User className="h-3.5 w-3.5" /> Nombre
                            </label>
                            <Input value={name} onChange={(e) => setName(e.target.value)} autoComplete="name" />
                        </div>
                        <div>
                            <label className="mb-1 flex items-center gap-1 text-sm font-medium text-gray-700">
                                <Mail className="h-3.5 w-3.5" /> Correo
                            </label>
                            <Input
                                type="email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                autoComplete="email"
                            />
                        </div>
                        <div className="flex flex-wrap gap-2 pt-2">
                            <Button
                                type="button"
                                onClick={handleSave}
                                disabled={isLoading || !name.trim() || !email.trim()}
                                className="bg-indigo-600 hover:bg-indigo-700"
                            >
                                {isLoading ? 'Guardando…' : 'Guardar cambios'}
                            </Button>
                        </div>
                    </div>
                </Card>
            </div>
        </div>
    );
};

export default AdminProfilePage;
