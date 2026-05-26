import api.HotelResource;
import model.IRoom;
import model.Reservation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

public class MainMenu {

    private static final HotelResource hotelResource = HotelResource.getInstance();
    private static final Scanner scanner = new Scanner(System.in);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    static {
        dateFormat.setLenient(false); // Rejects invalid dates like 20/01/2027
    }

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

    private static Date getValidDate(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                Date date = dateFormat.parse(input);

                // Reject past dates
                if (date.before(new Date())) {
                    System.out.println("Date cannot be in the past. Please enter a future date.");
                    continue;
                }
                return date;
            } catch (ParseException e) {
                System.out.println("Invalid date format or invalid date. Please use MM/dd/yyyy (e.g., 12/25/2026).");
            }
        }
    }

    private static void findAndReserveRoom() {
        Date checkIn = getValidDate("Enter check-in date (MM/dd/yyyy): ");

        Date checkOut;
        while (true) {
            checkOut = getValidDate("Enter check-out date (MM/dd/yyyy): ");
            if (checkOut.after(checkIn)) break;
            System.out.println("Check-out date must be after check-in date. Please try again.");
        }

        Collection<IRoom> availableRooms = hotelResource.findARoom(checkIn, checkOut);

        if (availableRooms.isEmpty()) {
            System.out.println("No rooms available for those dates.");


            Calendar cal = Calendar.getInstance();
            cal.setTime(checkIn);
            cal.add(Calendar.DAY_OF_MONTH, 7);
            Date newCheckIn = cal.getTime();

            cal.setTime(checkOut);
            cal.add(Calendar.DAY_OF_MONTH, 7);
            Date newCheckOut = cal.getTime();

            Collection<IRoom> recommendedRooms = hotelResource.findARoom(newCheckIn, newCheckOut);

            if (recommendedRooms.isEmpty()) {
                System.out.println("No rooms available for alternate dates either.");
            } else {
                System.out.println("\nWe recommend the following rooms for alternate dates:");
                System.out.println("Check-in: " + dateFormat.format(newCheckIn));
                System.out.println("Check-out: " + dateFormat.format(newCheckOut));
                recommendedRooms.forEach(System.out::println);
            }
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

        try {
            Reservation reservation = hotelResource.bookARoom(email, room, checkIn, checkOut);
            System.out.println("Reservation created: " + reservation);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
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