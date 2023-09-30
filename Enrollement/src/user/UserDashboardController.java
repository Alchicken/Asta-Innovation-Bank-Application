
package user;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;

public class UserDashboardController implements Initializable {
    
    
    @FXML
    private FontAwesomeIcon ico;
    
    @FXML
    private TextField fnamef;
    @FXML
    private TextField mnamef;
    @FXML
    private TextField lnamef;
    @FXML
    private TextField suffixf;
    @FXML
    private TextField emailf;
    @FXML
    private TextField usernamef;
    
    @FXML
    private PasswordField pinf;
    
    
    
    
    public void registerUser(MouseEvent event) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        String fname = fnamef.getText();
        String mname = mnamef.getText();
        String lname = lnamef.getText();
        String suffix = suffixf.getText();
        String email = emailf.getText();
        String username = usernamef.getText();
        String pin = pinf.getText();
        
        if (fname.isEmpty() && mname.isEmpty() && lname.isEmpty() && email.isEmpty() && username.isEmpty() && pin.isEmpty()) {
            
            showError("Login Error",null,"You must input to all field.");
            
        }
        else {
            
            try {
                
                Class.forName("org.sqlite.JDBC");
                con = DriverManager.getConnection("jdbc:sqlite:EnrollmentSystem.db");
                
                UserInfo userinfo = new UserInfo();
                String UserID = userinfo.UserIDGenerator();
                
                
                String sql = "INSERT INTO admin (UserID, Name, Email, Username, PIN) VALUES(?,?,?,?,?)";
                ps = con.prepareStatement(sql);
                
                String name = fname +" "+ lname;
                
                
                
                ps.setString(1, UserID);
                ps.setString(2, name);
                ps.setString(3, email);
                ps.setString(4, username);
                ps.setString(5, pin);
                
                
                int i = ps.executeUpdate();
                
                if(i > 0) {
                    
                    JOptionPane.showMessageDialog(null, "RegisterSuccess");
                    
                    // Clearing the fields
                    fnamef.setText("");
                    mnamef.setText("");
                    lnamef.setText("");
                    suffixf.setText("");
                    emailf.setText("");
                    usernamef.setText("");
                    pinf.setText("");
                    
                    
                    
                }
                else {
                    showError("Register Error",null,"Sorry, you can't register. Try Again!!!");
                }
                
                
            }
            catch(Exception e) {
                
                showError("Register Error", "Sorry, there is something technical error. Please try again later.", "Exception is : " + e.getMessage());
                
            }
            finally {
                try {
                    // Close the resources in a finally block to ensure they are always closed
                    if (rs != null) {
                        rs.close();
                    }
                    if (ps != null) {
                        ps.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } catch (SQLException e) {
                    // Handle any SQLException that might occur during resource closing
                    e.printStackTrace();
                }
            }
        }
        
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
