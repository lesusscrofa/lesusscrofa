import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { Formula } from '../model/formula';
import { FormulaPrice } from '../model/formula-price';

@Injectable({
  providedIn: 'root'
})
export class FormulaPriceService {

  private url: string;

  constructor(private http: HttpClient) { 
    this.url = environment.apiUrl + "/formulas/prices"
  }

  getActives(): Observable<FormulaPrice[]> {
    return this.http.get<FormulaPrice[]>(this.url + '/actives')
      .pipe(
        map(bps => bps.map(bp => FormulaPrice.fromJson(bp)))
      );
  }

  findAll(formula: Formula): Observable<FormulaPrice[]> {
    return this.http.get<FormulaPrice[]>(this.url + '?formula=' + formula)
      .pipe(
        map(bps => bps.map(bp => FormulaPrice.fromJson(bp)))
      );
  }

  create(billingPrice: FormulaPrice): Observable<FormulaPrice> {
    return this.http.post(this.url, billingPrice.toJson())
      .pipe(
        map(bp => FormulaPrice.fromJson(bp))
      );
  }

  update(billingPrice: FormulaPrice): Observable<FormulaPrice> {
    return this.http.put(this.url + '/' + billingPrice.id, billingPrice.toJson())
      .pipe(
        map(bp => FormulaPrice.fromJson(bp))
      );
  }

  delete(billingPrice: FormulaPrice): Observable<any> {
    return this.http.delete(this.url + '/' + billingPrice.id);
  }
}
