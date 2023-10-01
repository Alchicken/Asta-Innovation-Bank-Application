
package forgotpass;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import login.Banking;


public class ForgotPasswordController implements Initializable {
    
    @FXML
    private TextField acn;
    @FXML
    private ComboBox<String> sq;
    @FXML
    private TextField ans;
    
    ObservableList<String> list = FXCollections.observableArrayList("What is your pet name?", "Who is your childhood friend?", "What is your mother maiden name?","What is your nickname?");
    
    public void backToLogIn(MouseEvent event) throws IOException {
        
        Banking.stage.getScene().setRoot(FXMLLoader.load(getClass().getResource("/login/LoginScreen.fxml")));
    }
    
    public void closeApp(MouseEvent event) {
        Platform.exit();
        System.exit(0);
    }

    
    public void recoverPassword(MouseEvent event) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        if (!acn.getText().isEmpty() && !ans.getText().isEmpty() && sq.getValue() != null) {
            
            try {
            
                Class.forName("org.sqlite.JDBC");
                con = DriverManager.getConnection("jdbc:sqlite:BankDB.db");

                String sql = "SELECT * FROM investorsData WHERE ACN = ? and SecQuestion = ? and Answer = ?";

                ps = con.prepareStatement(sql);

                ps.setString(1, acn.getText());
                ps.setString(2, sq.getValue());
                ps.setString(3, ans.getText());


                rs = ps.executeQuery();
            
            if (rs.next()){
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("Password Recovery");
                a.setHeaderText("Password Succesfully Recovered.");
                a.setContentText("Account No. : " + rs.getString("ACN") + "\n\t      PIN : " + rs.getString("PIN"));
                a.showAndWait();
            }
            else {
                 Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Error");
                a.setHeaderText("Wrong Data");
                a.setContentText("Please try again.");
                a.showAndWait();
            }
 
            } catch(Exception e) {
                e.printStackTrace();
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Error");
                a.setHeaderText("Error in recovery");
                a.setContentText("There's some error. TRY AGAIN!!!\n" + e.getMessage());
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
            
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setHeaderText("All field must not be empty.");
            a.setContentText("Sorry, you can't proceed to recovering your password without entering data to all field.");
            a.showAndWait();
        }
           
    }
    
   
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sq.setItems(list);
    }    
    
}
