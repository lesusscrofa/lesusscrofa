import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { OrderedFoodView } from '../model-view/ordered-food-view';
import { DateUtils } from '../utils/date-utils';
import { StringUtils } from '../utils/string-utils';

@Injectable({
  providedIn: 'root'
})
export class OrderedFoodService {

  private url: string;

  constructor(private http: HttpClient) { 
    this.url = environment.apiUrl + "/orders/{0}/foods";
  }

  getOrderedFood(day: Date): Observable<OrderedFoodView[]> {
    return this.http.get<OrderedFoodView[]>(StringUtils.format(this.url, DateUtils.formatToIsoDate(day)))
      .pipe(
        map(foodsOrders => foodsOrders.map(foodOrder => OrderedFoodView.fromJson(foodOrder)))
      );
  }
}
