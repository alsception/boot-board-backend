package org.alsception.bootboard.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //Lombook for code injection (getters and setters)
@NoArgsConstructor
public class BBBoard implements Serializable 
{
    private Long id;   
    private Long userId;  
    private String title;    
    private String color;    
    private String type;    
    private int position;
    
    private LocalDateTime created;
    private LocalDateTime updated;
    
    private List<List> lists;
    
    public BBBoard(Long id){
        this.id = id;
    }
    
    public BBBoard(String title){
        this.title = title;
    }
    
    public BBBoard(Long id, String title){
        this.id = id;
        this.title = title;
    }
    
    @JsonCreator
    public BBBoard(
            @JsonProperty("id") Long id, 
            @JsonProperty("userId") Long userId, 
            @JsonProperty("boardId") Long boardId, 
            @JsonProperty("title") String title, 
            @JsonProperty("color") String color, 
            @JsonProperty("type") String type, 
            @JsonProperty("position") int position,
            @JsonProperty("created") LocalDateTime created,
            @JsonProperty("updated") LocalDateTime updated)
    {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.color = color;
        this.type = type;
        this.position = position;        
        this.created = created;
        this.updated = updated;      
    }
}