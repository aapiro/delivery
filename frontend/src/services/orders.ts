import { api } from './api';
import { Order, OrderItem, OrderStatus, PaymentMethod, PaymentStatus } from '../types';

// ============= ORDER SERVICE =============

export interface CreateOrderRequest {
  restaurantId: number;
  deliveryAddress: string;
  deliveryInstructions?: string;
  estimatedDeliveryTime?: string;
  paymentMethod: PaymentMethod;
  items: Array<{
    dishId: number;
    quantity: number;
    specialInstructions?: string;
  }>;
}

export interface UpdateOrderStatusRequest {
  status: OrderStatus;
}

export interface GetOrdersParams {
  status?: OrderStatus;
  limit?: number;
  offset?: number;
}

/**
 * Create a new order
 */
export const createOrder = async (orderData: CreateOrderRequest): Promise<Order> => {
  try {
    const response = await api.post<{ data: Order }>('/orders', orderData);
    return response.data;
  } catch (error) {
    throw new Error(`Failed to create order: ${error instanceof Error ? error.message : 'Unknown error'}`);
  }
};

/**
 * Get all orders for a user or admin
 */
export const getOrders = async (params?: GetOrdersParams): Promise<Order[]> => {
  try {
    const queryParams = new URLSearchParams();
    
    if (params?.status) queryParams.append('status', params.status);
    if (params?.limit) queryParams.append('limit', params.limit.toString());
    if (params?.offset) queryParams.append('offset', params.offset.toString());

    const queryString = queryParams.toString() ? `?${queryParams.toString()}` : '';
    const response = await api.get<{ data: Order[] }>(`/orders${queryString}`);
    
    return response.data;
  } catch (error) {
    throw new Error(`Failed to fetch orders: ${error instanceof Error ? error.message : 'Unknown error'}`);
  }
};

/**
 * Get a specific order by ID
 */
export const getOrderById = async (orderId: number): Promise<Order> => {
  try {
    const response = await api.get<{ data: Order }>(`/orders/${orderId}`);
    return response.data;
  } catch (error) {
    throw new Error(`Failed to fetch order: ${error instanceof Error ? error.message : 'Unknown error'}`);
  }
};

/**
 * Update an order status (admin only)
 */
export const updateOrderStatus = async (
  orderId: number, 
  statusData: UpdateOrderStatusRequest
): Promise<Order> => {
  try {
    const response = await api.put<{ data: Order }>(`/orders/${orderId}`, statusData);
    return response.data;
  } catch (error) {
    throw new Error(`Failed to update order status: ${error instanceof Error ? error.message : 'Unknown error'}`);
  }
};

/**
 * Cancel an order
 */
export const cancelOrder = async (orderId: number): Promise<Order> => {
  try {
    const response = await api.delete<{ data: Order }>(`/orders/${orderId}`);
    return response.data;
  } catch (error) {
    throw new Error(`Failed to cancel order: ${error instanceof Error ? error.message : 'Unknown error'}`);
  }
};

// ============= ORDER UTILS =============

/**
 * Format order status for display
 */
export const formatOrderStatus = (status: OrderStatus): string => {
  switch (status) {
    case OrderStatus.PENDING:
      return 'Pendiente';
    case OrderStatus.CONFIRMED:
      return 'Confirmado';
    case OrderStatus.PREPARING:
      return 'Preparando';
    case OrderStatus.READY:
      return 'Listo para recoger';
    case OrderStatus.OUT_FOR_DELIVERY:
      return 'En camino';
    case OrderStatus.DELIVERED:
      return 'Entregado';
    case OrderStatus.CANCELLED:
      return 'Cancelado';
    default:
      return status;
  }
};

/**
 * Format payment method for display
 */
export const formatPaymentMethod = (method: PaymentMethod): string => {
  switch (method) {
    case PaymentMethod.CREDIT_CARD:
      return 'Tarjeta de crédito';
    case PaymentMethod.DEBIT_CARD:
      return 'Tarjeta de débito';
    case PaymentMethod.CASH:
      return 'Efectivo';
    case PaymentMethod.DIGITAL_WALLET:
      return 'Billetera digital';
    default:
      return method;
  }
};

/**
 * Format payment status for display
 */
export const formatPaymentStatus = (status: PaymentStatus): string => {
  switch (status) {
    case PaymentStatus.PENDING:
      return 'Pendiente';
    case PaymentStatus.PROCESSING:
      return 'Procesando';
    case PaymentStatus.COMPLETED:
      return 'Completado';
    case PaymentStatus.FAILED:
      return 'Fallido';
    case PaymentStatus.REFUNDED:
      return 'Reembolsado';
    default:
      return status;
  }
};