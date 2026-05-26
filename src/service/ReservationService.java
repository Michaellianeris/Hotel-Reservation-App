package service;

import model.Customer;
import model.IRoom;
import model.Reservation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservationService {

    private static ReservationService instance;
    private final Map<String, IRoom> rooms = new HashMap<>(); // prevents duplicate room numbers
    private final List<Reservation> reservations = new ArrayList<>();

    private ReservationService() {}

    public static ReservationService getInstance() {
        if (instance == null) {
            instance = new ReservationService();
        }
        return instance;
    }

    public void addRoom(IRoom room) {
        if (rooms.containsKey(room.getRoomNumber())) {
            System.out.println("Room " + room.getRoomNumber() + " already exists. Skipping.");
            return;
        }
        rooms.put(room.getRoomNumber(), room);
    }

    public IRoom getARoom(String roomId) {
        return rooms.get(roomId);
    }

    public Collection<IRoom> getAllRooms() {
        return rooms.values();
    }

    public Reservation reserveARoom(Customer customer, IRoom room,
                                    Date checkInDate, Date checkOutDate) {
        // Prevent double-booking
        for (Reservation res : reservations) {
            if (res.getRoom().getRoomNumber().equals(room.getRoomNumber())) {
                if (res.getCheckInDate().before(checkOutDate) &&
                        res.getCheckOutDate().after(checkInDate)) {
                    throw new IllegalArgumentException(
                            "Room " + room.getRoomNumber() + " is already booked for these dates.");
                }
            }
        }
        Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);
        reservations.add(reservation);
        return reservation;
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
        List<IRoom> availableRooms = new ArrayList<>(rooms.values());
        for (Reservation res : reservations) {
            if (res.getCheckInDate().before(checkOutDate) &&
                    res.getCheckOutDate().after(checkInDate)) {
                availableRooms.remove(res.getRoom());
            }
        }
        return availableRooms;
    }

    public Collection<Reservation> getCustomersReservation(Customer customer) {
        List<Reservation> customerReservations = new ArrayList<>();
        for (Reservation res : reservations) {
            if (res.getCustomer().equals(customer)) {
                customerReservations.add(res);
            }
        }
        return customerReservations;
    }

    public void printAllReservation() {
        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
        } else {
            reservations.forEach(System.out::println);
        }
    }
}