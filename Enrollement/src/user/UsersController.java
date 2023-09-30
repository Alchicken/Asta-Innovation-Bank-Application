
package user;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;

public class UsersController implements Initializable {
    
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    @FXML
    private FontAwesomeIcon ico;
    
    @FXML
    private TableView<UserInfo> user_table;
    @FXML
    private TableColumn<UserInfo, String> user_name;
    @FXML
    private TableColumn<UserInfo, String> user_email;
    @FXML
    private TableColumn<UserInfo, String> user_username;
    @FXML
    private TableColumn<UserInfo, String> user_pin;
    
    
    
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
    
    public void userAdd(MouseEvent event) throws IOException {  
        Parent root = FXMLLoader.load(getClass().getResource("/user/UserDashboard.fxml"));               
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }
    
    public void showAllUsers(){
        user_name.setCellValueFactory(new PropertyValueFactory<UserInfo, String>("name"));
        user_email.setCellValueFactory(new PropertyValueFactory<UserInfo, String>("email"));
        user_username.setCellValueFactory(new PropertyValueFactory<UserInfo, String>("username"));
        user_pin.setCellValueFactory(new PropertyValueFactory<UserInfo, String>("pin"));
        

         ObservableList<UserInfo> list = FXCollections.observableArrayList();
         
         try {
            
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:EnrollmentSystem.db");

            String sql = "SELECT * FROM admin";
            ps = con.prepareStatement(sql);

            rs = ps.executeQuery();
            while (rs.next()){
                
                list.add(new UserInfo(rs.getString("Name"), rs.getString("Email"), rs.getString("Username"), rs.getString("PIN")));
                
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
        user_table.setItems(list);    
    }
    
    public void deleteUser(ActionEvent event) throws SQLException {

        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:EnrollmentSystem.db");

            // Check if a row is selected in the TableView
            UserInfo selectedUsers = user_table.getSelectionModel().getSelectedItem();
            if (selectedUsers != null) {

                int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this from the Database?", "Confirmation", JOptionPane.YES_NO_OPTION);

                if(choice == JOptionPane.YES_OPTION) {
                    String sql = "DELETE FROM admin WHERE Name = ?";
                    ps = con.prepareStatement(sql);
                    ps.setString(1, selectedUsers.getName());

                    // Execute the delete query
                    int rowsDeleted = ps.executeUpdate();

                    if (rowsDeleted > 0) {
                        con.setAutoCommit(false);
                        con.commit();
                        // Remove the selected item from the TableView
                        user_table.getItems().remove(selectedUsers);
                        user_table.refresh();
                        showInformation("Admin", "Deleted Success", "Users Data Successfully Deleted.");
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
    
    public void updateUser(ActionEvent event) {
        Connection con = null;
        PreparedStatement ps = null;

    try {
        Class.forName("org.sqlite.JDBC");
        con = DriverManager.getConnection("jdbc:sqlite:EnrollmentSystem.db");

        // Check if a row is selected in the TableView
        UserInfo selectedUser = user_table.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            // Create a dialog to input updated information
            Dialog<UserInfo> dialog = new Dialog<>();
            dialog.setTitle("Update User");
            dialog.setHeaderText("Update User Information");

            // Create the username and password fields
            TextField idField = new TextField();
            TextField nameField = new TextField(selectedUser.getName());
            TextField emailField = new TextField(selectedUser.getEmail());
            TextField usernameField = new TextField(selectedUser.getUsername());
            TextField pinField = new TextField(selectedUser.getPin());

            // Create layout for dialog
            GridPane grid = new GridPane();
            grid.add(new Label("UserID:"), 0, 0);
            grid.add(idField, 1, 0);
            grid.add(new Label("Name:"), 0, 1);
            grid.add(nameField, 1, 1);
            grid.add(new Label("Email:"), 0, 2);
            grid.add(emailField, 1, 2);
            grid.add(new Label("Username:"), 0, 3);
            grid.add(usernameField, 1, 3);
            grid.add(new Label("Pin:"), 0, 4);
            grid.add(pinField, 1, 4);

            dialog.getDialogPane().setContent(grid);

            // Add buttons to the dialog
            ButtonType updateButtonType = new ButtonType("Update", ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == updateButtonType) {
                    selectedUser.setEmail(emailField.getText());
                    selectedUser.setUsername(usernameField.getText());
                    selectedUser.setPin(pinField.getText());

                    return selectedUser;
                }
                return null;
            });

            Optional<UserInfo> result = dialog.showAndWait();

            if (result.isPresent()) {
                // Update the user in the database
                String sql = "UPDATE admin SET Name = ?, Email = ?, Username = ?, PIN = ? WHERE UserID = ?";
                ps = con.prepareStatement(sql);
                ps.setString(1, selectedUser.getName());
                ps.setString(2, selectedUser.getEmail());
                ps.setString(3, selectedUser.getUsername());
                ps.setString(4, selectedUser.getPin());
                ps.setString(5, idField.getText());
                
                

                // Execute the update query
                int rowsUpdated = ps.executeUpdate();

                if (rowsUpdated > 0) {
                    showInformation("Admin", "Update Success", "User Data Successfully Updated.");
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("No Rows Updated");
                    alert.setContentText("No matching data found for update.");
                    alert.showAndWait();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Row Selected");
            alert.setContentText("Please select a row to update.");
            alert.showAndWait();
        }
    } catch (ClassNotFoundException | SQLException e) {
        showError("Exception", null, "Exception is " + e.getMessage());
    } finally {
        try {
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
        showAllUsers();
    }    
    
}
