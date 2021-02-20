import { Component, EventEmitter, Inject, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { FormulaPrice } from 'src/app/model/formula-price';
import { Formula } from 'src/app/model/formula';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Client } from 'src/app/model/client';

@Component({
  selector: 'app-formula-price-edition',
  templateUrl: './formula-price-edition.component.html',
  styleUrls: ['./formula-price-edition.component.css']
})
export class FormulaPriceEditionComponent implements OnInit {

  vatValues = [0, 6, 12, 21];

  priceEditionForm: FormGroup;

  formulas = Object.values(Formula);

  constructor(public dialogRef: MatDialogRef<FormulaPriceEditionComponent>,
    @Inject(MAT_DIALOG_DATA) private _price: FormulaPrice,
      private fb: FormBuilder) { 
    
    this.priceEditionForm = this.fb.group({
      id: [null],
      start: [null, Validators.required],
      formula: [null, Validators.required],
      price: [null, [Validators.required, Validators.min(0)]],
      vat: [null, [Validators.required, Validators.min(0), Validators.max(100)]]
    });

    this.price = _price;
  }

  ngOnInit(): void {

  }

  cancel(): void {
    this.dialogRef.close(null);
  }

  save(): void {
    this.dialogRef.close(this.price);
  }

  public get price(): FormulaPrice {
    return new FormulaPrice(
      this.priceEditionForm.value['id'],
      this.priceEditionForm.value['start'],
      this.priceEditionForm.value['formula'],
      this.priceEditionForm.value['price'],
      this.priceEditionForm.value['vat']
    )
  }

  @Input()
  public set price(price: FormulaPrice) {
    if(price) {
      this.priceEditionForm.setValue({
        id: price.id,
        start: price.start,
        formula: price.formula,
        price: price.price,
        vat: price.vat
      });
    }
    else {
      this.priceEditionForm.reset();
    }
  }
}
