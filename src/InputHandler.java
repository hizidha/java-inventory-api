import item.model.Item;
import item.repository.ItemRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class InputHandler {
    private final ItemRepository itemRepository;
    private final Scanner scanner;

    public InputHandler() throws SQLException {
        Connection connection = DB.connect();
        this.itemRepository = new ItemRepository(connection);
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        boolean running = true;

        while (running) {
            printMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    viewAllItems();
                    break;
                case 2:
                    handleAddItem();
                    break;
                case 3:
                    handleFindItemById();
                    break;
                case 4:
                    handleUpdateItem();
                    break;
                case 5:
                    handleDeleteItem();
                    break;
                default:
                    running = false;
                    System.out.println("Exiting...");
                    break;
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("------------------");
        System.out.println("Choose an option:");
        System.out.println("1. View All Items");
        System.out.println("2. Add New Item");
        System.out.println("3. Find Item by ID");
        System.out.println("4. Update Item");
        System.out.println("5. Delete Item");
        System.out.println("------------------");
    }

    private void viewAllItems() {
        List<Item> items = itemRepository.getAll();
        items.forEach(System.out::println);
    }

    private void handleAddItem() {
        String name = getStringInput("Item name: ");
        int quantity = getIntInput("Item quantity: ");

        Item item = new Item(name, quantity);
        int id = itemRepository.add(item);
        printResult(id, "Item added with ID:", "Failed to add item.");
    }

    private void handleFindItemById() {
        int id = getIntInput("Enter item ID: ");
        Optional<Item> item = itemRepository.findItemby(id);
        printResult(item);
    }

    private void handleUpdateItem() {
        int id = getIntInput("Item ID to updateQuantity: ");
        String name = getStringInput("New item name: ");
        int quantity = getIntInput("New item quantity: ");

        Item item = new Item(id, name, quantity);
        int result = itemRepository.update(item);
        printResult(result, "Item updated successfully.", "Failed to updateQuantity item.");
    }

    private void handleDeleteItem() {
        int id = getIntInput("Enter item ID to delete: ");
        int result = itemRepository.delete(id);
        printResult(result, "Item deleted successfully.", "Failed to delete item.");
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private void printResult(int result, String successMessage, String failureMessage) {
        if (result > 0) {
            System.out.println(successMessage + " (" + result + ")");
        }

        if (result < 0) {
            System.out.println(failureMessage);
        }
    }

    private void printResult(Optional<Item> item) {
        item.ifPresentOrElse(
            foundItem -> System.out.println("Item found: " + foundItem),
            () -> System.out.println("Item not found.")
        );
    }
}