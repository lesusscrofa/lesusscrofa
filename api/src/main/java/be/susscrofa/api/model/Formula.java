package be.susscrofa.api.model;

public enum Formula {
    SOUP("Soupe"),
    DISH("Plat"),
    ALTERNATIVE_DISH("Plat alternatif"),
    DESSERT("Dessert"),
    OTHER("Autre"),
    SOUP_DISH("Soupe/plate"),
    DISH_DESSERT("Plat/dessert"),
    MENU("Menu"),
    SOUP_ALTERNATIVE_DISH("Soupe/plat alternatif"),
    ALTERNATIVE_DISH_DESSERT("Plat alternatif/dessert"),
    ALTERNATIVE_MENU("Menu avec plat alternatif");

    private String name;

    Formula(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
