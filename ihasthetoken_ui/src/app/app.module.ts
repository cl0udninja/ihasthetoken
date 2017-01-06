import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { ClarityModule } from 'clarity-angular';
import { AppComponent } from './app.component';
import { ROUTING } from "./app.routing";
import { UserManagementComponent } from './components/users/users.component';
import { TokenQueueComponent } from './components/tokens/tokens.component';

@NgModule({
    declarations: [
        AppComponent,
        UserManagementComponent,
        TokenQueueComponent
    ],
    imports: [
        BrowserModule,
        FormsModule,
        HttpModule,
        ClarityModule.forRoot(),
        ROUTING
    ],
    providers: [],
    bootstrap: [AppComponent]
})
export class AppModule {
}
