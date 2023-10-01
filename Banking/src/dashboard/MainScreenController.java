
package dashboard;


import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import login.LoginScreenController;


public class MainScreenController implements Initializable {

    
    @FXML
    private Label name;
    @FXML
    private Label acn;
    
    @FXML
    private Label body;
    
    @FXML
    private Pane dashboard_main;
    
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
               
                name.setText(rs.getString("Name"));
                acn.setText(rs.getString("ACN"));
            }
            else {
                 Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Error");
                a.setHeaderText("Account Not Successfully Created.");
                a.setContentText("Sorry, your account is not created. There is some error. TRY AGAIN!!!");
                a.showAndWait();
            }
 
            } catch(Exception e) {
                e.printStackTrace(); 

                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Error");
                a.setHeaderText("Error in Login");
                a.setContentText("Sorry, your account no or pin is wrong." + e.getMessage());
                a.showAndWait();

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
     
     @FXML
    public void transferMoney() throws IOException {
        
        Parent fxml = FXMLLoader.load(getClass().getResource("/transfer/TransferAmount.fxml"));
        dashboard_main.getChildren().removeAll();
        dashboard_main.getChildren().addAll(fxml);
    }
    @FXML
    public void transactionHistory() throws IOException {
        
        Parent fxml = FXMLLoader.load(getClass().getResource("/transactionhistory/TransactionHistory.fxml"));
        dashboard_main.getChildren().removeAll();
        dashboard_main.getChildren().addAll(fxml);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        body.setText("Astra Innovate Bank is more than just a bank; we're your financial partner \non the path to success. Explore our range of banking services, and let us help \nyou achieve your financial aspirations. Welcome to the future of banking. \nWelcome to Astra Innovate Bank. \n\nDiscover Astra Innovate Bank Today and Experience Banking Redefined.");
        setInfo();
    }    
    
}
