
package dashboard;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;
import login.Banking;
import static login.Banking.stage;
import login.LoginScreenController;

public class DashboardController implements Initializable {
    
    private double xOffset = 0;
    private double yOffset = 0;
    
    @FXML
    private Pane dashboard_main;
    
    @FXML
    private Text name;
    
    @FXML
    private FontAwesomeIcon ico;
    @FXML
    private Circle profilepic;
    
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
    
    public void setData() {
        
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
            try {
            
                Class.forName("org.sqlite.JDBC");
                con = DriverManager.getConnection("jdbc:sqlite:BankDB.db");

                String sql = "SELECT * FROM investorsData WHERE ACN = ?";

                ps = con.prepareStatement(sql);

                ps.setString(1, LoginScreenController.acc);

                rs = ps.executeQuery();
            
            if (rs.next()){
               
                name.setText(rs.getString("Name"));
                InputStream is = rs.getBinaryStream("ProfilePic");
                OutputStream os = new FileOutputStream(new File("pic.jpg"));
                byte[] content = new byte[1024];
                int size = 0;
                
                while((size = is.read(content)) != -1) {
                    
                    os.write(content, 0, size);
                    
                }
                os.close();
                is.close();
                
                profilepic.setStroke(Color.WHITE);
                Image img = new Image("file:pic.jpg", false);
                profilepic.setFill(new ImagePattern(img));  
            }
            else {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Error");
                a.setHeaderText("Account Not Successfully Created.");
                a.setContentText("Sorry, your account is not created. There is some error. TRY AGAIN!!!");
                a.showAndWait();
            }
 
            } catch(Exception e) {
                e.printStackTrace(); 

                Alert a = new Alert(Alert.AlertType.ERROR);
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
    
    @FXML
    public void click(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }
    @FXML
    public void drag(MouseEvent event) {
        LoginScreenController.stage.setX(event.getScreenX() - xOffset);
        LoginScreenController.stage.setY(event.getScreenY() - yOffset);
    }
    
    @FXML
    public void accountInformation(MouseEvent event) throws IOException {
        dashboard_main.getChildren().clear(); // I-clear ang mga dati
        Parent fxml = FXMLLoader.load(getClass().getResource("/acctinfo/AccountInformation.fxml"));
        dashboard_main.getChildren().removeAll();
        dashboard_main.getChildren().addAll(fxml);
    }
    
    @FXML
    public void withdraw(MouseEvent event) throws IOException {
        dashboard_main.getChildren().clear(); // I-clear ang mga dati
        Parent fxml = FXMLLoader.load(getClass().getResource("/withdraw/WithdrawAmount.fxml"));
        dashboard_main.getChildren().removeAll();
        dashboard_main.getChildren().addAll(fxml);
    }
    @FXML
    public void deposit(MouseEvent event) throws IOException {
        dashboard_main.getChildren().clear(); // I-clear ang mga dati
        Parent fxml = FXMLLoader.load(getClass().getResource("/deposit/DepositAmount.fxml"));
        dashboard_main.getChildren().removeAll();
        dashboard_main.getChildren().addAll(fxml);
    }
    
    @FXML
    public void mainScreen() throws IOException {
        
        Parent fxml = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
        dashboard_main.getChildren().removeAll();
        dashboard_main.getChildren().addAll(fxml);
    }
    @FXML
    public void transactionHistory() throws IOException {
        
        Parent fxml = FXMLLoader.load(getClass().getResource("/transactionhistory/TransactionHistory.fxml"));
        dashboard_main.getChildren().removeAll();
        dashboard_main.getChildren().addAll(fxml);
    }
    @FXML
    public void transferMoney() throws IOException {
        
        Parent fxml = FXMLLoader.load(getClass().getResource("/transfer/TransferAmount.fxml"));
        dashboard_main.getChildren().removeAll();
        dashboard_main.getChildren().addAll(fxml);
    }
    @FXML
    public void pinChange() throws IOException {
        
        Parent fxml = FXMLLoader.load(getClass().getResource("/pin/ChangePin.fxml"));
        dashboard_main.getChildren().removeAll();
        dashboard_main.getChildren().addAll(fxml);
    } 
            
    public void logout(MouseEvent event) throws IOException {
        ((Node)event.getSource()).getScene().getWindow().hide();
        
        Parent root = FXMLLoader.load(getClass().getResource("/login/LoginScreen.fxml"));               
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/design/bankStyle.css").toExternalForm());
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
        
        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
            
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                
                stage.setX(event.getSceneX() - xOffset);
                stage.setY(event.getSceneY() - yOffset);
            }
            
        });
        
    }
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        setData();
        try {
            mainScreen();
        } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
        

    }    
    
}
