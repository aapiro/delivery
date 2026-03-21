import React from 'react';
import { Link } from 'react-router-dom';
import { Home, LayoutDashboard } from 'lucide-react';
import { ROUTES } from '../../constants';
import Button from '../../components/ui/Button';
import { AdminPageHeader, AdminEmptyState } from '../../components/admin/common';

const AdminNotFoundPage: React.FC = () => (
    <div className="mx-auto max-w-lg space-y-6">
        <AdminPageHeader title="Página no encontrada" description="La ruta no existe en el panel de administración." />
        <AdminEmptyState
            title="404"
            message="Revisa la URL o vuelve al inicio del panel."
            icon={<span className="text-4xl font-bold text-gray-300">?</span>}
        />
        <div className="flex flex-wrap justify-center gap-3">
            <Link to={ROUTES.ADMIN.DASHBOARD}>
                <Button type="button" className="bg-indigo-600 hover:bg-indigo-700">
                    <LayoutDashboard className="mr-2 h-4 w-4" />
                    Dashboard
                </Button>
            </Link>
            <Link to={ROUTES.HOME}>
                <Button type="button" variant="outline">
                    <Home className="mr-2 h-4 w-4" />
                    Sitio público
                </Button>
            </Link>
        </div>
    </div>
);

export default AdminNotFoundPage;
