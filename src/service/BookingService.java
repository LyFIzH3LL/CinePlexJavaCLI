package service;

import java.util.*;

public class BookingService {
	private String selectedMovie;
	private String selectedTime;
	private int seatsToBook;
	private double pricePerSeat = 200.00;
	private double discount = 0.00;

	private final Scanner sc = new Scanner(System.in);

	private final Map<String, Map<String, Integer>> movieSchedule;

	public BookingService() {
		movieSchedule = new HashMap<>();

		movieSchedule.put("Borbaad", new HashMap<>(Map.of(
				"12:00 PM - 3.00 PM", 50,
				"03:00 PM - 06:00 PM", 50,
				"06:00 PM - 09:00 PM", 50)));
		movieSchedule.put("Daagi", new HashMap<>(Map.of(
				"01:00 PM - 04:00 PM", 50,
				"04:00 PM - 07:00 PM", 50,
				"07:00 PM - 10:00 PM", 50)));
		movieSchedule.put("Jinn 3", new HashMap<>(Map.of(
				"11:00 AM - 02:00 PM", 50,
				"02:00 PM - 05:00 PM", 50,
				"05:00 PM - 08:00 PM", 50)));
	}

	public void addMovieAsAdmin() {
		System.out.println("ADMIN: Add a New Movie");

		System.out.print("Enter Movie Name: ");
		String movieName = sc.nextLine();

		if (movieSchedule.containsKey(movieName)) {
			System.out.println("Movie Already Exists!!!");
			return;
		}

		Map<String, Integer> timeSlots = new HashMap<>();
		System.out.print("Enter number of time slots: ");
		int slotCount = sc.nextInt();
		sc.nextLine();

		for (int i = 1; i <= slotCount; i++) {
			System.out.print("Enter time slot #" + i + ": ");
			String time = sc.nextLine();

			System.out.print("Enter seat count for " + time + ": ");
			int seats = sc.nextInt();
			sc.nextLine();

			timeSlots.put(time, seats);
		}

		movieSchedule.put(movieName, timeSlots);
	}

	public void chooseMovie() {
		if (movieSchedule.isEmpty()) {
			System.out.println("No movies available to book :(");
			return;
		}

		System.out.println("Available Movies:");
		int index = 1;
		List<String> movieList = new ArrayList<>(movieSchedule.keySet());
		for (String movie : movieList) {
			System.out.println(index++ + ". " + movie);
		}

		System.out.print("Enter Movie Number: ");
		int movieChoice = getValidMovieChoice(movieList.size());

		if (movieChoice != -1) {
			selectedMovie = movieList.get(movieChoice - 1);
			chooseTimeSlot(selectedMovie);
		}
	}

	private int getValidMovieChoice(int size) {
		int movieChoice = -1;
		while (movieChoice < 1 || movieChoice > size) {
			try {
				movieChoice = Integer.parseInt(sc.nextLine());
				if (movieChoice < 1 || movieChoice > size) {
					System.out.println("Invalid selection! Please choose a valid movie number.");
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid input! Please enter a valid number.");
			}
		}
		return movieChoice;
	}

	private void chooseTimeSlot(String movie) {
		Map<String, Integer> timeSlots = movieSchedule.get(movie);
		System.out.println("Available Time Slots for \"" + movie + "\":");
		int index = 1;
		List<String> timeList = new ArrayList<>();

		for (String time : timeSlots.keySet()) {
			int availableSeats = timeSlots.get(time);
			System.out.println(index++ + ". " + time + " (Seats: " + availableSeats + ")");
			timeList.add(time);
		}

		System.out.print("Choose time slot number: ");
		int timeChoice = getValidTimeChoice(timeList.size());

		if (timeChoice != -1) {
			selectedTime = timeList.get(timeChoice - 1);
		}
	}

	private int getValidTimeChoice(int size) {
		int timeChoice = -1;
		while (timeChoice < 1 || timeChoice > size) {
			try {
				timeChoice = Integer.parseInt(sc.nextLine());
				if (timeChoice < 1 || timeChoice > size) {
					System.out.println("Invalid selection! Please choose a valid time slot number.");
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid input! Please enter a valid number.");
			}
		}
		return timeChoice;
	}

	public String getSelectedTime() {
		return selectedTime;
	}

	public void chooseSeats() {
		int availableSeats = movieSchedule.get(selectedMovie).get(selectedTime);
		System.out.println("Available seats: " + availableSeats);

		while (true) {
			System.out.print("Enter number of seats to book: ");
			try {
				seatsToBook = Integer.parseInt(sc.nextLine());

				if (seatsToBook > availableSeats) {
					System.out.println("Not enough seats available!!! Please enter a valid number of seats.");
				} else if (seatsToBook <= 0) {
					System.out.println("Invalid input! Please enter a valid number of seats.");
				} else {
					movieSchedule.get(selectedMovie).put(selectedTime, availableSeats - seatsToBook);
					System.out.println(seatsToBook + " seats booked for \"" + selectedMovie + "\" at " + selectedTime);
					break;
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid input! Please enter a valid number.");
			}
		}
	}

	public void applyPromo(Discount promoService, String code) {
		if (!code.isEmpty() && promoService.isValid(code)) {
			discount = promoService.applyDiscount(code);
			System.out.println("Promo Code Applied!!! Discount: " + (int) (discount * 100) + "%");
		} else if (!code.isEmpty()) {
			System.out.println("Invalid Promo Code");
		}
	}

	public double getDiscountedPrice() {
		double total = seatsToBook * pricePerSeat;
		return total - (total * discount);
	}

	public String getSelectedMovie() {
		return selectedMovie;
	}

	public String getSeatsToBook() {
		return String.valueOf(seatsToBook);
	}
}
