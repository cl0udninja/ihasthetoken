import { Component } from '@angular/core';
import { Router } from '@angular/router';
import {Ajax} from './ajax.service';

@Component({
    selector: 'my-app',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss'],
      providers: [Ajax]
})
export class AppComponent {
    constructor(private router: Router) {
    }
}
