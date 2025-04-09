package service;

//Handles Payments done in cash
public class CashPayment implements PaymentMethod {

	@Override
	public void pay(double amount) {
		System.out.printf("Paid %.2fBDT in cash\n", amount);
	}

}
