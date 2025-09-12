# Análisis de Endpoints - Inconsistencias entre Frontend y Backend

## Resumen

Se ha realizado un análisis comparativo entre los endpoints definidos en el frontend (React) y los implementados en el backend (Quarkus). Se han identificado múltiples inconsistencias y funcionalidades faltantes.

## Endpoints del Backend Implementados

### RestaurantResource
- GET /restaurants - Listar restaurantes ✓
- POST /restaurants - Crear restaurante ✓  
- GET /restaurants/{id} - Obtener restaurante por ID ✓
- PUT /restaurants/{id} - Actualizar restaurante ✓
- DELETE /restaurants/{id} - Eliminar restaurante ✓

### DishResource
- GET /dishes - Listar platos ✓
- POST /dishes - Crear plato ✓
- GET /dishes/{id} - Obtener plato por ID ✓
- PUT /dishes/{id} - Actualizar plato ✓
- DELETE /dishes/{id} - Eliminar plato ✓

### SearchResource
- GET /search/restaurants - Búsqueda de restaurantes ✓
- GET /search/dishes - Búsqueda de platos ✓

## Endpoints del Frontend Esperados (Incompletos)

### RestaurantEndpoints
- [ ] GET /restaurants/{id}/dishes - Obtener platos por restaurante ✗
- [ ] GET /restaurants/categories - Categorías de restaurantes ✗  
- [ ] GET /restaurants/search - Búsqueda avanzada de restaurantes ✗

### DishEndpoints
- [ ] GET /restaurants/{restaurantId}/dish-categories - Categorías de platos por restaurante ✗
- [ ] GET /dishes/search - Búsqueda de platos (ya implementado) ✓

### OrdersEndpoints
- [ ] GET /orders - Listar pedidos ✗
- [ ] POST /orders - Crear pedido ✗
- [ ] GET /orders/{id} - Obtener pedido por ID ✗
- [ ] PUT /orders/{id}/cancel - Cancelar pedido ✗
- [ ] GET /orders/{id}/track - Seguimiento de pedido ✗

### CartEndpoints
- [ ] GET /cart - Obtener carrito ✗
- [ ] POST /cart/add - Agregar al carrito ✗
- [ ] PUT /cart/update - Actualizar carrito ✗
- [ ] DELETE /cart/remove - Eliminar del carrito ✗
- [ ] DELETE /cart/clear - Limpiar carrito ✗

### UsersEndpoints
- [ ] GET /users/addresses - Obtener direcciones de usuario ✗
- [ ] POST /users/addresses - Agregar dirección ✗
- [ ] PUT /users/addresses/{id} - Actualizar dirección ✗
- [ ] DELETE /users/addresses/{id} - Eliminar dirección ✗

### PaymentsEndpoints
- [ ] GET /payments/methods - Métodos de pago ✗
- [ ] POST /payments/process - Procesar pago ✗
- [ ] POST /payments/webhook - Webhook de pagos ✗

### AdminEndpoints (Múltiples)
- [ ] Múltiples endpoints para gestión administrativa (no implementados)

## Recomendaciones

1. **Implementar endpoints faltantes**:
   - Endpoints de búsqueda avanzada de restaurantes
   - Endpoint para obtener platos por restaurante
   - Endpoint para categorías de restaurantes
   - Endpoints completos de pedidos, carrito y usuarios

2. **Alinear estructura de búsqueda**:
   - El frontend espera `/restaurants/search` pero el backend tiene `/search/restaurants`
   - Ajustar endpoints para coincidir con la definición del frontend

3. **Implementar funcionalidades administrativas**:
   - Implementar todos los endpoints relacionados con gestión admin (restaurantes, platos, categorías, pedidos, usuarios)

4. **Validación de datos**:
   - Asegurar que las estructuras de datos coincidan entre frontend y backend

## Prioridad Alta
- Implementar endpoints básicos de restaurantes y platos para funcionalidad principal
- Alinear estructura de búsqueda con el frontend