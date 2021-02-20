import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatDatepicker } from '@angular/material/datepicker';

@Component({
  selector: 'app-month-picker',
  templateUrl: './month-picker.component.html',
  styleUrls: ['./month-picker.component.css']
})
export class MonthPickerComponent implements OnInit {

  month: FormControl;

  @Output()
  monthSelected: EventEmitter<Date>;

  constructor() { 
    this.monthSelected = new EventEmitter();
    this.month = new FormControl(new Date());
  }

  ngOnInit(): void {
    this.monthSelected.emit(this.month.value);
  }

  _monthSelected(dateInput: Date, monthPicker: MatDatepicker<Date>) {
    const monthDate = this.month.value as Date;

    monthDate.setMonth(dateInput.getMonth());
    monthDate.setFullYear(dateInput.getFullYear());

    this.month.setValue(monthDate);

    monthPicker.close();

    this.monthSelected.emit(monthDate);
  }

}
