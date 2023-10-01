
package createacct;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import login.Banking;

public class CreateAccountController implements Initializable {
    
    private FileChooser filechooser = new FileChooser();
    private File file;
    
    @FXML
    private ImageView pic;
    private FileInputStream fis;
    
    // TEXTFIELDS
    @FXML
    private TextField name;
    @FXML
    private TextField idcardno;
    @FXML
    private TextField mobileno;
    @FXML
    private TextField city;
    @FXML
    private TextField address;
    @FXML
    private TextField acn;
    @FXML
    private TextField balance;
    @FXML
    private TextField answer;
    
    // DATEPICKER
    @FXML
    private DatePicker dob;
    
    // PASWORD FIELD
    @FXML
    private PasswordField pin;
    
    // COMBOBOXES
    @FXML
    private ComboBox<String> gender;
    @FXML
    private ComboBox<String> maritalstatus;
    @FXML
    private ComboBox<String> religion;
    @FXML
    private ComboBox<String> accttypes;
    @FXML
    private ComboBox<String> questions;
    
    
    ObservableList<String> list = FXCollections.observableArrayList("Male", "Female", "Other");
    ObservableList<String> list1 = FXCollections.observableArrayList("Single", "Married");
    ObservableList<String> list2 = FXCollections.observableArrayList("Islam", "Christian", "Hindu", "Others");
    ObservableList<String> list3 = FXCollections.observableArrayList("Saving","Current");
    ObservableList<String> list4 = FXCollections.observableArrayList("What is your pet name?", "Who is your childhood friend?", "What is your mother maiden name?","What is your nickname?");
    
    
    public void backToLogIn(MouseEvent event) throws IOException {
        
        Banking.stage.getScene().setRoot(FXMLLoader.load(getClass().getResource("/login/LoginScreen.fxml")));
    }
    
    public void closeApp(MouseEvent event) {
        Platform.exit();
        System.exit(0);
    }
    
    public void setUpPic(MouseEvent event) {
        
        filechooser.getExtensionFilters().add(new ExtensionFilter("Images Files", "*.png", "*.jpg"));
        file = filechooser.showOpenDialog(null);
        
        if (file != null) {
            
            Image img = new Image(file.toURI().toString(), 150, 150, true, true);
            pic.setImage(img);
            pic.setPreserveRatio(true);
            
        }
    }
    
    public boolean validateName() {
        Pattern pattern = Pattern.compile("[a-zA-Z ]+");
        Matcher matcher = pattern.matcher(name.getText());
        
        if(matcher.find() && matcher.group().equals(name.getText())) {
            return true;
        }
        else {
            Alert a = new Alert(AlertType.ERROR);
            a.setTitle("Invalid Name");
            a.setHeaderText(null);
            a.setContentText("Sorry, your name is invalid, enter character only. TRY AGAIN!!!");
            a.showAndWait();
            return false;
        }
    }
    public boolean validateMobileNo() {
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(mobileno.getText());
        
        if(matcher.find() && matcher.group().equals(mobileno.getText())) {
            return true;
        }
        else {
            
            Alert a = new Alert(AlertType.ERROR);
            a.setTitle("Invalid Mobile Number");
            a.setHeaderText(null);
            a.setContentText("Sorry, your Mobile Number is invalid, enter valid numbers only. TRY AGAIN!!!");
            a.showAndWait();
            return false;
        }
    }
    public boolean validateIDCardNo() {
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(idcardno.getText());
        
        if(matcher.find() && matcher.group().equals(idcardno.getText())) {
            return true;
        }
        else {
            Alert a = new Alert(AlertType.ERROR);
            a.setTitle("Invalid ID Card Number");
            a.setHeaderText(null);
            a.setContentText("Sorry, your ID card number is invalid, enter valid numbers only. TRY AGAIN!!!");
            a.showAndWait();
            return false;
        }
    }
    public boolean validateBalance() {
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(balance.getText());
        
        if(matcher.find() && matcher.group().equals(balance.getText())) {
            return true;
        }
        else {
            Alert a = new Alert(AlertType.ERROR);
            a.setTitle("Invalid Balance");
            a.setHeaderText(null);
            a.setContentText("Sorry, your Balance is invalid, enter valid numbers only. TRY AGAIN!!!");
            a.showAndWait();
            return false;
        }
    }
    
    public void clearAllFields() {
        
                name.clear();
                idcardno.clear();
                mobileno.clear();
                gender.setValue(null);
                religion.setValue(null);
                maritalstatus.setValue(null);
                dob.getEditor().clear();
                city.clear();
                address.clear();
                acn.clear();
                pin.clear();
                accttypes.setValue(null);
                balance.clear();
                questions.setValue(null);
                answer.clear();
                Image img = new Image("/images/userDefaultPic.png");
                pic.setImage(img);
                acn.setText(String.valueOf(generateAccountNo()));
        
    }
    
    public int generateAccountNo() {
        
        Random rand = new Random();
        int num = rand.nextInt(899999) + 100000;
        return num;
    }
    
    public void newAcctount(MouseEvent event) {
        Connection con = null;
        PreparedStatement ps = null;
        
        try {
            
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:BankDB.db");
            
            if(validateName() &&  validateMobileNo() && validateIDCardNo() && validateBalance()){
            
                String sql = "INSERT INTO investorsData (Name, ICN, MobileNo, Gender, Religion, MaritalStatus, DOB, City, Address, ACN, PIN, AcctType, Balance, SecQuestion, Answer, ProfilePic) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                ps = con.prepareStatement(sql);

                ps.setString(1, name.getText());
                ps.setString(2, idcardno.getText());
                ps.setString(3, mobileno.getText());
                ps.setString(4, gender.getValue());
                ps.setString(5, religion.getValue());
                ps.setString(6, maritalstatus.getValue());
                ps.setString(7, ((TextField) dob.getEditor()).getText());
                ps.setString(8, city.getText());
                ps.setString(9, address.getText());
                ps.setString(10, acn.getText());
                ps.setString(11, pin.getText());
                ps.setString(12, accttypes.getValue());
                ps.setString(13, balance.getText());
                ps.setString(14, questions.getValue());
                ps.setString(15, answer.getText());

                fis = new FileInputStream(file);
                ps.setBinaryStream(16, (InputStream)fis, (int) file.length());

                int i = ps.executeUpdate();

                if (i > 0 ){
                    Alert a = new Alert(AlertType.INFORMATION);
                    a.setTitle("Success");
                    a.setHeaderText("Account Successfully Created.");
                    a.setContentText("Thank you for registering to our Bank. Your account has successfully registered. You can now login using your Account Number and PIN.");
                    a.showAndWait();
                }
                else {
                    Alert a = new Alert(AlertType.ERROR);
                    a.setTitle("Error");
                    a.setHeaderText("Account Not Successfully Created.");
                    a.setContentText("Sorry, your account is not created. There is some error. TRY AGAIN!!!");
                    a.showAndWait();
                }
                clearAllFields();
            }
            
        } catch(Exception e) {
            e.printStackTrace(); 
            
            Alert a = new Alert(AlertType.ERROR);
            a.setTitle("Error");
            a.setHeaderText("Error in creating account.");
            a.setContentText("Sorry, your account is not created. There is some technical issue." + e.getMessage());
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
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        gender.setItems(list);
        maritalstatus.setItems(list1);
        religion.setItems(list2);
        accttypes.setItems(list3);
        questions.setItems(list4);
        acn.setText(String.valueOf(generateAccountNo()));
        acn.setEditable(false);
        
    }    
    
}
