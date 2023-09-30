
package students;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

public class MainDashboardController implements Initializable {
    
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    private FileChooser filechooser = new FileChooser();
    private File file;
    
    int q, i, id, deleteItem;
    
    @FXML
    private FontAwesomeIcon ico;
    
    /* Table */
    
    @FXML
    private TableView<Student> studentTable;
    @FXML
    private TableColumn<Student, Integer> idCol;
    @FXML
    private TableColumn<Student, String> student_idCol;
    @FXML
    private TableColumn<Student, String> nameCol;
    @FXML
    private TableColumn<Student, String> genderCol;
    @FXML
    private TableColumn<Student, String> dobCol;
    @FXML
    private TableColumn<Student, String> courseCol;
    @FXML
    private TableColumn<Student, String> civil_statusCOl;
    
    /* Table */
    
    /* Input Fields */ 
    
        @FXML
    private JFXTextField fnameF;

    @FXML
    private JFXTextField mnameF;

    @FXML
    private JFXTextField lnameF;

    @FXML
    private JFXTextField suffixF;

    @FXML
    private JFXComboBox<String> courseF;
    
    private ToggleGroup gender;

    @FXML
    private JFXRadioButton maleBtn;
    
    @FXML
    private JFXRadioButton femaleBtn;
    
    @FXML
    private JFXRadioButton otherBtn;
    
    @FXML
    private JFXDatePicker dobF;

    @FXML
    private JFXTextField ageF;
    
    @FXML
    private ImageView picture;
    private FileInputStream fis;

    @FXML
    private JFXComboBox<String> csF;

    @FXML
    private JFXTextField czF;
    
    @FXML
    private JFXTextField studidF;
    
    @FXML
    private JFXTextField fatherF1;

    @FXML
    private JFXTextField fatherF2;

    @FXML
    private JFXTextField fatherF3;

    @FXML
    private JFXTextField fatherF4;

    @FXML
    private JFXTextField motherF1;

    @FXML
    private JFXTextField motherF2;

    @FXML
    private JFXTextField motherF3;

    @FXML
    private JFXTextField motherF4;

    @FXML
    private JFXTextField guardianF1;

    @FXML
    private JFXTextField guardianF2;

    @FXML
    private JFXTextField guardianF3;

    @FXML
    private JFXTextField guardianF4;

    @FXML
    private JFXTextField addressF;

    @FXML
    private JFXTextField elemF1;

    @FXML
    private JFXTextField elemF2;

    @FXML
    private JFXTextField elemF3;

    @FXML
    private JFXTextField jhsF1;

    @FXML
    private JFXTextField jhsF2;

    @FXML
    private JFXTextField jhsF3;

    @FXML
    private JFXTextField shsF1;

    @FXML
    private JFXTextField shsF2;

    @FXML
    private JFXTextField shsF3;
    
    /* Input Fields */
    
    ObservableList<String> options = FXCollections.observableArrayList(
            
            "Single", "Married");
    
    
    public void setUpPic(MouseEvent event) {
       
        filechooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images Files", "*.png", "*.jpg"));
        file = filechooser.showOpenDialog(null);
        
        if (file != null) {
            
            Image img = new Image(file.toURI().toString(), 300, 300, true, true);
            picture.setImage(img);
            picture.setPreserveRatio(true);
            
        }
    }
    
    public void clearAllFields() {
        
                fnameF.clear();
                mnameF.clear();
                lnameF.clear();
                suffixF.clear();
                courseF.getSelectionModel().clearSelection();
                gender.selectToggle(null);
                csF.getSelectionModel().clearSelection();
                dobF.getEditor().clear();
                Image img = new Image("/images/8380015.jpg");
                picture.setImage(img);
                studidF.setText(String.valueOf(generateStudentID()));
        
    }
    
    public void admitStudent(MouseEvent event) throws ClassNotFoundException, SQLException, FileNotFoundException {
        
        String fname = fnameF.getText();
        String mname = mnameF.getText();
        String lname = lnameF.getText();
        String suffix = suffixF.getText();
        
        String name = fname + " " + mname + " " + lname + " " + suffix;
        
        gender = new ToggleGroup();
        
        RadioButton selectGender = (RadioButton)gender.getSelectedToggle();
        String selectedGender = selectGender != null ? selectGender.getText() : "";

        String selectedValue = courseF.getSelectionModel().getSelectedItem();
        
        String dob = dobF.getEditor().getText();
        
      
        String selectedCourse = csF.getValue();
        
        String civil_stat = csF.getValue();
        
        if (fnameF.getText().isEmpty() || lnameF.getText().isEmpty() || selectedGender == null || courseF == null || dob.isEmpty() || civil_stat == null) {
            
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setHeaderText("Error in creating account.");
            a.setContentText("Sorry, your account is not created. There is some technical issue.");
            a.showAndWait();
            
        }
        else {
            try {
                Class.forName("org.sqlite.JDBC");
                con = DriverManager.getConnection("jdbc:sqlite:EnrollmentSystem.db");

                String sql = "INSERT INTO waitlisted(Student_ID, Name, Gender, Date_Of_Birth, Course, Civil_Status, Picture) VALUES(?,?,?,?,?,?,?)";
                ps = con.prepareStatement(sql);

                rs = ps.executeQuery();

                ps.setString(1, studidF.getText());
                ps.setString(2, name);
                ps.setString(3, selectedGender);
                ps.setString(4, dob);
                ps.setString(5, selectedCourse);
                ps.setString(6, civil_stat);
                
                fis = new FileInputStream(file);
                ps.setBinaryStream(16, (InputStream)fis, (int) file.length());
                
                int prepareStatementExecute = ps.executeUpdate();
                
                if(prepareStatementExecute > 0) {
                    
                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setTitle("Success");
                    a.setHeaderText("Student Successfully Admitted.");
                    a.setContentText(null);
                    a.showAndWait();
                    
                    clearAllFields();
                    updateTable();
                }
                else {
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Error");
                    a.setHeaderText("Student can't admit.");
                    a.setContentText("Sorry, student can't admit. There is some error. TRY AGAIN!!!");
                    a.showAndWait();
                }
                

            }
            catch(Exception e) {
                e.printStackTrace(); 
            
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Error");
                a.setHeaderText("Error in creating account.");
                a.setContentText("Sorry, your account is not created. There is some technical issue." + e.getMessage());
                a.showAndWait();
            }
            finally {
                try {
                    // Close the resources in a finally block to ensure they are always closed
                    if (rs != null) {
                        rs.close();
                    }
                    if (ps != null) {
                        ps.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } catch (SQLException e) {
                    // Handle any SQLException that might occur during resource closing
                    e.printStackTrace();
                }
            }
        }
        
        
       
    }
    
    
    public void updateTable() throws SQLException, ClassNotFoundException {
        
                    
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        student_idCol.setCellValueFactory(new PropertyValueFactory<>("student_id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        dobCol.setCellValueFactory(new PropertyValueFactory<>("birthdate"));
        courseCol.setCellValueFactory(new PropertyValueFactory<>("course"));
        civil_statusCOl.setCellValueFactory(new PropertyValueFactory<>("civil_status"));
        
        ObservableList<Student> list = FXCollections.observableArrayList();

        
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:EnrollmentSystem.db");
            
            String sql = "SELECT * FROM waitlisted";
            ps = con.prepareStatement(sql);
            
            rs = ps.executeQuery();
            while (rs.next()){
                
                list.add(new Student(rs.getInt("ID"), rs.getString("Student_ID"), rs.getString("Name"), rs.getString("Gender"), rs.getString("Date_Of_Birth"), rs.getString("Course"), rs.getString("Civil_Status")));
                
            }

        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        finally {
             try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } 
            catch (SQLException e) {
                
            }
        }
    }
     
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

     public int generateStudentID() {
        
        Random rand = new Random();
        int num = rand.nextInt(9999) + 1;
        String nums = Integer.toString(num);
        String number = "000" + nums;
        int studentID = Integer.parseInt(number);
        return studentID;
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        csF.setItems(options);
        studidF.setText(String.valueOf(generateStudentID()));
        studidF.setEditable(false);
    }    
    
}
