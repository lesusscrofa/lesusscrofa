import { SelectionChange, SelectionModel } from '@angular/cdk/collections';
import { AfterViewInit, Component, EventEmitter, OnInit, Output, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTable } from '@angular/material/table';
import { Formula, getFormulaName } from 'src/app/model/formula';
import { FormulaPrice } from 'src/app/model/formula-price';
import { FormulaPriceService } from 'src/app/service/formula-price.service';
import { FormulaPriceListDataSource } from './formula-price-list-datasource';

@Component({
  selector: 'app-formula-price-list',
  templateUrl: './formula-price-list.component.html',
  styleUrls: ['./formula-price-list.component.css']
})
export class FormulaPriceListComponent implements AfterViewInit, OnInit {
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatTable) table: MatTable<FormulaPrice>;

  dataSource: FormulaPriceListDataSource;

  displayedColumns = ['select', 'id', 'start', 'formula', 'price', 'vat'];

  selection : SelectionModel<FormulaPrice>;

  formulas = Object.values(Formula);

  searchPriceControl: FormGroup;

  @Output()
  edit: EventEmitter<FormulaPrice>;

  @Output()
  delete: EventEmitter<FormulaPrice>;

  constructor(private fb: FormBuilder,
              private billingService: FormulaPriceService) {
    this.dataSource = new FormulaPriceListDataSource();
    
    this.selection = new SelectionModel(false, []);

    this.edit = new EventEmitter();
    this.delete = new EventEmitter();

    this.searchPriceControl = this.fb.group({
      formula: [Formula.ACTIF]
    });

    this.searchPriceControl.valueChanges.subscribe(() => this.search());
  }

  ngOnInit() {
    this.search();
  }

  ngAfterViewInit() {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
    this.table.dataSource = this.dataSource;
  }

  search() {
    const formula = this.searchPriceControl.value['formula'];

    if(Formula.ACTIF == formula) {
      this.billingService.getActives()
        .subscribe(
          fps => this.dataSource.loadPrices(fps)
        );
    }
    else {
      this.billingService.findAll(formula)
        .subscribe(
          fps => this.dataSource.loadPrices(fps)
        );
    }
  }

  editSelectedRow() {
    const fp = this.selection.selected[0];

    this.edit.emit(fp);
  }

  deleteSelectedRow() {
    const fp = this.selection.selected[0];

    this.delete.emit(fp);
  }
}
