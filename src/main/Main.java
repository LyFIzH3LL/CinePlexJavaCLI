package main;

import model.*;
import service.*;

import java.util.Scanner;
import java.io.*;
import java.util.*;

public class Main {
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "password123";
    private static final String USER_FILE = "registered_users.txt";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BookingService booking = new BookingService();

        while (true) {
            System.out.println("\nWelcome to Cineplex Booking System!");
            System.out.print("Are you an admin? (yes/no): ");
            String isAdmin = sc.nextLine().trim().toLowerCase();

            if (isAdmin.equals("yes")) {
                if (adminLogin(sc)) {
                    adminMenu(sc, booking);
                    continue;
                } else {
                    System.out.println("Exiting due to failed login.");
                    return;
                }
            }

            String userType;
            while (true) {
                System.out.print("Are you a registered user? (yes/no): ");
                userType = sc.nextLine().trim().toLowerCase();
                if (userType.equals("yes") || userType.equals("no")) {
                    break;
                } else {
                    System.out.println("Invalid input. Please type 'yes' or 'no'.");
                }
            }

            User user = null;
            if (userType.equals("yes")) {
                System.out.print("Enter your name: ");
                String name = sc.nextLine();

                if (isUserRegistered(name)) {
                    System.out.print("Enter your email: ");
                    String email = sc.nextLine();
                    user = new RegisteredUser(name, email);
                } else {
                    System.out.println("You are not a registered user.");
                    continue;
                }
            } else {
                System.out.print("Enter your name: ");
                String name = sc.nextLine();
                user = new GuestUser(name);
            }

            user.displayUserType();

            booking.chooseMovie();
            booking.chooseSeats();

            if (user instanceof RegisteredUser) {
                PromoService promoService = new PromoService();
                System.out.print("Enter promo code (or press enter to skip): ");
                String promo = sc.nextLine();
                booking.applyPromo(promoService, promo);
            } else {
                System.out.println("Promo codes are only available for registered users.");
            }

            String method;
            PaymentMethod payment = null;

            while (true) {
                System.out.print("Choose payment method (cash/card): ");
                method = sc.nextLine().trim().toLowerCase();

                if (method.equals("card")) {
                    payment = new CardPayment();
                    break;
                } else if (method.equals("cash")) {
                    payment = new CashPayment();
                    break;
                } else {
                    System.out.println("Invalid payment method. Please type 'cash' or 'card'.");
                }
            }

            payment.pay(booking.getDiscountedPrice());

            System.out.print("Processing your booking");
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.print(".");
            }

            System.out.println(" Booking confirmed. Enjoy your movie!");
            generateReceipt(user, booking);

            System.out.print("\nDo you want to return to the main menu? (yes/no): ");
            String again = sc.nextLine().trim().toLowerCase();
            if (!again.equals("yes")) {
                System.out.println("Thank you for using Cineplex Booking System!");
                break;
            }
        }
    }

    private static boolean adminLogin(Scanner sc) {
        System.out.println("\nAdmin Login");
        System.out.print("Enter username: ");
        String username = sc.nextLine();

        System.out.print("Enter password: ");
        String password = sc.nextLine();

        if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
            System.out.println("Login successful. Welcome, Admin!");
            return true;
        } else {
            System.out.println("Login failed.");
            return false;
        }
    }

    private static void adminMenu(Scanner sc, BookingService booking) {
        while (true) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Add a registered user");
            System.out.println("2. View registered users");
            System.out.println("3. Edit a registered user");
            System.out.println("4. Delete a registered user");
            System.out.println("5. Add a new movie");
            System.out.println("6. Exit");
            System.out.print("Select an option: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    addUser(sc);
                    break;
                case 2:
                    viewUsers();
                    break;
                case 3:
                    editUser(sc);
                    break;
                case 4:
                    deleteUser(sc);
                    break;
                case 5:
                    booking.addMovieAsAdmin();
                    break;
                case 6:
                    System.out.println("Returning to main menu...");
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void addUser(Scanner sc) {
        System.out.print("Enter name of new user: ");
        String name = sc.nextLine();
        System.out.print("Enter email of new user: ");
        String email = sc.nextLine();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE, true))) {
            writer.write(name + "," + email + "\n");
            System.out.println("User added successfully.");
        } catch (IOException e) {
            System.out.println("Error adding user: " + e.getMessage());
        }
    }

    private static void viewUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] user = line.split(",");
                System.out.println("Name: " + user[0] + ", Email: " + user[1]);
            }
        } catch (IOException e) {
            System.out.println("Error reading users: " + e.getMessage());
        }
    }

    private static void editUser(Scanner sc) {
        System.out.print("Enter the name of the user to edit: ");
        String name = sc.nextLine();
        File inputFile = new File(USER_FILE);
        File tempFile = new File("temp_users.txt");

        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] user = line.split(",");
                if (user[0].equalsIgnoreCase(name)) {
                    System.out.print("Enter new email: ");
                    String newEmail = sc.nextLine();
                    writer.write(user[0] + "," + newEmail + "\n");
                    found = true;
                } else {
                    writer.write(line + "\n");
                }
            }

            if (!found) {
                System.out.println("User not found.");
            } else {
                inputFile.delete();
                tempFile.renameTo(inputFile);
                System.out.println("User details updated.");
            }
        } catch (IOException e) {
            System.out.println("Error editing user: " + e.getMessage());
        }
    }

    private static void deleteUser(Scanner sc) {
        System.out.print("Enter the name of the user to delete: ");
        String name = sc.nextLine();
        File inputFile = new File(USER_FILE);
        File tempFile = new File("temp_users.txt");

        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] user = line.split(",");
                if (user[0].equalsIgnoreCase(name)) {
                    found = true;
                    System.out.println("User deleted.");
                } else {
                    writer.write(line + "\n");
                }
            }

            if (!found) {
                System.out.println("User not found.");
            } else {
                inputFile.delete();
                tempFile.renameTo(inputFile);
            }
        } catch (IOException e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
    }

    private static boolean isUserRegistered(String name) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] user = line.split(",");
                if (user[0].equalsIgnoreCase(name)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error checking user registration: " + e.getMessage());
        }
        return false;
    }

    private static void generateReceipt(User user, BookingService booking) {
        String fileName = "receipt_"
                + (user instanceof RegisteredUser ? ((RegisteredUser) user).getName() : ((GuestUser) user).getName())
                + ".txt";

        File receiptFile = new File(fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(receiptFile))) {
            writer.write("Cineplex Booking Receipt\n");
            writer.write("--------------------------------------------------\n");

            if (user instanceof RegisteredUser) {
                RegisteredUser registeredUser = (RegisteredUser) user;
                writer.write("Name: " + registeredUser.getName() + "\n");
                writer.write("Email: " + registeredUser.getEmail() + "\n");
            } else {
                GuestUser guestUser = (GuestUser) user;
                writer.write("Name: " + guestUser.getName() + "\n");
            }

            writer.write("Movie: " + booking.getSelectedMovie() + "\n");
            writer.write("Time: " + booking.getSelectedTime() + "\n");
            writer.write("Seats Booked: " + booking.getSeatsToBook() + "\n");
            writer.write("Total Price: $" + booking.getDiscountedPrice() + "\n");

            writer.write("--------------------------------------------------\n");
            writer.write("Thank you for booking with Cineplex! Enjoy your movie!");

            System.out.println("Receipt generated: " + fileName);

        } catch (IOException e) {
            System.out.println("Error generating receipt: " + e.getMessage());
        }
    }
}
