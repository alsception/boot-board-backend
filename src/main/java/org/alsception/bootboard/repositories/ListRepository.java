package org.alsception.bootboard.repositories;

import org.alsception.bootboard.entities.BBCard;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import org.alsception.bootboard.entities.BBList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;

@Repository
public class ListRepository {

    private final JdbcTemplate jdbcTemplate;
    
    private static final String TABLE_NAME = "lists ";
    private static final String SELECT_CLAUSE = "SELECT * FROM "+TABLE_NAME;
    private static final String WHERE_ID = " WHERE id = ?";
    private static final String ORDER_BY = " ORDER BY CASE WHEN `position` > 0 THEN 0 ELSE 1 END ASC, `position` ASC, `id` ASC";
    
    @Autowired
    private CardRepository cardRepository;

    public ListRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }   

    public int create(BBList clist) {
        String sql = "INSERT INTO " + TABLE_NAME + " (user_id, board_id, title, color, type, position) VALUES (?, ?, ?, ?, ?, ?)";
        System.out.println(sql);
        return jdbcTemplate.update(sql, clist.getUserId(), clist.getBoardId(), clist.getTitle(), clist.getColor(), clist.getType(), clist.getPosition());
    }
    
    //1. Load list without cards
    public Optional<BBList> findById(Long id) {
        String sql = SELECT_CLAUSE + WHERE_ID;
        try {
            BBList clist = jdbcTemplate.queryForObject(sql, new Object[]{id}, clistRowMapper());
            return Optional.of(clist); // Wrap the result in Optional
        } catch (EmptyResultDataAccessException e) {
            System.out.println("No card list found with id: " + id);
            return Optional.empty(); // Return an empty Optional if no result is found
        }
    }
    
    //2. Load list with cards
    public Optional<BBList> findByIdWithCards(Long id) 
    {
        Optional<BBList> cardList = this.findById(id);
        
        if(cardList.isPresent())
        {
            try{
                Optional<List<BBCard>> cards = this.cardRepository.findByListId(id);
                if(cards.isPresent())
                {
                    cardList.get().setCards(cards.get());
                }
            }catch(Exception e){
                System.out.println("Error loading cards for list "+id);
                e.printStackTrace();
            }            
        }
        return cardList;
    }    
    
    public List<BBCard> findCardsForList(Long id) 
    {
        Optional<BBList> cardList = this.findById(id);
        
        if(cardList.isPresent())
        {
            try{
                Optional<List<BBCard>> cards = this.cardRepository.findByListId(id);
                if(cards.isPresent())
                {
                    return cards.get();
                }
            }catch(Exception e){
                System.out.println("Error loading cards for list "+id);
                e.printStackTrace();
                return null;
            }            
        }
        return null;
    }    
    
    public Optional<List<BBList>> findAll() 
    {
        String sql = SELECT_CLAUSE + ORDER_BY;
        List<BBList> clc = jdbcTemplate.query(sql, (rs, rowNum) ->                                
            new BBList(
                rs.getLong("id"),                         
                rs.getLong("user_id"),                         
                rs.getLong("board_id"),                         
                rs.getString("title"),    
                rs.getString("color"),
                rs.getString("type"), 
                rs.getInt("position"),
                rs.getTimestamp("created").toLocalDateTime(),
                rs.getTimestamp("updated").toLocalDateTime(),
                null
            ));
        return Optional.ofNullable(clc);
    }
        
    public List<BBList> findAllWithCards() {
        Optional<List<BBList>> cardList = this.findAll();
        if(cardList.isPresent())
        {
            cardList.get().forEach(cl -> {
                List<BBCard> cards = findCardsForList(cl.getId());
                cl.setCards(cards);
            });
        }
        return cardList.get();
    }    
    
    
    // RowMapper to map result set to BBCard object
    private RowMapper<BBList> clistRowMapper() {
        return (rs, rowNum) -> {
            BBList clist = new BBList();
            clist.setId(rs.getLong("id"));
            clist.setBoardId(rs.getLong("board_id"));
            clist.setUserId(rs.getLong("user_id"));
            clist.setTitle(rs.getString("title"));
            clist.setColor(rs.getString("color"));
            clist.setType(rs.getString("type"));
            clist.setPosition(rs.getInt("position"));
            return clist;
        };
    }
    
    // Method to update an clist by ID
    public int update(BBList e) {
        String sql = "UPDATE " + TABLE_NAME + " SET "
                + "user_id = ?, "
                + "board_id = ?, "
                + "title = ?, "
                + "color = ?, "
                + "type = ?, "
                + "position = ?, "
                + "updated_at = CURRENT_TIMESTAMP "
                + WHERE_ID;        
        return jdbcTemplate.update(sql, e.getUserId(), e.getBoardId(), e.getTitle(), e.getColor(), e.getType(), e.getPosition(), e.getId());
    }
    
    //This can be generalized
    public int delete(Long id) {
        String sql = "DELETE FROM " + TABLE_NAME + WHERE_ID;
        return jdbcTemplate.update(sql, id);
    }
    
}
