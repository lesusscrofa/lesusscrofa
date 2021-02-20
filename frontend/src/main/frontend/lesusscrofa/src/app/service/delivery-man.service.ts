import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { DeliveryMan } from '../model/delivery-man';

@Injectable({
  providedIn: 'root'
})
export class DeliveryManService {

  private url: string;

  constructor(private http: HttpClient) { 
    this.url = environment.apiUrl + "/deliveryMans";
  }

  findAll(): Observable<DeliveryMan[]> {
    return this.http.get<DeliveryMan[]>(this.url)
      .pipe(
        map(dms => dms.map(dm => DeliveryMan.fromJson(dm)))
      );
  }

  get(id: number): Observable<DeliveryMan> {
    return this.http.get<DeliveryMan[]>(this.url + "/" + id)
      .pipe(
        map(dm => DeliveryMan.fromJson(dm))
      );
  }

  create(deliveryMan: DeliveryMan): Observable<DeliveryMan> {
    return this.http.post<DeliveryMan>(this.url, deliveryMan.toJson())
      .pipe(
        map(dm => DeliveryMan.fromJson(dm))
      );
  }

  update(deliveryMan: DeliveryMan): Observable<DeliveryMan> {
    return this.http.put<DeliveryMan>(this.url + "/" + deliveryMan.id, deliveryMan.toJson())
      .pipe(
        map(dm => DeliveryMan.fromJson(dm))
      );
  }

  delete(deliveryMan: DeliveryMan): Observable<any> {
    return this.http.delete<DeliveryMan>(this.url + "/" + deliveryMan.id);
  }
}
