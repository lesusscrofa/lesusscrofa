import { Formula } from './formula';

export class OrderSummary {

    public static fromJson(json: Object) {
        return new OrderSummary(json['description'],
                                json['formula'],
                                json['unitPrice'],
                                json['vat'],
                                json['quantity'],
                                json['reduction'],
                                json['unitPriceReductionIncluded'],
                                json['unitPriceVatIncluded'],
                                json['totalVatExcluded'],
                                json['totalVatIncluded'])
    }

    constructor(private _description: string,
                private _formula: Formula,
                private _unitPrice: number,
                private _vat: number,
                private _quantity: number,
                private _reduction: number,
                private _unitPriceReductionIncluded: number,
                private _unitPriceVatIncluded: number,
                private _totalVatExcluded: number,
                private _totalVatIncluded: number) {

    }

    public get description(): string {
        return this._description;
    }

    public get formula(): Formula {
        return this._formula;
    }

    public get unitPrice(): number {
        return this._unitPrice;
    }

    public get vat(): number {
        return this._vat;
    }

    public get quantity(): number {
        return this._quantity;
    }

    public get reduction(): number {
        return this._reduction;
    }

    public get unitPriceReductionIncluded(): number {
        return this._unitPriceReductionIncluded;
    }

    public get unitPriceVatIncluded(): number {
        return this._unitPriceVatIncluded;
    }

    public get totalVatExcluded(): number {
        return this._totalVatExcluded;
    }

    public get totalVatIncluded(): number {
        return this._totalVatIncluded;
    }
}