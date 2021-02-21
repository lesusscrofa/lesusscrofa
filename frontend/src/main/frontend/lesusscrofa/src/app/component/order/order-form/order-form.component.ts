import { Component, Input, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { combineLatest, Observable, ReplaySubject } from 'rxjs';
import { map } from 'rxjs/operators';
import { Client } from 'src/app/model/client';
import { Formula } from 'src/app/model/formula';
import { MenuOrder } from 'src/app/model/menu-order';
import { Order } from 'src/app/model/order';
import { Remark } from 'src/app/model/remark';
import { DateService } from 'src/app/service/date.service';
import { FoodService } from 'src/app/service/food.service';
import { MessageService, MessageType } from 'src/app/service/message.service';
import { OrderService } from 'src/app/service/order.service';
import { RemarkService } from 'src/app/service/remark.service';
import { EncodeRemarkComponent } from '../../remark/encode-remark/encode-remark.component';
import { FoodWithQuantity } from '../order-food-field/order-food-field.component';


@Component({
  selector: 'order-form',
  templateUrl: './order-form.component.html',
  styleUrls: ['./order-form.component.css']
})
export class OrderFormComponent implements OnInit {

  savingOrders = false;

  private monthSubject: ReplaySubject<Date> = new ReplaySubject();
  private month$: Observable<Date> = this.monthSubject.asObservable();

  private clientSubject: ReplaySubject<Client> = new ReplaySubject();
  private client$: Observable<Client> = this.clientSubject.asObservable();

  menusForm: FormGroup;

  displayedColumns = ['day','soup', 'dish', 'alternativeDish', 'dessert', 'others', 'delivery'];

  dataSource: MatTableDataSource<FormGroup>;

  constructor(private dateService: DateService,
              private orderService: OrderService,
              private foodService: FoodService,
              private remarkService: RemarkService,
              private messageService: MessageService,
              private fb: FormBuilder,
              private dialog: MatDialog) { 
    this.menusForm = this.fb.group({
      items: this.fb.array([])
    });

    this.dataSource = new MatTableDataSource();
  }

  ngOnInit(): void {
    combineLatest([this.month$, this.client$])
      .subscribe(([month, client]) => this.reloadOrders(client, month));
  }

  private reloadOrders(client: Client, month: Date) {
    if(!client) {
      return;
    }

    this.orderService.getMenusOrders(
      client,
      this.dateService.getLastMondayForMonth(month.getFullYear(), month.getMonth()),
      this.dateService.getLastSundayForMonth(month.getFullYear(), month.getMonth())
      ).pipe(
        map(menusFoods => menusFoods.sort(MenuOrder.compareToByDate))
    ).subscribe(menusFoods => this.initMenuForm(menusFoods));
  }

  monthSelected(month: Date) {
    this.monthSubject.next(month);
  }

  private initMenuForm(menuOrders: MenuOrder[]) {
    var ordersArray = menuOrders.map(menuOrder => this.fb.group({
      day: [menuOrder.day],
      menu: [menuOrder.menu],
      client: [menuOrder.client],
      delivery: [menuOrder.delivery],
      soup: [menuOrder.soup],
      soupQuantity: [menuOrder.soupQuantity],
      dish: [menuOrder.dish],
      dishQuantity: [menuOrder.dishQuantity],
      alternativeDish: [menuOrder.alternativeDish],
      alternativeDishQuantity: [menuOrder.alternativeDishQuantity],
      dessert: [menuOrder.dessert],
      dessertQuantity: [menuOrder.dessertQuantity],
      others: [this.orderService.getOrdersByFormula(menuOrder.client.id, menuOrder.day, Formula.OTHER)],
      orders: [menuOrder.orders],
      remark: [this.remarkService.getDailyRemark(menuOrder.client.id, menuOrder.day)]
    }));
    
    this.dataSource.data = ordersArray;

    this.menuItems.clear();
    ordersArray.forEach(orderArray => this.menuItems.push(orderArray));
  }

  placeOrders(): void {
    this.savingOrders = true;
    var menusOrders = this.toMenusOrders(this.menuItems.value);
    
    this.orderService.saveMenusOrders(menusOrders)
      .pipe(
        map(menusFoods => menusFoods.sort(MenuOrder.compareToByDate))
      )
      .subscribe(menusOrders => {
        this.initMenuForm(menusOrders);
        this.savingOrders = false;
        this.messageService.showMessage("Les commandes ont bien été sauvées", MessageType.INFORMATION);
      });
  }

  private toMenusOrders(menusFormArray: FormGroup[]): MenuOrder[] {
    return menusFormArray.map(m => this.toMenuOrder(m));
  }

  private toMenuOrder(menuFormGroup: FormGroup): MenuOrder {
    return new MenuOrder(
      menuFormGroup['day'],
      menuFormGroup['delivery'],
      menuFormGroup['menu'],
      menuFormGroup['client'],
      menuFormGroup['soup'],
      menuFormGroup['soupQuantity'],
      menuFormGroup['dish'],
      menuFormGroup['dishQuantity'],
      menuFormGroup['alternativeDish'],
      menuFormGroup['alternativeDishQuantity'],
      menuFormGroup['dessert'],
      menuFormGroup['dessertQuantity'],
      menuFormGroup['orders'],
      this.foodService
    );
  }

  otherFoodAdded(client: Client, foodWithQuantity: FoodWithQuantity, menuFormGroup: FormGroup) {
    const order = new Order(null, client.id, menuFormGroup.value['delivery'], menuFormGroup.value['day'],
                            null, null, null, null, null, null,
                            foodWithQuantity.foodId, this.foodService.get(foodWithQuantity.foodId),
                            Formula.OTHER, foodWithQuantity.quantity, 0, null);

    this.orderService.createOrder(order)
      .subscribe(o => this.reloadOtherFoods(menuFormGroup));
  }

  otherFoodModified(order: Order, menuFormGroup: FormGroup) {
    this.orderService.updateOrder(order)
      .subscribe(o => this.reloadOtherFoods(menuFormGroup));
  }

  otherFoodRemoved(order: Order, menuFormGroup: FormGroup) {
    this.orderService.deleteOrder(order)
      .subscribe(o => this.reloadOtherFoods(menuFormGroup));
  }

  editRemark(remark: Remark) {
    this.dialog.open(EncodeRemarkComponent, {data: remark})
      .afterClosed()
      .subscribe(r => this.saveRemark(remark));    
  }

  private saveRemark(remark: Remark) {
    if(remark.id != null && remark.message == '') {
      this.remarkService.delete(remark).subscribe(() => {
        remark.id = null;
      });
    }
    else if(remark.message !== '') {
      this.remarkService.save(Remark.fromJson(remark)).subscribe(r => {
        remark.id = r.id;
        remark.clientId = r.clientId;
        remark.day = r.day;
        remark.message = r.message;
      })
    }
  }

  private reloadOtherFoods(menuFormGroup: FormGroup) {
    menuFormGroup.patchValue({
      others: this.orderService.getOrdersByFormula(menuFormGroup.value['client'].id, menuFormGroup.value['day'], Formula.OTHER)
    })
  }

  get menuItems(): FormArray {
    return this.menusForm.get('items') as FormArray;
  }

  @Input()
  set client(client: Client) {
    this.clientSubject.next(client);
  }
}
