
package admin;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;

public class AdminBoardController implements Initializable {
    
    
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    private double xOffset = 0;
    private double yOffset = 0;
     
    
    @FXML
    private FontAwesomeIcon ico;
    
    @FXML
    private Button refreshBtn;
    
    @FXML
    private TableView<InvestorsInfo> investors_table;
    @FXML
    private TableColumn<InvestorsInfo, String> investors_name;
    @FXML
    private TableColumn<InvestorsInfo, String> investors_icn;
    @FXML
    private TableColumn<InvestorsInfo, String> investors_mobileno;
    @FXML
    private TableColumn<InvestorsInfo, String> investors_address;
    @FXML
    private TableColumn<InvestorsInfo, String> investors_acn;
    @FXML
    private TableColumn<InvestorsInfo, String> investors_pin;
    @FXML
    private TableColumn<InvestorsInfo, String> investors_acctype;
    @FXML
    private TableColumn<InvestorsInfo, String> investors_balance;
    
    
    @FXML
    public void refreshTable(ActionEvent event) {
        showAllInvestors();
    }
    
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
    
    public void showAllInvestors(){
        investors_name.setCellValueFactory(new PropertyValueFactory<InvestorsInfo, String>("name"));
        investors_icn.setCellValueFactory(new PropertyValueFactory<InvestorsInfo, String>("icn"));
        investors_mobileno.setCellValueFactory(new PropertyValueFactory<InvestorsInfo, String>("mobileno"));
        investors_address.setCellValueFactory(new PropertyValueFactory<InvestorsInfo, String>("address"));
        investors_acn.setCellValueFactory(new PropertyValueFactory<InvestorsInfo, String>("acn"));
        investors_pin.setCellValueFactory(new PropertyValueFactory<InvestorsInfo, String>("pin"));
        investors_acctype.setCellValueFactory(new PropertyValueFactory<InvestorsInfo, String>("acctype"));
        investors_balance.setCellValueFactory(new PropertyValueFactory<InvestorsInfo, String>("balance"));
        
         ObservableList<InvestorsInfo> list = FXCollections.observableArrayList();
         
         try {
            
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:BankDB.db");

            String sql = "SELECT * FROM investorsData";
            ps = con.prepareStatement(sql);

            rs = ps.executeQuery();
            while (rs.next()){
                
                list.add(new InvestorsInfo(rs.getString("Name"), rs.getString("ICN"), rs.getString("MobileNo"), rs.getString("Address"), rs.getString("ACN"), rs.getString("PIN"), rs.getString("AcctType"), rs.getString("Balance")));
                
            }

        } 
        catch(Exception e) {
            e.printStackTrace(); 
            showError("Error","Error in Fetching Data","There is an error in Fetching Data. TRY AGAIN!!!");
            System.out.println(e.getMessage());

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
        investors_table.setItems(list);
        
    }
    
    
    public void deleteData(ActionEvent event) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:BankDB.db");

            // Check if a row is selected in the TableView
            InvestorsInfo selectedInvestor = investors_table.getSelectionModel().getSelectedItem();
            if (selectedInvestor != null) {

                int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this from the Database?", "Confirmation", JOptionPane.YES_NO_OPTION);

                if(choice == JOptionPane.YES_OPTION) {
                    String sql = "DELETE FROM investorsData WHERE Name = ?";
                    ps = con.prepareStatement(sql);
                    ps.setString(1, selectedInvestor.getName());

                    // Execute the delete query
                    int rowsDeleted = ps.executeUpdate();

                    if (rowsDeleted > 0) {
                        con.setAutoCommit(false);
                        con.commit();
                        // Remove the selected item from the TableView
                        investors_table.getItems().remove(selectedInvestor);
                        investors_table.refresh();
                        showInformation("Admin", "Deleted Success", "Investors Data Successfully Deleted.");
                    } 
                    else {
                        con.rollback(); // Rollback the transaction if no rows were affected
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("No Rows Deleted");
                        alert.setContentText("No matching data found for deletion.");
                        alert.showAndWait();
                    }    
                }     
            } 
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("No Row Selected");
                alert.setContentText("Please select a row to delete.");
                alert.showAndWait();
            }
        } 
        catch (ClassNotFoundException | SQLException e) {
            if (con != null) {
                con.rollback();
            }
            showError("Exception", null, "Exception is " + e.getMessage());
        } 
        finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } 
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void updateWindow(ActionEvent event) throws IOException {
        try {
            FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("update.fxml"));
            Parent root1 = (Parent) fxmlloader.load();

            Stage stage = new Stage();
            stage.setTitle("Update Data Window");
            
            Image img = new Image("/images/bankIcon.png");
            stage.getIcons().add(img); 
        
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root1));
            stage.show();
        }
        catch(Exception e) {
            e.printStackTrace();
            showError("Exception", "Can't laod the Update Window.","There something exception: " + e.getMessage());
        }
        
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
        showAllInvestors();
    }    
    
}
