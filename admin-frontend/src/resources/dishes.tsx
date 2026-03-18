import {
  List, Datagrid, TextField, NumberField, BooleanField,
  Edit, SimpleForm, TextInput, NumberInput, BooleanInput,
  Create, EditButton, DeleteButton,
} from 'react-admin';

// ─── Dishes ──────────────────────────────────────────────────────────────────

export const DishList = () => (
  <List perPage={25} sort={{ field: 'id', order: 'DESC' }}>
    <Datagrid bulkActionButtons={false}>
      <NumberField source="id" />
      <NumberField source="restaurantId" label="Restaurante" />
      <TextField source="name" label="Nombre" />
      <NumberField source="price" label="Precio" />
      <BooleanField source="isAvailable" label="Disponible" />
      <BooleanField source="isPopular" label="Popular" />
      <NumberField source="preparationTime" label="Prep. (min)" />
      <EditButton />
      <DeleteButton />
    </Datagrid>
  </List>
);

const DishForm = () => (
  <>
    <NumberInput source="restaurantId" label="ID Restaurante" />
    <NumberInput source="categoryId" label="ID Categoría" />
    <TextInput source="name" label="Nombre" fullWidth />
    <TextInput source="description" label="Descripción" multiline fullWidth />
    <TextInput source="imageUrl" label="URL imagen" fullWidth />
    <NumberInput source="price" label="Precio" />
    <NumberInput source="preparationTime" label="Tiempo preparación (min)" />
    <NumberInput source="stock" label="Stock" />
    <BooleanInput source="stockUnlimited" label="Stock ilimitado" />
    <BooleanInput source="isAvailable" label="Disponible" />
    <BooleanInput source="isPopular" label="Popular" />
  </>
);

export const DishEdit = () => (
  <Edit>
    <SimpleForm><DishForm /></SimpleForm>
  </Edit>
);

export const DishCreate = () => (
  <Create redirect="list">
    <SimpleForm><DishForm /></SimpleForm>
  </Create>
);

// ─── Menu Categories ──────────────────────────────────────────────────────────

export const MenuCategoryList = () => (
  <List perPage={25} sort={{ field: 'id', order: 'DESC' }}>
    <Datagrid bulkActionButtons={false}>
      <NumberField source="id" />
      <NumberField source="restaurantId" label="Restaurante" />
      <TextField source="name" label="Nombre" />
      <TextField source="slug" />
      <NumberField source="displayOrder" label="Orden" />
      <BooleanField source="isActive" label="Activo" />
      <EditButton />
      <DeleteButton />
    </Datagrid>
  </List>
);

const MenuCategoryForm = () => (
  <>
    <NumberInput source="restaurantId" label="ID Restaurante" />
    <TextInput source="name" label="Nombre" fullWidth />
    <TextInput source="slug" />
    <TextInput source="icon" label="Icono" />
    <NumberInput source="displayOrder" label="Orden de visualización" />
    <BooleanInput source="isActive" label="Activo" />
  </>
);

export const MenuCategoryEdit = () => (
  <Edit>
    <SimpleForm><MenuCategoryForm /></SimpleForm>
  </Edit>
);

export const MenuCategoryCreate = () => (
  <Create redirect="list">
    <SimpleForm><MenuCategoryForm /></SimpleForm>
  </Create>
);

// ─── Dish Options ─────────────────────────────────────────────────────────────

export const DishOptionList = () => (
  <List perPage={25} sort={{ field: 'id', order: 'DESC' }}>
    <Datagrid bulkActionButtons={false}>
      <NumberField source="id" />
      <NumberField source="dishId" label="ID Plato" />
      <TextField source="name" label="Nombre" />
      <BooleanField source="required" label="Requerido" />
      <DeleteButton />
    </Datagrid>
  </List>
);

export const DishOptionCreate = () => (
  <Create redirect="list">
    <SimpleForm>
      <NumberInput source="dishId" label="ID Plato" />
      <TextInput source="name" label="Nombre" fullWidth />
      <BooleanInput source="required" label="Requerido" />
    </SimpleForm>
  </Create>
);

// ─── Dish Option Values ───────────────────────────────────────────────────────

export const DishOptionValueList = () => (
  <List perPage={25} sort={{ field: 'id', order: 'DESC' }}>
    <Datagrid bulkActionButtons={false}>
      <NumberField source="id" />
      <NumberField source="optionId" label="ID Opción" />
      <TextField source="name" label="Nombre" />
      <NumberField source="extraPrice" label="Precio extra" />
      <DeleteButton />
    </Datagrid>
  </List>
);

export const DishOptionValueCreate = () => (
  <Create redirect="list">
    <SimpleForm>
      <NumberInput source="optionId" label="ID Opción" />
      <TextInput source="name" label="Nombre" fullWidth />
      <NumberInput source="extraPrice" label="Precio extra" />
    </SimpleForm>
  </Create>
);

// ─── Dish Availability ────────────────────────────────────────────────────────

const daysOfWeek = [
  { id: 0, name: 'Domingo' },
  { id: 1, name: 'Lunes' },
  { id: 2, name: 'Martes' },
  { id: 3, name: 'Miércoles' },
  { id: 4, name: 'Jueves' },
  { id: 5, name: 'Viernes' },
  { id: 6, name: 'Sábado' },
];

export const DishAvailabilityList = () => (
  <List perPage={25} sort={{ field: 'id', order: 'DESC' }}>
    <Datagrid bulkActionButtons={false}>
      <NumberField source="id" />
      <NumberField source="dishId" label="ID Plato" />
      <NumberField source="dayOfWeek" label="Día semana" />
      <TextField source="startTime" label="Inicio" />
      <TextField source="endTime" label="Fin" />
      <DeleteButton />
    </Datagrid>
  </List>
);

export const DishAvailabilityCreate = () => (
  <Create redirect="list">
    <SimpleForm>
      <NumberInput source="dishId" label="ID Plato" />
      <NumberInput source="dayOfWeek" label="Día de la semana (0=Dom, 6=Sáb)" min={0} max={6} />
      <TextInput source="startTime" label="Hora inicio (HH:mm)" placeholder="09:00" />
      <TextInput source="endTime" label="Hora fin (HH:mm)" placeholder="22:00" />
    </SimpleForm>
  </Create>
);

