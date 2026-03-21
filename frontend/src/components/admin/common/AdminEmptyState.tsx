import React from 'react';
import { clsx } from 'clsx';
import { Inbox } from 'lucide-react';

export interface AdminEmptyStateProps {
    title?: string;
    message: string;
    icon?: React.ReactNode;
    className?: string;
}

/**
 * Lista o sección sin datos (estilo consistente con el admin).
 */
const AdminEmptyState: React.FC<AdminEmptyStateProps> = ({
    title,
    message,
    icon,
    className,
}) => (
    <div
        className={clsx(
            'rounded-xl border border-dashed border-gray-300 bg-white py-16 text-center text-gray-500',
            className
        )}
    >
        <div className="mx-auto mb-3 flex justify-center text-gray-400">
            {icon ?? <Inbox className="h-10 w-10" aria-hidden />}
        </div>
        {title != null && title !== '' && (
            <p className="text-sm font-semibold text-gray-700">{title}</p>
        )}
        <p className={clsx('text-sm', title && 'mt-1')}>{message}</p>
    </div>
);

export default AdminEmptyState;
