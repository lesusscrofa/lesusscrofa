import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Order } from 'src/app/model/order';
import { FoodWithQuantity } from '../order-food-field/order-food-field.component';

@Component({
  selector: 'app-food-order-quantity-edition',
  templateUrl: './food-order-quantity-edition.component.html',
  styleUrls: ['./food-order-quantity-edition.component.css']
})
export class FoodOrderQuantityEditionComponent implements OnInit {

  orderQuantityEditionForm: FormGroup;
  
  constructor(public dialogRef: MatDialogRef<FoodOrderQuantityEditionComponent>,
            @Inject(MAT_DIALOG_DATA) private _foodWithQuantity: FoodWithQuantity,
            private fb: FormBuilder) { 
      this.orderQuantityEditionForm = this.fb.group({
        quantity: [null, Validators.required]
      });

      this.foodWithQuantity = _foodWithQuantity;
    }

  ngOnInit(): void {
  }

  cancel(): void {
    this.dialogRef.close(null);
  }

  save(): void {
    var food = this.foodWithQuantity;

    food.quantity = this.orderQuantityEditionForm.value['quantity'];

    this.dialogRef.close(food);
  }

  public get foodWithQuantity(): FoodWithQuantity {
    return this._foodWithQuantity;
  }

  public set foodWithQuantity(foodWithQuantity: FoodWithQuantity) {
    this._foodWithQuantity = foodWithQuantity;

    if(foodWithQuantity) {
      this.orderQuantityEditionForm.setValue({
        quantity: foodWithQuantity.quantity
      })
    }
    else {
      this.orderQuantityEditionForm.reset();
    }
  }
}
