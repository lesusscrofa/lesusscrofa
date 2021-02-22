export class DeliveryView {

    public static fromJson(json: Object): DeliveryView {
        var others = json['others'] as [];

        return new DeliveryView(
            json['id'],
            json['firstName'],
            json['lastName'],
            json['deliveryPhone'],
            json['deliveryStreet'],
            json['deliveryZoneId'],
            json['deliveryZipCode'],
            json['deliveryCity'],
            json['deliveryManId'],
            json['deliveryManFirstName'],
            json['deliveryManLastName'],
            json['deliveryPosition'],
            new Date(json['day']),
            json['soupName'],
            json['soupQuantity'],
            json['dishName'],
            json['dishQuantity'],
            json['alternativeDishName'],
            json['alternativeDishQuantity'],
            json['dessertName'],
            json['dessertQuantity'],
            others.map(o => OtherFoodDeliveryView.fromJson(o))
        );
    }

    constructor(private _id: number,
        private _firstName: string,
        private _lastName: string,
        private _deliveryPhone: string,
        private _deliveryStreet: string,
        private _deliveryZoneId: number,
        private _deliveryZipCode: number,
        private _deliveryCity: string,
        private _deliveryManId: number,
        private _deliveryManFirstName: string,
        private _deliveryManLastName: string,
        private _deliveryPosition: number,
        private _day: Date,
        private _soupName: string,
        private _soupQuantity: number,
        private _dishName: string,
        private _dishQuantity: number,
        private _alternativeDishName: string,
        private _alternativeDishQuantity: number,
        private _dessertName: string,
        private _dessertQuantity: number,
        private _others: OtherFoodDeliveryView[]
        ) {}

    public get othersDescriptions(): string {
        return this.others.reduce(
            (acc, food, index) => acc + food.description + (index < this.others.length-1 ? ", " : ""), 
            "");
    }

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

    public get deliveryPhone(): string {
        return this._deliveryPhone;
    }

    public set deliveryPhone(deliveryPhone: string) {
        this._deliveryPhone = deliveryPhone;
    }

    public get deliveryStreet(): string {
        return this._deliveryStreet;
    }

    public set deliveryStreet(deliveryStreet: string) {
        this._deliveryStreet = deliveryStreet;
    }

    public get deliveryZoneId(): number {
        return this._deliveryZoneId;
    }

    public set deliveryZoneId(deliveryZoneId: number) {
        this._deliveryZoneId = deliveryZoneId;
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

    public get deliveryManId(): number {
        return this._deliveryManId;
    }

    public set deliveryManId(deliveryManId: number) {
        this._deliveryManId = deliveryManId;
    }

    public get deliveryManFirstName(): string {
        return this._deliveryManFirstName;
    }

    public set deliveryManFirstName(deliveryManFirstName: string) {
        this._deliveryManFirstName = deliveryManFirstName;
    }

    public get deliveryManLastName(): string {
        return this._deliveryManLastName;
    }

    public set deliveryManLastName(deliveryManLastName: string) {
        this._deliveryManLastName = deliveryManLastName;
    }

    public get deliveryPosition(): number {
        return this._deliveryPosition;
    }

    public set deliveryPosition(deliveryPosition: number) {
        this._deliveryPosition = deliveryPosition;
    }

    public get day(): Date {
        return this._day;
    }

    public set day(day: Date) {
        this._day = day;
    }

    public get soupName(): string {
        return this._soupName;
    }

    public set soupName(soupName: string) {
        this._soupName = soupName;
    }

    public get soupQuantity(): number {
        return this._soupQuantity;
    }

    public set soupQuantity(soupQuantity: number) {
        this._soupQuantity = soupQuantity;
    }

    public get dishName(): string {
        return this._dishName;
    }

    public set dishName(dishName: string) {
        this._dishName = dishName;
    }

    public get dishQuantity(): number {
        return this._dishQuantity;
    }

    public set dishQuantity(dishQuantity: number) {
        this._dishQuantity = dishQuantity;
    }

    public get alternativeDishName(): string {
        return this._alternativeDishName;
    }

    public set alternativeDishName(alternativeDishName: string) {
        this._alternativeDishName = alternativeDishName;
    }

    public get alternativeDishQuantity(): number {
        return this._alternativeDishQuantity;
    }

    public set alternativeDishQuantity(alternativeDishQuantity: number) {
        this._alternativeDishQuantity = alternativeDishQuantity;
    }

    public get dessertName(): string {
        return this._dessertName;
    }

    public set dessertName(dessertName: string) {
        this._dessertName = dessertName;
    }

    public get dessertQuantity(): number {
        return this._dessertQuantity;
    }

    public set dessertQuantity(dessertQuantity: number) {
        this._dessertQuantity = dessertQuantity;
    }

    public get others(): OtherFoodDeliveryView[] {
        return this._others;
    }

    public set others(others: OtherFoodDeliveryView[]) {
        this._others = others;
    }
}

export class OtherFoodDeliveryView {

    public static fromJson(json: Object): OtherFoodDeliveryView {
        return new OtherFoodDeliveryView(json['name'], json['quantity']);
    }

    constructor(private _name: string,
        private _quantity: number) {}

    public get description(): string {
        return this.quantity + " " + this.name;
    }

    public get name(): string {
        return this._name;
    }

    public set name(name: string) {
        this._name = name;
    }

    public get quantity(): number {
        return this._quantity;
    }

    public set quantity(quantity: number) {
        this._quantity = quantity;
    }
}