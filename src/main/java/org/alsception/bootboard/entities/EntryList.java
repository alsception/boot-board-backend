package org.alsception.bootboard.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //Lombook for code injection (getters and setters)
@NoArgsConstructor
public class EntryList implements Serializable 
{
    private Long id;   
    private Long userId;  
    private Long boardId;  
    private String title;    
    private String color;    
    private int position;
    private List<Entry> entries;
        
    public EntryList(Long id){
        this.id = id;
    }
    
    public EntryList(String title){
        this.title = title;
    }
    
    public EntryList(Long id, String title){
        this.id = id;
        this.title = title;
    }
    
    @JsonCreator
    public EntryList(
            @JsonProperty("id") Long id, 
            @JsonProperty("userId") Long userId, 
            @JsonProperty("boardId") Long boardId, 
            @JsonProperty("title") String title, 
            @JsonProperty("color") String color, 
            @JsonProperty("position") int position)
    {
        this.id = id;
        this.userId = userId;
        this.boardId = boardId; 
        this.title = title;
        this.title = title;
        this.color = color;
        this.position = position;
    }
}