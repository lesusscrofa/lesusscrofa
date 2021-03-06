export class Client {

	public static fromJson(json: Object): Client {
		return new Client(
			json['id'],
			json['firstName'],
			json['lastName'],
			json['deliveryStreet'],
			json['deliveryZipCode'],
			json['deliveryCity'],
			json['deliveryZoneId'],
			json['deliveryPhone'],
			json['deliveryEmail'],
			json['billingStreet'],
			json['billingZipCode'],
			json['billingCity'],
			json['billingPhone'],
			json['billingEmail'],
			json['reduction'],
			json['deliveryPosition'],
			json['deliveryPreferenceTakeAway']
		);
	}


	constructor(
		private _id: number,
		private _firstName: string,
		private _lastName: string,
		private _deliveryStreet: string,
		private _deliveryZipCode: number,
		private _deliveryCity: string,
		private _deliveryZoneId: number,
		private _deliveryPhone: string,
		private _deliveryEmail: string,
		private _billingStreet: string,
		private _billingZipCode: number,
		private _billingCity: string,
		private _billingPhone: string,
		private _billingEmail: string,
		private _reduction: number,
		private _deliveryPosition: number,
		private _deliveryPreferenceTakeAway: boolean
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

	public get deliveryStreet(): string {
		return this._deliveryStreet;
	}

	public set deliveryStreet(deliveryStreet: string) {
		this._deliveryStreet = deliveryStreet;
	}

	public get deliveryZipCode(): number {
		return this._deliveryZipCode;
	}

	public set deliveryZipCode(deliveryZipCode: number) {
		this._deliveryZipCode = deliveryZipCode;
	}

	public get deliveryCity(): string {
		return this._deliveryCity;
	}

	public set deliveryCity(deliveryCity: string) {
		this._deliveryCity = deliveryCity;
	}

	public get deliveryZoneId(): number {
		return this._deliveryZoneId;
	}

	public set deliveryZoneId(deliveryZoneId: number) {
		this._deliveryZoneId = deliveryZoneId;
	}

	public get deliveryPhone(): string {
		return this._deliveryPhone;
	}

	public set deliveryPhone(deliveryPhone: string) {
		this._deliveryPhone = deliveryPhone;
	}

	public get deliveryEmail(): string {
		return this._deliveryEmail;
	}

	public set deliveryEmail(deliveryEmail: string) {
		this._deliveryEmail = deliveryEmail;
	}

	public get billingStreet(): string {
		return this._billingStreet;
	}

	public set billingStreet(billingStreet: string) {
		this._billingStreet = billingStreet;
	}

	public get billingZipCode(): number {
		return this._billingZipCode;
	}

	public set billingZipCode(billingZipCode: number) {
		this._billingZipCode = billingZipCode;
	}

	public get billingCity(): string {
		return this._billingCity;
	}

	public set billingCity(billingCity: string) {
		this._billingCity = billingCity;
	}

	public get billingPhone(): string {
		return this._billingPhone;
	}

	public set billingPhone(billingPhone: string) {
		this._billingPhone = billingPhone;
	}

	public get billingEmail(): string {
		return this._billingEmail;
	}

	public set billingEmail(billingEmail: string) {
		this._billingEmail = billingEmail;
	}

	public get reduction() {
		return this._reduction;
	}

	public set reduction(reduction: number) {
		this._reduction = reduction;
	}

	public get deliveryPosition(): number {
		return this._deliveryPosition;
	}

	public set deliveryPosition(deliveryPosition: number) {
		this._deliveryPosition = deliveryPosition;
	}

	public get deliveryPreferenceTakeAway(): boolean {
		return this._deliveryPreferenceTakeAway;
	}

	public set deliveryPreferenceTakeAway(deliveryPreferenceTakeAway: boolean) {
		this._deliveryPreferenceTakeAway = deliveryPreferenceTakeAway;
	}

	public toJson(): Object {
		return {
			id: this.id,
			firstName: this.firstName,
			lastName : this.lastName,
			deliveryStreet: this.deliveryStreet,
			deliveryZipCode: this.deliveryZipCode,
			deliveryCity: this.deliveryCity,
			deliveryZoneId: this.deliveryZoneId,
			deliveryPhone: this.deliveryPhone,
			deliveryEmail: this.deliveryEmail,
			billingStreet: this.billingStreet,
			billingZipCode: this.billingZipCode,
			billingCity: this.billingCity,
			billingPhone: this.billingPhone,
			billingEmail: this.billingEmail,
			reduction: this.reduction,
			deliveryPosition: this.deliveryPosition,
			deliveryPreferenceTakeAway: this.deliveryPreferenceTakeAway
		}
	}
}