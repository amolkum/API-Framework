package utils;

import lombok.Getter;

@Getter
public class PojoClass {
    /*Getters and Setters*/
    private String id;
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String expectedResponse;
    private String password;
    private String phoneNumber;
    private String userStatus;

    public void setId(String id){
        this.id = id;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setFirstName(String firstName){
        this.firstName=firstName;
    }
    public void setLastName(String lastName){
        this.lastName=lastName;
    }
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber=phoneNumber;
    }
    public void setPassword(String password){
        this.password=password;
    }
    public void setUserStatus(String userStatus){
        this.userStatus=userStatus;
    }
    public void setExpectedResponse(String expectedResponse){
        this.expectedResponse=expectedResponse;
    }
    public void setEmail(String email){
        this.email=email;
    }
}
