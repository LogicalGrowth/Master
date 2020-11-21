// Metadata
export interface RouteInfo {
  path: string;
  title: string;
  type: string;
  collapse?: string;
  show?: boolean;
  icontype: string;
  // icon: string;
  children?: ChildrenItems[];
}

export interface ChildrenItems {
  path: string;
  title: string;
  ab: string;
  type?: string;
}

// Menu Items
export const ROUTES: RouteInfo[] = [
  {
    path: '/dashboard',
    title: 'Dashboard',
    type: 'link',
    icontype: 'nc-icon nc-bank',
  },
  {
    path: '',
    title: 'Entities',
    type: 'sub',
    collapse: 'entities',
    icontype: 'nc-icon nc-circle-10',
    show: true,
    children: [
      { path: 'resource', title: 'Resource', ab: 'R' },
      { path: 'application-user', title: 'Application User', ab: 'AU' },
      { path: 'payment-method', title: 'Payment Method', ab: 'PM' },
      { path: 'proyect', title: 'Proyect', ab: 'P' },
    ],
  },
  {
    path: '',
    title: 'Test',
    type: 'sub',
    collapse: 'test',
    icontype: 'nc-icon nc-circle-10',
    show: true,
    children: [
      { path: 'category', title: 'Category', ab: 'C' },
      { path: 'checkpoint', title: 'Checkpoint', ab: 'CP' },
      { path: 'review', title: 'Review', ab: 'R' },
    ],
  },
  {
    path: '/components',
    title: 'Components',
    type: 'sub',
    collapse: 'components',
    icontype: 'nc-icon nc-layout-11',
    show: false,
    children: [
      { path: 'buttons', title: 'Buttons', ab: 'B' },
      { path: 'grid', title: 'Grid System', ab: 'GS' },
      { path: 'panels', title: 'Panels', ab: 'P' },
      { path: 'sweet-alert', title: 'Sweet Alert', ab: 'SA' },
      { path: 'notifications', title: 'Notifications', ab: 'N' },
      { path: 'icons', title: 'Icons', ab: 'I' },
      { path: 'typography', title: 'Typography', ab: 'T' },
    ],
  },
  {
    path: '/forms',
    title: 'Forms',
    type: 'sub',
    collapse: 'forms',
    icontype: 'nc-icon nc-ruler-pencil',
    show: false,
    children: [
      { path: 'regular', title: 'Regular Forms', ab: 'RF' },
      { path: 'extended', title: 'Extended Forms', ab: 'EF' },
      { path: 'validation', title: 'Validation Forms', ab: 'VF' },
      { path: 'wizard', title: 'Wizard', ab: 'W' },
    ],
  },
  {
    path: '/tables',
    title: 'Tables',
    type: 'sub',
    collapse: 'tables',
    icontype: 'nc-icon nc-single-copy-04',
    show: false,
    children: [
      { path: 'regular', title: 'Regular Tables', ab: 'RT' },
      { path: 'extended', title: 'Extended Tables', ab: 'ET' },
      { path: 'datatables.net', title: 'Datatables.net', ab: 'DT' },
    ],
  },
  {
    path: '/maps',
    title: 'Maps',
    type: 'sub',
    collapse: 'maps',
    icontype: 'nc-icon nc-pin-3',
    show: false,
    children: [
      { path: 'google', title: 'Google Maps', ab: 'GM' },
      { path: 'fullscreen', title: 'Full Screen Map', ab: 'FSM' },
      { path: 'vector', title: 'Vector Map', ab: 'VM' },
    ],
  },
  {
    path: '/widgets',
    title: 'Widgets',
    type: 'link',
    icontype: 'nc-icon nc-box',
  },
  {
    path: '/charts',
    title: 'Charts',
    type: 'link',
    icontype: 'nc-icon nc-chart-bar-32',
  },
  {
    path: '/calendar',
    title: 'Calendar',
    type: 'link',
    icontype: 'nc-icon nc-calendar-60',
  },
  {
    path: '/pages',
    title: 'Pages',
    collapse: 'pages',
    type: 'sub',
    icontype: 'nc-icon nc-book-bookmark',
    show: false,
    children: [
      { path: 'timeline', title: 'Timeline Page', ab: 'T' },
      { path: 'user', title: 'User Page', ab: 'UP' },
      { path: 'login', title: 'Login Page', ab: 'LP' },
      { path: 'register', title: 'Register Page', ab: 'RP' },
      { path: 'lock', title: 'Lock Screen Page', ab: 'LSP' },
    ],
  },
];
