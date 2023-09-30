
package setup;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import login.UserLoginController;

public class SetupProcessController implements Initializable {

    @FXML
    private FontAwesomeIcon ico;
    
     
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
    
    public void student(MouseEvent event) throws IOException {
        ((Node)event.getSource()).getScene().getWindow().hide();
        
        Parent root = FXMLLoader.load(getClass().getResource("/students/StudentDashboard.fxml"));               
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }
    
    public void user(MouseEvent event) throws IOException {
        ((Node)event.getSource()).getScene().getWindow().hide();
        
        Parent root = FXMLLoader.load(getClass().getResource("/user/Users.fxml"));               
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }
    public void instructor(MouseEvent event) throws IOException {
        ((Node)event.getSource()).getScene().getWindow().hide();
             
        Parent root = FXMLLoader.load(getClass().getResource("/instructor/InstructorDashboard.fxml"));               
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
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
