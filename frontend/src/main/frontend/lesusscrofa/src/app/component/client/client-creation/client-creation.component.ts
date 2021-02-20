import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { Observable } from 'rxjs';
import { Client } from 'src/app/model/client';
import { DeliveryZone } from 'src/app/model/delivery-zone';
import { DeliveryZoneService } from 'src/app/service/delivery-zone.service';

@Component({
  selector: 'client-creation',
  templateUrl: './client-creation.component.html',
  styleUrls: ['./client-creation.component.css']
})
export class ClientCreationComponent implements OnInit {

  deliveryZones: Observable<DeliveryZone[]>;

  clientForm: FormGroup;

  constructor(public dialogRef: MatDialogRef<Client>,
    private deliveryZoneService: DeliveryZoneService,
    private fb: FormBuilder) { 
    this.clientForm = this.fb.group ({
      id: [''],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      deliveryZoneId: ['', Validators.required],
      deliveryPreferenceTakeAway: [false]
    })
  }

  ngOnInit(): void {
    this.deliveryZones = this.deliveryZoneService.findAll();
  }

  cancel(): void {
    this.dialogRef.close(null);
  }

  save() {
    this.dialogRef.close(this.client);
  }

  @Input()
  set client(client: Client) {
    if(!client) {
      this.clientForm.reset();
      return;
    }

    this.clientForm.patchValue({
      id: client.id,
      firstName: client.firstName,
      lastName: client.lastName,
      deliveryStreet: client.deliveryStreet,
      deliveryZipCode: client.deliveryZipCode,
      deliveryCity: client.deliveryCity,
      deliveryZoneId: client.deliveryZoneId,
      deliveryPhone: client.deliveryPhone,
      billingStreet: client.billingStreet,
      billingZipCode: client.billingZipCode,
      billingCity: client.billingCity,
      billingPhone: client.billingPhone,
      reduction: client.reduction,
      deliveryPosition: client.deliveryPosition
    });
  }

  get client(): Client {
    return new Client(
      this.clientForm.value['id'],
      this.clientForm.value['firstName'],
      this.clientForm.value['lastName'],
      this.clientForm.value['deliveryStreet'],
      this.clientForm.value['deliveryZipCode'],
      this.clientForm.value['deliveryCity'],
      this.clientForm.value['deliveryZoneId'],
      this.clientForm.value['deliveryPhone'],
      this.clientForm.value['billingStreet'],
      this.clientForm.value['billingZipCode'],
      this.clientForm.value['billingCity'],
      this.clientForm.value['billingPhone'],
      this.clientForm.value['reduction'],
      this.clientForm.value['deliveryPosition'],
      this.clientForm.value['deliveryPreferenceTakeAway']
    );
  }

}
