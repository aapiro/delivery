import axios, { AxiosRequestConfig } from 'axios';
import { adminApiClient } from '../services/adminApiClient';

/**
 * Puente Orval → mismo cliente que el panel admin (`adminApiClient`).
 * Base URL y Bearer del admin vienen de `API_CONFIG` + persist Zustand (no del token público).
 */
export const customInstance = <T>(config: AxiosRequestConfig): Promise<T> => {
    const source = axios.CancelToken.source();

    const promise = adminApiClient
        .request<T>({
            ...config,
            cancelToken: source.token,
        })
        .then(({ data }) => data);

    // @ts-expect-error Orval/React Query pueden colgar .cancel en la promesa
    promise.cancel = () => {
        source.cancel('Query was cancelled');
    };

    return promise;
};

export default customInstance;
