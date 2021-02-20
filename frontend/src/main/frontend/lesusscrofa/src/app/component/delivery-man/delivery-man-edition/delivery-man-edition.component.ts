import { Component, Inject, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { DeliveryMan } from 'src/app/model/delivery-man';

@Component({
  selector: 'app-delivery-man-edition',
  templateUrl: './delivery-man-edition.component.html',
  styleUrls: ['./delivery-man-edition.component.css']
})
export class DeliveryManEditionComponent implements OnInit {

  deliveryManEditionForm: FormGroup;

  constructor(public dialogRef: MatDialogRef<DeliveryManEditionComponent>,
    @Inject(MAT_DIALOG_DATA) private _deliveryMan: DeliveryMan,
      private fb: FormBuilder) { 
    
    this.deliveryManEditionForm = this.fb.group({
      id: [null],
      firstName: [null, Validators.required],
      lastName: [null, Validators.required]
    });

    this.deliveryMan = _deliveryMan;
  }

  ngOnInit(): void {

  }

  cancel(): void {
    this.dialogRef.close(null);
  }

  save(): void {
    this.dialogRef.close(this.deliveryMan);
  }

  public get deliveryMan(): DeliveryMan {
    return new DeliveryMan(
      this.deliveryManEditionForm.value['id'],
      this.deliveryManEditionForm.value['firstName'],
      this.deliveryManEditionForm.value['lastName']
    )
  }

  @Input()
  public set deliveryMan(deliveryMan: DeliveryMan) {
    if(deliveryMan) {
      this.deliveryManEditionForm.setValue({
        id: deliveryMan.id,
        firstName: deliveryMan.firstName,
        lastName: deliveryMan.lastName
      });
    }
    else {
      this.deliveryManEditionForm.reset();
    }
  }
}
