package Administrator;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.Scanner;


public class Administrator {
	
	private boolean accountExists(int accountNumber) {
	    Connection con = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    boolean exists = false;

	    try {
	        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank_db", "root", "root");
	        String query = "SELECT * FROM BANK_ACCOUNTS WHERE ACC_NUM = ?";
	        pstmt = con.prepareStatement(query);
	        pstmt.setInt(1, accountNumber);
	        rs = pstmt.executeQuery();

	        exists = rs.next(); // Check if the result set has any rows
	        
	    } catch (SQLException e) {
	        System.out.println("Error occurred while checking account existence: " + e.getMessage());
	    } 
	    
	    return exists;
	}

    
	 public void add_customer() throws SQLException {
	        Scanner sc = new Scanner(System.in);
	        System.out.println("Enter Account details:");

	     // Generate a random account number
	        Random random = new Random();
	        int accountNumber;
	        do {
	            accountNumber = 100 + random.nextInt(200); // Generate a random 6-digit number
	        } while (accountExists(accountNumber)); // Check if the generated number already exists

	        System.out.println("Account Number is: " +accountNumber);
	        // Enter account name
	        System.out.println("Enter the account holder name:");
	        String accountName = sc.next();

	        // Enter account Balance
	        System.out.println("Enter account balance: ");
	        double accountBalance = sc.nextDouble();
	        
	        Connection con = null;
	        Statement stmt = null;
	        
	        try {
	            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank_db", "root", "root");
	            stmt = con.createStatement();
	            
	            String query = String.format("INSERT INTO BANK_ACCOUNTS VALUES (%d, '%s', %.2f)", accountNumber, accountName, accountBalance);
	            stmt.executeUpdate(query);
	            
	            System.out.println("Account added successfully.");
	        } catch (SQLException e) {
	            System.out.println("Error occurred while adding account: " + e.getMessage());
	        } 
	        
	        
	    }

    
    
	 public void search_account(int accountNumber) {
		    Connection con = null;
		    PreparedStatement pstmt = null;
		    ResultSet rs = null;

		    try {
		        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank_db", "root", "root");
		        String query = String.format("SELECT * FROM BANK_ACCOUNTS WHERE ACC_NUM = %d",accountNumber);
		        pstmt = con.prepareStatement(query);
		        rs = pstmt.executeQuery();

		        if (rs.next()) {
		            int foundAccountNumber = rs.getInt("ACC_NUM");
		            String foundAccountName = rs.getString("ACC_NAME");
		            double foundAccountBalance = rs.getDouble("ACC_BALANCE");

		            System.out.println("Account found:");
		            System.out.println("Account Number: " + foundAccountNumber);
		            System.out.println("Account Name: " + foundAccountName);
		            System.out.println("Account Balance: " + foundAccountBalance);
		        } else {
		            System.out.println("Account with the given account number not found.");
		        }
		    } catch (SQLException e) {
		        System.out.println("Error occurred while searching account: " + e.getMessage());
		    } finally {
		        try {
		            if (rs != null) {
		                rs.close();
		            }
		            if (pstmt != null) {
		                pstmt.close();
		            }
		            if (con != null) {
		                con.close();
		            }
		        } catch (SQLException e) {
		            System.out.println("Error occurred while closing resources: " + e.getMessage());
		        }
		    }
		}

    
	 public void update_account(int accountNumber) {
	        Connection con = null;
	        PreparedStatement pstmt = null;

	        try {
	            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank_db", "root", "root");
	            Scanner sc = new Scanner(System.in);

	            // Prompt user for new account details
	            System.out.println("Enter new account details:");

	            System.out.println("Enter the new account holder name:");
	            String newName = sc.nextLine();

	            System.out.println("Enter the new account balance:");
	            double newBalance = sc.nextDouble();

	            // Prepare SQL query to update account details
	            String query = "UPDATE BANK_ACCOUNTS SET ACC_NAME = ?, ACC_BALANCE = ? WHERE ACC_NUM = ?";
	            pstmt = con.prepareStatement(query);
	            pstmt.setString(1, newName);
	            pstmt.setDouble(2, newBalance);
	            pstmt.setInt(3, accountNumber);

	            // Execute the update
	            int rowsUpdated = pstmt.executeUpdate();

	            if (rowsUpdated > 0) {
	                System.out.println("Account updated successfully.");
	            } else {
	                System.out.println("Failed to update account. Account not found or no changes made.");
	            }
	        } catch (SQLException e) {
	            System.out.println("Error occurred while updating account: " + e.getMessage());
	        } finally {
	            try {
	                if (pstmt != null) {
	                    pstmt.close();
	                }
	                if (con != null) {
	                    con.close();
	                }
	            } catch (SQLException e) {
	                System.out.println("Error occurred while closing resources: " + e.getMessage());
	            }
	        }
	    }

    
	 public void find_balance(int accountNumber) {
	        Connection con = null;
	        PreparedStatement pstmt = null;
	        ResultSet rs = null;

	        try {
	            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank_db", "root", "root");

	            // Prepare SQL query to select account balance
	            String query = "SELECT ACC_BALANCE FROM BANK_ACCOUNTS WHERE ACC_NUM = ?";
	            pstmt = con.prepareStatement(query);
	            pstmt.setInt(1, accountNumber);

	            // Execute the query
	            rs = pstmt.executeQuery();

	            if (rs.next()) {
	                double balance = rs.getDouble("ACC_BALANCE");
	                System.out.println("Balance for account number " + accountNumber + ": " + balance);
	            } else {
	                System.out.println("Account with the given account number not found.");
	            }
	        } catch (SQLException e) {
	            System.out.println("Error occurred while finding balance: " + e.getMessage());
	        } finally {
	            try {
	                if (rs != null) {
	                    rs.close();
	                }
	                if (pstmt != null) {
	                    pstmt.close();
	                }
	                if (con != null) {
	                    con.close();
	                }
	            } catch (SQLException e) {
	                System.out.println("Error occurred while closing resources: " + e.getMessage());
	            }
	        }
	    }

    
	 public void display_all() {
	        Connection con = null;
	        PreparedStatement pstmt = null;
	        ResultSet rs = null;

	        try {
	            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank_db", "root", "root");

	            // Prepare SQL query to select all account details
	            String query = "SELECT * FROM BANK_ACCOUNTS";
	            pstmt = con.prepareStatement(query);

	            // Execute the query
	            rs = pstmt.executeQuery();

	            System.out.println("All Accounts:");
	            // Iterate over the result set and print account details
	            while (rs.next()) {
	                int accountNumber = rs.getInt("ACC_NUM");
	                String accountName = rs.getString("ACC_NAME");
	                double accountBalance = rs.getDouble("ACC_BALANCE");

	                System.out.println("Account Number: " + accountNumber);
	                System.out.println("Account Name: " + accountName);
	                System.out.println("Account Balance: " + accountBalance);
	                System.out.println();
	            }
	        } catch (SQLException e) {
	            System.out.println("Error occurred while displaying all accounts: " + e.getMessage());
	        } finally {
	            try {
	                if (rs != null) {
	                    rs.close();
	                }
	                if (pstmt != null) {
	                    pstmt.close();
	                }
	                if (con != null) {
	                    con.close();
	                }
	            } catch (SQLException e) {
	                System.out.println("Error occurred while closing resources: " + e.getMessage());
	            }
	        }
	    }
    
	 public void close_account(int accountNumber) {
	        Connection con = null;
	        PreparedStatement pstmt = null;

	        try {
	            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank_db", "root", "root");

	            // Prepare SQL query to delete the account
	            String query = "DELETE FROM BANK_ACCOUNTS WHERE ACC_NUM = ?";
	            pstmt = con.prepareStatement(query);
	            pstmt.setInt(1, accountNumber);

	            // Execute the query
	            int rowsDeleted = pstmt.executeUpdate();

	            if (rowsDeleted > 0) {
	                System.out.println("Account closed successfully.");
	            } else {
	                System.out.println("Failed to close account. Account not found.");
	            }
	        } catch (SQLException e) {
	            System.out.println("Error occurred while closing account: " + e.getMessage());
	        } finally {
	            try {
	                if (pstmt != null) {
	                    pstmt.close();
	                }
	                if (con != null) {
	                    con.close();
	                }
	            } catch (SQLException e) {
	                System.out.println("Error occurred while closing resources: " + e.getMessage());
	            }
	        }
	    }

    
}