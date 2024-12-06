package org.alsception.bootboard.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //Lombook for code injection (getters and setters)
@NoArgsConstructor
public class User implements Serializable 
{
    private Long id;   
    private String username;
    
    @JsonIgnore
    private String password;
    
    private String firstName;
    private String lastName;    
    
    private String email;
    private boolean active;    
    
    public User(Long id){
        this.id = id;
    }
    
    public User(String username){
        this.username = username;
    }
    
    public User(Long id, String username){
        this.id = id;
        this.username = username;
    }
    
    @JsonCreator
    public User(
            @JsonProperty("id") Long id, 
            @JsonProperty("username") String username, 
            /*@JsonProperty("password")*/ String password, 
            @JsonProperty("firstName") String firstName, 
            @JsonProperty("lastName") String lastName, 
            @JsonProperty("email") String email,           
            @JsonProperty("active") boolean active
    ){
        this.id = id;
        this.username = username;
        this.password = password;        
        this.email = email;        
        this.firstName = firstName;
        this.lastName = lastName;        
        this.active = active;
    }
}