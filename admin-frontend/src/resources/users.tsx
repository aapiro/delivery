import {
  List, Datagrid, TextField, NumberField, BooleanField, EmailField,
  Edit, SimpleForm, TextInput, BooleanInput, SelectInput,
  Create, EditButton, DeleteButton,
} from 'react-admin';

const userTypeChoices = [
  { id: 'CUSTOMER', name: 'Cliente' },
  { id: 'COURIER', name: 'Repartidor' },
  { id: 'RESTAURANT_OWNER', name: 'Dueño restaurante' },
  { id: 'ADMIN', name: 'Admin' },
];

export const UserList = () => (
  <List perPage={25} sort={{ field: 'id', order: 'DESC' }}>
    <Datagrid bulkActionButtons={false}>
      <NumberField source="id" />
      <TextField source="fullName" label="Nombre completo" />
      <EmailField source="email" />
      <TextField source="phone" label="Teléfono" />
      <TextField source="userType" label="Tipo" />
      <BooleanField source="isActive" label="Activo" />
      <BooleanField source="isVerified" label="Verificado" />
      <EditButton />
      <DeleteButton />
    </Datagrid>
  </List>
);

const UserForm = () => (
  <>
    <TextInput source="fullName" label="Nombre completo" fullWidth />
    <TextInput source="email" fullWidth />
    <TextInput source="phone" label="Teléfono" />
    <SelectInput source="userType" label="Tipo de usuario" choices={userTypeChoices} />
    <BooleanInput source="isActive" label="Activo" />
    <BooleanInput source="isVerified" label="Verificado" />
  </>
);

export const UserEdit = () => (
  <Edit>
    <SimpleForm><UserForm /></SimpleForm>
  </Edit>
);

export const UserCreate = () => (
  <Create redirect="list">
    <SimpleForm><UserForm /></SimpleForm>
  </Create>
);

