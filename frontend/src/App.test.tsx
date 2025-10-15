import React from 'react';
import { render, screen } from '@testing-library/react';
import App from './App';

test('renders learn react link', () => {
  render(<App />);
  const linkElement = screen.getByText(/learn react/i);
  expect(linkElement).toBeInTheDocument();
});

test('app renders without crashing', () => {
  render(<App />);
  // Check that at least one main route is present
  expect(screen.getByText(/carrito/i)).toBeInTheDocument(); // Cart placeholder text
});
