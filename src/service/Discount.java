package service;


//Interface for applying discounts and checking validity
public interface Discount {
	double applyDiscount(String code);
	boolean isValid (String code);

}
