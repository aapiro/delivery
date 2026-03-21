import React from 'react';
import { clsx } from 'clsx';
import Card from '../../ui/Card';

export interface AdminCardGridSkeletonProps {
    /** Tarjetas a mostrar (p. ej. categorías en grid) */
    count?: number;
    className?: string;
    columnsClassName?: string;
}

/**
 * Grid de tarjetas en pulso (categorías, tarjetas de catálogo, etc.).
 */
const AdminCardGridSkeleton: React.FC<AdminCardGridSkeletonProps> = ({
    count = 8,
    className,
    columnsClassName = 'grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6',
}) => (
    <div className={clsx(columnsClassName, className)} aria-busy="true" aria-label="Cargando">
        {Array.from({ length: count }).map((_, index) => (
            <Card key={index} className="animate-pulse p-6">
                <div className="flex items-center space-x-4">
                    <div className="h-16 w-16 rounded-lg bg-gray-200" />
                    <div className="flex-1 space-y-2">
                        <div className="h-4 w-3/4 rounded bg-gray-200" />
                        <div className="h-3 w-1/2 rounded bg-gray-200" />
                    </div>
                </div>
            </Card>
        ))}
    </div>
);

export default AdminCardGridSkeleton;
