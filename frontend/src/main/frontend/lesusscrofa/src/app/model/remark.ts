import { DateUtils } from "../utils/date-utils";

export class Remark {

    public static fromJson(json: Object) {
        var day = json['day'];
        return new Remark(json['id'],
                        json['clientId'],
                        day ? new Date(json['day']) : null,
                        json['message']);
    }

    constructor(private _id: number, 
                private _clientId: number,
                private _day: Date,
                private _message: string) {}

    public toJson(): Object {
        return {
            id: this._id,
            clientId: this.clientId,
            day: this.day ? DateUtils.formatToIsoDate(this._day) : null,
            message: this._message
        }
    }

    get id(): number {
        return this._id;
    }

    set id(id: number) {
        this._id = id;
    }
    
    get clientId(): number {
        return this._clientId;
    }

    set clientId(clientId: number) {
        this._clientId = clientId;
    }

    get day(): Date {
        return this._day;
    }

    set day(day: Date) {
        this._day = day;
    }

    get message(): string {
        return this._message;
    }

    set message(message: string) {
        this._message = message;
    }
}