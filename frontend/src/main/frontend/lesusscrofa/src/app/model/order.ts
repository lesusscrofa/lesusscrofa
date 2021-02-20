import { Observable } from 'rxjs';
import { FoodService } from '../service/food.service';
import { DateUtils } from '../utils/date-utils';
import { Food } from './food';
import { Formula } from './formula';

export class Order {
	
	public static fromJson(json: Object, foodService: FoodService): Order {
		var soupId = json['soupId'];
		var dishId = json['dishId'];
		var dessertId = json['dessertId'];
		var otherId = json['otherId'];

		return new Order(
			json['id'],
			json['clientId'],
			json['delivery'],
			new Date(json['day']),
			soupId,
			soupId != null ? foodService.get(soupId) : null,
			dishId,
			dishId != null ? foodService.get(dishId) : null,
			dessertId,
			dessertId != null ? foodService.get(dessertId) : null,
			otherId,
			otherId != null ? foodService.get(otherId): null,
			json['formula'],
			json['quantity'],
			json['price'],
			json['reduction']
		)
	}

	public static createMealTypeMapFromOrders(orders: Order[]): Map<Formula, Order> {
        return orders.reduce((menusOrders, menuOrder) => {
            menusOrders.set(menuOrder.formula, menuOrder);

            return menusOrders;
        }, new Map());
    }

	constructor(
		private _id: number,
		private _clientId: number,
		private _delivery: boolean,
		private _day: Date,
		private _soupId: number,
		private _soup: Observable<Food>,
		private _dishId: number,
		private _dish: Observable<Food>,
		private _dessertId: number,
		private _dessert: Observable<Food>,
		private _otherId: number,
		private _other: Observable<Food>,
		private _formula: Formula,
		private _quantity: number,
		private _price: number,
		private _reduction: number
	) { }

	toJson() {
		return {
			id: this.id,
			clientId: this.clientId,
			delivery: this.delivery,
			day: DateUtils.formatToIsoDate(this.day),
			soupId: this.soupId,
			dishId: this.dishId,
			dessertId: this.dessertId,
			otherId: this.otherId,
			formula: this.formula,
			quantity: this.quantity,
			price: this.price,
			reduction: this.reduction
		};
	}

	public get id(): number {
		return this._id;
	}

	public set id(id: number) {
		this._id = id;
	}

	public get delivery(): boolean {
		return this._delivery;
	}

	public set delivery(delivery: boolean) {
		this._delivery = delivery;
	}

	public get day(): Date {
		return this._day;
	}

	public set day(day: Date) {
		this._day = day;
	}

	public get clientId(): number {
		return this._clientId;
	}

	public set clientId(clientId: number) {
		this._clientId = clientId;
	}

	public get soupId(): number {
		return this._soupId;
	}

	public set soupId(soupId: number) {
		this._soupId = soupId;
	}

	public get soup(): Observable<Food> {
		return this._soup;
	}

	public get dishId(): number {
		return this._dishId;
	}

	public set dishId(dishId: number) {
		this._dishId = dishId;
	}

	public get dish(): Observable<Food> {
		return this._dish;
	}

	public get dessertId(): number {
		return this._dessertId;
	}

	public set dessertId(dessertId: number) {
		this._dessertId = dessertId;
	}

	public get dessert(): Observable<Food> {
		return this._dessert;
	}

	public get otherId(): number {
		return this._otherId;
	}

	public set otherId(otherId: number) {
		this._otherId = otherId;
	}

	public get other(): Observable<Food> {
		return this._other;
	}

	public get formula(): Formula {
		return this._formula;
	}

	public set formula(formula: Formula) {
		this._formula = formula;
	}

	public get quantity(): number {
		return this._quantity;
	}

	public set quantity(quantity: number) {
		this._quantity = quantity;
	}

	public get price(): number {
		return this._price;
	}

	public set price(price: number) {
		this._price = price;
	}

	public get reduction(): number {
		return this._reduction;
	}

	public set reduction(reduction: number) {
		this._reduction = reduction;
	}
}