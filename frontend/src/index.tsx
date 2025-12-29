import React from 'react';
import ReactDOM from 'react-dom/client';
import { QueryClientProvider } from '@tanstack/react-query';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';
import { BrowserRouter } from 'react-router-dom';
import './index.css';
import App from './App';
import { queryClient } from './services/queryClient';
import { AuthProvider } from './components/common/AuthProvider';
import {Toaster} from "sonner";

const root = ReactDOM.createRoot(
    document.getElementById('root') as HTMLElement
);

root.render(
    <React.StrictMode>
        <QueryClientProvider client={queryClient}>
            <BrowserRouter>
                <Toaster position="top-right" richColors /> {/* Añade esto */}
                <AuthProvider>
                    <App />
                    {/* React Query DevTools - temporalmente deshabilitado por error de locale */}
                    {/* {process.env.NODE_ENV === 'development' && (
                        <ReactQueryDevtools initialIsOpen={false} />
                    )} */}
                </AuthProvider>
            </BrowserRouter>
        </QueryClientProvider>
    </React.StrictMode>
);