import {
  List, Datagrid, TextField, NumberField, BooleanField,
  Edit, SimpleForm, TextInput, NumberInput, BooleanInput,
  Create, EditButton, DeleteButton,
} from 'react-admin';

export const RestaurantList = () => (
  <List perPage={25} sort={{ field: 'id', order: 'DESC' }}>
    <Datagrid bulkActionButtons={false}>
      <NumberField source="id" />
      <TextField source="name" />
      <BooleanField source="isActive" label="Activo" />
      <BooleanField source="isOpen" label="Abierto" />
      <NumberField source="rating" />
      <NumberField source="deliveryFee" label="Tarifa entrega" />
      <NumberField source="minimumOrder" label="Mínimo pedido" />
      <NumberField source="reviewCount" label="Reseñas" />
      <EditButton />
      <DeleteButton />
    </Datagrid>
  </List>
);

const RestaurantForm = () => (
  <>
    <TextInput source="name" label="Nombre" fullWidth />
    <TextInput source="description" label="Descripción" multiline fullWidth />
    <TextInput source="imageUrl" label="URL imagen" fullWidth />
    <NumberInput source="deliveryFee" label="Tarifa de entrega" />
    <NumberInput source="minimumOrder" label="Pedido mínimo" />
    <NumberInput source="deliveryTimeMin" label="Tiempo entrega mín (min)" />
    <NumberInput source="deliveryTimeMax" label="Tiempo entrega máx (min)" />
    <NumberInput source="avgPreparationTime" label="Tiempo preparación promedio" />
    <NumberInput source="maxActiveOrders" label="Máx pedidos activos" />
    <BooleanInput source="isActive" label="Activo" />
    <BooleanInput source="isOpen" label="Abierto" />
  </>
);

export const RestaurantEdit = () => (
  <Edit>
    <SimpleForm><RestaurantForm /></SimpleForm>
  </Edit>
);

export const RestaurantCreate = () => (
  <Create redirect="list">
    <SimpleForm><RestaurantForm /></SimpleForm>
  </Create>
);

