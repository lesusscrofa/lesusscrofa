import { Component, Inject, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Food } from 'src/app/model/food';
import { ServiceType } from 'src/app/model/service-type';

@Component({
  selector: 'app-food-edition',
  templateUrl: './food-edition.component.html',
  styleUrls: ['./food-edition.component.css']
})
export class FoodEditionComponent implements OnInit {

  vatValues = [0, 6, 12, 21];

  unitValues = ['pc.', 'l']

  foodEditionForm: FormGroup;

  constructor(public dialogRef: MatDialogRef<FoodEditionComponent>,
    @Inject(MAT_DIALOG_DATA) private _food: Food,
      private fb: FormBuilder) { 
    
    this.foodEditionForm = this.fb.group({
      id: [null],
      name: [null, Validators.required],
      start: [null, Validators.required],
      end: [null],
      service: [ServiceType.Other],
      price: [null, [Validators.required, Validators.min(0)]],
      vat: [6, [Validators.required, Validators.min(0), Validators.max(100)]],
      unit: [null, Validators.maxLength(10)]
    });

    this.food = _food;
  }

  ngOnInit(): void {

  }

  cancel(): void {
    this.dialogRef.close(null);
  }

  save(): void {
    this.dialogRef.close(this.food);
  }

  public get food(): Food {
    return new Food(
      this.foodEditionForm.value['id'],
      this.foodEditionForm.value['start'],
      this.foodEditionForm.value['end'],
      this.foodEditionForm.value['name'],
      this.foodEditionForm.value['service'],
      this.foodEditionForm.value['price'],
      this.foodEditionForm.value['vat'],
      this.foodEditionForm.value['unit']
    )
  }

  @Input()
  public set food(food: Food) {
    if(food) {
      this.foodEditionForm.setValue({
        id: food.id,
        name: food.name,
        start: food.start,
        end: food.end,
        price: food.price,
        service: food.service,
        vat: food.vat,
        unit: food.unit
      });
    }
    else {
      this.foodEditionForm.reset();
      this.foodEditionForm.patchValue({
        service: ServiceType.Other,
        vat: 6
      });
    }
  }
}
