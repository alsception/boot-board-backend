package org.alsception.bootboard.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //Lombook for code injection (getters and setters)
@NoArgsConstructor
public class CardList implements Serializable 
{
    private Long id;   
    private Long userId;  
    private Long boardId;  
    private String title;    
    private String color;    
    private String type;    
    private int position;
    private List<Card> cards;
        
    public CardList(Long id){
        this.id = id;
    }
    
    public CardList(String title){
        this.title = title;
    }
    
    public CardList(Long id, String title){
        this.id = id;
        this.title = title;
    }
    
    @JsonCreator
    public CardList(
            @JsonProperty("id") Long id, 
            @JsonProperty("userId") Long userId, 
            @JsonProperty("boardId") Long boardId, 
            @JsonProperty("title") String title, 
            @JsonProperty("color") String color, 
            @JsonProperty("type") String type, 
            @JsonProperty("position") int position,
            @JsonProperty("cards") List<Card> cards)
    {
        this.id = id;
        this.userId = userId;
        this.boardId = boardId; 
        this.title = title;
        this.color = color;
        this.type = type;
        this.position = position;
        this.cards = cards;
    }
}