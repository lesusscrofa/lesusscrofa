import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormArray, FormBuilder, FormGroup } from '@angular/forms';
import { MatTableDataSource } from '@angular/material/table';
import { Food } from 'src/app/model/food';
import { Menu } from 'src/app/model/menu';
import { MenuService } from 'src/app/service/menu.service';
import { MessageService, MessageType } from 'src/app/service/message.service';
import { DateUtils } from 'src/app/utils/date-utils';

@Component({
  selector: 'app-menu-edition',
  templateUrl: './menu-edition.component.html',
  styleUrls: ['./menu-edition.component.css']
})
export class MenuEditionComponent implements OnInit {

  savingMenus: boolean = false;

  isDatesSameDay = DateUtils.isDatesSameDay;

  dataSource: MatTableDataSource<FormGroup>;

  menuForm: FormGroup;

  displayedColumns = ['start', 'end','soup', 'dish', 'dessert'];

  constructor(private menuService: MenuService,
              private messageService: MessageService,
              private fb: FormBuilder) { 
    this.dataSource = new MatTableDataSource();

    this.menuForm = this.fb.group({
      entries: this.fb.array([])
    });
  }

  ngOnInit(): void {
  }

  saveMenus() {
    this.savingMenus = true;

    var menusArray: [] = this.menusEntries.value;
    var menus = menusArray.map(m => this.toMenu(m));

    this.menuService.save(menus)
      .subscribe(m => {
        this.menus = m;
        this.savingMenus = false;
        this.messageService.showMessage("Les menus ont bien été sauvés", MessageType.INFORMATION);
      });
  }

  private loadMenusForm(menus: Menu[]) {
    var menusArray = menus.map(menu => {
      return this.fb.group({
        start: menu.start,
        end: menu.end, 
        soup: menu.isAlternativeMenu ? null : this.createFoodForm(menu.soup),
        dish: this.createFoodForm(menu.dish),
        dessert: menu.isAlternativeMenu ? null :  this.createFoodForm(menu.dessert),
        alternativeMenu: menu.isAlternativeMenu
      });
    });

    this.menusEntries.clear();
    menusArray.forEach(menuArray => this.menusEntries.push(menuArray));

    this.dataSource.data = menusArray;
  }

  private createFoodForm(food: Food): FormGroup {
    return this.fb.group({
      id: [food.id],
      name: [food.name],
      start: [food.start],
      end: [food.end],
      service: [food.service],
      price: [food.price],
      vat: [food.vat],
      unit: [food.unit]
    });
  }

  private toMenu(menuGroup: FormGroup) {
    var soupGroup = menuGroup['soup'];
    var dishGroup = menuGroup['dish'];
    var dessertGroup = menuGroup['dessert'];

    return new Menu(
      menuGroup['start'], 
      menuGroup['end'], 
      soupGroup ? this.toFood(soupGroup) : null, 
      this.toFood(dishGroup), 
      null,
      dessertGroup ? this.toFood(dessertGroup) : null, 
      menuGroup['alternativeMenu']);
  }

  private toFood(foodGroup: FormGroup) {
    return new Food(
      foodGroup['id'],
      foodGroup['start'],
      foodGroup['end'],
      foodGroup['name'],
      foodGroup['service'],
      foodGroup['price'],
      foodGroup['vat'],
      foodGroup['unit']);
  }

  get menusEntries(): FormArray {
    return this.menuForm.get('entries') as FormArray;
  }

  @Input()
  set menus(menus: Menu[]) {
    if(menus) {
      this.loadMenusForm(menus);
    }
  }
}
