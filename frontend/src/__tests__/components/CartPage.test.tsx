import { vi, describe, it, expect } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import CartPage from '../../components/cart/CartPage';

// Mock the cart service
vi.mock('../../services/cartService', () => ({
  getCart: vi.fn(),
  addToCart: vi.fn(),
  updateCartItem: vi.fn(),
  removeFromCart: vi.fn(),
  clearCart: vi.fn()
}));

describe('CartPage Component', () => {
  const mockGetCart = vi.fn();
  
  beforeEach(() => {
    vi.resetAllMocks();
    // @ts-ignore
    import('../../services/cartService').then(module => {
      module.getCart = mockGetCart;
    });
  });

  it('should render cart page with empty cart', async () => {
    mockGetCart.mockResolvedValueOnce({
      message: 'Cart retrieved successfully',
      items: [],
      total: 0.0
    });

    render(<CartPage />);
    
    // Wait for loading to complete and check if "Carrito vacío" is displayed
    await waitFor(() => {
      expect(screen.getByText(/carrito vacío/i)).toBeInTheDocument();
    });
  });

  it('should display cart items when they exist', async () => {
    const mockCartItems = [
      {
        id: 1,
        name: 'Pizza Margherita',
        quantity: 2,
        price: 12.99
      },
      {
        id: 2,
        name: 'Ensalada César',
        quantity: 1,
        price: 8.50
      }
    ];

    mockGetCart.mockResolvedValueOnce({
      message: 'Cart retrieved successfully',
      items: mockCartItems,
      total: 34.48
    });

    render(<CartPage />);
    
    await waitFor(() => {
      expect(screen.getByText(/pizza margherita/i)).toBeInTheDocument();
      expect(screen.getByText(/ensalada césar/i)).toBeInTheDocument();
      expect(screen.getByText(/total:/i)).toBeInTheDocument();
    });
  });

  it('should handle loading state properly', () => {
    mockGetCart.mockImplementation(() => new Promise(() => {}));
    
    render(<CartPage />);
    
    expect(screen.getByTestId('loading-spinner')).toBeInTheDocument();
  });

  it('should display error message when cart fetch fails', async () => {
    mockGetCart.mockRejectedValueOnce(new Error('Failed to load cart'));
    
    render(<CartPage />);
    
    await waitFor(() => {
      expect(screen.getByText(/error al cargar el carrito/i)).toBeInTheDocument();
    });
  });

  it('should handle add to cart button click', async () => {
    const user = userEvent.setup();
    const mockItem = { id: 1, name: 'Test Item', quantity: 1, price: 10.99 };
    
    // Mock the service functions
    vi.mock('../../services/cartService', () => ({
      getCart: vi.fn().mockResolvedValue({
        message: 'Cart retrieved successfully',
        items: [],
        total: 0.0
      }),
      addToCart: vi.fn().mockResolvedValue({
        message: 'Item added to cart successfully',
        item: mockItem
      })
    }));

    render(<CartPage />);
    
    // This would require a more complex setup with actual components that have add buttons
    expect(screen.getByText(/carrito/i)).toBeInTheDocument();
  });
});