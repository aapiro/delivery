import React from 'react';
import { Link } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import Layout from '../components/layout/Layout';
import Card from '../components/ui/Card';
import Button from '../components/ui/Button';
import { api } from '../services/api';
import { ROUTES } from '../constants';
import { useAuthStore } from '../store';

type BackendProfile = {
    message?: string;
    user?: { id: number; name?: string; email: string; userType?: string };
};

const ProfilePage: React.FC = () => {
    const { user, isAuthenticated, updateUser } = useAuthStore();

    const { data, isLoading } = useQuery({
        queryKey: ['profile'],
        enabled: isAuthenticated,
        queryFn: async () => {
            const res = await api.get<any>('/auth/profile');
            return res as any;
        },
        staleTime: 2 * 60 * 1000,
    });

    React.useEffect(() => {
        const backendUser = (data as any)?.user ?? (data as any)?.data?.user;
        if (!backendUser) return;
        updateUser({
            name: backendUser.name || user?.name || '',
            email: backendUser.email,
            isActive: true,
        });
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [data]);

    if (!isAuthenticated) {
        return (
            <Layout>
                <div className="max-w-2xl mx-auto px-4 py-12 text-center">
                    <h1 className="text-2xl font-bold text-gray-900 mb-4">Inicia sesión</h1>
                    <p className="text-gray-600 mb-6">Para ver tu perfil.</p>
                    <Link to={ROUTES.LOGIN}>
                        <Button variant="primary">Ir a login</Button>
                    </Link>
                </div>
            </Layout>
        );
    }

    return (
        <Layout>
            <div className="max-w-3xl mx-auto px-4 py-8">
                <div className="flex items-end justify-between gap-6 mb-6">
                    <div>
                        <h1 className="text-3xl font-bold text-gray-900">Mi perfil</h1>
                        <p className="text-gray-600 mt-1">
                            {isLoading ? 'Cargando...' : 'Gestiona tu información'}
                        </p>
                    </div>
                </div>

                <Card padding="lg">
                    <div className="space-y-4">
                        <div>
                            <p className="text-sm text-gray-500">Nombre</p>
                            <p className="font-medium text-gray-900">
                                {(data as any)?.user?.name ?? user?.name ?? '—'}
                            </p>
                        </div>
                        <div>
                            <p className="text-sm text-gray-500">Email</p>
                            <p className="font-medium text-gray-900">
                                {(data as any)?.user?.email ?? user?.email ?? '—'}
                            </p>
                        </div>
                        <div>
                            <p className="text-sm text-gray-500">Tipo</p>
                            <p className="font-medium text-gray-900">
                                {(data as any)?.user?.userType ?? 'CUSTOMER'}
                            </p>
                        </div>

                        <div className="pt-2">
                            <Link to={ROUTES.ADDRESSES}>
                                <Button variant="outline">Ver direcciones</Button>
                            </Link>
                        </div>
                    </div>
                </Card>
            </div>
        </Layout>
    );
};

export default ProfilePage;

