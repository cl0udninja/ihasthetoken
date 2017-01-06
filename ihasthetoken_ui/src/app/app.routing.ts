import { ModuleWithProviders } from '@angular/core/src/metadata/ng_module';
import { Routes, RouterModule } from '@angular/router';

import { TokenQueueComponent } from './components/tokens/index';
import { UserManagementComponent } from './components/users/index';

export const ROUTES: Routes = [
    { path: '', redirectTo: 'tokens', pathMatch: 'full' },
    { path: 'tokens', component: TokenQueueComponent },
    { path: 'users', component: UserManagementComponent }
];

export const ROUTING: ModuleWithProviders = RouterModule.forRoot(ROUTES, { useHash: true });
