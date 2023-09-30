
package students;

public class Student {
    
    private int id;
    private String student_id;
    private String name;
    private String gender;
    private String birthdate;
    private String course;
    private String civil_status;
    
    public Student(int id, String student_id, String name, String gender, String birthdate, String course, String civil_status) {
        this.id = id;
        this.student_id = student_id;
        this.name = name;
        this.gender = gender;
        this.birthdate = birthdate;
        this.course = course;
        this.civil_status = civil_status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getCivil_status() {
        return civil_status;
    }

    public void setCivil_status(String civil_status) {
        this.civil_status = civil_status;
    }
    
    
}
