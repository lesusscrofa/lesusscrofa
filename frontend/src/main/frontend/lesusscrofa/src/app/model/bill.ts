import { FoodService } from '../service/food.service';
import { OrderSummary } from './order-summary';
import { Client } from './client';
import { Order } from './order';

export class Bill {

	public static fromJson(json: Object, foodService: FoodService): Bill {
		const ordersJson = json['orders'] as Order[];
		const ordersSummaries = json['ordersSummaries'] as OrderSummary[];

		return new Bill(
			Client.fromJson(json['client']),
			new Date(json['createDate']),
			new Date(json['from']),
			new Date(json['to']),
			ordersSummaries.map(orderSummaryJson => OrderSummary.fromJson(ordersSummaries)),
			json['totalVatExcluded'],
			json['totalVatIncluded']
		);
	}


	constructor(
		private _client: Client,
		private _createDate: Date,
		private _from: Date,
		private _to: Date,
		private _ordersSummaries: OrderSummary[],
		private _totalVatExcluded: number,
		private _totalVatIncluded: number
	) {}

	public get client(): Client {
		return this._client;
	}

	public get createDate(): Date {
		return this._createDate;
	}

	public get from(): Date {
		return this._from;
	}

	public get to(): Date {
		return this._to;
	}

	public get billOrdersSummaries(): OrderSummary[] {
		return this._ordersSummaries;
	}
	
	public get totalVatExcluded() {
		return this._totalVatExcluded;
	}
	
	public get totalVatIncluded() {
		return this._totalVatIncluded;
	}
}