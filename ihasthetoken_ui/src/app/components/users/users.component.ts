import { Component, OnInit } from "@angular/core";
import { Ajax } from '../../ajax.service';

@Component({
    styleUrls: ['./users.component.scss'],
    templateUrl: './users.component.html',
})

export class UserManagementComponent implements OnInit {
    initialized: boolean = false;
    users: User[] = null;
    userError: String = null;
    signUpDialog: boolean = false;
    newUser: User = new User();
    editDialog: boolean = false;

    constructor(private ajax: Ajax) { }

    ngOnInit() {
        this.loadUsers()
    }

    loadUsers() {
        this.initialized = false;
        this.ajax.get('/user').then(users => {
            this.users = users;
            this.initialized = true;
        }).catch(e => {
            console.log('Error loading users', e);
            this.userError = 'Error loading users, check the server side logs for details!';
        });
    }

    cancelSignUp() {
        this.newUser = new User();
        this.signUpDialog = false;
        this.editDialog = false;
    }

    signUp() {
        this.ajax.post('/user', null, this.newUser).then(() => {
            this.newUser = new User();
            this.signUpDialog = false;
            this.editDialog = false;
            this.loadUsers();
        }).catch(e => {
            console.error('Error saving user', e);
            this.newUser = new User();
            this.signUpDialog = false;
            this.editDialog = false;
            this.userError = 'Error sabing user, please try again!';
        })
    }

    editUser(user: User) {
        if (!user) {
            return;
        }
        this.newUser = user;
        this.editDialog = true;
    }
}

export class User {
    id: string;
    name: string;
    email: string;
}
