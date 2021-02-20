import { FoodService } from '../service/food.service';
import { Client } from './client';
import { Food } from './food';
import { Formula } from './formula';
import { Menu } from './menu';
import { Order } from './order';

export class MenuOrder {
    
    constructor(
        private _day: Date,
        private _delivery: boolean,
        private _menu: Menu,
        private _client: Client,
        private _soup: Food,
        private _soupQuantity: number,
        private _dish: Food,
        private _dishQuantity: number,
        private _alternativeDish: Food,
        private _alternativeDishQuantity: number,
        private _dessert: Food,
        private _dessertQuantity: number,
        private _orders: Map<Formula, Order>,
        private foodService: FoodService
    ) { }

    public static compareToByDate(m1: MenuOrder, m2: MenuOrder): number {
		var day1 = m1.day;
		var day2 = m2.day;

		if(day1 > day2) {
			return 1;
		}
		else if(day1 < day2) {
			return -1;
		}
		else {
			return 0;
		}
	}

    public static fromOrders(client: Client, menu: Menu, orders: Order[], foodService: FoodService): MenuOrder {
        var ordersMap = Order.createMealTypeMapFromOrders(orders.filter(order => order.formula != Formula.OTHER));
        
        var delivery = orders.length > 0 ? orders[0].delivery : !client.deliveryPreferenceTakeAway;
        
        var menuOrder = new MenuOrder(
            menu.start, delivery, menu, client,
            menu.soup, 0, menu.dish, 0, menu.alternativeDish, 0, menu.dessert, 0, 
            ordersMap, foodService);

        orders.forEach(o => MenuOrder.addOrderToMenuOrder(o, menuOrder));
        
        return menuOrder;
    }

    private static addOrderToMenuOrder(order: Order, menuOrder: MenuOrder) {
        if(Formula.MENU == order.formula) {
            MenuOrder.addQuantityToMenuOrder(menuOrder, order.quantity, order.quantity, 0, order.quantity);
        }
        else if(Formula.ALTERNATIVE_MENU == order.formula) {
            MenuOrder.addQuantityToMenuOrder(menuOrder, order.quantity, 0, order.quantity, order.quantity);
        }
        else if(Formula.DISH_DESSERT == order.formula) {
            MenuOrder.addQuantityToMenuOrder(menuOrder, 0, order.quantity, 0, order.quantity);
        }
        else if(Formula.ALTERNATIVE_DISH_DESSERT == order.formula) {
            MenuOrder.addQuantityToMenuOrder(menuOrder, 0, 0, order.quantity, order.quantity);
        }
        else if(Formula.SOUP_DISH == order.formula) {
            MenuOrder.addQuantityToMenuOrder(menuOrder, order.quantity, order.quantity, 0, 0);
        }
        else if(Formula.SOUP_ALTERNATIVE_DISH == order.formula) {
            MenuOrder.addQuantityToMenuOrder(menuOrder, order.quantity, 0, order.quantity, 0);
        }
        else if(Formula.SOUP == order.formula) {
            MenuOrder.addQuantityToMenuOrder(menuOrder, order.quantity, 0, 0, 0);
        }
        else if(Formula.DISH == order.formula) {
            MenuOrder.addQuantityToMenuOrder(menuOrder, 0, order.quantity, 0, 0);
        }
        else if(Formula.ALTERNATIVE_DISH == order.formula) {
            MenuOrder.addQuantityToMenuOrder(menuOrder, 0, 0, order.quantity, 0);
        }
        else if(Formula.DESSERT == order.formula) {
            MenuOrder.addQuantityToMenuOrder(menuOrder, 0, 0, 0, order.quantity);
        }
    }

    private static addQuantityToMenuOrder (
        menuOrder: MenuOrder, soupQuantity: number, dishQuantity: number, alternativeDishQuantity: number, dessertQuantity: number) {
            menuOrder.soupQuantity += soupQuantity;
            menuOrder.dishQuantity += dishQuantity;
            menuOrder.alternativeDishQuantity += alternativeDishQuantity;
            menuOrder.dessertQuantity += dessertQuantity;
    }

    public toOrders(): Order[] {
        var soupQuantity = this.soupQuantity;
        var dishQuantity = this.dishQuantity;
        var alternativeDishQuantity = this.alternativeDishQuantity;
        var dessertQuantity = this.dessertQuantity;

        var menuQuantity = Math.min(soupQuantity, dishQuantity, dessertQuantity);
        this.getOrCreateOrder(this.orders, Formula.MENU, menuQuantity);
        soupQuantity -= menuQuantity;
        dishQuantity -= menuQuantity;
        dessertQuantity -= menuQuantity;

        var alternativeMenuQuantity = Math.min(soupQuantity, alternativeDishQuantity, dessertQuantity);
        this.getOrCreateOrder(this.orders, Formula.ALTERNATIVE_MENU, alternativeMenuQuantity);
        soupQuantity -= alternativeMenuQuantity;
        alternativeDishQuantity -= alternativeMenuQuantity;
        dessertQuantity -= alternativeMenuQuantity;

        var soupDishQuantity = Math.min(soupQuantity, dishQuantity);
        this.getOrCreateOrder(this.orders, Formula.SOUP_DISH, soupDishQuantity);
        soupQuantity -= soupDishQuantity;
        dishQuantity -= soupDishQuantity;

        var alternativeSoupDishQuantity = Math.min(soupQuantity, alternativeDishQuantity);
        this.getOrCreateOrder(this.orders, Formula.SOUP_ALTERNATIVE_DISH, alternativeSoupDishQuantity);
        soupQuantity -= alternativeSoupDishQuantity;
        alternativeDishQuantity -= alternativeSoupDishQuantity;

        var dishDessertQuantity = Math.min(dishQuantity, dessertQuantity);
        this.getOrCreateOrder(this.orders, Formula.DISH_DESSERT, dishDessertQuantity);
        dishQuantity -= dishDessertQuantity;
        dessertQuantity -= dishDessertQuantity;

        var alternativeDishDessertQuantity = Math.min(alternativeDishQuantity, dessertQuantity);
        this.getOrCreateOrder(this.orders, Formula.ALTERNATIVE_DISH_DESSERT, alternativeDishDessertQuantity);
        alternativeDishQuantity -= alternativeDishDessertQuantity;
        dessertQuantity -= alternativeDishDessertQuantity;

        this.getOrCreateOrder(this.orders, Formula.SOUP, soupQuantity);

        this.getOrCreateOrder(this.orders, Formula.DISH, dishQuantity);

        this.getOrCreateOrder(this.orders, Formula.ALTERNATIVE_DISH, alternativeDishQuantity);

        this.getOrCreateOrder(this.orders, Formula.DESSERT, dessertQuantity);
        
        return Array.from(this.orders.values());
    }

    private getOrCreateOrder(orders: Map<Formula, Order>, formula: Formula, quantity: number): Order {
        var order = orders.get(formula);

        if(!order && quantity > 0) {
            order = this.createOrder(formula);
            orders.set(formula, order);
        }

        if(order) {
            order.quantity = quantity;
            order.delivery = this.delivery;
        }

        return order;
    }

    private createOrder(type: Formula) {
        var soup = null;
        var dish = null;
        var dessert = null;

        switch(type) {
            case Formula.MENU:
                soup = this.soup;
                dish = this.dish;
                dessert = this.dessert;
                break;
            case Formula.ALTERNATIVE_MENU:
                soup = this.soup;
                dish = this.alternativeDish;
                dessert = this.dessert;
                break;
            case Formula.SOUP_DISH:
                soup = this.soup;
                dish = this.dish;
                break;
            case Formula.SOUP_ALTERNATIVE_DISH:
                soup = this.soup;
                dish = this.alternativeDish;
                break;
            case Formula.DISH_DESSERT:
                dish = this.dish;
                dessert = this.dessert;
                break;
            case Formula.ALTERNATIVE_DISH_DESSERT:
                dish = this.alternativeDish;
                dessert = this.dessert;
                break;
            case Formula.SOUP:
                soup = this.soup;
                break;
            case Formula.DISH:
                dish = this.dish;
                break;
            case Formula.ALTERNATIVE_DISH:
                dish = this.alternativeDish;
                break;
            case Formula.DESSERT:
                dessert = this.dessert;
                break;
        }

        return new Order(null, this.client.id, this.delivery, this.day,
            soup ? soup.id : null, soup ? this.foodService.get(soup.id) : null,
            dish ? dish.id : null, dish ? this.foodService.get(dish.id) : null,
            dessert ? dessert.id : null, dessert ? this.foodService.get(dessert.id) : null,
            null, null,
            type, 0, 0, null);
    }

    createOtherFoodOrder(foodId: number, quantity: number): Order {
        return new Order(null, this.client.id, this.delivery, this.day, 
            null, null, null, null, null, null,
            foodId, this.foodService.get(foodId), Formula.OTHER, quantity, 0, null);
    }

    get day(): Date {
        return this._day;
    }

    set day(day: Date) {
        this._day = day;
    }

    get delivery(): boolean {
        return this._delivery;
    }

    set delivery(delivery: boolean) {
        this._delivery = delivery;
    }

    get menu(): Menu {
        return this._menu;
    }

    set menu(menu: Menu) {
        this._menu = menu;
    }

    get client(): Client {
        return this._client;
    }

    set client(client: Client) {
        this._client = client;
    }

    get soup(): Food {
        return this._soup;
    }

    set soup(soup: Food) {
        this._soup = soup;
    }

    get soupQuantity(): number {
        return this._soupQuantity;
    }

    set soupQuantity(soupQuantity: number) {
        this._soupQuantity = soupQuantity;
    }

    get dish(): Food {
        return this._dish;
    }

    set dish(dish: Food) {
        this._dish = dish;
    }

    get dishQuantity(): number {
        return this._dishQuantity;
    }

    set dishQuantity(dishQuantity: number) {
        this._dishQuantity = dishQuantity;
    }

    get alternativeDish(): Food {
        return this._alternativeDish;
    }

    get alternativeDishQuantity(): number {
        return this._alternativeDishQuantity;
    }

    set alternativeDishQuantity(alternativeDishQuantity: number) {
        this._alternativeDishQuantity = alternativeDishQuantity;
    }

    get dessert(): Food {
        return this._dessert;
    }

    set dessert(dessert: Food) {
        this._dessert = dessert;
    }

    get dessertQuantity(): number {
        return this._dessertQuantity;
    }

    set dessertQuantity(dessertQuantity: number) {
        this._dessertQuantity = dessertQuantity;
    }

    get orders(): Map<Formula, Order> {
        return this._orders;
    }

    set orders(orders: Map<Formula, Order>) {
        this._orders = orders;
    }
}