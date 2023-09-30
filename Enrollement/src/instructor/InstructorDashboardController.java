
package instructor;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class InstructorDashboardController implements Initializable {
    
    Connection con = null;
    PreparedStatement ps = null;
    
    
    @FXML
    private Pane regPane;
    
    @FXML
    private FontAwesomeIcon showReg;
    
    @FXML
    private FontAwesomeIcon closeReg;
    
    @FXML
    private JFXTextField ins_id;
    
    @FXML
    private JFXTextField ins_name;
    
    @FXML
    private JFXTextField ins_age;
    
    @FXML
    private JFXTextField ins_degree;
    
    @FXML
    private JFXComboBox<String> ins_course;
    
    
    public void registerInstructor(MouseEvent event) {
        
        
    }
    
    
    
    public void showRegister(MouseEvent event) {
        
        regPane.setVisible(true);
        showReg.setVisible(false);
        closeReg.setVisible(true);
    }
    
    public void closeRegister(MouseEvent event) {
        
        regPane.setVisible(false);
        showReg.setVisible(true);
        closeReg.setVisible(false);
    }
    
    

   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        regPane.setVisible(false);
    }    
    
}
