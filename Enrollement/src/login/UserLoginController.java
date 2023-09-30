
package login;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import static javafx.scene.input.KeyCode.I;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;

public class UserLoginController implements Initializable {

    public static Stage stage = null;
    
    private double xOffset = 0;
    private double yOffset = 0;
    
    @FXML
    private TextField username_field;
    @FXML
    private PasswordField pin_field;
    
    @FXML
    private FontAwesomeIcon ico;
    
    
    @FXML
    public void closeApp(MouseEvent event) {
        Platform.exit();
        System.exit(0);
    }
    @FXML
    public void minimizeApp(MouseEvent event) {
        Stage stage = (Stage) ico.getScene().getWindow();
        stage.setIconified(true);
    }
    
    
    public void login(MouseEvent event) throws SQLException, ClassNotFoundException {
        
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        String username = username_field.getText();
        String pin = pin_field.getText();
        
        if (username.isEmpty() && pin.isEmpty()) {
            
            showError("Login Error",null,"You must input to all field.");
            
        }
        else {
            
            try {
                
                Class.forName("org.sqlite.JDBC");
                con = DriverManager.getConnection("jdbc:sqlite:EnrollmentSystem.db");
                
                String sql = "SELECT * FROM admin WHERE Username = ? and PIN = ?";
                ps = con.prepareStatement(sql);
                
                ps.setString(1, username);
                ps.setString(2, pin);
                
                rs = ps.executeQuery();
                
                if(rs.next()) {
                    ((Node)event.getSource()).getScene().getWindow().hide();
                    Parent root = FXMLLoader.load(getClass().getResource("/maindashboard/EnrollDashboard.fxml"));

                    Scene scene = new Scene(root);

                    Stage stage = new Stage();
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.setScene(scene);
                    stage.show();

                    this.stage = stage;
                    
                    // Clearing the fields
                    username_field.setText("");
                    pin_field.setText("");
                    
                }
                else {
                    showError("Login Error",null,"Sorry, your username or pin is wrong. Try Again!!!");
                }
                
                
            }
            catch(Exception e) {
                
                showError("Login Error", "Sorry, there is something technical error. Please try again later.", "Exception is : " + e.getMessage());
                
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
        // TODO
    }    
    
}
