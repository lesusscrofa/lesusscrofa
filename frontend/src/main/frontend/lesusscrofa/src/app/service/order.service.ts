import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { EMPTY, forkJoin, Observable, of, pipe, throwError } from 'rxjs';
import { catchError, defaultIfEmpty, map, mergeMap, tap } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { Client } from '../model/client';
import { Formula } from '../model/formula';
import { Menu } from '../model/menu';
import { MenuOrder } from '../model/menu-order';
import { Order } from '../model/order';
import { DateUtils } from '../utils/date-utils';
import { FoodService } from './food.service';
import { MenuService } from './menu.service';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  private url: string;
  private apiUrl: string;

  constructor(private http: HttpClient,
    private menuService: MenuService,
    private foodService: FoodService) { 
      this.url = environment.apiUrl + "/orders";
      this.apiUrl = environment.apiUrl;
    }

  saveOrders(orders: Order[]): Observable<Order[]> {
    return forkJoin(orders.map(order => this.saveOrder(order)));
  }

  saveOrder(order: Order): Observable<Order> {
    if(order.id) {
      if(order.quantity > 0) {
        return this.updateOrder(order);
      }
      else {
        this.deleteOrder(order).subscribe();
        return of(order);
      }
    }
    else {
      return this.createOrder(order);
    }
  }

  saveMenusOrders(menusOrders: MenuOrder[]): Observable<MenuOrder[]> {
    return forkJoin(menusOrders.map(menuOrder => this.saveMenuOrder(menuOrder)));
  }

  saveMenuOrder(menuOrder: MenuOrder): Observable<MenuOrder> {
    var orders = menuOrder.toOrders();
    
    if(orders.length == 0) {
      return of(menuOrder);
    }
    else {
      return this.saveOrders(orders)
      .pipe(
        map(orders => MenuOrder.fromOrders(menuOrder.client, menuOrder.menu, orders, this.foodService))
      );
    }
  }

  createOrder(order: Order): Observable<Order> {
    return this.http.post<Order>(this.url, order.toJson())
      .pipe(
        map(order => Order.fromJson(order, this.foodService))
      );
  }

  updateOrder(order: Order): Observable<Order> {
    return this.http.put<Order>(this.url + '/' + order.id, order.toJson())
      .pipe(
        map(order => Order.fromJson(order, this.foodService))
      );
  }

  deleteOrder(order: Order): Observable<void> {
    return this.http.delete<void>(this.url + '/' + order.id);
  }

  getOrders(clientId: number, day: Date): Observable<Order[]> {
    var url = this.apiUrl  + '/clients/' + clientId + '/orders?day=' + DateUtils.formatToIsoDate(day);
    
    return this.http.get<Order[]>(url)
      .pipe(
        map(orders => orders.map(o => Order.fromJson(o, this.foodService))),
        catchError(err => {
          if(err.status === 404) {
            return of(undefined);
         } else {
            return throwError(err);
         }
        })
      );
  }

  getOrdersByFormula(clientId: number, day: Date, formula: Formula): Observable<Order[]> {
    var url = this.apiUrl  + '/clients/' + clientId + '/orders?day=' + DateUtils.formatToIsoDate(day)
            + '&formula='+formula;
    
    return this.http.get<Order[]>(url)
      .pipe(
        map(orders => orders.map(o => Order.fromJson(o, this.foodService))),
        catchError(err => {
          if(err.status === 404) {
            return of(undefined);
         } else {
            return throwError(err);
         }
        })
      );
  }

  get(href: string): Observable<Order> {
    return this.http.get<Order>(href);
  }

  getMenusOrders(client: Client, start: Date, end: Date): Observable<MenuOrder[]> {
    return this.menuService.getMenusByDates(start, end, true)
      .pipe(
        mergeMap(menus => this.getOrdersMenusForMenus(client, menus)),
        map(menusFoods => [].concat(...menusFoods)),
        defaultIfEmpty([])
      );
  }

  private getOrdersMenusForMenus(client: Client, menus: Menu[]): Observable<MenuOrder[][]> {
    return forkJoin(
      menus.map(menu => this.getOrdersMenusForMenu(client, menu))  
    );
  }

  private getOrdersMenusForMenu(client: Client, menu: Menu): Observable<MenuOrder[]> {
    return this.getOrders(client.id, menu.start)
      .pipe(map(orders => this.createOrderMenu(client, menu, orders)))
  }

  private createOrderMenu(client: Client, menu: Menu, orders: Order[]): MenuOrder[] {
    return [MenuOrder.fromOrders(client, menu, orders, this.foodService)];
  }
}
