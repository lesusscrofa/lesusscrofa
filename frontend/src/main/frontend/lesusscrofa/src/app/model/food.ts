import { DateUtils } from '../utils/date-utils';
import { ServiceType } from './service-type';

export class Food {

	public static substractFoodsFromArray(foods: Food[], foodsIdToRemove: number[]): Food[] {
		return foods.filter(food => foodsIdToRemove.indexOf(food.id) == -1);
	}

	public static fromJson(json: Object): Food {
		return new Food(
			json['id'],
			new Date(json['start']),
			json['end'] != null ? new Date(json['end']) : null,
			json['name'],
			json['service'],
			json['price'],
			json['vat'],
			json['unit']
		)
	}

	public static foodOrder(service: ServiceType) {
		switch(service) {
			case ServiceType.Soup:
				return 1;
			case ServiceType.Dish:
				return 2;
			case ServiceType.AlternativeDish:
				return 3;
			case ServiceType.Dessert:
				return 4;
			case ServiceType.Other:
				return 5;
			default:
				throw new Error("Unknown service : " + service);
		}
	}

	constructor(private _id: number,
		private _start: Date,
		private _end: Date,
		private _name: string,
		private _service: ServiceType,
		private _price: number,
		private _vat: number,
		private _unit: string) {}

	public toJson(): any {
		return {
			id: this._id,
			start: DateUtils.formatToIsoDate(this._start),
			end: DateUtils.formatToIsoDate(this._end),
			name: this._name,
			service: this._service,
			price: this._price,
			vat: this._vat,
			unit: this._unit
		}
	}

	public isInitialized(): boolean {
		if((!this.name || /^\s*$/.test(this.name)) && !this.price) {
		  return false
		}
	
		return true;
	  }

	public get id(): number {
		return this._id;
	}

	public set id(id: number) {
		this._id = id;
	}

	public get start(): Date {
		return this._start;
	}

	public set start(start: Date) {
		this._start = start;
	}

	public get end(): Date {
		return this._end;
	}

	public set end(end: Date) {
		this._end = end;
	}

	public get name(): string {
		return this._name;
	}

	public set name(name: string) {
		this._name = name;
	}

	public get service(): ServiceType {
		return this._service;
	}

	public set service(service: ServiceType) {
		this._service = service;
	}

	public get price(): number {
		return this._price;
	}

	public set price(price: number) {
		this._price = price;
	}

	public get vat(): number {
		return this._vat;
	}

	public set vat(vat: number) {
		this._vat = vat;
	}

	public get unit(): string {
		return this._unit;
	}

	public set unit(unit: string) {
		this._unit = unit;
	}
}