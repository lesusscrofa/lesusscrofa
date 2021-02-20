import { DateUtils } from "../utils/date-utils";
import { Formula } from "./formula";

export class FormulaPrice {

    public static fromJson(json: Object) {
        return new FormulaPrice(json['id'],
                                new Date(json['start']),
                                json['formula'],
                                json['price'],
                                json['vat']);
    }

    constructor(private _id: number,
        private _start: Date,
        private _formula: Formula,
        private _price: number,
        private _vat: number) {

    }

    public toJson() {
        return {
            id: this.id,
            start: DateUtils.formatToIsoDate(this.start),
            formula: this.formula,
            price: this._price,
            vat: this._vat
        }
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

    public get formula(): Formula {
        return this._formula;
    }

    public set formula(formula: Formula) {
        this._formula = formula;
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
}