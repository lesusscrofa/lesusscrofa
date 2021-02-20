import { DataSource } from '@angular/cdk/collections';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { map, startWith } from 'rxjs/operators';
import { Observable, BehaviorSubject, combineLatest } from 'rxjs';
import { DeliveryZone } from 'src/app/model/delivery-zone';

export class DeliveryZoneListDataSource extends DataSource<DeliveryZone> {
  private deliveryZonesSubject = new BehaviorSubject<DeliveryZone[]>([]);
  data = this.deliveryZonesSubject.asObservable();
  paginator: MatPaginator;
  sort: MatSort;

  constructor() {
    super();
  }

  connect(): Observable<DeliveryZone[]> {

    return combineLatest([this.data, this.paginator.page.pipe(startWith({})), this.sort.sortChange.pipe(startWith({}))])
    .pipe(
      map(([fps, p, s]) => this.getPagedData(this.getSortedData(fps))));
  }

  disconnect() {}

  public loadData(deliveryZone:DeliveryZone[]) {
    this.deliveryZonesSubject.next(deliveryZone);
  }

  private getPagedData(data: DeliveryZone[]) {
    const startIndex = this.paginator.pageIndex * this.paginator.pageSize;
    return data.slice(startIndex, this.paginator.pageSize);
  }

  private getSortedData(data: DeliveryZone[]) {
    if (!this.sort.active || this.sort.direction === '') {
      return data;
    }

    return data.sort((a, b) => {
      const isAsc = this.sort.direction === 'asc';
      switch (this.sort.active) {
        case 'id': return compare(+a.id, +b.id, isAsc);
        case 'city': return compare(a.name, b.name, isAsc);
        default: return 0;
      }
    });
  }
}

function compare(a: string | number | Date, b: string | number | Date, isAsc: boolean) {
  return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
}
