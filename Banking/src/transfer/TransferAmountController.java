
package transfer;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import login.LoginScreenController;


public class TransferAmountController implements Initializable {
    
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

     @FXML
    private Label acn;
    @FXML
    private Label balance;
    
    @FXML
    private TextField acct_no_field;
    @FXML
    private TextField amt_field;
    @FXML
    private TextField pin_field;
    
    @FXML
    private Pane dashboard_main;
    
    
    // FOR DATE AND TIME 
    Calendar cal = Calendar.getInstance();
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH);
    int day = cal.get(Calendar.DAY_OF_MONTH);
    int hour = cal.get(Calendar.HOUR);
    int minutes = cal.get(Calendar.MINUTE);
    int seconds = cal.get(Calendar.SECOND);
    int daynight = cal.get(Calendar.AM_PM);
    
        
    DateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd");
    Date d = new Date();
    String date = dateformat.format(d);
    
    LocalTime localtime = LocalTime.now();
    DateTimeFormatter dt = DateTimeFormatter.ofPattern("hh:mm:ss a");
    String time  = localtime.format(dt);

    
    
    
    public void setInfo() {
        
            try {
            
                Class.forName("org.sqlite.JDBC");
                con = DriverManager.getConnection("jdbc:sqlite:BankDB.db");

                String sql = "SELECT * FROM investorsData WHERE ACN = ?";

                ps = con.prepareStatement(sql);

                ps.setString(1, LoginScreenController.acc);

                rs = ps.executeQuery();
            
            if (rs.next()){
  
                acn.setText(rs.getString("ACN"));
                balance.setText(rs.getString("Balance"));
                
            }
            else {
                showError("Error","Account not successfully created.","Sorry, your account is not created. There is some error. TRY AGAIN!!!");
            }
 
            } catch(Exception e) {
                e.printStackTrace(); 
                showError("Error","Error in Login","Sorry, your account no or pin is wrong.");
                System.out.println(e.getMessage());

            }
             finally {
                try {
                    if (ps != null) {
                        ps.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } catch (SQLException e) {

                }
            }  
    }
    
    public void checkBtn() {
        
         try {
            
                Class.forName("org.sqlite.JDBC");
                con = DriverManager.getConnection("jdbc:sqlite:BankDB.db");

                String sql = "SELECT * FROM investorsData WHERE ACN = ?";

                ps = con.prepareStatement(sql);

                ps.setString(1, acct_no_field.getText());

                rs = ps.executeQuery();
            
                if (rs.next()){
  
                   showInformation("Account Information","Account Found!","Account Number : " + acct_no_field.getText() + "\nName :     " + rs.getString("Name") + "\nMobile Number : " + rs.getString("MobileNo"));
                
                }
            else {
                showError("Account Information","There is no credentials found.","Sorry, you account no is not yet register to our server. Please register first and TRY AGAIN!!!");
            }
 
            } catch(Exception e) {
                e.printStackTrace(); 
                showError("Error","Error in Fetching Data","There is an error in Fetching Data. TRY AGAIN!!!");
                System.out.println(e.getMessage());

            }
             finally {
                try {
                    if (ps != null) {
                        ps.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } catch (SQLException e) {

                }
            }
    }
    
    
   public void transfertBtn() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection con = DriverManager.getConnection("jdbc:sqlite:BankDB.db");

            // Check if PIN is correct
            String checkPinSql = "SELECT * FROM investorsData WHERE ACN = ? AND PIN = ?";
            PreparedStatement checkPinStatement = con.prepareStatement(checkPinSql);
            checkPinStatement.setString(1, LoginScreenController.acc);
            checkPinStatement.setString(2, pin_field.getText());
            ResultSet pinResult = checkPinStatement.executeQuery();

            if (pinResult.next()) {
                // Check for empty fields
                if (pin_field.getText().isEmpty() || acct_no_field.getText().isEmpty() || amt_field.getText().isEmpty()) {
                    showError("Error", "Input field must not be empty", "You must enter the data needed to proceed with the transfer. Thank you!");
                    return;
                }

                int transferAmount = Integer.parseInt(amt_field.getText());
                int totalAmount = Integer.parseInt(balance.getText());

                if (transferAmount > totalAmount) {
                    showError("Error", "Insufficient Balance", "Sorry, you don't have enough balance. Please try again.");
                } else {
                    // Perform the transfer
                    int updatedBalance = totalAmount - transferAmount;
                    String updateBalanceSql = "UPDATE investorsData SET Balance = ? WHERE ACN = ?";
                    PreparedStatement updateBalanceStatement = con.prepareStatement(updateBalanceSql);
                    updateBalanceStatement.setInt(1, updatedBalance);
                    updateBalanceStatement.setString(2, LoginScreenController.acc);
                    updateBalanceStatement.executeUpdate();

                    // Find the recipient's account
                    String recipientSql = "SELECT * FROM investorsData WHERE ACN = ?";
                    PreparedStatement recipientStatement = con.prepareStatement(recipientSql);
                    recipientStatement.setString(1, acct_no_field.getText());
                    ResultSet recipientResult = recipientStatement.executeQuery();

                    if (recipientResult.next()) {
                        int currentBalance = Integer.parseInt(balance.getText());
                        int recipientBalance = Integer.parseInt(recipientResult.getString("Balance"));
                        int newRecipientBalance = currentBalance + recipientBalance;

                        String updateRecipientBalanceSql = "UPDATE investorsData SET Balance = ? WHERE ACN = ?";
                        PreparedStatement updateRecipientStatement = con.prepareStatement(updateRecipientBalanceSql);
                        updateRecipientStatement.setInt(1, newRecipientBalance);
                        updateRecipientStatement.setString(2, acct_no_field.getText());
                        updateRecipientStatement.executeUpdate();

                        // Insert the transfer record
                        String insertTransferSql = "INSERT INTO investorsTransfer (ACN, TransferAmount, SendTo, Date, Time) VALUES (?, ?, ?, ?, ?)";
                        PreparedStatement insertTransferStatement = con.prepareStatement(insertTransferSql);
                        insertTransferStatement.setString(1, LoginScreenController.acc);
                        insertTransferStatement.setInt(2, transferAmount);
                        insertTransferStatement.setString(3, acct_no_field.getText());
                        insertTransferStatement.setString(4, date);
                        insertTransferStatement.setString(5, time);
                        insertTransferStatement.executeUpdate();

                        // Show success message and update UI
                        showInformation("Transfer Success", "Amount Transfer Success.", "Amount: " + transferAmount + " has been successfully transferred. Thank you!");

                        // Console Receipt
                        System.out.println("Console Receipt");
                        System.out.println("Transfer Money");
                        System.out.println("Date : " + date);
                        System.out.println("Time : " + time);
                        System.out.println("Account Number : " + LoginScreenController.acc);
                        System.out.println("Send to : " + acct_no_field.getText());
                        System.out.println("Transfer Amount : " + transferAmount + " Php");
                        System.out.println();

                        // Clear input fields
                        acct_no_field.setText("");
                        amt_field.setText("");
                        pin_field.setText("");

                        balance.setText(String.valueOf(updatedBalance));
                    } else {
                        showError("Error", "Recipient Account Not Found", "Recipient account not found. Please enter a valid recipient account.");
                    }
                }
            } else {
                showError("Error", "Wrong PIN", "Account PIN is wrong. Please try again.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error","Error in Fetching Data","There is an error in Fetching Data. TRY AGAIN!!!");
        }
        finally {
                try {
                    if (ps != null) {
                        ps.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } catch (SQLException e) {

                }
            }
    }
    
    private void showError(String title, String header, String content) {
    Alert a = new Alert(Alert.AlertType.ERROR);
    a.setTitle(title);
    a.setHeaderText(header);
    a.setContentText(content);
    a.showAndWait();
}
    private void showInformation(String title, String header, String content) {
    Alert a = new Alert(Alert.AlertType.INFORMATION);
    a.setTitle(title);
    a.setHeaderText(header);
    a.setContentText(content);
    a.showAndWait();
}


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setInfo();
    }    
    
}
