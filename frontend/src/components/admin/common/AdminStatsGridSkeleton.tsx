import React from 'react';
import Card from '../../ui/Card';

export interface AdminStatsGridSkeletonProps {
    /** Número de tarjetas de estadística (por defecto 4, como el dashboard) */
    count?: number;
    className?: string;
}

/**
 * Grid de tarjetas en pulso para el dashboard u otros KPIs.
 */
const AdminStatsGridSkeleton: React.FC<AdminStatsGridSkeletonProps> = ({
    count = 4,
    className,
}) => (
    <div
        className={className ?? 'grid grid-cols-1 gap-6 md:grid-cols-2 lg:grid-cols-4'}
        aria-busy="true"
        aria-label="Cargando estadísticas"
    >
        {Array.from({ length: count }).map((_, i) => (
            <Card key={i} className="p-6">
                <div className="animate-pulse">
                    <div className="mb-2 h-4 w-3/4 rounded bg-gray-200" />
                    <div className="h-8 w-1/2 rounded bg-gray-200" />
                </div>
            </Card>
        ))}
    </div>
);

export default AdminStatsGridSkeleton;
