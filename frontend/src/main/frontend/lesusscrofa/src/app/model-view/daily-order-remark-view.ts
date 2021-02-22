import { OrderedFoodView } from "./ordered-food-view";

export class DailyOrderRemarkView {

    public static fromJson(json: Object) {
        const foodsOrdersJson = json['foodsOrders'] as [];
        const foodOrders = foodsOrdersJson.map(fj => OrderedFoodView.fromJson(fj));

        return new DailyOrderRemarkView(json['clientId'],
                        json['clientFirstName'],
                        json['clientLastName'],
                        new Date(json['day']),
                        json['clientMessage'],
                        json['dailyMessage'],
                        foodOrders);
    }

    constructor(private _clientId: number,
                private _clientFirstName: string,
                private _clientLastName: string,
                private _day: Date,
                private _clientMessage: string,
                private _dailyMessage: string,
                private _foodsOrders: OrderedFoodView[]) {}
    
    get clientId(): number {
        return this._clientId;
    }

    get clientFirstName(): string {
        return this._clientFirstName;
    }

    get clientLastName(): string {
        return this._clientLastName;
    }

    get day(): Date {
        return this._day;
    }

    get clientMessage(): string {
        return this._clientMessage;
    }

    get dailyMessage(): string {
        return this._dailyMessage;
    }

    get foodsOrders(): OrderedFoodView[] {
        return this._foodsOrders;
    }

    get foodsOrdersResume(): string {
        return this._foodsOrders
            .map(fo => fo.quantity + ' ' + fo.foodName)
            .reduce((prev, current, index, foodsOrders) => prev + ", " + current);
    }
}