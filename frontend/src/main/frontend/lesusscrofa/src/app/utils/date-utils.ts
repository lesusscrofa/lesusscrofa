import { DatePipe } from '@angular/common';

export class DateUtils {

    private static datePipe = new DatePipe("en-US");

    public static formatToIsoDate(date: Date) : string {
        return this.datePipe.transform(date, "yyyy-MM-dd");
    }

    public static isDatesSameDay(d1: Date, d2: Date) {
        d1 = DateUtils.truncateMinutesSeconds(d1);
        d2 = DateUtils.truncateMinutesSeconds(d2);

        return d1.getFullYear() === d2.getFullYear()
            && d1.getMonth() === d2.getMonth()
            && d1.getUTCDate() === d2.getUTCDate()
    }

    public static addDays(date: Date, daysToAdd: number): Date {
        return new Date(date.getTime() + daysToAdd * 24*60*60*1000);
    }

    private static truncateMinutesSeconds(date: Date): Date {
        date = new Date(date);

        date.setHours(1);
        date.setMinutes(0);
        date.setSeconds(0);
        date.setMilliseconds(0);

        return date;
    }
}