/**
 * Piezas reutilizables del panel admin (layout de página, estados, tarjetas).
 */
export { default as AdminPageHeader } from './AdminPageHeader';
export type { AdminPageHeaderProps } from './AdminPageHeader';

export { default as AdminSectionCard } from './AdminSectionCard';
export type { AdminSectionCardProps } from './AdminSectionCard';

export { default as AdminErrorState } from './AdminErrorState';
export type { AdminErrorStateProps } from './AdminErrorState';

export { default as AdminEmptyState } from './AdminEmptyState';
export type { AdminEmptyStateProps } from './AdminEmptyState';

export { default as AdminListPageSkeleton } from './AdminListPageSkeleton';
export type { AdminListPageSkeletonProps } from './AdminListPageSkeleton';

export { default as AdminStatsGridSkeleton } from './AdminStatsGridSkeleton';
export type { AdminStatsGridSkeletonProps } from './AdminStatsGridSkeleton';

export { default as AdminCardGridSkeleton } from './AdminCardGridSkeleton';
export type { AdminCardGridSkeletonProps } from './AdminCardGridSkeleton';

export { default as AdminQueryBoundary } from './AdminQueryBoundary';
export type { AdminQueryBoundaryProps } from './AdminQueryBoundary';

export { getAdminErrorMessage } from './adminErrorUtils';

export { default as ProtectedRoute } from './ProtectedRoute';
