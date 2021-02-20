import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { EMPTY, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { DeliveryMan } from '../model/delivery-man';
import { DeliveryZone } from '../model/delivery-zone';
import { DeliveryManService } from './delivery-man.service';

@Injectable({
  providedIn: 'root'
})
export class DeliveryZoneService {

  private url: string;

  constructor(private http: HttpClient,
            private deliveryManService: DeliveryManService) { 
    this.url = environment.apiUrl + "/deliveryZones";
  }

  findAll(): Observable<DeliveryZone[]> {
    return this.http.get<DeliveryZone[]>(this.url)
      .pipe(
        map(dms => dms.map(dm => this.createDeliveryZoneFromJson(dm)))
      );
  }

  get(id: number): Observable<DeliveryZone> {
    return this.http.get<DeliveryZone>(this.url + "/" + id)
      .pipe(
        map(dm => this.createDeliveryZoneFromJson(dm))
      );
  }

  create(deliveryZone: DeliveryZone): Observable<DeliveryZone> {
    return this.http.post<DeliveryZone>(this.url, deliveryZone.toJson())
      .pipe(
        map(dm => this.createDeliveryZoneFromJson(dm))
      );
  }

  update(deliveryZone: DeliveryZone): Observable<DeliveryZone> {
    return this.http.put<DeliveryZone>(this.url + "/" + deliveryZone.id, deliveryZone.toJson())
      .pipe(
        map(dm => this.createDeliveryZoneFromJson(dm))
      );
  }

  delete(deliveryZone: DeliveryZone): Observable<any> {
    return this.http.delete<DeliveryZone>(this.url + "/" + deliveryZone.id);
  }

  private createDeliveryZoneFromJson(dm: any): DeliveryZone {
    return DeliveryZone.fromJson(dm, dm.deliveryManId ? this.deliveryManService.get(dm.deliveryManId) : EMPTY);
  }
}
