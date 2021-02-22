import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { Food } from '../model/food';
import { ServiceType } from '../model/service-type';
import { DateUtils } from '../utils/date-utils';

@Injectable({
  providedIn: 'root'
})
export class FoodService {

  private url: string;

  constructor(private http: HttpClient) { 
    this.url = environment.apiUrl + "/foods";
  }

  get(id: number): Observable<Food> {
    return this.http.get<Food>(this.url + '/' + id)
      .pipe(
        map(f => Food.fromJson(f))
      );
  }

  getAlternativeDishesByDates(start: Date, end: Date): Observable<Food[]> {
    return this.getFoodsByDates(start, end, ServiceType.AlternativeDish);
  }

  getFoodsByDates(start: Date, end: Date, service: ServiceType): Observable<Food[]> {
    var url = this.url + '?start='+DateUtils.formatToIsoDate(start)
      +'&end='+DateUtils.formatToIsoDate(end)
      +'&service='+service.valueOf();
    
    return this.http.get<any>(url)
      .pipe(
        map(foods => foods.map((f: Object) => Food.fromJson(f)))
      );
  }

  getAllOtherFoodsContaining(partialName: string, day: Date): Observable<Food[]> {
    var url = this.url + '?start='+DateUtils.formatToIsoDate(day)
      +'&end='+DateUtils.formatToIsoDate(day)
      + '&service='+ServiceType.Other
      + '&partialName='+partialName;
    
    return this.http.get<any>(url)
      .pipe(
        map(foods => foods.map((f: Object) => Food.fromJson(f)))
      );
  }

  getAllOtherFoods(): Observable<Food[]> {
    var url = this.url + '?service='+ServiceType.Other;
    
    return this.http.get<any>(url)
      .pipe(
        map(foods => foods.map((f: Object) => Food.fromJson(f)))
      );
  }

  create(food: Food): Observable<Food> {
    return this.http.post<Food>(this.url, food.toJson())
      .pipe(
        map(f => Food.fromJson(f))
      );
  }

  update(food: Food): Observable<Food> {
    return this.http.put<Food>(this.url + "/" + food.id, food.toJson())
      .pipe(
        map(f => Food.fromJson(f))
      );
  }

  delete(food: Food): Observable<any> {
    return this.http.delete<Food>(this.url + "/" + food.id);
  }
}
