export class DeliveryMan {

	public static fromJson(json: Object): DeliveryMan {
		return new DeliveryMan(
			json['id'],
			json['firstName'],
			json['lastName'],
		)
	}


	constructor(
		private _id: number,
		private _firstName: string,
		private _lastName: string
	) {}

	public get id(): number {
		return this._id;
	}

	public set id(id: number) {
		this._id = id;
	}

	public get firstName(): string {
		return this._firstName;
	}

	public set firstName(firstName: string) {
		this._firstName = firstName;
	}

	public get lastName(): string {
		return this._lastName;
	}

	public set lastName(lastName: string) {
		this._lastName = lastName;
	}

	public toJson(): Object {
		return {
			id: this.id,
			firstName: this.firstName,
			lastName : this.lastName
		}
	}
}