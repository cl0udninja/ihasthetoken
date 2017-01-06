/* tslint:disable:no-unused-variable */

import { TestBed, async, ComponentFixture } from '@angular/core/testing';
import { AppComponent } from './app.component';
import { ClarityModule } from "clarity-angular";
import { FormsModule } from '@angular/forms';
import { ROUTING } from "./app.routing";
import { APP_BASE_HREF } from "@angular/common";
import { UserManagementComponent } from './components/users/users.component';
import { TokenQueueComponent } from './components/tokens/tokens.component';

describe('AppComponent', () => {

    let fixture: ComponentFixture<any>;
    let compiled: any;

    beforeEach(() => {
        TestBed.configureTestingModule({
            declarations: [
                AppComponent,
                UserManagementComponent,
                TokenQueueComponent
            ],
            imports: [
                ClarityModule.forRoot(),
                FormsModule,
                ROUTING
            ],
            providers: [{ provide: APP_BASE_HREF, useValue: '/' }]
        });

        fixture = TestBed.createComponent(AppComponent);
        fixture.detectChanges();
        compiled = fixture.nativeElement;


    });

    afterEach(() => {
        fixture.destroy();
    });

    it('should create the app', async(() => {
        expect(compiled).toBeTruthy();
    }));


});
