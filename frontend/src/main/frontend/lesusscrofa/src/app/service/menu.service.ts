import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, combineLatest, of, forkJoin } from 'rxjs';
import { filter, flatMap, map, reduce } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { Food } from '../model/food';
import { Menu } from '../model/menu';
import { ServiceType } from '../model/service-type';
import { DateUtils } from '../utils/date-utils';
import { DateService } from './date.service';
import { FoodService } from './food.service';

@Injectable({
  providedIn: 'root'
})
export class MenuService {

  private url: string;

  constructor(
    private http: HttpClient,
    private foodService: FoodService,
    private dateService: DateService
    ) { 
      this.url = environment.apiUrl + "/menus";
    }

  save(menus: Menu[]): Observable<Menu[]> {
    return forkJoin(menus.map(m => this.saveMenu(m)));
  }

  private saveMenu(menu: Menu): Observable<Menu> {
    if(menu.isMenuInitialized()) {
      if(menu.isMenuCreated()) {
        return this.updateMenu(menu);
      }
      else {
        return this.createMenu(menu);
      }
    }

    return of(menu)
  }

  private createMenu(menu: Menu): Observable<Menu>  {
    if(menu.isAlternativeMenu) {
      return this.foodService.create(menu.dish)
        .pipe(
          map(dish => new Menu(dish.start, dish.end, null, dish, null, null, true))
        );
    }
    else {
      return this.create(menu);
    }
  }

  private updateMenu(menu: Menu): Observable<Menu> {
    if(menu.isAlternativeMenu) {
      return this.foodService.update(menu.dish)
        .pipe(
          map(dish => new Menu(dish.start, dish.end, null, dish, null, null, true))
        );
    }
    else {
      return this.update(menu);
    }
  }

   create(menu: Menu): Observable<Menu> {
    return this.http.post<Menu>(this.url, menu.toJson())
      .pipe(
        map(m => Menu.fromJson(m))
      );
  }

  update(menu: Menu): Observable<Menu> {
    return this.http
      .put<Menu>(this.url + '/' + DateUtils.formatToIsoDate(menu.start), menu.toJson())
        .pipe(
          map(m => Menu.fromJson(m))
        );;
  }

  getMenusByDates(start: Date, end: Date, includeAlternative: boolean): Observable<Menu[]> {
    var url = this.url + '?start='+DateUtils.formatToIsoDate(start)
      +'&end='+DateUtils.formatToIsoDate(end)
      +'&includeAlternative='+includeAlternative;
    
    return this.http.get<any>(url)
      .pipe(
        map(menus => menus.map((m: Object) => Menu.fromJson(m)))
      );
  }

  getMenusByDatesAndCreateMissingsForEdition(start: Date, end: Date): Observable<Menu[]> {
    return this.getMenusByDates(start, end, false).pipe(
      flatMap(
        menus => this.createEmptyMenusByDates(start, end).pipe(
          reduce((acc: Menu[], dummyMenu: Menu) => this.addMenuIfNotAlreadyInArray(dummyMenu, acc), menus)
        )
      )
    )
  }

  getMenusByDatesAndCreateMissingsAndAlternativeForEdition(start: Date, end: Date): Observable<Menu[]> {
    return forkJoin([this.getMenusByDatesAndCreateMissingsForEdition(start, end), this.getAlternativeDishesByDatesForEdition(start, end)])
      .pipe(
        map(([m1, m2]) => [...m1,...m2]
          ),
        flatMap(
          menus => this.createEmptyAlternativeMenusByDates(start, end).pipe(
            reduce((acc: Menu[], dummyMenu: Menu) => this.addMenuIfNotAlreadyInArray(dummyMenu, acc), menus)
          )
      )
    )
  }

  private getAlternativeDishesByDatesForEdition(start: Date, end: Date): Observable<Menu[]> {
    return this.foodService.getAlternativeDishesByDates(start, end)
      .pipe(
        map(foods => foods.map(f => new Menu(f.start, f.end, null, f, null, null, true)))
        );
  }

  private createEmptyAlternativeMenusByDates(start: Date, end: Date): Observable<Menu> {
    return this.dateService.getMondayBetween(start, end).pipe(
      map(day => this.createEmptyMenuForDay(day, DateUtils.addDays(day, 5), true))
    )
  }

  private createEmptyMenusByDates(start: Date, end: Date): Observable<Menu> {
    return this.dateService.getDatesBetween(start, end).pipe(
      filter(day => day.getDay() != 0),
      map(day => this.createEmptyMenuForDay(day, day, false))
    )
  }

  private createEmptyMenuForDay(start: Date, end: Date, alternativeMenu: boolean): Menu {
    return new Menu(
      start, 
      end,
      alternativeMenu ? null : new Food(null, start, end, '', ServiceType.Soup, null, null, null), 
      new Food(null, start, end, '', alternativeMenu ? ServiceType.AlternativeDish : ServiceType.Dish, null, null, null), 
      null,
      alternativeMenu ? null : new Food(null, start, end, '', ServiceType.Dessert, null, null, null),
      alternativeMenu);
  }

  private addMenuIfNotAlreadyInArray(menu: Menu, menus: Menu[]): Menu[] {
    if(!this.doesMenuExistForDayInArray(menu, menus)) {
      menus.push(menu);
    }

    return menus;
  }

  private doesMenuExistForDayInArray(menu: Menu, menus: Menu[]): boolean {
    var filter = menus.filter(m => 
      DateUtils.isDatesSameDay(m.start, menu.start)
      && DateUtils.isDatesSameDay(m.end, menu.end))
      
      return filter.length > 0
  }

}
