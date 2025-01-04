//package org.alsception.bootboard.entities;
//
//import com.fasterxml.jackson.annotation.JsonCreator;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import java.io.Serializable;
//import java.time.LocalDateTime;
//import java.util.List;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data //Lombook for getters and setters
//@NoArgsConstructor
//public class BBEntry implements Serializable 
//{
//    private Long id;   
//    private Long userId;  
//    private Long parentId;  
//    
//    private String parentType;
//    private String title;    
//    private String description;
//    private String color;    
//    private String type;
//    private String objectType;
//    
//    private int position;
//    private boolean active;    
//    
//    private LocalDateTime created;
//    private LocalDateTime updated;
//    
//    private List<BBEntry> children;
//        
//    public BBEntry(Long id){
//        this.id = id;
//    }
//    
//    public BBEntry(String title){
//        this.title = title;
//    }
//    
//    public BBEntry(Long id, String description){
//        this.id = id;
//        this.description = description;
//    }    
//
//    @JsonCreator
//    public BBEntry(Long id, Long userId, Long parentId, String parentType, String title, String description, String color, String type, String objectType, int position, boolean active, LocalDateTime created, LocalDateTime updated, List<BBEntry> children) {
//        this.id = id;
//        this.userId = userId;
//        this.parentId = parentId;
//        this.parentType = parentType;
//        this.title = title;
//        this.description = description;
//        this.color = color;
//        this.type = type;
//        this.objectType = objectType;
//        this.position = position;
//        this.active = active;
//        this.created = created;
//        this.updated = updated;
//        this.children = children;
//    }
//}