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
            System.out.print("Enter room number: ");
            String roomNumber = scanner.nextLine().trim();

            System.out.print("Enter room price (0 for free): ");
            double price = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Enter room type (1 = Single, 2 = Double): ");
            String typeInput = scanner.nextLine().trim();
            RoomType roomType = typeInput.equals("1") ? RoomType.SINGLE : RoomType.DOUBLE;

            IRoom room = (price == 0)
                    ? new FreeRoom(roomNumber, roomType)
                    : new Room(roomNumber, price, roomType);
            newRooms.add(room);

            System.out.print("Add another room? (yes/no): ");
            addMore = scanner.nextLine().trim().equalsIgnoreCase("yes");
        }

        adminResource.addRoom(newRooms);
        System.out.println("Rooms added successfully!");
    }
}