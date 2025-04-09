package model;

import service.BookingService;

public class GuestUser implements User {
	private String name;

	public GuestUser(String name) {
		this.name = name;
	}

	@Override
	public void displayUserType() {
		System.out.println(" Hello " + name + "! You are booking as a Guest.");
	}

	@Override
	public void bookTicket(BookingService bookingService) {
		System.out.println("Guest Booking:");
		bookingService.chooseMovie();
		bookingService.chooseSeats();
		System.out.println("Note: Promo codes are not available for guests.");
	}

	public String getName() {
		return name;

	}
}
