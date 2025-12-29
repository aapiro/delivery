import axios, { AxiosRequestConfig, AxiosResponse } from 'axios';

// En lugar de importar todo el apiService que trae constantes y lógica pesada,
// vamos a crear una instancia de axios dedicada para Orval aquí mismo
// o importar solo la configuración necesaria.

export const customInstance = <T>(config: AxiosRequestConfig): Promise<T> => {
    const source = axios.CancelToken.source();

    // Usamos la URL que ya sabemos que funciona
    const baseURL = 'http://localhost:8080/api';

    // Recuperamos el token manualmente del localStorage para no importar el apiService
    const token = localStorage.getItem('delivery_token'); // Usa tu CACHE_KEY real aquí

    const promise = axios({
        ...config,
        baseURL,
        headers: {
            ...config.headers,
            Authorization: token ? `Bearer ${token}` : undefined,
        },
        cancelToken: source.token,
    }).then(({ data }) => data);

    // @ts-ignore
    promise.cancel = () => {
        source.cancel('Query was cancelled');
    };

    return promise;
};

export default customInstance;