/*
	Calculates the combination of n items taken r at a time (nCr).
	This function calculates the number of ways to choose r items from a set of n items
	Without considering the order of the items.

 */
public class MyMath {

	public static int nCr(int n, int r) {
		if (n<r) {
			System.out.println("n less than r");
			return -1; // Throws an expection for error handling
		}
		// Calculate the combination using the formula: n! / ((n-r)! * r!)
		return MyMath.factorial(n)/
			( MyMath.factorial(n-r) * MyMath.factorial(r) );
	}
	
	public static int factorial(int x) {
		if (x<=0) return 1;
		
		int product = 1;
		// Calculate the factorial by multiplying all integers from x down to 1
		while (x>0) product *= x--;
		
		return product;
	}
}
