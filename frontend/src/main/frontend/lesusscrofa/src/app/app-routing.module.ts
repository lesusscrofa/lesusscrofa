import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { FormulaPriceEditionComponent } from './component/formula-price/formula-price-edition/formula-price-edition.component';
import { FormulaPricesEditionComponent } from './page/formula-prices-edition/formula-prices-edition.component';
import { ClientComponent } from './page/client/client.component';
import { DeliveryComponent } from './page/delivery/delivery.component';
import { OrderedFoodComponent } from './page/ordered-food/ordered-food.component';
import { MenuComponent } from './page/menu/menu.component';
import { DeliveryMansEditionComponent } from './page/delivery-mans-edition/delivery-mans-edition.component';
import { DeliveryZonesEditionComponent } from './page/delivery-zones-edition/delivery-zones-edition.component';
import { AuthGuard } from '@auth0/auth0-angular';
import { FoodsEditionComponent } from './page/foods-edition/foods-edition.component';

const routes: Routes = [
  { path: 'client', component: ClientComponent, canActivate: [AuthGuard] },
  { path: 'menu', component: MenuComponent, canActivate: [AuthGuard] },
  { path: 'order/food', component: OrderedFoodComponent, canActivate: [AuthGuard] },
  { path: 'order/food/byclient', component: DeliveryComponent, canActivate: [AuthGuard] },
  { path: 'formula/price', component: FormulaPricesEditionComponent, canActivate: [AuthGuard] },
  { path: 'food', component: FoodsEditionComponent, canActivate: [AuthGuard] },
  { path: 'deliveryMan', component: DeliveryMansEditionComponent, canActivate: [AuthGuard] },
  { path: 'deliveryZone', component: DeliveryZonesEditionComponent, canActivate: [AuthGuard] }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
