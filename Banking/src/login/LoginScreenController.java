
package login;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;


public class LoginScreenController implements Initializable {
    
    public static Stage stage = null;
    public static String acc;
    @FXML
    private Pane main_area;
    @FXML
    private TextField acn;
    @FXML
    private PasswordField pin;
    
    
    
    @FXML
    public void closeApp(MouseEvent event) {
        Platform.exit();
        System.exit(0);
    }
    
    @FXML
    public void createAcct(MouseEvent event) throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("/createacct/CreateAccount.fxml"));
        main_area.getChildren().removeAll();
        main_area.getChildren().addAll(fxml);
    }
        
    @FXML
    public void forgotPassword(MouseEvent event) throws IOException {
        main_area.getChildren().clear(); // I-clear ang mga dati
        Parent fxml = FXMLLoader.load(getClass().getResource("/forgotpass/ForgotPassword.fxml"));
        main_area.getChildren().removeAll();
        main_area.getChildren().addAll(fxml);
    }
    
    public void loginAcctount(MouseEvent event) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        if (!acn.getText().isEmpty() && !pin.getText().isEmpty()) {
            
            try {
            
                Class.forName("org.sqlite.JDBC");
                con = DriverManager.getConnection("jdbc:sqlite:BankDB.db");

                String sql = "SELECT * FROM investorsData WHERE ACN = ? and PIN = ?";

                ps = con.prepareStatement(sql);

                ps.setString(1, acn.getText());
                ps.setString(2, pin.getText());
                acc = acn.getText();
                
                rs = ps.executeQuery();
            
            if (rs.next()){
                ((Node)event.getSource()).getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("/dashboard/Dashboard.fxml"));
                
                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("/design/bankStyle.css").toExternalForm());
                
                Stage stage = new Stage();
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setScene(scene);
                stage.show();
                
                this.stage = stage;
                
            }
            else {
                 Alert a = new Alert(AlertType.ERROR);
                a.setTitle("Error");
                a.setHeaderText("Account Not Successfully Created.");
                a.setContentText("Sorry, your account is not created. There is some error. TRY AGAIN!!!");
                a.showAndWait();
            }
 
            } catch(Exception e) {
                e.printStackTrace(); 

                Alert a = new Alert(AlertType.ERROR);
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
        
        else {
            
            Alert a = new Alert(AlertType.ERROR);
            a.setTitle("Error");
            a.setHeaderText("All field must not be empty.");
            a.setContentText("Sorry, you can't proceed to login without entering data to all field.");
            a.showAndWait();
        }
        
        
    }
    
    @FXML
    public void adminBoard(MouseEvent event) throws IOException {
        ((Node)event.getSource()).getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("adminLogin.fxml"));
                
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/design/bankStyle.css").toExternalForm());
                
        Stage stage = new Stage();
        Image img = new Image("/images/bankIcon.png");
        stage.getIcons().add(img);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
                
        this.stage = stage;
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
