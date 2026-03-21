import React from 'react';
import { clsx } from 'clsx';
import Card from '../../ui/Card';

export interface AdminSectionCardProps {
    title?: string;
    description?: string;
    children: React.ReactNode;
    className?: string;
    bodyClassName?: string;
    padding?: 'none' | 'sm' | 'md' | 'lg' | 'xl';
}

/**
 * Bloque de contenido del admin con el mismo estilo de tarjeta y encabezado opcional.
 */
const AdminSectionCard: React.FC<AdminSectionCardProps> = ({
    title,
    description,
    children,
    className,
    bodyClassName,
    padding = 'lg',
}) => {
    const hasHeader = Boolean(title || description);

    return (
        <Card padding={padding} className={clsx(className)}>
            {hasHeader && (
                <div className="mb-4">
                    {title != null && title !== '' && (
                        <h3 className="text-lg font-semibold text-gray-900">{title}</h3>
                    )}
                    {description != null && description !== '' && (
                        <p className="mt-1 text-sm text-gray-600">{description}</p>
                    )}
                </div>
            )}
            <div className={bodyClassName}>{children}</div>
        </Card>
    );
};

export default AdminSectionCard;
