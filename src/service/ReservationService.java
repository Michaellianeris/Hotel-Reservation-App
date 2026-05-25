package service;

import model.Customer;
import model.IRoom;
import model.Reservation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class ReservationService {

    private static ReservationService instance;
    private final Collection<IRoom> rooms = new ArrayList<>();
    private final Collection<Reservation> reservations = new ArrayList<>();

    private ReservationService() {}

    public static ReservationService getInstance() {
        if (instance == null) {
            instance = new ReservationService();
        }
        return instance;
    }

    public void addRoom(IRoom room) {
        rooms.add(room);
    }

    public IRoom getARoom(String roomId) {
        for (IRoom room : rooms) {
            if (room.getRoomNumber().equals(roomId)) {
                return room;
            }
        }
        return null;
    }

    public Reservation reserveARoom(Customer customer, IRoom room,
                                    Date checkInDate, Date checkOutDate) {
        Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);
        reservations.add(reservation);
        return reservation;
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
        Collection<IRoom> availableRooms = new ArrayList<>();
        for (IRoom room : rooms) {
            if (isRoomAvailable(room, checkInDate, checkOutDate)) {
                availableRooms.add(room);
            }
        }
        return availableRooms;
    }

    private boolean isRoomAvailable(IRoom room, Date checkIn, Date checkOut) {
        for (Reservation res : reservations) {
            if (res.getRoom().getRoomNumber().equals(room.getRoomNumber())) {
                // Overlap check: new dates overlap if not (checkOut <= existing checkIn OR checkIn >= existing checkOut)
                if (!(checkOut.before(res.getCheckInDate()) || checkIn.after(res.getCheckOutDate()))) {
                    return false;
                }
            }
        }
        return true;
    }

    public Collection<Reservation> getCustomersReservation(Customer customer) {
        Collection<Reservation> customerReservations = new ArrayList<>();
        for (Reservation res : reservations) {
            if (res.getCustomer().equals(customer)) {
                customerReservations.add(res);
            }
        }
        return customerReservations;
    }

    public Collection<IRoom> getAllRooms() {
        return rooms;
    }

    public void printAllReservation() {
        for (Reservation res : reservations) {
            System.out.println(res);
        }
    }
}