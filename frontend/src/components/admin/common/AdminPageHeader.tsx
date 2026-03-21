import React from 'react';
import { clsx } from 'clsx';

export interface AdminPageHeaderProps {
    /** Título principal de la pantalla (h1) */
    title: string;
    /** Subtítulo o métrica bajo el título */
    description?: React.ReactNode;
    /** Botones u otras acciones alineadas a la derecha */
    actions?: React.ReactNode;
    className?: string;
}

/**
 * Cabecera consistente para páginas del panel admin (título + descripción + acciones).
 */
const AdminPageHeader: React.FC<AdminPageHeaderProps> = ({
    title,
    description,
    actions,
    className,
}) => (
    <div
        className={clsx(
            'flex flex-col gap-4 sm:flex-row sm:items-start sm:justify-between',
            className
        )}
    >
        <div className="min-w-0">
            <h1 className="text-2xl font-bold text-gray-900">{title}</h1>
            {description != null && description !== '' && (
                <div className="mt-1 text-sm text-gray-600">{description}</div>
            )}
        </div>
        {actions != null && <div className="flex flex-shrink-0 flex-wrap items-center gap-2">{actions}</div>}
    </div>
);

export default AdminPageHeader;
