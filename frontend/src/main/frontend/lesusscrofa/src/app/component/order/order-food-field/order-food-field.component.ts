import { Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';
import { Observable } from 'rxjs';
import { Food } from 'src/app/model/food';
import { FoodService } from 'src/app/service/food.service';
import { debounceTime, distinctUntilChanged, filter, map, startWith, switchMap } from 'rxjs/operators';
import { MatDialog } from '@angular/material/dialog';
import { FoodOrderQuantityEditionComponent } from '../food-order-quantity-edition/food-order-quantity-edition.component';
import { Order } from 'src/app/model/order';

export interface FoodWithQuantity {
  foodId: number,
  quantity: number
}

@Component({
  selector: 'app-order-food-field',
  templateUrl: './order-food-field.component.html',
  styleUrls: ['./order-food-field.component.css']
})
export class OrderFoodFieldComponent implements OnInit {

  @ViewChild('foodInput') foodInput: ElementRef<HTMLInputElement>;

  foodControl: FormControl;

  @Input()
  day: Date;

  @Input()
  set otherFoodsOrders(otherFoodsOrders: Order[]) {
    this._otherFoodsOrders= otherFoodsOrders;
  }

  get otherFoodsOrders(): Order[] {
    return this._otherFoodsOrders;
  }

  _otherFoodsOrders: Order[];

  @Output()
  otherFoodAdded: EventEmitter<FoodWithQuantity>;

  @Output()
  otherFoodModified: EventEmitter<Order>;

  @Output()
  otherFoodRemoved: EventEmitter<Order>;

  filteredFoods: Observable<Food[]>;

  constructor(private foodService: FoodService,
              public dialog: MatDialog) { 
    this.foodControl = new FormControl();
    this.otherFoodAdded = new EventEmitter();
    this.otherFoodModified = new EventEmitter();
    this.otherFoodRemoved = new EventEmitter();
  }

  ngOnInit(): void {
    this.filteredFoods = this.foodControl.valueChanges.pipe(
      startWith(null),
      debounceTime(500),
      distinctUntilChanged(),
      map(val => typeof val === 'string' ? val : ""),
      switchMap((foodName: string | null) => this.foodService.getAllOtherFoodsContaining(foodName, this.day)),
      map(foods => Food.substractFoodsFromArray(foods, this.otherFoodsOrders.map(o => o.otherId))));
  }

  newOrder(event: MatAutocompleteSelectedEvent): void {
    const food = event.option.value as Food;
    var foodWithQuantity = {
      foodId: food.id,
      quantity: 1
    }

    this.dialog.open(FoodOrderQuantityEditionComponent, 
                      {
                        data: foodWithQuantity
                      }).afterClosed().subscribe(fq => {
      this.otherFoodAdded.emit(fq);
      this.foodInput.nativeElement.value = '';
  
      this.foodControl.setValue(null);
    });
  }

  modifyOrder(order: Order) {
    const foodWithQuantity = {
      foodId: order.dishId,
      quantity: order.quantity
    }

    this.dialog.open(FoodOrderQuantityEditionComponent, 
                    {
                      data: foodWithQuantity
                    }).afterClosed().subscribe(fq => {
        order.quantity = fq.quantity;
        this.otherFoodModified.emit(order);
      });
  }

  removeOrder(order: Order) {
    this.otherFoodRemoved.emit(order);
  }

  retrieveFood(foodId: number): Observable<Food> {
    return this.foodService.get(foodId);
  }
}
