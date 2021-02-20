export enum Formula {
    ACTIF = "ACTIF",
    SOUP = "SOUP",
    DISH = "DISH",
    ALTERNATIVE_DISH = "ALTERNATIVE_DISH",
    DESSERT = "DESSERT",
    OTHER = "OTHER",
    SOUP_DISH = "SOUP_DISH",
    DISH_DESSERT = "DISH_DESSERT",
    MENU = "MENU",
    SOUP_ALTERNATIVE_DISH = "SOUP_ALTERNATIVE_DISH",
    ALTERNATIVE_DISH_DESSERT = "ALTERNATIVE_DISH_DESSERT",
    ALTERNATIVE_MENU = "ALTERNATIVE_MENU"
}

export function getFormulaName(formula: Formula): string {
    if(!formula) {
        return null;
    }

    switch(formula) {
        case Formula.ACTIF: 
            return "Actives";
        case Formula.SOUP: 
            return "Soupe";
        case Formula.DISH: 
            return "Plat";
        case Formula.ALTERNATIVE_DISH: 
            return "Plat alternatif";
        case Formula.DESSERT: 
            return "Dessert";
        case Formula.OTHER: 
            return "Autre";
        case Formula.SOUP_DISH: 
            return "Soupe/plat";
        case Formula.DISH_DESSERT: 
            return "Plat/dessert";
        case Formula.MENU: 
            return "Menu";
        case Formula.SOUP_ALTERNATIVE_DISH: 
            return "Soupe/plat alternatif";
        case Formula.ALTERNATIVE_DISH_DESSERT: 
            return "Plat alternatif/dessert";
        case Formula.ALTERNATIVE_MENU: 
            return "Menu avec plat alternatif";
        default:
            throw new Error("unknown formula " +  formula);
    }
}