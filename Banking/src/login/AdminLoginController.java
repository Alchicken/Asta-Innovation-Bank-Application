
package login;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
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
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AdminLoginController implements Initializable {
    
    public static Stage stage = null;
     
     @FXML
    private FontAwesomeIcon ico;
    
    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField pin;
    
   
    
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
    
   public void adminLoginAccount(MouseEvent event) {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    // Check if username and PIN fields are not empty
    if (!username.getText().isEmpty() && !pin.getText().isEmpty()) {
        try {
            // Load the SQLite JDBC driver and establish a connection
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:BankDB.db");

            // Define the SQL query
            String sql = "SELECT * FROM admins WHERE Username = ? and PIN = ?";
            ps = con.prepareStatement(sql);

            // Set the parameters for the prepared statement
            ps.setString(1, username.getText());
            ps.setString(2, pin.getText());

            // Execute the query
            rs = ps.executeQuery();

            if (rs.next()) {
                // Close the current window
                ((Node) event.getSource()).getScene().getWindow().hide();

                // Load the admin dashboard
                Parent root = FXMLLoader.load(getClass().getResource("/admin/AdminBoard.fxml"));
                Scene scene = new Scene(root);

                // Add CSS stylesheet to the scene
                scene.getStylesheets().add(getClass().getResource("/design/bankStyle.css").toExternalForm());

                // Create and show a new stage for the admin dashboard
                Stage stage = new Stage();
                stage.initStyle(StageStyle.UNDECORATED);
                Image img = new Image("/images/bankIcon.png");
                stage.getIcons().add(img);
                stage.setScene(scene);
                stage.show();

                // Store the stage for later use if needed
                this.stage = stage;
            } else {
                // Display an error message if login fails
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Admin");
                a.setHeaderText("Admin Login Failed");
                a.setContentText("Sorry, you can't log in to admin. TRY AGAIN!!!");
                a.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();

            // Display an error message if there's an exception
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Admin");
            a.setHeaderText("Error in Fetching Data");
            a.setContentText("There is an error in fetching data. Try again!");
            a.showAndWait();
        } finally {
            try {
                // Close resources in the finally block
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
                e.printStackTrace();
            }
        }
    } else {
        // Display an error message if fields are empty
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Error");
        a.setHeaderText("All Fields Must Not Be Empty");
        a.setContentText("Sorry, you can't proceed to login without entering data in all fields.");
        a.showAndWait();
    }
}

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
