import { Component, EventEmitter, Inject, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Observable } from 'rxjs';
import { DeliveryMan } from 'src/app/model/delivery-man';
import { DeliveryZone } from 'src/app/model/delivery-zone';
import { DeliveryManService } from 'src/app/service/delivery-man.service';

@Component({
  selector: 'app-delivery-zone-edition',
  templateUrl: './delivery-zone-edition.component.html',
  styleUrls: ['./delivery-zone-edition.component.css']
})
export class DeliveryZoneEditionComponent implements OnInit {

  deliveryZoneEditionForm: FormGroup;

  deliveryMans: Observable<DeliveryMan[]>;

  constructor(public dialogRef: MatDialogRef<DeliveryZoneEditionComponent>,
              @Inject(MAT_DIALOG_DATA) private _deliveryZone: DeliveryZone,
              private fb: FormBuilder,
              private deliveryManService: DeliveryManService) { 
    
    this.deliveryZoneEditionForm = this.fb.group({
      id: [null],
      name: [null, Validators.required],
      deliveryManId: [null, Validators.required]
    });

    this.deliveryZone = _deliveryZone;
  }

  ngOnInit(): void {
    this.deliveryMans = this.deliveryManService.findAll();
  }

  cancel(): void {
    this.dialogRef.close(null);
  }

  save(): void {
    this.dialogRef.close(this.deliveryZone);
  }

  public get deliveryZone(): DeliveryZone {
    const deliveryManId = this.deliveryZoneEditionForm.value['deliveryManId'];

    return new DeliveryZone(
      this.deliveryZoneEditionForm.value['id'],
      this.deliveryZoneEditionForm.value['name'],
      deliveryManId,
      deliveryManId ? this.deliveryManService.get(deliveryManId) : null
    )
  }

  @Input()
  public set deliveryZone(deliveryZone: DeliveryZone) {
    if(deliveryZone) {
      this.deliveryZoneEditionForm.setValue({
        id: deliveryZone.id,
        name: deliveryZone.name,
        deliveryManId: deliveryZone.deliveryManId
      });
    }
    else {
      this.deliveryZoneEditionForm.reset();
    }
  }
}
