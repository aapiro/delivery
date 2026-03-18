import { Admin, Resource } from 'react-admin';

// Icons
import RestaurantIcon from '@mui/icons-material/Restaurant';
import PeopleIcon from '@mui/icons-material/People';
import DeliveryDiningIcon from '@mui/icons-material/DeliveryDining';
import FastfoodIcon from '@mui/icons-material/Fastfood';
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';
import LocationOnIcon from '@mui/icons-material/LocationOn';
import CategoryIcon from '@mui/icons-material/Category';
import TuneIcon from '@mui/icons-material/Tune';
import StarIcon from '@mui/icons-material/Star';
import PaymentIcon from '@mui/icons-material/Payment';
import AccountBalanceWalletIcon from '@mui/icons-material/AccountBalanceWallet';
import NotificationsIcon from '@mui/icons-material/Notifications';
import HistoryIcon from '@mui/icons-material/History';
import MonetizationOnIcon from '@mui/icons-material/MonetizationOn';
import CardGiftcardIcon from '@mui/icons-material/CardGiftcard';
import LocalAtmIcon from '@mui/icons-material/LocalAtm';
import SwapHorizIcon from '@mui/icons-material/SwapHoriz';
import ReportProblemIcon from '@mui/icons-material/ReportProblem';
import TrackChangesIcon from '@mui/icons-material/TrackChanges';
import PhoneAndroidIcon from '@mui/icons-material/PhoneAndroid';
import ManageAccountsIcon from '@mui/icons-material/ManageAccounts';
import AssignmentIcon from '@mui/icons-material/Assignment';

// Resources
import { RestaurantList, RestaurantEdit, RestaurantCreate } from './resources/restaurants';
import { UserList, UserEdit, UserCreate } from './resources/users';
import { CourierList, CourierEdit, CourierCreate } from './resources/couriers';
import {
  DishList, DishEdit, DishCreate,
  MenuCategoryList, MenuCategoryEdit, MenuCategoryCreate,
  DishOptionList, DishOptionCreate,
  DishOptionValueList, DishOptionValueCreate,
  DishAvailabilityList, DishAvailabilityCreate,
} from './resources/dishes';
import {
  OrderList, OrderEdit, OrderCreate,
  OrderItemList, OrderItemCreate,
  OrderStatusHistoryList, OrderStatusHistoryCreate,
  OrderTrackingList, OrderTrackingCreate,
  OrderIssueList, OrderIssueCreate,
} from './resources/orders';
import { CourierReviewList, CourierReviewCreate, DishReviewList, DishReviewCreate } from './resources/reviews';
import { AddressList, AddressEdit, AddressCreate } from './resources/addresses';
import {
  PaymentMethodList, PaymentMethodCreate,
  PayoutList, PayoutCreate,
  ReferralCodeList, ReferralCodeEdit, ReferralCodeCreate,
  TipList, TipCreate,
  TransactionList, TransactionCreate,
  UserWalletList, UserWalletCreate,
  WalletTransactionList, WalletTransactionCreate,
} from './resources/financial';
import {
  NotificationList, NotificationCreate,
  NotificationTokenList, NotificationTokenCreate,
  AuditLogList,
} from './resources/misc';

import dataProvider from './dataProvider';

const App = () => (
  <Admin dataProvider={dataProvider} title="Delivery Admin">

    {/* ── Core ─────────────────────────────────────────────────────────── */}
    <Resource
      name="restaurants"
      list={RestaurantList}
      edit={RestaurantEdit}
      create={RestaurantCreate}
      icon={RestaurantIcon}
      options={{ label: 'Restaurantes' }}
    />
    <Resource
      name="users"
      list={UserList}
      edit={UserEdit}
      create={UserCreate}
      icon={PeopleIcon}
      options={{ label: 'Usuarios' }}
    />
    <Resource
      name="couriers"
      list={CourierList}
      edit={CourierEdit}
      create={CourierCreate}
      icon={DeliveryDiningIcon}
      options={{ label: 'Repartidores' }}
    />
    <Resource
      name="addresses"
      list={AddressList}
      edit={AddressEdit}
      create={AddressCreate}
      icon={LocationOnIcon}
      options={{ label: 'Direcciones' }}
    />

    {/* ── Dishes & Menu ────────────────────────────────────────────────── */}
    <Resource
      name="dishes"
      list={DishList}
      edit={DishEdit}
      create={DishCreate}
      icon={FastfoodIcon}
      options={{ label: 'Platos' }}
    />
    <Resource
      name="menu-categories"
      list={MenuCategoryList}
      edit={MenuCategoryEdit}
      create={MenuCategoryCreate}
      icon={CategoryIcon}
      options={{ label: 'Categorías menú' }}
    />
    <Resource
      name="dish-options"
      list={DishOptionList}
      create={DishOptionCreate}
      icon={TuneIcon}
      options={{ label: 'Opciones de plato' }}
    />
    <Resource
      name="dish-option-values"
      list={DishOptionValueList}
      create={DishOptionValueCreate}
      icon={TuneIcon}
      options={{ label: 'Valores de opción' }}
    />
    <Resource
      name="dish-availability"
      list={DishAvailabilityList}
      create={DishAvailabilityCreate}
      icon={ManageAccountsIcon}
      options={{ label: 'Disponibilidad platos' }}
    />

    {/* ── Orders ───────────────────────────────────────────────────────── */}
    <Resource
      name="orders"
      list={OrderList}
      edit={OrderEdit}
      create={OrderCreate}
      icon={ShoppingCartIcon}
      options={{ label: 'Pedidos' }}
    />
    <Resource
      name="order-items"
      list={OrderItemList}
      create={OrderItemCreate}
      icon={AssignmentIcon}
      options={{ label: 'Líneas de pedido' }}
    />
    <Resource
      name="order-status-history"
      list={OrderStatusHistoryList}
      create={OrderStatusHistoryCreate}
      icon={HistoryIcon}
      options={{ label: 'Historial estados' }}
    />
    <Resource
      name="order-tracking"
      list={OrderTrackingList}
      create={OrderTrackingCreate}
      icon={TrackChangesIcon}
      options={{ label: 'Seguimiento pedidos' }}
    />
    <Resource
      name="order-issues"
      list={OrderIssueList}
      create={OrderIssueCreate}
      icon={ReportProblemIcon}
      options={{ label: 'Incidencias pedidos' }}
    />

    {/* ── Reviews ──────────────────────────────────────────────────────── */}
    <Resource
      name="courier-reviews"
      list={CourierReviewList}
      create={CourierReviewCreate}
      icon={StarIcon}
      options={{ label: 'Reseñas repartidor' }}
    />
    <Resource
      name="dish-reviews"
      list={DishReviewList}
      create={DishReviewCreate}
      icon={StarIcon}
      options={{ label: 'Reseñas platos' }}
    />

    {/* ── Financial ────────────────────────────────────────────────────── */}
    <Resource
      name="payment-methods"
      list={PaymentMethodList}
      create={PaymentMethodCreate}
      icon={PaymentIcon}
      options={{ label: 'Métodos de pago' }}
    />
    <Resource
      name="transactions"
      list={TransactionList}
      create={TransactionCreate}
      icon={SwapHorizIcon}
      options={{ label: 'Transacciones' }}
    />
    <Resource
      name="payouts"
      list={PayoutList}
      create={PayoutCreate}
      icon={MonetizationOnIcon}
      options={{ label: 'Pagos a terceros' }}
    />
    <Resource
      name="tips"
      list={TipList}
      create={TipCreate}
      icon={LocalAtmIcon}
      options={{ label: 'Propinas' }}
    />
    <Resource
      name="referral-codes"
      list={ReferralCodeList}
      edit={ReferralCodeEdit}
      create={ReferralCodeCreate}
      icon={CardGiftcardIcon}
      options={{ label: 'Códigos referido' }}
    />
    <Resource
      name="user-wallets"
      list={UserWalletList}
      create={UserWalletCreate}
      icon={AccountBalanceWalletIcon}
      options={{ label: 'Wallets usuario' }}
    />
    <Resource
      name="wallet-transactions"
      list={WalletTransactionList}
      create={WalletTransactionCreate}
      icon={AccountBalanceWalletIcon}
      options={{ label: 'Transacciones wallet' }}
    />

    {/* ── Misc ─────────────────────────────────────────────────────────── */}
    <Resource
      name="notifications"
      list={NotificationList}
      create={NotificationCreate}
      icon={NotificationsIcon}
      options={{ label: 'Notificaciones' }}
    />
    <Resource
      name="notification-tokens"
      list={NotificationTokenList}
      create={NotificationTokenCreate}
      icon={PhoneAndroidIcon}
      options={{ label: 'Tokens notificación' }}
    />
    <Resource
      name="audit-logs"
      list={AuditLogList}
      icon={HistoryIcon}
      options={{ label: 'Auditoría' }}
    />
  </Admin>
);

export default App;

