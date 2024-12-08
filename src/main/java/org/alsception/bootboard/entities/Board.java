package org.alsception.bootboard.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //Lombook for code injection (getters and setters)
@NoArgsConstructor
public class Board implements Serializable 
{
    private Long id;   
    private Long userId;  
    private String title;    
    private String color;    
    private String type;    
    private int position;
    private List<CardList> lists;
        
    public Board(Long id){
        this.id = id;
    }
    
    public Board(String title){
        this.title = title;
    }
    
    public Board(Long id, String title){
        this.id = id;
        this.title = title;
    }
    
    @JsonCreator
    public Board(
            @JsonProperty("id") Long id, 
            @JsonProperty("userId") Long userId, 
            @JsonProperty("boardId") Long boardId, 
            @JsonProperty("title") String title, 
            @JsonProperty("color") String color, 
            @JsonProperty("type") String type, 
            @JsonProperty("position") int position)
    {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.color = color;
        this.type = type;
        this.position = position;
    }
}