import {
  List, Datagrid, TextField, NumberField, DateField,
  Edit, SimpleForm, TextInput, NumberInput, SelectInput,
  Create, EditButton, DeleteButton,
} from 'react-admin';

const orderStatusChoices = [
  { id: 'PENDING', name: 'Pendiente' },
  { id: 'CONFIRMED', name: 'Confirmado' },
  { id: 'PREPARING', name: 'Preparando' },
  { id: 'READY', name: 'Listo' },
  { id: 'PICKED_UP', name: 'Recogido' },
  { id: 'DELIVERED', name: 'Entregado' },
  { id: 'CANCELLED', name: 'Cancelado' },
];

const deliveryTypeChoices = [
  { id: 'DELIVERY', name: 'Entrega a domicilio' },
  { id: 'PICKUP', name: 'Recogida en local' },
];

// ─── Orders ───────────────────────────────────────────────────────────────────

export const OrderList = () => (
  <List perPage={25} sort={{ field: 'id', order: 'DESC' }}>
    <Datagrid bulkActionButtons={false}>
      <NumberField source="id" />
      <NumberField source="userId" label="Usuario" />
      <NumberField source="restaurantId" label="Restaurante" />
      <TextField source="status" label="Estado" />
      <NumberField source="totalAmount" label="Total" />
      <TextField source="deliveryType" label="Tipo entrega" />
      <DateField source="orderDate" label="Fecha" showTime />
      <EditButton />
      <DeleteButton />
    </Datagrid>
  </List>
);

const OrderForm = () => (
  <>
    <NumberInput source="userId" label="ID Usuario" />
    <NumberInput source="restaurantId" label="ID Restaurante" />
    <NumberInput source="courierId" label="ID Repartidor" />
    <SelectInput source="status" label="Estado" choices={orderStatusChoices} />
    <SelectInput source="deliveryType" label="Tipo de entrega" choices={deliveryTypeChoices} />
    <TextInput source="deliveryAddress" label="Dirección de entrega" multiline fullWidth />
    <TextInput source="specialInstructions" label="Instrucciones especiales" multiline fullWidth />
    <NumberInput source="subtotal" label="Subtotal" />
    <NumberInput source="deliveryFee" label="Tarifa entrega" />
    <NumberInput source="serviceFee" label="Tarifa servicio" />
    <NumberInput source="taxAmount" label="Impuestos" />
    <NumberInput source="discountAmount" label="Descuento" />
    <NumberInput source="totalAmount" label="Total" />
  </>
);

export const OrderEdit = () => (
  <Edit>
    <SimpleForm><OrderForm /></SimpleForm>
  </Edit>
);

export const OrderCreate = () => (
  <Create redirect="list">
    <SimpleForm><OrderForm /></SimpleForm>
  </Create>
);

// ─── Order Items ──────────────────────────────────────────────────────────────

export const OrderItemList = () => (
  <List perPage={25} sort={{ field: 'id', order: 'DESC' }}>
    <Datagrid bulkActionButtons={false}>
      <NumberField source="id" />
      <NumberField source="orderId" label="Pedido" />
      <NumberField source="dishId" label="Plato" />
      <NumberField source="quantity" label="Cantidad" />
      <NumberField source="price" label="Precio" />
      <DeleteButton />
    </Datagrid>
  </List>
);

export const OrderItemCreate = () => (
  <Create redirect="list">
    <SimpleForm>
      <NumberInput source="orderId" label="ID Pedido" />
      <NumberInput source="dishId" label="ID Plato" />
      <NumberInput source="quantity" label="Cantidad" />
      <NumberInput source="price" label="Precio" />
    </SimpleForm>
  </Create>
);

// ─── Order Status History ─────────────────────────────────────────────────────

export const OrderStatusHistoryList = () => (
  <List perPage={25} sort={{ field: 'id', order: 'DESC' }}>
    <Datagrid bulkActionButtons={false}>
      <NumberField source="id" />
      <NumberField source="orderId" label="Pedido" />
      <TextField source="status" label="Estado" />
      <TextField source="notes" label="Notas" />
      <NumberField source="changedByUserId" label="Cambiado por" />
      <DateField source="createdAt" label="Fecha" showTime />
    </Datagrid>
  </List>
);

export const OrderStatusHistoryCreate = () => (
  <Create redirect="list">
    <SimpleForm>
      <NumberInput source="orderId" label="ID Pedido" />
      <SelectInput source="status" label="Estado" choices={orderStatusChoices} />
      <TextInput source="notes" label="Notas" multiline fullWidth />
      <NumberInput source="changedByUserId" label="ID Usuario que cambia" />
    </SimpleForm>
  </Create>
);

// ─── Order Tracking ───────────────────────────────────────────────────────────

export const OrderTrackingList = () => (
  <List perPage={25} sort={{ field: 'id', order: 'DESC' }}>
    <Datagrid bulkActionButtons={false}>
      <NumberField source="id" />
      <NumberField source="orderId" label="Pedido" />
      <NumberField source="courierId" label="Repartidor" />
      <TextField source="status" label="Estado" />
      <NumberField source="latitude" label="Latitud" />
      <NumberField source="longitude" label="Longitud" />
      <DateField source="createdAt" label="Fecha" showTime />
    </Datagrid>
  </List>
);

export const OrderTrackingCreate = () => (
  <Create redirect="list">
    <SimpleForm>
      <NumberInput source="orderId" label="ID Pedido" />
      <NumberInput source="courierId" label="ID Repartidor" />
      <SelectInput source="status" label="Estado" choices={orderStatusChoices} />
      <NumberInput source="latitude" label="Latitud" />
      <NumberInput source="longitude" label="Longitud" />
    </SimpleForm>
  </Create>
);

// ─── Order Issues ─────────────────────────────────────────────────────────────

export const OrderIssueList = () => (
  <List perPage={25} sort={{ field: 'id', order: 'DESC' }}>
    <Datagrid bulkActionButtons={false}>
      <NumberField source="id" />
      <NumberField source="orderId" label="Pedido" />
      <TextField source="type" label="Tipo" />
      <TextField source="description" label="Descripción" />
      <DateField source="createdAt" label="Fecha" showTime />
      <DeleteButton />
    </Datagrid>
  </List>
);

export const OrderIssueCreate = () => (
  <Create redirect="list">
    <SimpleForm>
      <NumberInput source="orderId" label="ID Pedido" />
      <TextInput source="type" label="Tipo de incidencia" />
      <TextInput source="description" label="Descripción" multiline fullWidth />
    </SimpleForm>
  </Create>
);

