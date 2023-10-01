
package admin;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
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
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import login.Banking;

public class UpdateController implements Initializable {
    
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    
    @FXML
    private FontAwesomeIcon ico;
    
    @FXML
    private TextField change;
    @FXML
    private TextField acctNo;
    @FXML
    private TextField new_value;
    @FXML
    private PasswordField pin;
    

    
     public void backToLogIn(MouseEvent event) throws IOException {
        
        Banking.stage.getScene().setRoot(FXMLLoader.load(getClass().getResource("/login/LoginScreen.fxml")));
    }
    @FXML
    public void closeApp(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    @FXML
    public void minimizeApp(MouseEvent event) {
        Stage stage = (Stage) ico.getScene().getWindow();
        stage.setIconified(true);
    }
    
    
    public void checkBtn() {
        
        try {
            
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:BankDB.db");
            
            if (!acctNo.getText().isEmpty()) {
                
                String sql = "SELECT * FROM investorsData WHERE ACN = ?";

                ps = con.prepareStatement(sql);
                ps.setString(1, acctNo.getText());

                

                rs = ps.executeQuery();
                         
                if (rs.next()){
  
                   showInformation("Account Information","Account Found!","Account Number : " + acctNo.getText() + "\nName :     " + rs.getString("Name") + "\nMobile Number : " + rs.getString("MobileNo"));
                
                }
                else {
                    showError("Account Information","There is no credentials found.","Sorry, you account no is not yet register to our server. Please register first and TRY AGAIN!!!");
                }  
            }
            else {
                showError("Admin",null,"Admin, you must enter data in the input field.");
            }
        } 
        catch(Exception e) {
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
            } 
            catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void updateData(MouseEvent event) {
        
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        
        
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:BankDB.db");
            
            String what = change.getText();
            String acn = acctNo.getText();
            String acctPin = pin.getText();
            String newValue = new_value.getText();
            
            if(!what.isEmpty() && !acn.isEmpty() && !acctPin.isEmpty() && !newValue.isEmpty()) {
            
                String sql = "UPDATE investorsData SET '" + what + "' = '" + newValue + "' WHERE ACN = ?";

                ps = con.prepareStatement(sql);
            
                ps.setString(1, acn);


                int i = ps.executeUpdate();
                if (i > 0) {

                    showInformation("Admin",null,"Data Successfully Updated.\nAccount No : " + acn + ".");

                    change.setText("");
                    acctNo.setText("");
                    pin.setText("");
                    new_value.setText("");
                } 
            }
            else {
                showError("Admin",null,"You must need to enter data to the input field.");
            }
            
           
        } 
        catch(Exception e) {
            e.printStackTrace();
            
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Exception");
            alert.setContentText("Exception is " + e.getMessage());
            alert.showAndWait();
        }
        finally {
            try {
                if (res != null) {
                    res.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } 
            catch (SQLException e) {
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
