import { Injectable } from "@angular/core";
import { Subject } from "rxjs";

Injectable({
    providedIn: 'root',
  })
  export class SharedService {
    static token: Subject<any> = new Subject<any>();
    static tokenValue: any = '';
    static mapModalVisible = new Subject<boolean>();
    static closeSideBar=new Subject<boolean>();
        constructor(){

    }

    static setToken(): any {
        this.token.next(SharedService.tokenValue);
    }
  }