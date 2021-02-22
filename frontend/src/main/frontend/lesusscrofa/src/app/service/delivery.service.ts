import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { DeliveryView } from '../model-view/delivery-view';
import { DateUtils } from '../utils/date-utils';
import { StringUtils } from '../utils/string-utils';

@Injectable({
  providedIn: 'root'
})
export class DeliveryService {

  private url: string;

  constructor(private http: HttpClient) { 
    this.url = environment.apiUrl + "/deliveries/{0}";
  }

  getDelivery(day: Date, deliveryManId: number): Observable<DeliveryView[]> {
    var requestUrl = StringUtils.format(this.url, DateUtils.formatToIsoDate(day));
    requestUrl = deliveryManId ? requestUrl + '?deliveryManId=' + deliveryManId : requestUrl;

    return this.http.get<DeliveryView[]>(requestUrl)
      .pipe(
        map(foodsOrdersClients => foodsOrdersClients.map(foodOrderClient => DeliveryView.fromJson(foodOrderClient)))
      );
  }
}
