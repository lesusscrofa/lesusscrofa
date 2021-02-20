import { DataSource } from '@angular/cdk/collections';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { map, startWith } from 'rxjs/operators';
import { Observable, BehaviorSubject, combineLatest } from 'rxjs';
import { Food } from 'src/app/model/food';

export class FoodListDataSource extends DataSource<Food> {
  private foodsSubject = new BehaviorSubject<Food[]>([]);
  data = this.foodsSubject.asObservable();
  paginator: MatPaginator;
  sort: MatSort;

  constructor() {
    super();
  }

  connect(): Observable<Food[]> {

    return combineLatest([this.data, this.paginator.page.pipe(startWith({})), this.sort.sortChange.pipe(startWith({}))])
    .pipe(
      map(([fps, p, s]) => this.getPagedData(this.getSortedData(fps))));
  }

  disconnect() {}

  public loadData(foods:Food[]) {
    this.foodsSubject.next(foods);
  }

  private getPagedData(data: Food[]) {
    const startIndex = this.paginator.pageIndex * this.paginator.pageSize;
    return data.slice(startIndex, this.paginator.pageSize);
  }

  private getSortedData(data: Food[]) {
    if (!this.sort.active || this.sort.direction === '') {
      return data;
    }

    return data.sort((a, b) => {
      const isAsc = this.sort.direction === 'asc';
      switch (this.sort.active) {
        case 'id': return compare(+a.id, +b.id, isAsc);
        case 'name': return compare(a.name, b.name, isAsc);
        case 'start': return compare(a.start, b.start, isAsc);
        case 'end': return compare(a.end, b.end, isAsc);
        case 'price': return compare(a.price, b.price, isAsc);
        default: return 0;
      }
    });
  }
}

function compare(a: string | number | Date, b: string | number | Date, isAsc: boolean) {
  return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
}
