
package pin;

import dashboard.DashboardController;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import login.LoginScreenController;

public class ChangePinController implements Initializable {

    @FXML
    private TextField old_pin;
    @FXML
    private TextField new_pin;
    @FXML
    private TextField con_new_pin;
    
    DashboardController dc = new DashboardController();
    
    public void changePin(MouseEvent event) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

    try {
        Class.forName("org.sqlite.JDBC");
        con = DriverManager.getConnection("jdbc:sqlite:BankDB.db");

        if (old_pin.getText().isEmpty() || new_pin.getText().isEmpty() || con_new_pin.getText().isEmpty()) {
            showError("Error", "Missing Input", "You must enter data to proceed to change your PIN.");
            return;
        }
        else {
            String sql = "SELECT * FROM investorsData WHERE ACN = ? and PIN = ?";
            ps = con.prepareStatement(sql);

            ps.setString(1, LoginScreenController.acc);
            ps.setString(2, old_pin.getText());

            rs = ps.executeQuery();

            if (rs.next()) {
                if(new_pin.getText().equals(con_new_pin.getText())) {
                    String updateSql = "UPDATE investorsData SET PIN = '" + new_pin.getText() + "'WHERE ACN = '" + LoginScreenController.acc + "'";
                    ps = con.prepareStatement(updateSql);
                    ps.execute();

                    showInformation("PIN Change", "PIN Change Successfully", "Your PIN has been changed successfully. You have to remember and login your new PIN.");

                    old_pin.setText("");
                    new_pin.setText("");
                    con_new_pin.setText("");

                    dc.logout(event);
                }
                else {
                    showError("PIN Change", "PIN doesn't match.", "Please check your PIN confirmation is correct to proceed.");
                }
            }
            else {
                showError("Error", "Account Not Found", "Please enter valid account data to proceed changing your PIN.");
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
        showError("Error", "Login Error", "Sorry, your account no or pin is wrong." + e.getMessage());
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
        
    }    
    
}
