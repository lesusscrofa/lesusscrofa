import { CollectionViewer } from '@angular/cdk/collections';
import { DataSource } from '@angular/cdk/table';
import { Injectable } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
  })
export class OrderDataSource implements DataSource<FormGroup> {

    private orderSubject = new BehaviorSubject<FormGroup[]>([]);

    constructor(private fb: FormBuilder) {}

    connect(collectionViewer: CollectionViewer): Observable<FormGroup[]> {
        return this.orderSubject.asObservable();
    }

    disconnect(collectionViewer: CollectionViewer): void {
        this.orderSubject.complete();
    }

    loadOrders(ordersArray: FormGroup[]) {
        this.orderSubject.next(ordersArray);
    }
}