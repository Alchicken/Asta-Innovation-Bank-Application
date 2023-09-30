
package maindashboard;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;
import login.UserLoginController;

public class EnrollDashboardController implements Initializable {
    
    private double xOffset = 0;
    private double yOffset = 0;
    
    public static Stage stage = null;
    
    
    @FXML
    private FontAwesomeIcon ico;
    
    @FXML
    private void closeApp(MouseEvent event) {
        int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to close the Application?", "Confirmation", JOptionPane.YES_NO_OPTION);
        
        if(choice == JOptionPane.YES_OPTION) {
            Platform.exit();
            System.exit(0);
        }
        
    }
    @FXML
    public void minimizeApp(MouseEvent event) {
        Stage stage = (Stage) ico.getScene().getWindow();
        stage.setIconified(true);
    }
    
    @FXML
    public void click(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }
    @FXML
    public void drag(MouseEvent event) {
        UserLoginController.stage.setX(event.getScreenX() - xOffset);
        UserLoginController.stage.setY(event.getScreenY() - yOffset);
    }
    
    public void setUp(MouseEvent event) throws IOException {
        
        Parent root = FXMLLoader.load(getClass().getResource("/setup/SetupProcess.fxml"));

        Scene scene = new Scene(root);
        //scene.getStylesheets().add(getClass().getResource("/design/bankStyle.css").toExternalForm());

        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();

        this.stage = stage;
    }
    
    public void help(MouseEvent event) throws IOException {
        
        Parent root = FXMLLoader.load(getClass().getResource("/help/HelpProcess.fxml"));

        Scene scene = new Scene(root);

        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();

        this.stage = stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
        
    }    
    
}
