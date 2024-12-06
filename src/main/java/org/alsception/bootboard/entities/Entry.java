package org.alsception.bootboard.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //Lombook for code injection (getters and setters)
@NoArgsConstructor
public class Entry implements Serializable 
{
    private Long id;   
    private Long userId;  
    private Long listId;  
    private String text;
    private String title;    
    private String color;    
    private int position;
        
    public Entry(Long id){
        this.id = id;
    }
    
    public Entry(String text){
        this.text = text;
    }
    
    public Entry(Long id, String text){
        this.id = id;
        this.text = text;
    }
    
    @JsonCreator
    public Entry(
            @JsonProperty("id") Long id, 
            @JsonProperty("userId") Long userId, 
            @JsonProperty("listId") Long listId, 
            @JsonProperty("text") String text, 
            @JsonProperty("title") String title, 
            @JsonProperty("color") String color, 
            @JsonProperty("position") int position)
    {
        this.id = id;
        this.userId = userId;
        this.listId = listId; 
        this.text = text;
        this.title = title;
        this.color = color;
        this.position = position;
    }
}