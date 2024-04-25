package Customer;

import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Customer {

    private int accountNumber;

    public Customer(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void deposit(double amount) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank_db", "root", "root");

            // Prepare SQL query to update account balance
            String query = "UPDATE BANK_ACCOUNTS SET ACC_BALANCE = ACC_BALANCE + ? WHERE ACC_NUM = ?";
            pstmt = con.prepareStatement(query);
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, accountNumber);

            // Execute the query
            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Deposit of " + amount + " successful.");
            } else {
                System.out.println("Failed to deposit money. Account not found.");
            }
            pstmt.close();
            
            // New Balance
            String printQuery = "Select ACC_BALANCE FROM BANK_ACCOUNTS WHERE ACC_NUM = ?";
            pstmt = con.prepareStatement(printQuery);
            pstmt.setInt(1, accountNumber);
            rs = pstmt.executeQuery();
            if (rs.next()) {
            	double newBalance = rs.getDouble("ACC_BALANCE");
                System.out.println("New Balance: " + newBalance);
            }
            pstmt.close();
            
            // Update transactions table
            String insertQuery = "INSERT INTO BANK_TRANSACTIONS (from_account, to_account, amount) VALUES (?, ?, ?)";
            pstmt = con.prepareStatement(insertQuery);
            pstmt.setInt(1, accountNumber); // from_account is the current account
            pstmt.setInt(2, accountNumber); // to_account is also the current account for deposit
            pstmt.setDouble(3, amount);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Error occurred while depositing money: " + e.getMessage());
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

    public void withdraw(double amount) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank_db", "root", "root");

            // Check if the account has sufficient balance
            String checkQuery = "SELECT ACC_BALANCE FROM BANK_ACCOUNTS WHRE ACC_NUM = ?";
            pstmt = con.prepareStatement(checkQuery);
            pstmt.setInt(1, accountNumber);
            rs = pstmt.executeQuery();
            if(rs.next()) {
            	double currentBalance = rs.getDouble("ACC_BALANCE");
            	if(currentBalance < amount) {
            		System.out.println("Insufficient Balance.");
            		pstmt.close();
            		return;
            	}
            }
            pstmt.close();
           
            // Prepare SQL query to update account balance
            String query = "UPDATE BANK_ACCOUNTS SET ACC_BALANCE = ACC_BALANCE - ? WHERE ACC_NUM = ?";
            pstmt = con.prepareStatement(query);
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, accountNumber);
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Withdrawal of " + amount + " successful.");
            } else {
                System.out.println("Failed to withdraw money. Account not found or insufficient balance.");
            }
            pstmt.close();
            
            // New Balance
            String printQuery = "Select ACC_BALANCE FROM BANK_ACCOUNTS WHERE ACC_NUM = ?";
            pstmt = con.prepareStatement(printQuery);
            pstmt.setInt(1, accountNumber);
            rs = pstmt.executeQuery();
            if (rs.next()) {
            	double newBalance = rs.getDouble("ACC_BALANCE");
                System.out.println("New Balance: " + newBalance);
            }
            pstmt.close();
            
            // Update transactions table
            String insertQuery = "INSERT INTO BANK_TRANSACTIONS (from_account, to_account, amount) VALUES (?, ?, ?)";
            pstmt = con.prepareStatement(insertQuery);
            pstmt.setInt(1, accountNumber); // from_account is the current account for withdrawal
            pstmt.setInt(2, accountNumber); // to_account is also the current account
            pstmt.setDouble(3, -amount); // negative amount for withdrawal
            pstmt.executeUpdate();
            pstmt.close();
            
        } catch (SQLException e) {
            System.out.println("Error occurred while withdrawing money: " + e.getMessage());
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

    public void getMiniStatement() {
        List<String> miniStatement = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank_db", "root", "root");

            String query = "SELECT * FROM BANK_TRANSACTIONS WHERE from_account = ? OR to_account = ? ORDER BY transaction_date DESC LIMIT 5";
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, accountNumber);
            pstmt.setInt(2, accountNumber);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                int fromAccount = rs.getInt("from_account");
                int toAccount = rs.getInt("to_account");
                double amount = rs.getDouble("amount");
                Timestamp transactionDate = rs.getTimestamp("transaction_date");
                String transactionDetails = String.format("Amount: %.2f - Date: %s", amount, transactionDate);
                miniStatement.add(transactionDetails);               
            }
        } catch (SQLException e) {
            System.out.println("Error occurred while fetching mini statement: " + e.getMessage());
        } finally {
            // Close resources
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
        System.out.println(miniStatement);
    }


    public void transferAmount(int targetAccountNumber, double amount) {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank_db", "root", "root");

            // Check if the current account has sufficient balance
            String balanceCheckQuery = "SELECT ACC_BALANCE FROM BANK_ACCOUNTS WHERE ACC_NUM = ?";
            pstmt = con.prepareStatement(balanceCheckQuery);
            pstmt.setInt(1, accountNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                double currentBalance = rs.getDouble("ACC_BALANCE");
                if (currentBalance < amount) {
                    System.out.println("Insufficient balance.");
                    pstmt.close();
                    return; // Exit the method if balance is insufficient
                }
            }
            pstmt.close();
            
            
            // Deduct amount from current account
            String deductQuery = "UPDATE BANK_ACCOUNTS SET ACC_BALANCE = ACC_BALANCE - ? WHERE ACC_NUM = ?";
            pstmt = con.prepareStatement(deductQuery);
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, accountNumber);
            pstmt.executeUpdate();
            pstmt.close();

            // Add amount to target account
            String addQuery = "UPDATE BANK_ACCOUNTS SET ACC_BALANCE = ACC_BALANCE + ? WHERE ACC_NUM = ?";
            pstmt = con.prepareStatement(addQuery);
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, targetAccountNumber);
            pstmt.executeUpdate();
            pstmt.close();
            
            // Insert transaction record into BANK_TRANSACTIONS table
            String insertQuery = "INSERT INTO BANK_TRANSACTIONS (FROM_ACCOUNT, TO_ACCOUNT, AMOUNT) VALUES (?, ?, ?)";
            pstmt = con.prepareStatement(insertQuery);
            pstmt.setInt(1, accountNumber);
            pstmt.setInt(2, targetAccountNumber);
            pstmt.setDouble(3, amount);
            pstmt.executeUpdate();
            pstmt.close();

            
            System.out.println("Amount transferred successfully.");
        } catch (SQLException e) {
            System.out.println("Error occurred during transfer: " + e.getMessage());
        } finally {
            // Close resources
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
