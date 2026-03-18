import {
  List, Datagrid, TextField, NumberField,
  Edit, SimpleForm, TextInput, NumberInput, BooleanInput, SelectInput,
  Create, EditButton, DeleteButton,
} from 'react-admin';

const addressTypeChoices = [
  { id: 'HOME', name: 'Casa' },
  { id: 'WORK', name: 'Trabajo' },
  { id: 'OTHER', name: 'Otro' },
];

export const AddressList = () => (
  <List perPage={25} sort={{ field: 'id', order: 'DESC' }}>
    <Datagrid bulkActionButtons={false}>
      <NumberField source="id" />
      <NumberField source="userId" label="Usuario" />
      <TextField source="addressLine" label="Dirección" />
      <TextField source="city" label="Ciudad" />
      <TextField source="country" label="País" />
      <TextField source="addressType" label="Tipo" />
      <EditButton />
      <DeleteButton />
    </Datagrid>
  </List>
);

const AddressForm = () => (
  <>
    <NumberInput source="userId" label="ID Usuario" />
    <NumberInput source="restaurantId" label="ID Restaurante (opcional)" />
    <SelectInput source="addressType" label="Tipo" choices={addressTypeChoices} />
    <TextInput source="addressLine" label="Dirección completa" fullWidth />
    <TextInput source="street" label="Calle" fullWidth />
    <TextInput source="apartment" label="Apartamento" />
    <TextInput source="floor" label="Piso" />
    <TextInput source="city" label="Ciudad" />
    <TextInput source="state" label="Provincia/Estado" />
    <TextInput source="country" label="País" />
    <TextInput source="zipCode" label="Código postal" />
    <TextInput source="deliveryInstructions" label="Instrucciones de entrega" multiline fullWidth />
    <BooleanInput source="isDefault" label="Predeterminada" />
  </>
);

export const AddressEdit = () => (
  <Edit>
    <SimpleForm><AddressForm /></SimpleForm>
  </Edit>
);

export const AddressCreate = () => (
  <Create redirect="list">
    <SimpleForm><AddressForm /></SimpleForm>
  </Create>
);
