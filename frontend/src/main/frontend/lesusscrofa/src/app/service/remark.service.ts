import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of, throwError } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { DailyOrderRemarkView } from '../model-view/daily-order-remark-view';
import { Remark } from '../model/remark';
import { DateUtils } from '../utils/date-utils';
import { StringUtils } from '../utils/string-utils';

@Injectable({
  providedIn: 'root'
})
export class RemarkService {

  private clientRemarkUrl: string;

  private dayRemarkUrl: string

  private dailyOrderRamrkUrl: string;

  constructor(private http: HttpClient) { 
    this.clientRemarkUrl = environment.apiUrl + "/clients/{0}/remarks";
    this.dayRemarkUrl = environment.apiUrl + "/clients/{0}/remarks/{1}";
    this.dailyOrderRamrkUrl = environment.apiUrl + "/orders/{0}/remarks";
  }

  getDailyOrderRemark(day: Date): Observable<DailyOrderRemarkView[]> {
    return this.http.get<DailyOrderRemarkView[]>(StringUtils.format(this.dailyOrderRamrkUrl, DateUtils.formatToIsoDate(day)))
      .pipe(
        map(drs => drs.map(dr => DailyOrderRemarkView.fromJson(dr)))
      );
  }

  getClientRemark(clientId: number): Observable<Remark> {
    return this.http.get<Remark>(StringUtils.format(this.clientRemarkUrl, clientId.toString()))
      .pipe(
        map(r => Remark.fromJson(r))
      );
  }

  getDailyRemark(clientId: number, day: Date): Observable<Remark> {
    return this.http.get<Remark>(StringUtils.format(this.dayRemarkUrl, clientId.toString(), DateUtils.formatToIsoDate(day)))
      .pipe(
        map(r => Remark.fromJson(r))
      );
  }

  save(remark: Remark): Observable<Remark> {
    if(remark.day) {
      return this.http.put<Remark>(StringUtils.format(this.dayRemarkUrl, remark.clientId.toString(), DateUtils.formatToIsoDate(remark.day)), remark.toJson())
        .pipe(
          map(r => Remark.fromJson(r))
        );
    }
    else {
      return this.http.put<Remark>(StringUtils.format(this.dayRemarkUrl, remark.clientId.toString()), remark.toJson())
        .pipe(
          map(r => Remark.fromJson(r))
        );
    }
  } 

  delete(remark: Remark): Observable<Remark> {
    if(remark.day) {
      return this.http.delete<Remark>(StringUtils.format(this.dayRemarkUrl, remark.clientId.toString(), DateUtils.formatToIsoDate(remark.day)))
        .pipe(
          map(r => Remark.fromJson(r))
        );
    }
    else {
      return this.http.delete<Remark>(StringUtils.format(this.dayRemarkUrl, remark.clientId.toString()))
        .pipe(
          map(r => Remark.fromJson(r))
        );
    }
  } 
}
