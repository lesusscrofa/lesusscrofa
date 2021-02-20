import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { MatDatepicker } from '@angular/material/datepicker';
import { map, tap } from 'rxjs/operators';
import { Food } from 'src/app/model/food';
import { Menu } from 'src/app/model/menu';
import { DateService } from 'src/app/service/date.service';
import { MenuService } from 'src/app/service/menu.service';
import { DateUtils } from 'src/app/utils/date-utils';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent implements OnInit {

  menus: Menu[];

  constructor(private dateService: DateService,
      private menuService: MenuService) {}

  ngOnInit(): void {}

  saveMenus(menus: Menu[]) {
    this.menuService.save(menus)
      .subscribe(m => this.menus = m);
  }

  monthSelected(monthDate: Date) {
    this.loadMenus(monthDate);
  }

  private loadMenus(day: Date) {
    this.menuService.getMenusByDatesAndCreateMissingsAndAlternativeForEdition(
      this.dateService.getLastMondayForMonth(day.getFullYear(), day.getMonth()),
      this.dateService.getLastSundayForMonth(day.getFullYear(), day.getMonth())
    ).pipe(
      map(menus => menus.sort(Menu.compareToByDate))
    ).subscribe(m => this.menus = m);
  }

  
}
