import { DateUtils } from '../utils/date-utils';
import { Food } from './food';

export class Menu {

	public static fromJson(json: Object): Menu {
		return new Menu(
			new Date(json['day']),
			new Date(json['day']),
			json['soup'] ? Food.fromJson(json['soup']) : null,
			json['dish'] ? Food.fromJson(json['dish']) : null,
			json['alternativeDish'] ? Food.fromJson(json['alternativeDish']) : null,
			json['dessert'] ? Food.fromJson(json['dessert']) : null,
			null
		)
	}

	constructor(private _start: Date,
		private _end: Date,
		private _soup: Food,
		private _dish: Food,
		private _alternativeDish: Food,
		private _dessert: Food,
		private _alternativeMenu: boolean) {}

	public isMenuCreated(): boolean {
		return this.dish && this.dish.id != null;
	}

	public isMenuInitialized(): boolean {
		return (this.soup && this.soup.isInitialized()
			|| this.dish && this.dish.isInitialized()
			|| this.dessert && this.dessert.isInitialized())
	}

	public toJson(): any {
		var json = {
			day: DateUtils.formatToIsoDate(this._start),
			soup: this._soup.toJson(),
			dish: this._dish.toJson(),
			alternative_dish: this._alternativeDish ? this._alternativeDish.toJson() : null,
			dessert: this._dessert.toJson()
		};

		return json;
	}

	public static compareToByDate(m1: Menu, m2: Menu): number {
		var day1 = m1.end;
		var day2 = m2.end;

		if(day1 > day2) {
			return 1;
		}
		else if(day1 < day2) {
			return -1;
		}
		else {
			return 0;
		}
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

	public get soup(): Food {
		return this._soup;
	}

	public set soup(soup: Food) {
		this._soup = soup;
	}

	public get dish(): Food {
		return this._dish;
	}

	public set alternativeDish(alternativeDish: Food) {
		this._alternativeDish = alternativeDish;
	}

	public get alternativeDish(): Food {
		return this._alternativeDish;
	}

	public set dish(dish: Food) {
		this._dish = dish;
	}

	public get dessert(): Food {
		return this._dessert;
	}

	public set dessert(dessert: Food) {
		this._dessert = dessert;
	}

	public get isAlternativeMenu(): boolean {
		return this._alternativeMenu;
	}

	public set isAlternativeMenu(isAlternativeMenu: boolean) {
		this._alternativeMenu = isAlternativeMenu;
	}
}