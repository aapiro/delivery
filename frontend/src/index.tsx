import React from 'react';
import ReactDOM from 'react-dom/client';
import {QueryClientProvider} from '@tanstack/react-query';
import {ReactQueryDevtools} from '@tanstack/react-query-devtools';
import {BrowserRouter} from 'react-router-dom';
import './index.css';
import App from './App';
import {queryClient} from './services/queryClient';
import {AuthProvider} from './components/common/AuthProvider';
//import { Toaster } from 'sonner';
import {Toaster} from "./components/ui/toaster";
import {ChakraProvider, defaultSystem} from '@chakra-ui/react'
import {DevSupport} from "@react-buddy/ide-toolbox";
import {ComponentPreviews, useInitial} from "./dev";
/* ---------- Tema opcional ---------- */

const root = ReactDOM.createRoot(
    document.getElementById('root') as HTMLElement
);

root.render(
    <React.StrictMode>
        <ChakraProvider value={defaultSystem}>
            <Toaster/>
            {/* Este script debe ir justo dentro de ChakraProvider */}
            <QueryClientProvider client={queryClient}>
                <BrowserRouter>
                    {/*<Toaster position="top-right" richColors />*/}
                    <AuthProvider>
                        <DevSupport ComponentPreviews={ComponentPreviews}
                                    useInitialHook={useInitial}
                        >
                            <App/>
                        </DevSupport>
                        {/* React Query DevTools - temporalmente deshabilitado por error de locale */}
                    </AuthProvider>
                </BrowserRouter>
                {/* {process.env.NODE_ENV === 'development' && */(
                    <ReactQueryDevtools initialIsOpen={false}/>
                )}
            </QueryClientProvider>
        </ChakraProvider>
    </React.StrictMode>
);