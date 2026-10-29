package ecommerce;

import ecommerce.service.Store;
import ecommerce.ui.ConsoleMenu;

/**
 * Entry point of the Console-Based E-Commerce System.
 * Author: Zait Bexultanov, group ISUUS-24-1
 */
public class Main {

    public static void main(String[] args) {
        Store store = new Store();
        store.loadSampleData();

        ConsoleMenu menu = new ConsoleMenu(store);
        try {
            menu.start();
        } catch (ConsoleMenu.InputClosedException e) {
            System.out.println();
            System.out.println("Input closed. Goodbye!");
        }
    }
}
