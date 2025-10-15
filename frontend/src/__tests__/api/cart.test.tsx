import { vi, describe, it, expect } from 'vitest';
import { 
  getCart,
  addToCart,
  updateCartItem,
  removeFromCart,
  clearCart
} from '../../services/cartService';

// Mock fetch globally
global.fetch = vi.fn();

describe('Cart API Service', () => {
  beforeEach(() => {
    vi.resetAllMocks();
  });

  it('should get cart contents successfully', async () => {
    const mockResponse = {
      message: 'Cart retrieved successfully',
      items: [],
      total: 0.0
    };
    
    (global.fetch as any).mockResolvedValueOnce({
      ok: true,
      json: async () => mockResponse
    });

    const result = await getCart();
    
    expect(result).toEqual(mockResponse);
    expect(fetch).toHaveBeenCalledWith('/api/cart', {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' }
    });
  });

  it('should add item to cart successfully', async () => {
    const mockItem = { id: 1, name: 'Test Item', quantity: 1, price: 10.99 };
    const mockResponse = {
      message: 'Item added to cart successfully',
      item: mockItem
    };

    (global.fetch as any).mockResolvedValueOnce({
      ok: true,
      json: async () => mockResponse
    });

    const result = await addToCart(mockItem);
    
    expect(result).toEqual(mockResponse);
    expect(fetch).toHaveBeenCalledWith('/api/cart/add', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(mockItem)
    });
  });

  it('should update cart item successfully', async () => {
    const mockUpdate = { id: 1, quantity: 2 };
    const mockResponse = {
      message: 'Cart updated successfully',
      item: { ...mockUpdate, price: 10.99 }
    };

    (global.fetch as any).mockResolvedValueOnce({
      ok: true,
      json: async () => mockResponse
    });

    const result = await updateCartItem(mockUpdate);
    
    expect(result).toEqual(mockResponse);
    expect(fetch).toHaveBeenCalledWith('/api/cart/update', {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(mockUpdate)
    });
  });

  it('should remove item from cart successfully', async () => {
    const mockItemId = 1;
    const mockResponse = {
      message: 'Item removed from cart successfully'
    };

    (global.fetch as any).mockResolvedValueOnce({
      ok: true,
      json: async () => mockResponse
    });

    const result = await removeFromCart(mockItemId);
    
    expect(result).toEqual(mockResponse);
    expect(fetch).toHaveBeenCalledWith(`/api/cart/remove/${mockItemId}`, {
      method: 'DELETE',
      headers: { 'Content-Type': 'application/json' }
    });
  });

  it('should clear cart successfully', async () => {
    const mockResponse = {
      message: 'Cart cleared successfully'
    };

    (global.fetch as any).mockResolvedValueOnce({
      ok: true,
      json: async () => mockResponse
    });

    const result = await clearCart();
    
    expect(result).toEqual(mockResponse);
    expect(fetch).toHaveBeenCalledWith('/api/cart/clear', {
      method: 'DELETE',
      headers: { 'Content-Type': 'application/json' }
    });
  });

  it('should handle cart API error', async () => {
    (global.fetch as any).mockResolvedValueOnce({
      ok: false,
      status: 500,
      json: async () => ({ message: 'Internal server error' })
    });

    await expect(getCart()).rejects.toThrow('Failed to fetch cart');
  });
});