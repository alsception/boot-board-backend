package org.alsception.bootboard.repositories;

import org.alsception.bootboard.entities.Card;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;

@Repository
public class CardRepository {

    private final JdbcTemplate jdbcTemplate;
    
    private static final String TABLE_NAME = "cards";
    private static final String SELECT_CLAUSE = "SELECT * FROM `"+TABLE_NAME+"`";
    private static final String WHERE_ID = " WHERE `id` = ?";

    public CardRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }   

    public int create(Card card) {
        String sql = "INSERT INTO " + TABLE_NAME + " (user_id, list_id, title, text, color, type, position) VALUES (?, ?, ?, ?, ?, ?, ?)";
        System.out.println(sql);
        return jdbcTemplate.update(sql, card.getUserId(), card.getListId(), card.getTitle(), card.getText(), card.getColor(), card.getType(), card.getPosition());
    }
    
    public List<Card> findAll() {
        String sql = SELECT_CLAUSE;
        return jdbcTemplate.query(sql, (rs, rowNum) ->                                
            new Card(
                rs.getLong("id"),                         
                rs.getLong("user_id"),                         
                rs.getLong("list_id"),                         
                rs.getString("title"),    
                rs.getString("text"), 
                rs.getString("color"),
                rs.getString("type"), 
                rs.getInt("position")
            ));
    }
    
    public Optional<Card> findById(Long id) {
        String sql = SELECT_CLAUSE + WHERE_ID;
        try {
            Card card = jdbcTemplate.queryForObject(sql, new Object[]{id}, cardRowMapper());
            return Optional.of(card); // Wrap the result in Optional
        } catch (EmptyResultDataAccessException e) {
            System.out.println("No card found with id: " + id);
            return Optional.empty(); // Return an empty Optional if no result is found
        }
    }
    
    public Optional<List<Card>> findByListId(Long listId) 
    {
        System.out.println("findByListId");
        String sql = SELECT_CLAUSE + " WHERE list_id = ?";

        try {
            List<Card> cards = jdbcTemplate.query(
                    sql, new Object[]{listId}, // Pass the parameter value for the placeholder
                    (rs, rowNum) -> new Card(
                        rs.getLong("id"),
                        rs.getLong("user_id"),
                        rs.getLong("list_id"),
                        rs.getString("title"),
                        rs.getString("text"),
                        rs.getString("color"),
                        rs.getString("type"),
                        rs.getInt("position")
                    ));
            return Optional.of(cards); // Wrap the result in Optional
        } catch (EmptyResultDataAccessException e) {
            System.out.println("No card found with list_id: " + listId);
            return Optional.empty(); // Return an empty Optional if no result is found
        }
    }
    
    // RowMapper to map result set to Card object
    private RowMapper<Card> cardRowMapper() {
        return (rs, rowNum) -> {
            Card card = new Card();
            card.setId(rs.getLong("id"));
            card.setListId(rs.getLong("list_id"));
            card.setUserId(rs.getLong("user_id"));
            card.setTitle(rs.getString("title"));
            card.setText(rs.getString("text"));
            card.setColor(rs.getString("color"));
            card.setType(rs.getString("type"));
            card.setPosition(rs.getInt("position"));
            return card;
        };
    }
    
    // This method will update only list's (meta)data, without touching underlying cards
    public int update(Card e) {
        String sql = "UPDATE " + TABLE_NAME + " SET "
                + "user_id = ?, "
                + "list_id = ?, "
                + "text = ?, "
                + "title = ?, "
                + "color = ?, "
                + "type = ?, "
                + "position = ?, "
                + "updated_at = CURRENT_TIMESTAMP "
                + WHERE_ID;        
        return jdbcTemplate.update(sql, e.getUserId(), e.getListId(), e.getText(), e.getTitle(), e.getColor(), e.getType(), e.getPosition(), e.getId());
    }
    
    public int delete(Long id) {
        String sql = "DELETE FROM " + TABLE_NAME + WHERE_ID;
        return jdbcTemplate.update(sql, id);
    }
    
}
