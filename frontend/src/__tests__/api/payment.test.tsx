import { vi, describe, it, expect } from 'vitest';
import { 
  getPaymentMethods,
  processPayment,
  handleWebhook
} from '../../services/paymentService';

// Mock fetch globally
global.fetch = vi.fn();

describe('Payment API Service', () => {
  beforeEach(() => {
    vi.resetAllMocks();
  });

  it('should get payment methods successfully', async () => {
    const mockResponse = {
      message: 'Payment methods retrieved successfully',
      methods: [
        { id: 'credit_card', name: 'Tarjeta de Crédito' },
        { id: 'paypal', name: 'PayPal' }
      ]
    };
    
    (global.fetch as any).mockResolvedValueOnce({
      ok: true,
      json: async () => mockResponse
    });

    const result = await getPaymentMethods();
    
    expect(result).toEqual(mockResponse);
    expect(fetch).toHaveBeenCalledWith('/api/payments/methods', {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' }
    });
  });

  it('should process payment successfully', async () => {
    const mockPaymentData = {
      amount: 100.0,
      method: 'credit_card',
      orderId: 'order_123'
    };
    
    const mockResponse = {
      message: 'Payment processed successfully',
      transactionId: 'txn_456',
      status: 'completed'
    };

    (global.fetch as any).mockResolvedValueOnce({
      ok: true,
      json: async () => mockResponse
    });

    const result = await processPayment(mockPaymentData);
    
    expect(result).toEqual(mockResponse);
    expect(fetch).toHaveBeenCalledWith('/api/payments/process', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(mockPaymentData)
    });
  });

  it('should handle webhook successfully', async () => {
    const mockWebhookData = {
      event: 'payment.completed',
      data: { transactionId: 'txn_456' }
    };
    
    const mockResponse = {
      message: 'Webhook processed successfully'
    };

    (global.fetch as any).mockResolvedValueOnce({
      ok: true,
      json: async () => mockResponse
    });

    const result = await handleWebhook(mockWebhookData);
    
    expect(result).toEqual(mockResponse);
    expect(fetch).toHaveBeenCalledWith('/api/payments/webhook', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(mockWebhookData)
    });
  });

  it('should handle payment API error', async () => {
    (global.fetch as any).mockResolvedValueOnce({
      ok: false,
      status: 500,
      json: async () => ({ message: 'Internal server error' })
    });

    await expect(getPaymentMethods()).rejects.toThrow('Failed to fetch payment methods');
  });
});