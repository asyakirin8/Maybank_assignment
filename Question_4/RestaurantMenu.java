import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RestaurantMenu {
    private List<MenuItem> menuItems;

    public RestaurantMenu() {
        this.menuItems = new ArrayList<>();
    }

    public void addItem(MenuItem item) {
        menuItems.add(item);
    }

    public void sortByCategory() {
        Collections.sort(menuItems, Comparator.comparing(MenuItem::getCategory)
                                              .thenComparing(MenuItem::getName));
    }

    public void sortByName() {
        Collections.sort(menuItems, Comparator.comparing(MenuItem::getName));
    }

    public void sortByPrice() {
        Collections.sort(menuItems, Comparator.comparingDouble(MenuItem::getPrice));
    }

    public void displayMenu() {
        for (MenuItem item : menuItems) {
            System.out.println(item);
        }
    }

    public static void main(String[] args) {
        RestaurantMenu menu = new RestaurantMenu();

        // Adding items to the menu
        menu.addItem(new MenuItem("Burger", "Main Course", 5));
        menu.addItem(new MenuItem("Fries", "Side Dish", 3));
        menu.addItem(new MenuItem("Nasi Lemak Ayam", "Main Course", 10));
        menu.addItem(new MenuItem("Teh Ais", "Beverage", 2));
        menu.addItem(new MenuItem("Salad", "Side Dish", 5));

        // Sort by category and display
        System.out.println("Menu sorted by Category:");
        menu.sortByCategory();
        menu.displayMenu();

        // Sort by name and display
        System.out.println("\nMenu sorted by Name:");
        menu.sortByName();
        menu.displayMenu();

        // Sort by price and display
        System.out.println("\nMenu sorted by Price:");
        menu.sortByPrice();
        menu.displayMenu();
    }
}
