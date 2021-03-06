import { OrderedFoodView } from "./ordered-food-view";

export class FoodOrderClientView {

    public static fromJson(json: Object): FoodOrderClientView {
        var foodsOrders = json['foodsOrders'] as [];

		return new FoodOrderClientView(
			json['id'],
			json['firstName'],
			json['lastName'],
			foodsOrders.map(foodOrder => OrderedFoodView.fromJson(foodOrder))
		)
	}

    constructor(private _id: number,
        private _firstName: string,
        private _lastName: string,
        private _foodsOrders: OrderedFoodView[]
        ) {}

    public get foodsDescription() {
        return this.foodsOrders.map(foodOrder => foodOrder.description + ", ")
    }

    public get id(): number {
        return this._id;
    }

    public set id(id: number) {
        this._id = id;
    }

    public get firstName() {
        return this._firstName;
    }

    public set firstName(firstName: string) {
        this._firstName = firstName;
    }

    public get lastName() {
        return this._lastName;
    }

    public set lastName(lastName: string) {
        this._lastName = lastName;
    }

    public get foodsOrders(): OrderedFoodView[] {
        return this._foodsOrders;
    }

    public set foodsOrders(foodsOrders: OrderedFoodView[]) {
        this._foodsOrders = foodsOrders;
    }
}