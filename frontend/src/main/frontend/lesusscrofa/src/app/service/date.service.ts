import { Injectable } from '@angular/core';
import { generate, Observable } from 'rxjs';
import { filter } from 'rxjs/operators';
import { DateUtils } from '../utils/date-utils';

@Injectable({
  providedIn: 'root'
})
export class DateService {

  public getLastMondayForMonth(year: number, month: number) {
    var day = this.getFirstDayOfMonth(year, month);

    var nbrDayBetweenLastMonday = 1 - day.getDay();

    return DateUtils.addDays(day, nbrDayBetweenLastMonday);
  }

  public getLastSundayForMonth(year: number, month: number) {
    var day = this.getLastDayOfMonth(year, month);

    var nbrDayBetweenLastMonday = 0 + day.getDay();

    return DateUtils.addDays(day, nbrDayBetweenLastMonday)
  }

  public getFirstDayOfMonth(year: number, month: number): Date {
    return new Date(year, month, 1);
  }

  public getLastDayOfMonth(year: number, month: number): Date {
    return new Date(year, month+1, 0, 23, 59, 59, 999);
  }

  public getDatesBetween(start: Date, end: Date): Observable<Date> {
    return generate(
      start,
      current => current.getTime() <= end.getTime(),
      current => DateUtils.addDays(current, 1));
  }

  public getMondayBetween(start: Date, end: Date): Observable<Date> {
    return this.getDatesBetween(start, end).pipe(
      filter(day => day.getDay() == 1)
    );
  }
}
