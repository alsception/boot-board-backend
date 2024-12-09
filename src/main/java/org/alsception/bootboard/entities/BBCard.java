package org.alsception.bootboard.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //Lombook for getters and setters
@NoArgsConstructor
public class BBCard implements Serializable 
{
    private Long id;   
    private Long userId;  
    private Long listId;  
    private String title;    
    private String text;
    private String color;    
    private String type;
    private int position;
    
    private LocalDateTime created;
    private LocalDateTime updated;
        
    public BBCard(Long id){
        this.id = id;
    }
    
    public BBCard(String title){
        this.title = title;
    }
    
    public BBCard(Long id, String text){
        this.id = id;
        this.text = text;
    }
    
    @JsonCreator
    public BBCard(
            @JsonProperty("id") Long id, 
            @JsonProperty("userId") Long userId, 
            @JsonProperty("listId") Long listId, 
            @JsonProperty("title") String title, 
            @JsonProperty("text") String text, 
            @JsonProperty("color") String color, 
            @JsonProperty("type") String type, 
            @JsonProperty("position") int position,
            @JsonProperty("created") LocalDateTime created,
            @JsonProperty("updated") LocalDateTime updated)
    {
        this.id = id;
        this.userId = userId;
        this.listId = listId; 
        this.text = text;
        this.title = title;
        this.color = color;
        this.type = type;
        this.position = position;
        this.created = created;
        this.updated = updated;        
    }
}