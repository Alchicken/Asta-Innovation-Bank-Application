
package login;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Enrollement extends Application {

     
     private double xOffset = 0;
     private double yOffset = 0;
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        
        Parent root = FXMLLoader.load(getClass().getResource("UserLogin.fxml"));

        Scene scene = new Scene(root);
        
        Stage stage = new Stage();
        
        Image img = new Image("/images/schoolLogo.png"); 
        stage.getIcons().add(img);
        
        scene.getStylesheets().add(getClass().getResource("/design/enrollment.css").toExternalForm());
        
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        
        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
        
       
        stage.show();
      
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
