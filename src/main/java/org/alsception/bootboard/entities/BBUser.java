package org.alsception.bootboard.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //Lombook for code injection (getters and setters)
@NoArgsConstructor
public class BBUser implements Serializable 
{
    private Long id;   
    private String username;
    
    @JsonIgnore
    private String password;
    
    private String firstName;
    private String lastName;    
    
    private String email;
    private boolean active;    
        
    private LocalDateTime created;
    private LocalDateTime updated;
    
    public BBUser(Long id){
        this.id = id;
    }
    
    public BBUser(String username){
        this.username = username;
    }
    
    public BBUser(Long id, String username){
        this.id = id;
        this.username = username;
    }
    
    @JsonCreator
    public BBUser(
            @JsonProperty("id") Long id, 
            @JsonProperty("username") String username, 
            /*@JsonProperty("password")*/ String password, 
            @JsonProperty("firstName") String firstName, 
            @JsonProperty("lastName") String lastName, 
            @JsonProperty("email") String email,           
            @JsonProperty("active") boolean active,
            @JsonProperty("created") LocalDateTime created,
            @JsonProperty("updated") LocalDateTime updated)
    {
        this.id = id;
        this.username = username;
        this.password = password;        
        this.email = email;        
        this.firstName = firstName;
        this.lastName = lastName;        
        this.active = active;
        this.created = created;
        this.updated = updated;   
    }
}