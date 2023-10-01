
package withdraw;

import java.net.URL;
import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import login.LoginScreenController;


public class WithdrawAmountController implements Initializable {
    
    @FXML
    private Label acn;
    @FXML
    private Label balance;
    
    @FXML
    private TextField amt_field;
    @FXML
    private TextField pin_field;
    
    
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
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
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
    
    public void withdrawBtn() {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
        Class.forName("org.sqlite.JDBC");
        con = DriverManager.getConnection("jdbc:sqlite:BankDB.db");

        String pin = pin_field.getText();
        String amountText = amt_field.getText();

        if (pin.isEmpty() || amountText.isEmpty()) {
            showError("Error", "Missing Input", "You must enter data to proceed to withdrawal.");
            return;
        }

        int withdraw_amount = Integer.parseInt(amountText);

        String sql = "SELECT * FROM investorsData WHERE ACN = ? AND PIN = ?";
        ps = con.prepareStatement(sql);
        ps.setString(1, LoginScreenController.acc);
        ps.setString(2, pin);

        rs = ps.executeQuery();

        if (rs.next()) {
            if (rs.getString("PIN").equals(pin)) {
                int total_amount = Integer.parseInt(balance.getText());

                if (withdraw_amount > total_amount) {
                    showError("Error", "Insufficient Balance", "Sorry, you don't have enough balance. TRY AGAIN!!!");
                } else {
                    int total = total_amount - withdraw_amount;

                    String updateSql = "UPDATE investorsData SET Balance = ? WHERE ACN = ?";
                    ps = con.prepareStatement(updateSql);
                    ps.setInt(1, total);
                    ps.setString(2, LoginScreenController.acc);
                    ps.executeUpdate();

                    String insertSql = "INSERT INTO investorsWithdraw(ACN, WithdrawAmount, RemainingAmount, Date, Time) VALUES (?,?,?,?,?)";
                    ps = con.prepareStatement(insertSql);
                    ps.setString(1, LoginScreenController.acc);
                    ps.setInt(2, withdraw_amount);
                    ps.setInt(3, total);
                    ps.setString(4, date);
                    ps.setString(5, time);

                    int i = ps.executeUpdate();
                    if (i > 0) {
                        showInformation("Withdraw Success", "Amount Withdrawal Success.",
                                "Amount: " + withdraw_amount + " has been successfully withdrew.\nThank you, Comeback Again!");
                        
                        String accname = rs.getString("Name");
                        
                        // Console Reciept                        
                        System.out.println("Console Receipt");
                        System.out.println("WITHDRAW");
                        System.out.println("Date : " + date);
                        System.out.println("Time : " + time);
                        System.out.println("Account Name   : " + accname);
                        System.out.println("Account Number : " + LoginScreenController.acc);
                        System.out.println("Withdraw Amount: " + withdraw_amount + " Php");
                        System.out.println();
                        
                       
                        amt_field.setText("");
                        pin_field.setText("");
                        
                        String updatedTotal = Integer.toString(total);
                        balance.setText(updatedTotal);
                    } else {
                        showError("Error", "Withdraw Error", "Error in withdrawing, something technical error. Try again later!");
                    }
                }
            } else {
                showError("Error", "Wrong PIN", "Account PIN is wrong. Try again!");
            }
        } else {
            showError("Error", "Account Not Found", "Please enter valid account data to proceed with your withdrawal.");
        }

    } catch (Exception e) {
        e.printStackTrace();
        showError("Error","Error in Fetching Data","There is an error in Fetching Data. TRY AGAIN!!!");
    } finally {
        try {
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
