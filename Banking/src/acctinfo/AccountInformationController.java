
package acctinfo;

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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import login.LoginScreenController;


public class AccountInformationController implements Initializable {
    
    @FXML 
    private Text accno;
    @FXML 
    private Text gen;
    @FXML 
    private Text acctype;
    @FXML 
    private Text reli;
    @FXML
    private Label balance;
    
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
                
                balance.setText(rs.getString("Balance"));
                accno.setText(rs.getString("ACN"));
                gen.setText(rs.getString("Gender"));
                acctype.setText(rs.getString("AcctType"));
                reli.setText(rs.getString("Religion"));
                
              
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
    public void withdraw(MouseEvent event) throws IOException {
        dashboard_main.getChildren().clear(); // I-clear ang mga dati
        Parent fxml = FXMLLoader.load(getClass().getResource("/withdraw/WithdrawAmount.fxml"));
        dashboard_main.getChildren().removeAll();
        dashboard_main.getChildren().addAll(fxml);
    }
    @FXML
    public void deposit(MouseEvent event) throws IOException {
        dashboard_main.getChildren().clear(); // I-clear ang mga dati
        Parent fxml = FXMLLoader.load(getClass().getResource("/deposit/DepositAmount.fxml"));
        dashboard_main.getChildren().removeAll();
        dashboard_main.getChildren().addAll(fxml);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setInfo();
    }    
    
}
