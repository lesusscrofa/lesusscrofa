import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatDatepickerInputEvent } from '@angular/material/datepicker';
import { Observable } from 'rxjs';
import { OrderedFoodView } from 'src/app/model-view/ordered-food-view';
import { OrderedFoodService } from 'src/app/service/ordered-food.service';

@Component({
  selector: 'app-ordered-food',
  templateUrl: './ordered-food.component.html',
  styleUrls: ['./ordered-food.component.css']
})
export class OrderedFoodComponent implements OnInit {

  day: FormControl;

  orderedFoods: Observable<OrderedFoodView[]>;

  constructor(private foodOrderService: OrderedFoodService) { 
    this.day = new FormControl(new Date());
  }

  ngOnInit(): void {
    this.loadOrderedFoods(this.day.value);
  }

  dateChanged(dateInput: MatDatepickerInputEvent<Date>) {
    var day = dateInput.value;

    this.loadOrderedFoods(day);
  }

  private loadOrderedFoods(day: Date) {
    this.orderedFoods = this.foodOrderService.getOrderedFood(day);
  }
}
