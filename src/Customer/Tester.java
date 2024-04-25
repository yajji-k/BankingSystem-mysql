package Customer;

import java.util.Scanner;

public class Tester {
	
	public static void main(String[] args) {
		
		Scanner sc  = new Scanner(System.in);
//		System.out.println("Enter Your account number: ");
//		int accnum = sc.nextInt();
		Customer customer = new Customer(123);
//		customer.deposit(100);
		customer.withdraw(100);
	}
	
}
