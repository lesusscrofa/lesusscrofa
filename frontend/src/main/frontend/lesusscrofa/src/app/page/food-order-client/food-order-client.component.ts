import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { Observable } from 'rxjs';
import { FoodOrderClientFlatView } from 'src/app/model-view/food-order-client-flat-view';
import { FoodOrderService } from 'src/app/service/food-order.service';
import {ClientService} from "../../service/client.service";
import {map, mergeMap} from "rxjs/operators";
import { DeliveryMan } from 'src/app/model/delivery-man';
import { DeliveryManService } from 'src/app/service/delivery-man.service';

@Component({
  selector: 'app-food-order-client',
  templateUrl: './food-order-client.component.html',
  styleUrls: ['./food-order-client.component.css']
})
export class FoodOrderClientComponent implements OnInit {

  parameters: FormGroup;

  foodsOrdersClients$: Observable<FoodOrderClientFlatView[]>;

  deliveryMans: Observable<DeliveryMan[]>;

  constructor(private fb: FormBuilder,
      private foodOrderService: FoodOrderService,
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
    this.foodsOrdersClients$ = this.foodOrderService.getFoodsOrdersByClientsFlat(day, deliveryManId);
  }

  updateClientDeliveryPosition(params: {clientIdToUpdate: number, newDeliveryPosition : number}): void {
    this.foodsOrdersClients$ = this.clientService.get(params.clientIdToUpdate).pipe(
        map(client => {
          client.deliveryPosition = params.newDeliveryPosition
          return client;
        }),
        mergeMap(client => this.clientService.update(client)),
        mergeMap(client => this.foodOrderService.getFoodsOrdersByClientsFlat(this.day.value, this.deliveryManId.value))
    );
  }

  public get day(): FormControl {
    return this.parameters.controls['day'] as FormControl;
  }

  public get deliveryManId(): FormControl {
    return this.parameters.controls['deliveryManId'] as FormControl;
  }
}
