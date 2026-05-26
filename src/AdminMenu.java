import api.AdminResource;
import model.FreeRoom;
import model.IRoom;
import model.Room;
import model.RoomType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class AdminMenu {

    private static final AdminResource adminResource = AdminResource.getInstance();
    private static final Scanner scanner = new Scanner(System.in);

    public static void open() {
        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> seeAllCustomers();
                case "2" -> seeAllRooms();
                case "3" -> seeAllReservations();
                case "4" -> addARoom();
                case "5" -> {
                    System.out.println("Returning to Main Menu...");
                    running = false;
                }
                default -> System.out.println("Invalid option. Please enter 1-5.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n===== Admin Menu =====");
        System.out.println("1. See all Customers");
        System.out.println("2. See all Rooms");
        System.out.println("3. See all Reservations");
        System.out.println("4. Add a Room");
        System.out.println("5. Back to Main Menu");
        System.out.print("Select an option: ");
    }

    private static void seeAllCustomers() {
        Collection<?> customers = adminResource.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("No customers found.");
        } else {
            customers.forEach(System.out::println);
        }
    }

    private static void seeAllRooms() {
        Collection<IRoom> rooms = adminResource.getAllRooms();
        if (rooms.isEmpty()) {
            System.out.println("No rooms found.");
        } else {
            rooms.forEach(System.out::println);
        }
    }

    private static void seeAllReservations() {
        adminResource.displayAllReservations();
    }

    private static void addARoom() {
        List<IRoom> newRooms = new ArrayList<>();
        boolean addMore = true;

        while (addMore) {

            String roomNumber;
            while (true) {
                System.out.print("Enter room number: ");
                roomNumber = scanner.nextLine().trim();
                if (roomNumber.isEmpty()) {
                    System.out.println("Room number cannot be empty. Please try again.");
                } else if (adminResource.getRoom(roomNumber) != null) {
                    System.out.println("Room " + roomNumber + " already exists. Please enter a different number.");
                } else {
                    break;
                }
            }


            double price;
            while (true) {
                try {
                    System.out.print("Enter room price (0 for free): ");
                    String priceInput = scanner.nextLine().trim();
                    if (priceInput.isEmpty() || priceInput.equalsIgnoreCase("null")) {
                        System.out.println("Price cannot be empty or null. Please enter a valid number.");
                        continue;
                    }
                    price = Double.parseDouble(priceInput);
                    if (price < 0) {
                        System.out.println("Price cannot be negative. Please enter 0 or a positive number.");
                        continue;
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid price. Please enter a numeric value (e.g., 99.99).");
                }
            }


            RoomType roomType;
            while (true) {
                System.out.print("Enter room type (1 = Single, 2 = Double): ");
                String typeInput = scanner.nextLine().trim();
                if (typeInput.equals("1")) {
                    roomType = RoomType.SINGLE;
                    break;
                } else if (typeInput.equals("2")) {
                    roomType = RoomType.DOUBLE;
                    break;
                } else {
                    System.out.println("Invalid room type. Please enter 1 for Single or 2 for Double.");
                }
            }

            IRoom room = (price == 0)
                    ? new FreeRoom(roomNumber, roomType)
                    : new Room(roomNumber, price, roomType);
            newRooms.add(room);
            System.out.println("Room added: " + room);

            System.out.print("Add another room? (yes/no): ");
            addMore = scanner.nextLine().trim().equalsIgnoreCase("yes");
        }

        adminResource.addRoom(newRooms);
        System.out.println("All rooms saved successfully!");
    }
}