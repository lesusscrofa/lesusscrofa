import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Bill } from '../model/bill';
import { StringUtils } from '../utils/string-utils';

@Injectable({
  providedIn: 'root'
})
export class BillService {

  private url: string;

  constructor(private http: HttpClient) { 
    this.url = environment.apiUrl + "/bill/client/{0}/month/{1}/year/{2}"
  }

  get(clientId: number, month: number, year: number): Observable<Bill> {
    return this.http.get<Bill>(StringUtils.format(this.url, clientId.toString(), month.toString(), year.toString()));
  }
}
