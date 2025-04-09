package service;

//Handles Payments done in cards
public class CardPayment implements PaymentMethod {

	@Override
	public void pay(double amount) {
		System.out.printf("Paid %.2fBDT in card\n", amount);
	}

}
