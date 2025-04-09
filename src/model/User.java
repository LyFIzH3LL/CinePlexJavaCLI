package model;

// Interface for different types of users
public interface User {
	void displayUserType();

	void bookTicket(service.BookingService bookingService);
}
