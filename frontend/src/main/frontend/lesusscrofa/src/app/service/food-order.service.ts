import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { FoodOrderClientFlatView } from '../model-view/food-order-client-flat-view';
import { FoodOrderClientView } from '../model-view/food-order-client-view';
import { FoodOrderView } from '../model-view/food-order-view';
import { DateUtils } from '../utils/date-utils';
import { StringUtils } from '../utils/string-utils';

@Injectable({
  providedIn: 'root'
})
export class FoodOrderService {

  private url: string;

  constructor(private http: HttpClient) { 
    this.url = environment.apiUrl + "/orders/{0}/foods";
  }

  getFoodsOrders(day: Date): Observable<FoodOrderView[]> {
    return this.http.get<FoodOrderView[]>(StringUtils.format(this.url, DateUtils.formatToIsoDate(day)))
      .pipe(
        map(foodsOrders => foodsOrders.map(foodOrder => FoodOrderView.fromJson(foodOrder)))
      );
  }

  getFoodsOrdersByClients(day: Date): Observable<FoodOrderClientView[]> {
    return this.http.get<FoodOrderClientView[]>(StringUtils.format(this.url + '/byClient', DateUtils.formatToIsoDate(day)))
      .pipe(
        map(foodsOrdersClients => foodsOrdersClients.map(foodOrderClient => FoodOrderClientView.fromJson(foodOrderClient)))
      );
  }

  getFoodsOrdersByClientsFlat(day: Date, deliveryManId: number): Observable<FoodOrderClientFlatView[]> {
    var requestUrl = StringUtils.format(this.url + '/byClient/flat', DateUtils.formatToIsoDate(day));
    requestUrl = deliveryManId ? requestUrl + '?deliveryManId=' + deliveryManId : requestUrl;

    return this.http.get<FoodOrderClientFlatView[]>(requestUrl)
      .pipe(
        map(foodsOrdersClients => foodsOrdersClients.map(foodOrderClient => FoodOrderClientFlatView.fromJson(foodOrderClient)))
      );
  }
}
