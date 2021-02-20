import { CollectionViewer } from '@angular/cdk/collections';
import { DataSource } from '@angular/cdk/table';
import { Injectable } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
  })
export class MenuDataSource implements DataSource<FormGroup> {
    
    private menuSubject = new BehaviorSubject<FormGroup[]>([]);

    constructor(private fb: FormBuilder) {}

    connect(collectionViewer: CollectionViewer): Observable<FormGroup[] | readonly FormGroup[]> {
        return this.menuSubject.asObservable();
    }
    disconnect(collectionViewer: CollectionViewer): void {
        this.menuSubject.complete();
    }
    
    loadMenus(menusArray: FormGroup[]) {
        this.menuSubject.next(menusArray);
    }
}