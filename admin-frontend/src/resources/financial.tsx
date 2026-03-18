import {
  List, Datagrid, TextField, NumberField, BooleanField, DateField,
  Edit, SimpleForm, TextInput, NumberInput, BooleanInput, SelectInput, DateInput,
  Create, EditButton, DeleteButton,
} from 'react-admin';

// ─── Payment Methods ──────────────────────────────────────────────────────────

const paymentTypeChoices = [
  { id: 'CREDIT_CARD', name: 'Tarjeta crédito' },
  { id: 'DEBIT_CARD', name: 'Tarjeta débito' },
  { id: 'PAYPAL', name: 'PayPal' },
  { id: 'BANK_TRANSFER', name: 'Transferencia' },
  { id: 'CASH', name: 'Efectivo' },
];

export const PaymentMethodList = () => (
  <List perPage={25} sort={{ field: 'id', order: 'DESC' }}>
    <Datagrid bulkActionButtons={false}>
      <NumberField source="id" />
      <NumberField source="userId" label="Usuario" />
      <TextField source="type" label="Tipo" />
      <TextField source="provider" label="Proveedor" />
      <TextField source="lastFour" label="Últimos 4" />
      <BooleanField source="isDefault" label="Predeterminado" />
      <BooleanField source="isActive" label="Activo" />
      <DeleteButton />
    </Datagrid>
  </List>
);

export const PaymentMethodCreate = () => (
  <Create redirect="list">
    <SimpleForm>
      <NumberInput source="userId" label="ID Usuario" />
      <SelectInput source="type" label="Tipo" choices={paymentTypeChoices} />
      <TextInput source="provider" label="Proveedor" />
      <TextInput source="token" label="Token" fullWidth />
      <TextInput source="lastFour" label="Últimos 4 dígitos" />
      <BooleanInput source="isDefault" label="Predeterminado" />
      <BooleanInput source="isActive" label="Activo" defaultValue={true} />
    </SimpleForm>
  </Create>
);

// ─── Payouts ──────────────────────────────────────────────────────────────────

const recipientTypeChoices = [
  { id: 'COURIER', name: 'Repartidor' },
  { id: 'RESTAURANT', name: 'Restaurante' },
];

const payoutStatusChoices = [
  { id: 'PENDING', name: 'Pendiente' },
  { id: 'PROCESSING', name: 'Procesando' },
  { id: 'COMPLETED', name: 'Completado' },
  { id: 'FAILED', name: 'Fallido' },
];

export const PayoutList = () => (
  <List perPage={25} sort={{ field: 'id', order: 'DESC' }}>
    <Datagrid bulkActionButtons={false}>
      <NumberField source="id" />
      <TextField source="recipientType" label="Tipo receptor" />
      <NumberField source="recipientId" label="ID Receptor" />
      <NumberField source="amount" label="Monto" />
      <TextField source="status" label="Estado" />
      <DateField source="periodStart" label="Inicio periodo" />
      <DateField source="periodEnd" label="Fin periodo" />
    </Datagrid>
  </List>
);

export const PayoutCreate = () => (
  <Create redirect="list">
    <SimpleForm>
      <SelectInput source="recipientType" label="Tipo de receptor" choices={recipientTypeChoices} />
      <NumberInput source="recipientId" label="ID Receptor" />
      <NumberInput source="amount" label="Monto" />
      <DateInput source="periodStart" label="Inicio del periodo" />
      <DateInput source="periodEnd" label="Fin del periodo" />
      <SelectInput source="status" label="Estado" choices={payoutStatusChoices} defaultValue="PENDING" />
    </SimpleForm>
  </Create>
);

// ─── Referral Codes ───────────────────────────────────────────────────────────

export const ReferralCodeList = () => (
  <List perPage={25} sort={{ field: 'id', order: 'DESC' }}>
    <Datagrid bulkActionButtons={false}>
      <NumberField source="id" />
      <NumberField source="userId" label="Usuario" />
      <TextField source="code" label="Código" />
      <NumberField source="discountAmount" label="Descuento" />
      <NumberField source="maxUses" label="Máx. usos" />
      <NumberField source="timesUsed" label="Usos" />
      <EditButton />
      <DeleteButton />
    </Datagrid>
  </List>
);

const ReferralCodeForm = ({ isEdit }: { isEdit?: boolean }) => (
  <>
    {!isEdit && <NumberInput source="userId" label="ID Usuario" />}
    <TextInput source="code" label="Código" fullWidth />
    <NumberInput source="discountAmount" label="Monto descuento" />
    <NumberInput source="maxUses" label="Máximo de usos" />
  </>
);

export const ReferralCodeEdit = () => (
  <Edit>
    <SimpleForm><ReferralCodeForm isEdit /></SimpleForm>
  </Edit>
);

export const ReferralCodeCreate = () => (
  <Create redirect="list">
    <SimpleForm><ReferralCodeForm /></SimpleForm>
  </Create>
);

// ─── Tips ─────────────────────────────────────────────────────────────────────

export const TipList = () => (
  <List perPage={25} sort={{ field: 'id', order: 'DESC' }}>
    <Datagrid bulkActionButtons={false}>
      <NumberField source="id" />
      <NumberField source="orderId" label="Pedido" />
      <NumberField source="courierId" label="Repartidor" />
      <NumberField source="amount" label="Monto" />
      <TextField source="tipType" label="Tipo" />
      <DateField source="createdAt" label="Fecha" showTime />
    </Datagrid>
  </List>
);

export const TipCreate = () => (
  <Create redirect="list">
    <SimpleForm>
      <NumberInput source="orderId" label="ID Pedido" />
      <NumberInput source="courierId" label="ID Repartidor" />
      <NumberInput source="amount" label="Monto" />
      <TextInput source="tipType" label="Tipo de propina" />
    </SimpleForm>
  </Create>
);

// ─── Transactions ─────────────────────────────────────────────────────────────

const transactionStatusChoices = [
  { id: 'PENDING', name: 'Pendiente' },
  { id: 'COMPLETED', name: 'Completada' },
  { id: 'FAILED', name: 'Fallida' },
  { id: 'REFUNDED', name: 'Reembolsada' },
];

export const TransactionList = () => (
  <List perPage={25} sort={{ field: 'id', order: 'DESC' }}>
    <Datagrid bulkActionButtons={false}>
      <NumberField source="id" />
      <NumberField source="orderId" label="Pedido" />
      <NumberField source="paymentMethodId" label="Método pago" />
      <NumberField source="amount" label="Monto" />
      <TextField source="status" label="Estado" />
      <TextField source="providerTransactionId" label="ID proveedor" />
      <DateField source="createdAt" label="Fecha" showTime />
    </Datagrid>
  </List>
);

export const TransactionCreate = () => (
  <Create redirect="list">
    <SimpleForm>
      <NumberInput source="orderId" label="ID Pedido" />
      <NumberInput source="paymentMethodId" label="ID Método de pago" />
      <NumberInput source="amount" label="Monto" />
      <SelectInput source="status" label="Estado" choices={transactionStatusChoices} defaultValue="PENDING" />
      <TextInput source="providerTransactionId" label="ID de transacción del proveedor" fullWidth />
    </SimpleForm>
  </Create>
);

// ─── User Wallets ─────────────────────────────────────────────────────────────

export const UserWalletList = () => (
  <List perPage={25} sort={{ field: 'id', order: 'DESC' }}>
    <Datagrid bulkActionButtons={false}>
      <NumberField source="id" />
      <NumberField source="userId" label="Usuario" />
      <NumberField source="balance" label="Saldo" />
      <TextField source="currency" label="Moneda" />
    </Datagrid>
  </List>
);

export const UserWalletCreate = () => (
  <Create redirect="list">
    <SimpleForm>
      <NumberInput source="userId" label="ID Usuario" />
      <NumberInput source="balance" label="Saldo inicial" defaultValue={0} />
      <TextInput source="currency" label="Moneda (ej. EUR)" defaultValue="EUR" />
    </SimpleForm>
  </Create>
);

// ─── Wallet Transactions ──────────────────────────────────────────────────────

const walletTxTypeChoices = [
  { id: 'CREDIT', name: 'Crédito' },
  { id: 'DEBIT', name: 'Débito' },
  { id: 'REFUND', name: 'Reembolso' },
];

export const WalletTransactionList = () => (
  <List perPage={25} sort={{ field: 'id', order: 'DESC' }}>
    <Datagrid bulkActionButtons={false}>
      <NumberField source="id" />
      <NumberField source="walletId" label="Wallet" />
      <NumberField source="orderId" label="Pedido" />
      <TextField source="type" label="Tipo" />
      <NumberField source="amount" label="Monto" />
      <TextField source="description" label="Descripción" />
      <DateField source="createdAt" label="Fecha" showTime />
    </Datagrid>
  </List>
);

export const WalletTransactionCreate = () => (
  <Create redirect="list">
    <SimpleForm>
      <NumberInput source="walletId" label="ID Wallet" />
      <NumberInput source="orderId" label="ID Pedido (opcional)" />
      <SelectInput source="type" label="Tipo" choices={walletTxTypeChoices} />
      <NumberInput source="amount" label="Monto" />
      <TextInput source="description" label="Descripción" multiline fullWidth />
    </SimpleForm>
  </Create>
);

