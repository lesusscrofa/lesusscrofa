import { DataSource } from '@angular/cdk/collections';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { map, startWith } from 'rxjs/operators';
import { Observable, BehaviorSubject, combineLatest } from 'rxjs';
import { FormulaPrice } from 'src/app/model/formula-price';
import { getFormulaName } from 'src/app/model/formula';

export class FormulaPriceListDataSource extends DataSource<FormulaPrice> {
  private pricesSubject = new BehaviorSubject<FormulaPrice[]>([]);
  data = this.pricesSubject.asObservable();
  paginator: MatPaginator;
  sort: MatSort;

  constructor() {
    super();
  }

  connect(): Observable<FormulaPrice[]> {

    return combineLatest([this.data, this.paginator.page.pipe(startWith({})), this.sort.sortChange.pipe(startWith({}))])
    .pipe(
      map(([fps, p, s]) => this.getPagedData(this.getSortedData(fps))));
  }

  disconnect() {}

  public loadPrices(prices:FormulaPrice[]) {
    this.pricesSubject.next(prices);
  }

  private getPagedData(data: FormulaPrice[]) {
    const startIndex = this.paginator.pageIndex * this.paginator.pageSize;
    return data.slice(startIndex, this.paginator.pageSize);
  }

  private getSortedData(data: FormulaPrice[]) {
    if (!this.sort.active || this.sort.direction === '') {
      return data;
    }

    return data.sort((a, b) => {
      const isAsc = this.sort.direction === 'asc';
      switch (this.sort.active) {
        case 'id': return compare(+a.id, +b.id, isAsc);
        case 'start': return compare(a.start, b.start, isAsc);
        case 'formula': return compare(getFormulaName(a.formula), getFormulaName(b.formula), isAsc);
        case 'price': return compare(+a.price, +b.price, isAsc);
        default: return 0;
      }
    });
  }
}

function compare(a: string | number | Date, b: string | number | Date, isAsc: boolean) {
  return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
}
