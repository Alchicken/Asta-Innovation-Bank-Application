
package transactionhistory;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import login.LoginScreenController;

public class TransactionHistoryController implements Initializable {

    
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    // Withdraw Table History
    @FXML
    private TableView<History> withdraw_table;
    @FXML
    private TableColumn<History, String> withdraw_acn;
    @FXML
    private TableColumn<History, String> withdraw_amount;
    @FXML
    private TableColumn<History, String> withdraw_remainingamount;
    @FXML
    private TableColumn<History, String> withdraw_date;
    @FXML
    private TableColumn<History, String> withdraw_time;
    
    // Deposit Table History
    @FXML
    private TableView<History> deposit_table;
    @FXML
    private TableColumn<History, String> deposit_acn;
    @FXML
    private TableColumn<History, String> deposit_amount;
    @FXML
    private TableColumn<History, String> deposit_newamount;
    @FXML
    private TableColumn<History, String> deposit_date;
    @FXML
    private TableColumn<History, String> deposit_time;
    
    // Transfer Table History
    @FXML
    private TableView<History> transfer_table;
    @FXML
    private TableColumn<History, String> transfer_acn;
    @FXML
    private TableColumn<History, String> transfer_amount;
    @FXML
    private TableColumn<History, String> transfer_sendto;
    @FXML
    private TableColumn<History, String> transfer_date;
    @FXML
    private TableColumn<History, String> transfer_time;
    
     // Recieved Table History
    @FXML
    private TableView<History> recieved_table;
    @FXML
    private TableColumn<History, String> recieved_acn;
    @FXML
    private TableColumn<History, String> recieved_amount;
    @FXML
    private TableColumn<History, String> recieved_from;
    @FXML
    private TableColumn<History, String> recieved_date;
    @FXML
    private TableColumn<History, String> recieved_time;
    
    
    public void withdraw(){
        withdraw_acn.setCellValueFactory(new PropertyValueFactory<History, String>("name"));
        withdraw_amount.setCellValueFactory(new PropertyValueFactory<History, String>("amount"));
        withdraw_remainingamount.setCellValueFactory(new PropertyValueFactory<History, String>("generic"));
        withdraw_date.setCellValueFactory(new PropertyValueFactory<History, String>("date"));
        withdraw_time.setCellValueFactory(new PropertyValueFactory<History, String>("time"));
        
        
        ObservableList<History> list = FXCollections.observableArrayList();
        
        try {
            
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:BankDB.db");

            String sql = "SELECT * FROM investorsWithdraw WHERE ACN = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, LoginScreenController.acc);

            rs = ps.executeQuery();
            while (rs.next()){
                
                list.add(new History(rs.getString("ACN"), rs.getString("WithdrawAmount"), rs.getString("RemainingAmount"), rs.getString("Date"), rs.getString("Time")));
                
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
        withdraw_table.setItems(list);
    }
    
    
    public void deposit(){
        
        deposit_acn.setCellValueFactory(new PropertyValueFactory<History, String>("name"));
        deposit_amount.setCellValueFactory(new PropertyValueFactory<History, String>("amount"));
        deposit_newamount.setCellValueFactory(new PropertyValueFactory<History, String>("generic"));
        deposit_date.setCellValueFactory(new PropertyValueFactory<History, String>("date"));
        deposit_time.setCellValueFactory(new PropertyValueFactory<History, String>("time"));
        
        
        ObservableList<History> list = FXCollections.observableArrayList();
        
        try {
            
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:BankDB.db");

            String sql = "SELECT * FROM investorsDeposit WHERE ACN = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, LoginScreenController.acc);

            rs = ps.executeQuery();
            while (rs.next()){
                
                list.add(new History(rs.getString("ACN"), rs.getString("DepositAmount"), rs.getString("NewAmount"), rs.getString("Date"), rs.getString("Time")));
                
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
        deposit_table.setItems(list);
        
    }
    public void transfer(){
        
        transfer_acn.setCellValueFactory(new PropertyValueFactory<History, String>("name"));
        transfer_amount.setCellValueFactory(new PropertyValueFactory<History, String>("amount"));
        transfer_sendto.setCellValueFactory(new PropertyValueFactory<History, String>("generic"));
        transfer_date.setCellValueFactory(new PropertyValueFactory<History, String>("date"));
        transfer_time.setCellValueFactory(new PropertyValueFactory<History, String>("time"));
        
        
        ObservableList<History> list = FXCollections.observableArrayList();
        
        try {
            
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:BankDB.db");

            String sql = "SELECT * FROM investorsTransfer WHERE ACN = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, LoginScreenController.acc);

            rs = ps.executeQuery();
            while (rs.next()){
                
                list.add(new History(rs.getString("ACN"), rs.getString("TransferAmount"), rs.getString("SendTo"), rs.getString("Date"), rs.getString("Time")));
                
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
        transfer_table.setItems(list);
    }
    public void recieved(){
        
        recieved_acn.setCellValueFactory(new PropertyValueFactory<History, String>("name"));
        recieved_amount.setCellValueFactory(new PropertyValueFactory<History, String>("amount"));
        recieved_from.setCellValueFactory(new PropertyValueFactory<History, String>("generic"));
        recieved_date.setCellValueFactory(new PropertyValueFactory<History, String>("date"));
        recieved_time.setCellValueFactory(new PropertyValueFactory<History, String>("time"));
        
        
        ObservableList<History> list = FXCollections.observableArrayList();
        
        try {
            
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:BankDB.db");

            String sql = "SELECT * FROM investorsTransfer WHERE SendTo = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, LoginScreenController.acc);

            rs = ps.executeQuery();
            while (rs.next()){
                
                list.add(new History(rs.getString("SendTo"), rs.getString("TransferAmount"), rs.getString("ACN"), rs.getString("Date"), rs.getString("Time")));
                
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
        recieved_table.setItems(list);
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
        withdraw();
        deposit();
        transfer();
        recieved();
    }    
    
}
