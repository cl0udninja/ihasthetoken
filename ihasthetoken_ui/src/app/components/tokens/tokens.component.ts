import { Component, OnInit, OnDestroy } from '@angular/core';
import { Ajax } from '../../ajax.service';
import { User } from '../users/users.component';

@Component({
    styleUrls: ['./tokens.component.scss'],
    templateUrl: './tokens.component.html'
})
export class TokenQueueComponent implements OnInit, OnDestroy {
    tokens: Token[] = [];
    users: { [key: string]: User } = {};
    userArray: User[] = [];
    initialized: boolean = false;
    markDoneDialog: boolean = false;
    getInLineDialog: boolean = false;

    tokenToDelete: Token = null;
    tokenError: string = null;

    userToGetInLine: User = new User();

    pollId: number;

    constructor(private ajax: Ajax) { }

    public hasTokens() {
        return this.tokens.length > 0;
    }

    ngOnInit() {
        this.loadTokens();
        this.pollId = window.setInterval(() => this.loadTokens(), 15000);
    }
    ngOnDestroy() {
        if (this.pollId) {
            window.clearInterval(this.pollId);
        }
    }

    loadTokens() {
        this.initialized = false;
        this.loadTokenData().then(() => {
            this.initialized = true;
        }).catch(e => {
            console.error('Error loading tokens', e);
            this.tokenError = 'Error loading tokens';
        });
    }

    getLabelClass(token: Token): string {
        if (token.status === 'IN_PROGRESS') {
            return 'label-success';
        }
        if (token.status === 'CREATED') {
            return 'label-light-blue';
        }
        return '';
    }

    loadTokenData(): Promise<boolean> {
        return this.ajax.get('/user').then(users => {
            this.userArray = users;
            users.forEach(user => {
                this.users[user.id] = user;
            });
            return this.ajax.get('/token').then(tokens => {
                this.tokens = tokens;
                this.tokens.forEach(token => {
                    token.userName = this.users[token.userId].name;
                });
                return true;
            });
        });
    }
    openMarkDone(token: Token) {
        if (!token || token.status !== 'IN_PROGRESS') {
            return;
        }
        this.tokenToDelete = token;
        this.markDoneDialog = true;
    }
    noDoneToken() {
        this.tokenToDelete = null;
        this.markDoneDialog = false;
    }
    yesDoneToken() {
        this.tokenToDelete.status = 'RELEASED';
        this.ajax.post('/token', null, this.tokenToDelete).then(() => {
            this.tokenToDelete = null;
            this.markDoneDialog = false;
            this.loadTokens();
        }).catch(e => {
            console.error('Error releasing token', e);
            this.tokenToDelete = null;
            this.tokenError = 'Error releasing token';
            this.markDoneDialog = false;
            this.loadTokens();
        });
    }
    getInLine() {
        var token: Token = {
            userId: this.userToGetInLine.id,
            id: null,
            created: null,
            status: 'CREATED',
            userName: this.userToGetInLine.name
        };
        this.ajax.post('/token', null, token).then(() => {
            this.getInLineDialog = false;
            this.userToGetInLine = new User();
            this.loadTokens();
        }).catch(e => {
            console.error('Error signing up', e);
            this.tokenError = 'Error signing up';
            this.getInLineDialog = false;
            this.userToGetInLine = new User();
            this.loadTokens();
        });
    }
    cancelGetInLine() {
        this.getInLineDialog = false;
        this.userToGetInLine = new User();
    }
}

export class Token {
    id: string;
    userId: string;
    userName: string;
    created: Date;
    status: string;
}
