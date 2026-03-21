import React from 'react';
import { Link } from 'react-router-dom';
import { ArrowLeft, Bell, Database, Lock, Sliders } from 'lucide-react';
import { ROUTES } from '../../constants';
import Card from '../../components/ui/Card';
import { AdminPageHeader } from '../../components/admin/common';

/**
 * Centro de configuración del sistema (secciones preparadas para futuras integraciones).
 */
const AdminSettingsPage: React.FC = () => {
    const sections = [
        {
            icon: Sliders,
            title: 'Preferencias generales',
            body: 'Zona horaria, idioma y formato de moneda. Pendiente de API.',
        },
        {
            icon: Bell,
            title: 'Notificaciones',
            body: 'Alertas por email o webhook para pedidos y errores. Pendiente de API.',
        },
        {
            icon: Lock,
            title: 'Seguridad',
            body: 'Política de contraseñas, 2FA y sesiones. Pendiente de API.',
        },
        {
            icon: Database,
            title: 'Datos y copias',
            body: 'Exportación programada y mantenimiento. Pendiente de API.',
        },
    ];

    return (
        <div className="space-y-6">
            <Link
                to={ROUTES.ADMIN.DASHBOARD}
                className="inline-flex items-center text-sm text-gray-500 hover:text-indigo-600"
            >
                <ArrowLeft className="mr-1 h-4 w-4" /> Volver al panel
            </Link>

            <AdminPageHeader
                title="Configuración del sistema"
                description="Área reservada para ajustes globales. Cada bloque se conectará al backend cuando exista el contrato."
            />

            <div className="grid gap-4 md:grid-cols-2">
                {sections.map(({ icon: Icon, title, body }) => (
                    <Card key={title} className="border border-dashed border-gray-200 p-5">
                        <div className="mb-2 flex items-center gap-2 text-gray-900">
                            <Icon className="h-5 w-5 text-indigo-600" />
                            <h2 className="font-semibold">{title}</h2>
                        </div>
                        <p className="text-sm text-gray-600">{body}</p>
                        <span className="mt-3 inline-block rounded-full bg-gray-100 px-2 py-0.5 text-xs font-medium text-gray-600">
                            Próximamente
                        </span>
                    </Card>
                ))}
            </div>
        </div>
    );
};

export default AdminSettingsPage;
