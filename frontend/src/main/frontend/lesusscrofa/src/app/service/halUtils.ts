export class HalUtils {
    public static extractId(href: string): number {
        return parseInt(href.substring(href.lastIndexOf('/')+1));
    }
}