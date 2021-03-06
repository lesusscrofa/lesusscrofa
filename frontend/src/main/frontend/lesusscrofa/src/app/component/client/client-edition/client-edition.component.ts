import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Observable } from 'rxjs';
import { Client } from 'src/app/model/client';
import { DeliveryZone } from 'src/app/model/delivery-zone';
import { DeliveryZoneService } from 'src/app/service/delivery-zone.service';

@Component({
  selector: 'app-client-edition',
  templateUrl: './client-edition.component.html',
  styleUrls: ['./client-edition.component.css']
})
export class ClientEditionComponent implements OnInit {

  deliveryZones: Observable<DeliveryZone[]>;

  clientForm = this.fb.group({
    id : [null],
    firstName: [null, Validators.required],
    lastName: [null, Validators.required],
    deliveryStreet: [null], 
    deliveryZipCode: [null, Validators.compose([
      Validators.minLength(4), Validators.maxLength(4)])
    ],
    deliveryCity: [null],   
    deliveryZoneId: [null],
    deliveryPhone: [null],
    deliveryEmail: [null, Validators.email],
    billingStreet: [null],    
    billingZipCode: [null, Validators.compose([
      Validators.minLength(4), Validators.maxLength(4)])
    ],
    billingCity: [null],
    billingPhone: [null],
    billingEmail: [null, Validators.email],
    reduction: [null],
    deliveryPreferenceTakeAway: [null],
    deliveryPosition: [null]
  });

  sameAddressForBilling: boolean;

  @Output()
  clientEdited: EventEmitter<Client>;

  constructor(private fb: FormBuilder,
      private deliveryZoneService: DeliveryZoneService) {
    this.sameAddressForBilling = true;
    this.clientEdited = new EventEmitter();
  }
  ngOnInit(): void {
    this.deliveryZones = this.deliveryZoneService.findAll();
  }

  onSubmit() {
    this.clientEdited.emit(this.client);
  }

  @Input()
  set client(client: Client) {
    this.clientForm.patchValue({
      id: client.id,
      firstName: client.firstName,
      lastName: client.lastName,
      deliveryStreet: client.deliveryStreet,
      deliveryZipCode: client.deliveryZipCode,
      deliveryCity: client.deliveryCity,
      deliveryZoneId: client.deliveryZoneId,
      deliveryPhone: client.deliveryPhone,
      deliveryEmail: client.deliveryEmail,
      billingStreet: client.billingStreet,
      billingZipCode: client.billingZipCode,
      billingCity: client.billingCity,
      billingPhone: client.billingPhone,
      billingEmail: client.billingEmail,
      reduction: client.reduction,
      deliveryPosition: client.deliveryPosition,
      deliveryPreferenceTakeAway: client.deliveryPreferenceTakeAway
    });

    this.sameAddressForBilling = !client.billingStreet && !client.billingPhone && !client.billingEmail; 
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
      this.clientForm.value['deliveryEmail'],
      this.sameAddressForBilling ? null: this.clientForm.value['billingStreet'],
      this.sameAddressForBilling ? null: this.clientForm.value['billingZipCode'],
      this.sameAddressForBilling ? null: this.clientForm.value['billingCity'],
      this.sameAddressForBilling ? null: this.clientForm.value['billingPhone'],
      this.sameAddressForBilling ? null: this.clientForm.value['billingEmail'],
      this.clientForm.value['reduction'],
      this.clientForm.value['deliveryPosition'],
      this.clientForm.value['deliveryPreferenceTakeAway']
    );
  }

  toggleSameAddressForBilling(sameAddressForBilling: boolean) {
    this.sameAddressForBilling = sameAddressForBilling;
  }
}
