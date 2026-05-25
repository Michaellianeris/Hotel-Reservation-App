import api.HotelResource;
import model.IRoom;
import model.Reservation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

public class MainMenu {

    private static final HotelResource hotelResource = HotelResource.getInstance();
    private static final Scanner scanner = new Scanner(System.in);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> findAndReserveRoom();
                case "2" -> seeMyReservations();
                case "3" -> createAccount();
                case "4" -> AdminMenu.open();
                case "5" -> {
                    System.out.println("Exiting. Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid option. Please enter 1-5.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n===== Main Menu =====");
        System.out.println("1. Find and reserve a room");
        System.out.println("2. See my reservations");
        System.out.println("3. Create an account");
        System.out.println("4. Admin");
        System.out.println("5. Exit");
        System.out.print("Select an option: ");
    }

    private static void findAndReserveRoom() {
        try {
            System.out.print("Enter check-in date (MM/dd/yyyy): ");
            Date checkIn = dateFormat.parse(scanner.nextLine().trim());

            System.out.print("Enter check-out date (MM/dd/yyyy): ");
            Date checkOut = dateFormat.parse(scanner.nextLine().trim());

            Collection<IRoom> availableRooms = hotelResource.findARoom(checkIn, checkOut);

            if (availableRooms.isEmpty()) {
                System.out.println("No rooms available for those dates.");
                return;
            }

            System.out.println("Available rooms:");
            availableRooms.forEach(System.out::println);

            System.out.print("Would you like to book a room? (yes/no): ");
            if (!scanner.nextLine().trim().equalsIgnoreCase("yes")) return;

            System.out.print("Do you have an account? (yes/no): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("no")) {
                createAccount();
            }

            System.out.print("Enter your email: ");
            String email = scanner.nextLine().trim();

            System.out.print("Enter room number: ");
            String roomNumber = scanner.nextLine().trim();
            IRoom room = hotelResource.getRoom(roomNumber);

            if (room == null) {
                System.out.println("Room not found.");
                return;
            }

            Reservation reservation = hotelResource.bookARoom(email, room, checkIn, checkOut);
            System.out.println("Reservation created: " + reservation);

        } catch (ParseException e) {
            System.out.println("Invalid date format. Use MM/dd/yyyy.");
        }
    }

    private static void seeMyReservations() {
        System.out.print("Enter your email: ");
        String email = scanner.nextLine().trim();
        Collection<Reservation> reservations = hotelResource.getCustomersReservations(email);
        if (reservations == null || reservations.isEmpty()) {
            System.out.println("No reservations found.");
        } else {
            reservations.forEach(System.out::println);
        }
    }

    private static void createAccount() {
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine().trim();
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine().trim();
        System.out.print("Enter email (name@domain.com): ");
        String email = scanner.nextLine().trim();
        try {
            hotelResource.createACustomer(email, firstName, lastName);
            System.out.println("Account created successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}