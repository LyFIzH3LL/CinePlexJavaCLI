package service;
import java.util.HashMap;
import java.util.Map;

/*
 * Handles logics related to discounts
 */
public class PromoService implements Discount {
	
	
	/*
	 * Using Mapping, discount codes are stored here
	 */
	private final Map<String, Double> promoCodes;
	
	
	
	
	/*
	 * Constructor
	 */
	public PromoService()
	{
		//Map Initialized
		promoCodes = new HashMap<>();
		
		/*
		 * Discount codes are stored here.
		 */
		promoCodes.put("SAVE10", 0.10);
		promoCodes.put("FAMILY15", 0.15);
		promoCodes.put("MOVIE20", 0.20);
		promoCodes.put("STUDENT25", 0.25);
		promoCodes.put("EARLYBIRD30", 0.30);
		promoCodes.put("NEWUSER50", 0.50);
		promoCodes.put("MOVIEFRIEND10", 0.10);
		promoCodes.put("VIP30", 0.30);
		promoCodes.put("HOLIDAY20", 0.20);
		promoCodes.put("WEEKEND15", 0.15);
		promoCodes.put("BULK10", 0.10);
		promoCodes.put("CINEMA10", 0.10);
		promoCodes.put("LOVESAVE", 0.05);
		promoCodes.put("REFERRAL20", 0.20);
		promoCodes.put("MVP40", 0.40);
		promoCodes.put("SUMMER25", 0.25);
		promoCodes.put("SENIOR15", 0.15);

	}
	
	
	/*
	 * Applies discount based on the provided code and returns the 
	 * corresponding amount, also string is converted to upper-case
	 * to ensure case-insensitivity.
	 */
	@Override
	public double applyDiscount(String Code)
	{
		return promoCodes.getOrDefault(Code.toUpperCase(), 0.0);
	}
	/*
	 * Checks if the code exists in the map in the same way.
	 */
	@Override
	public boolean isValid(String Code)
	{
		return promoCodes.containsKey(Code.toUpperCase());
	}

}
