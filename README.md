# 🏨 Hotel Reservation Application

A Java console-based hotel reservation system built as part of the Udacity Java Programming Nanodegree.

## 📋 Description

This application allows customers to browse available rooms, make reservations, and manage their bookings through a command-line interface. Admins can manage rooms, view all customers, and display all reservations.

## Project Structure

```
src/
├── api/
│   ├── HotelResource.java        # Customer-facing API
│   └── AdminResource.java        # Admin-facing API
├── model/
│   ├── Customer.java             # Customer model with email validation
│   ├── IRoom.java                # Room interface
│   ├── Room.java                 # Room implementation
│   ├── FreeRoom.java             # Free room (extends Room)
│   ├── RoomType.java             # Enum: SINGLE, DOUBLE
│   └── Reservation.java          # Reservation model
├── service/
│   ├── CustomerService.java      # Customer business logic (Singleton)
│   └── ReservationService.java   # Reservation business logic (Singleton)
├── AdminMenu.java                # Admin console menu
├── MainMenu.java                 # Main console menu (entry point)
└── Driver.java                   # Test driver class
```

## Features

### Customer Menu
- Find and reserve available rooms by date
- View personal reservations
- Create a new account

### Admin Menu
- View all customers
- View all rooms
- View all reservations
- Add new rooms

## How to Run

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/Hotel.git
   ```
2. Open the project in **IntelliJ IDEA**
3. Run `MainMenu.java` as the entry point

## Technologies Used

- **Java** (JDK 17+)
- **IntelliJ IDEA**
- **Collections Framework** (ArrayList, HashMap)
- **Scanner** for console input
- **Regex** for email validation
- **Singleton Pattern** for service classes

## Design Patterns

- **Singleton** — `CustomerService`, `ReservationService`, `HotelResource`, `AdminResource`
- **Interface** — `IRoom` implemented by `Room` and `FreeRoom`
- **Inheritance** — `FreeRoom` extends `Room`

## Concepts Demonstrated

- Object-Oriented Programming (OOP)
- Interfaces and Abstract classes
- Enumerations
- Exception handling (`IllegalArgumentException`)
- Java Collections
- Static references and Singleton pattern

## Author

Michail Lianeris
