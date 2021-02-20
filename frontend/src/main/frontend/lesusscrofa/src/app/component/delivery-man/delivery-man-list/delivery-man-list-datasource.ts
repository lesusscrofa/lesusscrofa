import { DataSource } from '@angular/cdk/collections';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { map, startWith } from 'rxjs/operators';
import { Observable, BehaviorSubject, combineLatest } from 'rxjs';
import { DeliveryMan } from 'src/app/model/delivery-man';

export class DeliveryManListDataSource extends DataSource<DeliveryMan> {
  private deliveryMansSubject = new BehaviorSubject<DeliveryMan[]>([]);
  data = this.deliveryMansSubject.asObservable();
  paginator: MatPaginator;
  sort: MatSort;

  constructor() {
    super();
  }

  connect(): Observable<DeliveryMan[]> {

    return combineLatest([this.data, this.paginator.page.pipe(startWith({})), this.sort.sortChange.pipe(startWith({}))])
    .pipe(
      map(([fps, p, s]) => this.getPagedData(this.getSortedData(fps))));
  }

  disconnect() {}

  public loadData(deliveryMans:DeliveryMan[]) {
    this.deliveryMansSubject.next(deliveryMans);
  }

  private getPagedData(data: DeliveryMan[]) {
    const startIndex = this.paginator.pageIndex * this.paginator.pageSize;
    return data.slice(startIndex, this.paginator.pageSize);
  }

  private getSortedData(data: DeliveryMan[]) {
    if (!this.sort.active || this.sort.direction === '') {
      return data;
    }

    return data.sort((a, b) => {
      const isAsc = this.sort.direction === 'asc';
      switch (this.sort.active) {
        case 'id': return compare(+a.id, +b.id, isAsc);
        case 'firstName': return compare(a.firstName, b.lastName, isAsc);
        case 'lastName': return compare(a.lastName, b.lastName, isAsc);
        default: return 0;
      }
    });
  }
}

function compare(a: string | number | Date, b: string | number | Date, isAsc: boolean) {
  return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
}
