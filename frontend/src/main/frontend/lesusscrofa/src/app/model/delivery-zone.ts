import { Observable } from "rxjs";
import { DeliveryMan } from "./delivery-man";

export class DeliveryZone {

	public static fromJson(json: Object, deliveryMan: Observable<DeliveryMan>): DeliveryZone {
		return new DeliveryZone(
			json['id'],
			json['name'],
			json['deliveryManId'],
			deliveryMan
		)
	}


	constructor(
		private _id: number,
		private _name: string,
		private _deliveryManId: number,
		private _deliveryMan: Observable<DeliveryMan>
	) {}

	public get id(): number {
		return this._id;
	}

	public set id(id: number) {
		this._id = id;
	}

	public get name(): string {
		return this._name;
	}

	public set name(name: string) {
		this._name = name;
	}

	public get deliveryManId(): number {
		return this._deliveryManId;
	}

	public set deliveryManId(deliveryManId: number) {
		this._deliveryManId = deliveryManId;
	}

	public get deliveryMan(): Observable<DeliveryMan> {
		return this._deliveryMan;
	}

	public set deliveryMan(deliveryMan: Observable<DeliveryMan>) {
		this._deliveryMan = deliveryMan;
	}

	public toJson(): Object {
		return {
			id: this.id,
			name : this.name,
			deliveryManId: this.deliveryManId
		}
	}
}