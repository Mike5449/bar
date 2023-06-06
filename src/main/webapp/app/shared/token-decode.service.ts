
import { Injectable } from '@angular/core';
import { LocalStorageService, SessionStorageService } from 'ngx-webstorage';
import jwtDecode from 'jwt-decode';
import { SharedService } from './shared.service';

@Injectable({
  providedIn: 'root'
})
export class TokenDecodeService {

  constructor(private localStorageService: LocalStorageService, private sessionStorageService: SessionStorageService) {}

  getTokenInformation(): any {
    let result: any = '';
    const token: string | null =
      this.localStorageService.retrieve('authenticationToken') ?? this.sessionStorageService.retrieve('authenticationToken');
      if (token) {
        result = jwtDecode(token != null ? token : '');
      }
      SharedService.tokenValue = result;
    return result;
  }
}
