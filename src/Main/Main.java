package Main;

import java.sql.SQLException;
import java.util.Scanner;

import Administrator.Administrator;
import Customer.Customer;

public class Main {

	
	private static void adminMode(Scanner scanner) throws SQLException {
	    Administrator admin = new Administrator();
	    boolean exit = false;

	    while (!exit) {
	        System.out.println("Administrator Mode");
	        System.out.println("Choose Action:");
	        System.out.println("1. Add Customer");
	        System.out.println("2. Search Account");
	        System.out.println("3. Update Account");
	        System.out.println("4. Find Balance");
	        System.out.println("5. Close Account");
	        System.out.println("6. Display All Accounts");
	        System.out.println("7. Exit");

	        int choice = scanner.nextInt();
	        int accnum;

	        switch (choice) {
	            case 1:
	                admin.add_customer();
	                break;
	            case 2:
	                System.out.println("Enter Account Number: ");
	                accnum = scanner.nextInt();
	                admin.search_account(accnum);
	                break;
	            case 3:
	                System.out.println("Enter Account Number: ");
	                accnum = scanner.nextInt();
	                admin.update_account(accnum);
	                break;
	            case 4:
	                System.out.println("Enter Account Number: ");
	                accnum = scanner.nextInt();
	                admin.find_balance(accnum);
	                break;
	            case 5:
	                System.out.println("Enter Account Number: ");
	                accnum = scanner.nextInt();
	                admin.close_account(accnum);
	                break;
	            case 6:
	                admin.display_all();
	                break;
	            case 7:
	                System.out.println("Exiting Administrator Mode...");
	                exit = true;
	                break;
	            default:
	                System.out.println("Invalid choice. Please try again.");
	                break;
	        }
	    }
	}
	
	private static void customerMode(Scanner scanner) {
	    boolean exit = false;
	    while (!exit) {
	        System.out.println("Enter your account number:");
	        int accountNumber = scanner.nextInt();
	        Customer customer = new Customer(accountNumber);

	        System.out.println("Customer Mode");
	        System.out.println("Choose Action:");
	        System.out.println("1. Deposit");
	        System.out.println("2. Withdraw");
	        System.out.println("3. Print Mini Statement");
	        System.out.println("4. Transfer Amount");
	        System.out.println("5. Exit");

	        int choice = scanner.nextInt();

	        switch (choice) {
	            case 1:
	                System.out.println("Enter amount to deposit:");
	                double depositAmount = scanner.nextDouble();
	                customer.deposit(depositAmount);
	                break;
	            case 2:
	                System.out.println("Enter amount to withdraw:");
	                double withdrawAmount = scanner.nextDouble();
	                customer.withdraw(withdrawAmount);
	                break;
	            case 3:
	                customer.getMiniStatement();
	                break;
	            case 4:
	                System.out.println("Enter target account number:");
	                int targetAccountNumber = scanner.nextInt();
	                System.out.println("Enter amount to transfer:");
	                double transferAmount = scanner.nextDouble();
	                customer.transferAmount(targetAccountNumber, transferAmount);
	                break;
	            case 5:
	                System.out.println("Exiting Customer Mode...");
	                exit = true;
	                break;
	            default:
	                System.out.println("Invalid choice. Please try again.");
	                break;
	        }
	    }
	}
	
	
	public static void main(String[] args) throws SQLException {
	    Scanner scanner = new Scanner(System.in);
	    System.out.println("Welcome to the Bank System!");

	    while (true) {
	        System.out.println("Choose Mode:");
	        System.out.println("1. Administrator Mode");
	        System.out.println("2. Customer Mode");
	        System.out.println("3. Exit");

	        int choice = scanner.nextInt();

	        switch (choice) {
	            case 1:
	                adminMode(scanner);
	                break;
	            case 2:
	                customerMode(scanner);
	                break;
	            case 3:
	                System.out.println("Exiting...");
	                scanner.close();
	                System.exit(0);
	            default:
	                System.out.println("Invalid choice.");
	                break;
	        }
	    }
	}
}
