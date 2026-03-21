import React from 'react';
import AdminErrorState from './AdminErrorState';
import AdminListPageSkeleton from './AdminListPageSkeleton';

export interface AdminQueryBoundaryProps {
    isLoading: boolean;
    error: unknown;
    /** Contenido mientras carga (por defecto esqueleto de lista) */
    loadingFallback?: React.ReactNode;
    errorTitle?: string;
    errorMessage?: string;
    /** Si no se define, `AdminErrorState` hace reload completo */
    onRetry?: () => void;
    children: React.ReactNode;
}

/**
 * Encapsula el patrón loading / error / contenido en pantallas admin basadas en React Query.
 */
const AdminQueryBoundary: React.FC<AdminQueryBoundaryProps> = ({
    isLoading,
    error,
    loadingFallback,
    errorTitle,
    errorMessage,
    onRetry,
    children,
}) => {
    if (isLoading) {
        return <>{loadingFallback ?? <AdminListPageSkeleton />}</>;
    }

    if (error) {
        return (
            <AdminErrorState
                title={errorTitle}
                message={errorMessage}
                error={error}
                onRetry={onRetry}
            />
        );
    }

    return <>{children}</>;
};

export default AdminQueryBoundary;
