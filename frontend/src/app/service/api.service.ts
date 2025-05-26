import { HttpClient, HttpHeaders } from '@angular/common/http';
import { EventEmitter, Injectable } from '@angular/core';
import CryptoJs from "crypto-js"
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class ApiService {

  private static BASE_URL = 'http://localhost:2904/v1/api';
  private static ENCRYPTION_KEY = 'encryp-key';
  authStatusChange = new EventEmitter<void>();

  constructor(http: HttpClient) { }

  encryptAndSaveToStorage(key:string, value:string):void {
    const encryptedValue = CryptoJs.AES.encrypt(value, ApiService.ENCRYPTION_KEY).toString();
    localStorage.setItem(key, encryptedValue)
  }

  private getFromStorageAndDecrypt(key:string): any {
    try {
      const encryptedValue = localStorage.getItem(key);
      if(!encryptedValue) return null
      return CryptoJs.AES.decrypt(encryptedValue, ApiService.ENCRYPTION_KEY).toString(CryptoJs.enc.Utf8);
    } catch (error) {
      return null
    }
  }

  private clearAuth() {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
  }

  private getHeader(): HttpHeaders {
    const token = this.getFromStorageAndDecrypt("token");
    return new HttpHeaders({
      Authorization: 'Bearer ${token}'
    })
  }

  // AUTHENTICATION CHECKER
  logout(): void {
    this.clearAuth()
    this.authStatusChange.emit()
  }

  isAuthenticated():boolean{
    const token = this.getFromStorageAndDecrypt("token");
    return !!token;
  }

  isAdmin():boolean{
    const role = this.getFromStorageAndDecrypt("role");
    return role === "MANAGER";
  }
}
