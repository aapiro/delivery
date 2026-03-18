import {
  List, Datagrid, TextField, NumberField, BooleanField, DateField,
  SimpleForm, TextInput, NumberInput, BooleanInput, SelectInput,
  Create,
} from 'react-admin';

// ─── Notifications ────────────────────────────────────────────────────────────

const notificationTypeChoices = [
  { id: 'ORDER_UPDATE', name: 'Actualización pedido' },
  { id: 'PROMOTION', name: 'Promoción' },
  { id: 'SYSTEM', name: 'Sistema' },
  { id: 'PAYMENT', name: 'Pago' },
];

export const NotificationList = () => (
  <List perPage={25} sort={{ field: 'id', order: 'DESC' }}>
    <Datagrid bulkActionButtons={false}>
      <NumberField source="id" />
      <NumberField source="userId" label="Usuario" />
      <TextField source="title" label="Título" />
      <TextField source="type" label="Tipo" />
      <BooleanField source="isRead" label="Leída" />
      <DateField source="createdAt" label="Fecha" showTime />
    </Datagrid>
  </List>
);

export const NotificationCreate = () => (
  <Create redirect="list">
    <SimpleForm>
      <NumberInput source="userId" label="ID Usuario" />
      <TextInput source="title" label="Título" fullWidth />
      <TextInput source="message" label="Mensaje" multiline fullWidth />
      <SelectInput source="type" label="Tipo" choices={notificationTypeChoices} />
      <TextInput source="data" label="Datos extra (JSON)" multiline fullWidth />
    </SimpleForm>
  </Create>
);

// ─── Notification Tokens ──────────────────────────────────────────────────────

const platformChoices = [
  { id: 'IOS', name: 'iOS' },
  { id: 'ANDROID', name: 'Android' },
  { id: 'WEB', name: 'Web' },
];

export const NotificationTokenList = () => (
  <List perPage={25} sort={{ field: 'id', order: 'DESC' }}>
    <Datagrid bulkActionButtons={false}>
      <NumberField source="id" />
      <NumberField source="userId" label="Usuario" />
      <TextField source="platform" label="Plataforma" />
      <TextField source="token" label="Token" />
      <BooleanField source="isActive" label="Activo" />
    </Datagrid>
  </List>
);

export const NotificationTokenCreate = () => (
  <Create redirect="list">
    <SimpleForm>
      <NumberInput source="userId" label="ID Usuario" />
      <SelectInput source="platform" label="Plataforma" choices={platformChoices} />
      <TextInput source="token" label="Token FCM/APNs" fullWidth />
      <BooleanInput source="isActive" label="Activo" defaultValue={true} />
    </SimpleForm>
  </Create>
);

// ─── Audit Logs (read-only) ───────────────────────────────────────────────────

export const AuditLogList = () => (
  <List perPage={25} sort={{ field: 'id', order: 'DESC' }}>
    <Datagrid bulkActionButtons={false}>
      <NumberField source="id" />
      <TextField source="tableName" label="Tabla" />
      <NumberField source="recordId" label="ID Registro" />
      <TextField source="action" label="Acción" />
      <DateField source="createdAt" label="Fecha" showTime />
    </Datagrid>
  </List>
);

