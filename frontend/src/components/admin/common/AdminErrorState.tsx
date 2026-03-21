import React from 'react';
import { clsx } from 'clsx';
import Button from '../../ui/Button';
import { getAdminErrorMessage } from './adminErrorUtils';

export interface AdminErrorStateProps {
    /** Título corto (opcional) */
    title?: string;
    /** Texto detallado; si no se pasa, se infiere de `error` */
    message?: string;
    error?: unknown;
    fallbackMessage?: string;
    onRetry?: () => void;
    retryLabel?: string;
    className?: string;
}

/**
 * Estado de error unificado con reintento opcional (invalidate/refetch o reload).
 */
const AdminErrorState: React.FC<AdminErrorStateProps> = ({
    title = 'Algo salió mal',
    message,
    error,
    fallbackMessage = 'No se pudo completar la operación.',
    onRetry,
    retryLabel = 'Reintentar',
    className,
}) => {
    const resolvedMessage =
        message ?? (error !== undefined ? getAdminErrorMessage(error, fallbackMessage) : fallbackMessage);

    const handleRetry = () => {
        if (onRetry) {
            onRetry();
            return;
        }
        window.location.reload();
    };

    return (
        <div
            className={clsx(
                'flex flex-col items-center justify-center rounded-xl border border-dashed border-red-200 bg-red-50/50 py-12 px-4 text-center',
                className
            )}
            role="alert"
        >
            <p className="text-base font-semibold text-red-800">{title}</p>
            <p className="mt-2 max-w-md text-sm text-red-700">{resolvedMessage}</p>
            <Button type="button" className="mt-6 bg-blue-600 hover:bg-blue-700" onClick={handleRetry}>
                {retryLabel}
            </Button>
        </div>
    );
};

export default AdminErrorState;
