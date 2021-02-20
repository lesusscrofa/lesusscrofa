import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatDatepickerInputEvent } from '@angular/material/datepicker';
import { Observable } from 'rxjs';
import { FoodOrderView } from 'src/app/model-view/food-order-view';
import { FoodOrderService } from 'src/app/service/food-order.service';

@Component({
  selector: 'app-food-order',
  templateUrl: './food-order.component.html',
  styleUrls: ['./food-order.component.css']
})
export class FoodOrderComponent implements OnInit {

  day: FormControl;

  foodsOrders: Observable<FoodOrderView[]>;

  constructor(private foodOrderService: FoodOrderService) { 
    this.day = new FormControl(new Date());
  }

  ngOnInit(): void {
    this.loadFoodsOrders(this.day.value);
  }

  dateChanged(dateInput: MatDatepickerInputEvent<Date>) {
    var day = dateInput.value;

    this.loadFoodsOrders(day);
  }

  private loadFoodsOrders(day: Date) {
    this.foodsOrders = this.foodOrderService.getFoodsOrders(day);
  }
}
