import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ClientComponent } from './page/client/client.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import {MatBadgeModule} from '@angular/material/badge';
import {MatChipsModule} from '@angular/material/chips';
import { MatInputModule} from '@angular/material/input';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule, MAT_FORM_FIELD_DEFAULT_OPTIONS } from '@angular/material/form-field';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatTableModule } from '@angular/material/table';
import { MatTabsModule } from '@angular/material/tabs';
import { OrderFormComponent } from './component/order/order-form/order-form.component';
import { ClientSearchComponent } from './component/client/client-search/client-search.component';
import { ClientCreationComponent } from './component/client/client-creation/client-creation.component';
import { MenuComponent } from './page/menu/menu.component';
import { OrderedFoodComponent } from './page/ordered-food/ordered-food.component';
import { OrderedFoodListComponent } from './component/ordered-food/ordered-food-list/ordered-food-list.component';
import { MatNativeDateModule, MAT_DATE_LOCALE } from '@angular/material/core';
import { MenuEditionComponent } from './component/menu/menu-edition/menu-edition.component';
import { DeliveryComponent } from './page/delivery/delivery.component';
import { DeliveryListComponent } from './component/delivery/delivery-list/delivery-list.component';
import { ClientEditionComponent } from './component/client/client-edition/client-edition.component';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatRadioModule } from '@angular/material/radio';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { FormulaPriceListComponent } from './component/formula-price/formula-price-list/formula-price-list.component';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatProgressSpinnerModule, MatProgressSpinner } from '@angular/material/progress-spinner';
import { MatSortModule } from '@angular/material/sort';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { FormulaPriceEditionComponent } from './component/formula-price/formula-price-edition/formula-price-edition.component';
import { FormulaPricesEditionComponent } from './page/formula-prices-edition/formula-prices-edition.component';
import { WarningDialogComponent } from './component/common/warning-dialog/warning-dialog.component';
import { DeliveryMansEditionComponent } from './page/delivery-mans-edition/delivery-mans-edition.component';
import { DeliveryZonesEditionComponent } from './page/delivery-zones-edition/delivery-zones-edition.component';
import { DeliveryManEditionComponent } from './component/delivery-man/delivery-man-edition/delivery-man-edition.component';
import { DeliveryManListComponent } from './component/delivery-man/delivery-man-list/delivery-man-list.component';
import { DeliveryZoneEditionComponent } from './component/delivery-zone/delivery-zone-edition/delivery-zone-edition.component';
import { DeliveryZoneListComponent } from './component/delivery-zone/delivery-zone-list/delivery-zone-list.component';
import {DragDropModule} from "@angular/cdk/drag-drop";
import { HomeComponent } from './page/home/home.component';
import { LayoutModule } from '@angular/cdk/layout';
import { MatToolbarModule } from '@angular/material/toolbar';
import { AuthHttpInterceptor, AuthModule } from '@auth0/auth0-angular';
import { AuthButtonComponent } from './component/home/auth-button/auth-button.component';
import { HttpErrorInterceptorService } from './service/http-error-interceptor.service';
import { BillComponent } from './component/bill/bill/bill.component';
import { BillOrderListComponent } from './component/bill/bill-order-list/bill-order-list.component';
import { FoodsEditionComponent } from './page/foods-edition/foods-edition.component';
import { FoodEditionComponent } from './component/food/food-edition/food-edition.component';
import { FoodListComponent } from './component/food/food-list/food-list.component';
import { OrderFoodFieldComponent } from './component/order/order-food-field/order-food-field.component';
import { FoodOrderQuantityEditionComponent } from './component/order/food-order-quantity-edition/food-order-quantity-edition.component';
import { EncodeRemarkComponent } from './component/remark/encode-remark/encode-remark.component';
import { MonthPickerComponent } from './component/common/month-picker/month-picker.component';
import { RemarkListComponent } from './component/remark/remark-list/remark-list.component';
import { FormulaToStringPipe } from './pipe/formula-to-string.pipe';
import { LoadingButtonDirective } from './component/common/loading-button.directive';
import { FlexLayoutModule } from '@angular/flex-layout';


@NgModule({
  declarations: [
    AppComponent,
    BillOrderListComponent,
    ClientComponent,
    OrderFormComponent,
    ClientSearchComponent,
    ClientCreationComponent,
    DeliveryManEditionComponent,
    DeliveryManListComponent,
    DeliveryZoneEditionComponent,
    DeliveryZoneListComponent,
    FoodEditionComponent,
    FoodListComponent,
    FoodsEditionComponent,
    MenuComponent,
    OrderedFoodComponent,
    OrderedFoodListComponent,
    MenuEditionComponent,
    DeliveryComponent,
    DeliveryListComponent,
    ClientEditionComponent,
    FormulaPriceListComponent,
    FormulaPriceEditionComponent,
    FormulaPricesEditionComponent,
    WarningDialogComponent,
    DeliveryMansEditionComponent,
    DeliveryZonesEditionComponent,
    HomeComponent,
    AuthButtonComponent,
    BillComponent,
    OrderFoodFieldComponent,
    FoodOrderQuantityEditionComponent,
    EncodeRemarkComponent,
    MonthPickerComponent,
    RemarkListComponent,
    FormulaToStringPipe,
    LoadingButtonDirective
  ],
  imports: [
    AuthModule.forRoot({
      domain: 'lesusscrofa.eu.auth0.com',
      clientId: 'UoO2y4sUQdWdVybztraLpsMiYdtQ3bNw',
      audience: 'https://www.lesusscrofa.be/',
      scope: 'admin',

      httpInterceptor: {
        allowedList: [
          {
            uri: 'http://localhost:4200/api/*',
            tokenOptions: {
              audience: 'https://www.lesusscrofa.be/',    
              scope: 'admin'
            }
          },
          {
            uri: 'http://localhost:8080/api/*',
            tokenOptions: {
              audience: 'https://www.lesusscrofa.be/',
              scope: 'admin'
            }
          },
          {
            uri: 'https://lesusscrofa.herokuapp.com/api/*',
            tokenOptions: {
              audience: 'https://www.lesusscrofa.be/',
              scope: 'admin'
            }
          }
        ]
      }
    
    }),
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    DragDropModule,
    FlexLayoutModule,
    FormsModule,
    HttpClientModule,
    MatAutocompleteModule,
    MatBadgeModule,
    MatCheckboxModule,
    MatChipsModule,
    MatDatepickerModule,
    MatDialogModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatNativeDateModule,
    MatProgressSpinnerModule,
    MatListModule,
    MatSidenavModule,
    MatSnackBarModule,
    MatTableModule,
    MatTabsModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatSelectModule,
    MatRadioModule,
    MatCardModule,
    MatPaginatorModule,
    MatSortModule,
    LayoutModule,
    MatToolbarModule
  ],
  providers: [
    { provide: MAT_DATE_LOCALE, useValue: 'fr-BE'},
    { provide: MAT_FORM_FIELD_DEFAULT_OPTIONS, useValue: { appearance: 'fill' } },
    { provide: HTTP_INTERCEPTORS, useClass: AuthHttpInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: HttpErrorInterceptorService, multi: true }
  ],
  entryComponents: [ 
    MatProgressSpinner 
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
