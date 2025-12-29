module.exports = {
    deliveryApi: {
        output: {
            mode: 'tags-split',
            target: 'src/api/generated/endpoints.ts',
            schemas: 'src/api/generated/model',
            client: 'react-query',
            // --- ESTO ES LO NUEVO ---
            override: {
                mutator: {
                    path: './src/api/custom-instance.ts', // Ruta a nuestro puente
                    name: 'customInstance',
                },
            },
        },
        input: {
            target: 'http://localhost:8080/q/openapi',
            // target: './openapi.yaml',
        },
    },
};