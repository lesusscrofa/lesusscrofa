import { Food } from '../model/food';
import { ServiceType } from '../model/service-type';

export class OrderedFoodView {

	public static fromJson(json: Object): OrderedFoodView {
		return new OrderedFoodView(
			json['foodId'],
			new Date(json['orderDay']),
			json['foodName'],
			json['foodService'],
			json['quantity']
		)
	}

	public static compareToByService(f1: OrderedFoodView, f2: OrderedFoodView) {
		if(Food.foodOrder(f1.foodService) == Food.foodOrder(f2.foodService)) {
			return 0;
		}
		else if(Food.foodOrder(f1.foodService) > Food.foodOrder(f2.foodService)) {
			return 1;
		}
		else {
			return -1;
		}
	}

	constructor(private _foodId: number,
		private _orderDay: Date,
		private _foodName: string,
		private _foodService: ServiceType,
		private _quantity: number) {}

	public get description() {
		return this.quantity + " " + this.foodName;
	}


	public get foodId(): number {
		return this._foodId;
	}

	public set foodId(foodId: number) {
		this._foodId = foodId;
	}

	public get orderDay(): Date {
		return this._orderDay;
	}

	public set orderDay(orderDay: Date) {
		this._orderDay = orderDay;
	}

	public get foodName(): string {
		return this._foodName;
	}

	public set foodName(foodName: string) {
		this._foodName = foodName;
	}

	public get foodService(): ServiceType {
		return this._foodService;
	}

	public set foodService(foodService: ServiceType) {
		this._foodService = foodService;
	}

	public get quantity(): number {
		return this._quantity;
	}

	public set quantity(quantity: number) {
		this._quantity = quantity;
	}
}