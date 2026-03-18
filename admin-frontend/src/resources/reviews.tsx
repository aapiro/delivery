import {
  List, Datagrid, TextField, NumberField, DateField,
  SimpleForm, TextInput, NumberInput,
  Create,
} from 'react-admin';

// ─── Courier Reviews ──────────────────────────────────────────────────────────

export const CourierReviewList = () => (
  <List perPage={25} sort={{ field: 'id', order: 'DESC' }}>
    <Datagrid bulkActionButtons={false}>
      <NumberField source="id" />
      <NumberField source="userId" label="Usuario" />
      <NumberField source="courierId" label="Repartidor" />
      <NumberField source="orderId" label="Pedido" />
      <NumberField source="rating" label="Puntuación" />
      <TextField source="comment" label="Comentario" />
      <DateField source="createdAt" label="Fecha" showTime />
    </Datagrid>
  </List>
);

export const CourierReviewCreate = () => (
  <Create redirect="list">
    <SimpleForm>
      <NumberInput source="userId" label="ID Usuario" />
      <NumberInput source="courierId" label="ID Repartidor" />
      <NumberInput source="orderId" label="ID Pedido" />
      <NumberInput source="rating" label="Puntuación (1-5)" min={1} max={5} />
      <TextInput source="comment" label="Comentario" multiline fullWidth />
    </SimpleForm>
  </Create>
);

// ─── Dish Reviews ─────────────────────────────────────────────────────────────

export const DishReviewList = () => (
  <List perPage={25} sort={{ field: 'id', order: 'DESC' }}>
    <Datagrid bulkActionButtons={false}>
      <NumberField source="id" />
      <NumberField source="userId" label="Usuario" />
      <NumberField source="dishId" label="Plato" />
      <NumberField source="orderId" label="Pedido" />
      <NumberField source="rating" label="Puntuación" />
      <TextField source="comment" label="Comentario" />
      <TextField source="tags" label="Tags" />
      <DateField source="createdAt" label="Fecha" showTime />
    </Datagrid>
  </List>
);

export const DishReviewCreate = () => (
  <Create redirect="list">
    <SimpleForm>
      <NumberInput source="userId" label="ID Usuario" />
      <NumberInput source="dishId" label="ID Plato" />
      <NumberInput source="orderId" label="ID Pedido" />
      <NumberInput source="rating" label="Puntuación (1-5)" min={1} max={5} />
      <TextInput source="comment" label="Comentario" multiline fullWidth />
      <TextInput source="tags" label="Tags" />
    </SimpleForm>
  </Create>
);

