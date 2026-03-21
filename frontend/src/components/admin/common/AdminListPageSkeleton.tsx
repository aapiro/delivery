import React from 'react';
import Card from '../../ui/Card';

export interface AdminListPageSkeletonProps {
    /** Líneas de “contenido” dentro de la tarjeta principal */
    contentLines?: number;
    className?: string;
}

/**
 * Placeholder de carga para páginas tipo lista (título + bloque de filtros/contenido).
 */
const AdminListPageSkeleton: React.FC<AdminListPageSkeletonProps> = ({
    contentLines = 4,
    className,
}) => (
    <div className={className ?? 'space-y-6'} aria-busy="true" aria-label="Cargando">
        <div className="h-8 w-48 animate-pulse rounded bg-gray-200" />
        <Card className="p-6">
            <div className="animate-pulse space-y-4">
                {Array.from({ length: contentLines }).map((_, i) => (
                    <div
                        key={i}
                        className="h-4 rounded bg-gray-200"
                        style={{ width: `${100 - i * 12}%` }}
                    />
                ))}
            </div>
        </Card>
    </div>
);

export default AdminListPageSkeleton;
