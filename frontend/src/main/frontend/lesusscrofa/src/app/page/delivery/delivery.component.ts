import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { Observable } from 'rxjs';
import { DeliveryView } from 'src/app/model-view/delivery-view';
import {ClientService} from "../../service/client.service";
import {map, mergeMap, tap} from "rxjs/operators";
import { DeliveryMan } from 'src/app/model/delivery-man';
import { DeliveryManService } from 'src/app/service/delivery-man.service';
import { DeliveryService } from 'src/app/service/delivery.service';

@Component({
  selector: 'app-delivery',
  templateUrl: './delivery.component.html',
  styleUrls: ['./delivery.component.css']
})
export class DeliveryComponent implements OnInit {

  parameters: FormGroup;

  deliveries$: Observable<DeliveryView[]>;

  isDeliveriesLoading = true;

  deliveryMans: Observable<DeliveryMan[]>;

  constructor(private fb: FormBuilder,
      private deliveryService: DeliveryService,
      private clientService: ClientService,
      private deliveryManService: DeliveryManService) {
    this.parameters = this.fb.group({
      day: [new Date()],
      deliveryManId: []
    });

    this.parameters.valueChanges.subscribe(() => this.parametersChanged());
  }

  ngOnInit(): void {
    this.loadFoodsOrders(this.day.value, this.deliveryManId.value);

    this.deliveryMans = this.deliveryManService.findAll();
  }

  parametersChanged() {
    this.loadFoodsOrders(this.day.value, this.deliveryManId.value);
  }

  private loadFoodsOrders(day: Date, deliveryManId: number) {
    this.deliveries$ = this.deliveryService.getDelivery(day, deliveryManId)
      .pipe(
        tap(d => this.isDeliveriesLoading = false)
      );
  }

  updateClientDeliveryPosition(params: {clientIdToUpdate: number, newDeliveryPosition : number}): void {
    this.isDeliveriesLoading = true;

    this.deliveries$ = this.clientService.get(params.clientIdToUpdate).pipe(
        map(client => {
          client.deliveryPosition = params.newDeliveryPosition
          return client;
        }),
        mergeMap(client => this.clientService.update(client)),
        mergeMap(client => this.deliveryService.getDelivery(this.day.value, this.deliveryManId.value)),
        tap(d => this.isDeliveriesLoading = false)
    );
  }

  public get day(): FormControl {
    return this.parameters.controls['day'] as FormControl;
  }

  public get deliveryManId(): FormControl {
    return this.parameters.controls['deliveryManId'] as FormControl;
  }
}
