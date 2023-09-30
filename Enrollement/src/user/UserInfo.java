
package user;

import java.util.Random;

public final class UserInfo {
    
    
    private String name;
    private String email;
    private String username;
    private String pin;
    private String userID; 
    
    public UserInfo(String name, String email, String username, String pin) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.pin = pin;
    }
    
    public UserInfo(String userID,String name, String email, String username, String pin) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.pin = pin;
        this.userID = userID;
    }

    UserInfo() {
        
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    
    public String UserIDGenerator() {
        
        Random rand = new Random();
        int num = rand.nextInt(999);
        String randnum = Integer.toString(num);
        String custId = "0" + randnum;
        return custId;
    }
   

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPin() {
        return pin;
    }
    
    
    
}
