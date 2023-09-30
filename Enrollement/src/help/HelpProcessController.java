
package help;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class HelpProcessController implements Initializable {
    
    @FXML
    private FontAwesomeIcon ico;
    
    @FXML
    public WebView webView;
    public WebEngine engine;
    
    @FXML
    private Button fb;
    @FXML
    private Button gmail;
    @FXML
    private Button whatsapp;
    @FXML
    private Button insta;
    
    
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
    
    @FXML
    public void facebook(MouseEvent event) {
        engine.load("https://www.facebook.com/sarip.aldikhen");  
    }
    @FXML
    public void gmail(MouseEvent event) {
        engine.load("https://mail.google.com/mail/u/0/#inbox?compose=new");  
    }
    @FXML
    public void whatsapp(MouseEvent event) {
        engine.load("https://www.whatsapp.com/");  
    }
    @FXML
    public void instagram(MouseEvent event) {
        engine.load("https://www.instagram.com/aldkhnsrp_23/");  
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        engine = webView.getEngine();
        engine.loadContent("<html><body><h1>Golden Merry University</h1><hr><h2>This is Golden Merry Help Bar</h2><p>If you want help, kindly choose a button that you want to send messages.</body></html>");
    }    
    
}
