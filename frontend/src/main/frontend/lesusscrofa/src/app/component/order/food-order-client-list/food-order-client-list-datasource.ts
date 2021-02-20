import { DataSource } from '@angular/cdk/collections';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { map, startWith } from 'rxjs/operators';
import { Observable, BehaviorSubject, combineLatest } from 'rxjs';
import {FoodOrderClientFlatView} from "../../../model-view/food-order-client-flat-view";

export class FoodOrderClientListDatasource extends DataSource<FoodOrderClientFlatView> {
  private foodOrderClientFlatViewSubject = new BehaviorSubject<FoodOrderClientFlatView[]>([]);
  data = this.foodOrderClientFlatViewSubject.asObservable();
  paginator: MatPaginator;
  sort: MatSort;

  constructor() {
    super();
  }

  connect(): Observable<FoodOrderClientFlatView[]> {

    return combineLatest([this.data, this.paginator.page.pipe(startWith({})), this.sort.sortChange.pipe(startWith({}))])
    .pipe(
      map(([fps, p, s]) => this.getPagedData(this.getSortedData(fps))));
  }

  disconnect() {}

  public loadData(foodOrderClientFlatView:FoodOrderClientFlatView[]) {
    this.foodOrderClientFlatViewSubject.next(foodOrderClientFlatView);
  }

  private getPagedData(data: FoodOrderClientFlatView[]) {
    const startIndex = this.paginator.pageIndex * this.paginator.pageSize;
    return data.slice(startIndex, this.paginator.pageSize);
  }

  private getSortedData(data: FoodOrderClientFlatView[]) {
    if (!this.sort.active || this.sort.direction === '') {
      return data;
    }

    return data.sort((a, b) => {
      const isAsc = this.sort.direction === 'asc';
      switch (this.sort.active) {
        case 'id': return compare(+a.id, +b.id, isAsc);
        case 'deliveryPosition': return compare(+a.deliveryPosition, +b.deliveryPosition, isAsc);
        case 'firstName': return compare(a.firstName, b.firstName, isAsc);
        case 'lastName': return compare(a.lastName, b.lastName, isAsc);
        case 'soupName': return compare(a.soupName, b.soupName, isAsc);
        case 'soupQuantity': return compare(a.soupQuantity, b.soupQuantity, isAsc);
        case 'dishName': return compare(a.dishName, b.dishName, isAsc);
        case 'dishQuantity': return compare(a.dishQuantity, b.dishQuantity, isAsc);
        case 'alternativeDishName': return compare(a.alternativeDishName, b.alternativeDishName, isAsc);
        case 'alternativeDishQuantity': return compare(a.alternativeDishQuantity, b.alternativeDishQuantity, isAsc);
        case 'dessertName': return compare(a.dessertName, b.dessertName, isAsc);
        case 'dessertQuantity': return compare(a.dessertQuantity, b.dessertQuantity, isAsc);
        default: return 0;
      }
    });
  }
}

function compare(a: string | number | Date, b: string | number | Date, isAsc: boolean) {
  return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
}
