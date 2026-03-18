import {
  List, Datagrid, TextField, NumberField, BooleanField,
  Edit, SimpleForm, TextInput, NumberInput, BooleanInput, DateInput,
  Create, EditButton, DeleteButton,
} from 'react-admin';

export const CourierList = () => (
  <List perPage={25} sort={{ field: 'id', order: 'DESC' }}>
    <Datagrid bulkActionButtons={false}>
      <NumberField source="id" />
      <NumberField source="userId" label="ID Usuario" />
      <TextField source="name" label="Nombre" />
      <TextField source="vehicleType" label="Vehículo" />
      <TextField source="vehiclePlate" label="Matrícula" />
      <BooleanField source="isActive" label="Activo" />
      <BooleanField source="isOnline" label="En línea" />
      <NumberField source="currentOrdersCount" label="Pedidos actuales" />
      <EditButton />
      <DeleteButton />
    </Datagrid>
  </List>
);

const CourierForm = () => (
  <>
    <NumberInput source="userId" label="ID Usuario" />
    <TextInput source="name" label="Nombre" fullWidth />
    <TextInput source="vehicleType" label="Tipo de vehículo" />
    <TextInput source="vehiclePlate" label="Matrícula" />
    <TextInput source="licenseNumber" label="N° licencia" />
    <DateInput source="licenseExpiry" label="Vencimiento licencia" />
    <BooleanInput source="isActive" label="Activo" />
    <BooleanInput source="isOnline" label="En línea" />
    <NumberInput source="currentOrdersCount" label="Pedidos actuales" />
  </>
);

export const CourierEdit = () => (
  <Edit>
    <SimpleForm><CourierForm /></SimpleForm>
  </Edit>
);

export const CourierCreate = () => (
  <Create redirect="list">
    <SimpleForm><CourierForm /></SimpleForm>
  </Create>
);

