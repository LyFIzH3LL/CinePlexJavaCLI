package model;

import service.BookingService;
import service.PromoService;

import java.util.Scanner;

public class RegisteredUser implements User {
	private String name;
	private String email;
	private final Scanner sc = new Scanner(System.in);

	public RegisteredUser(String name, String email) {
		this.name = name;
		this.email = email;
	}

	@Override
	public void displayUserType() {
		System.out.println("Welcome back " + name + " (" + email + ")! You are a Registered User.");
	}

	@Override
	public void bookTicket(BookingService bookingService) {
		System.out.println("Registered Booking:");
		bookingService.chooseMovie();
		bookingService.chooseSeats();

		System.out.print("Enter promo code (if any): ");
		String promoCode = sc.nextLine();
		bookingService.applyPromo(new PromoService(), promoCode);
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}
}
