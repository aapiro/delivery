import React from 'react';
import { ChevronLeft, ChevronRight } from 'lucide-react';
import Button from './Button';

// 1. Definimos EXACTAMENTE qué datos puede recibir el componente
interface PaginationProps {
    currentPage: number;
    totalPages: number;
    totalElements: number; // <-- Esta es la que te daba error
    pageSize: number;      // <-- Esta también
    onPageChange: (page: number) => void;
    isLoading?: boolean;   // Opcional
}

const Pagination: React.FC<PaginationProps> = ({
                                                   currentPage,
                                                   totalPages,
                                                   totalElements,
                                                   pageSize,
                                                   onPageChange,
                                                   isLoading
                                               }) => {
    // Si no hay páginas, no mostramos nada
    if (totalPages <= 0) return null;

    return (
        <div className="flex items-center justify-between px-4 py-3 bg-white border-t border-gray-200 sm:px-6">
            {/* Versión Móvil */}
            <div className="flex justify-between flex-1 sm:hidden">
                <Button
                    onClick={() => onPageChange(currentPage - 1)}
                    disabled={currentPage === 0 || isLoading}
                >
                    Anterior
                </Button>
                <Button
                    onClick={() => onPageChange(currentPage + 1)}
                    disabled={currentPage >= totalPages - 1 || isLoading}
                >
                    Siguiente
                </Button>
            </div>

            {/* Versión Escritorio */}
            <div className="hidden sm:flex-1 sm:flex sm:items-center sm:justify-between">
                <div>
                    <p className="text-sm text-gray-700">
                        Mostrando <span className="font-medium">{currentPage * pageSize + 1}</span> a{' '}
                        <span className="font-medium">
                            {Math.min((currentPage + 1) * pageSize, totalElements)}
                        </span> de{' '}
                        <span className="font-medium">{totalElements}</span> resultados
                    </p>
                </div>

                <div className="flex gap-2">
                    <Button
                        variant="outline"
                        size="sm"
                        onClick={() => onPageChange(currentPage - 1)}
                        disabled={currentPage === 0 || isLoading}
                    >
                        <ChevronLeft className="w-4 h-4" /> Anterior
                    </Button>

                    <div className="flex items-center px-4 text-sm font-semibold text-gray-700 bg-gray-50 rounded-md border">
                        {currentPage + 1} / {totalPages}
                    </div>

                    <Button
                        variant="outline"
                        size="sm"
                        onClick={() => onPageChange(currentPage + 1)}
                        disabled={currentPage >= totalPages - 1 || isLoading}
                    >
                        Siguiente <ChevronRight className="w-4 h-4" />
                    </Button>
                </div>
            </div>
        </div>
    );
};

export default Pagination;