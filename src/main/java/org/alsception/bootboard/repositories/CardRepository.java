package org.alsception.bootboard.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import org.alsception.bootboard.entities.BBCard;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    public int create(BBCard card) {
        String sql = "INSERT INTO " + TABLE_NAME + " (user_id, list_id, title, description, color, type, position) VALUES (?, ?, ?, ?, ?, ?, ?)";
        System.out.println(sql);
        return jdbcTemplate.update(sql, card.getUserId(), card.getListId(), card.getTitle(), card.getDescription(), card.getColor(), card.getType(), card.getPosition());
    }
    
    public List<BBCard> findAll() {
        String sql = SELECT_CLAUSE;
        return jdbcTemplate.query(sql, (rs, rowNum) ->                                
            new BBCard(
                rs.getLong("id"),                         
                rs.getLong("user_id"),                         
                rs.getLong("list_id"),                         
                rs.getString("title"),    
                rs.getString("description"), 
                rs.getString("color"),
                rs.getString("type"), 
                rs.getInt("position"),
                rs.getTimestamp("created").toLocalDateTime(),
                rs.getTimestamp("updated").toLocalDateTime()
            ));
    }
    
    public Optional<BBCard> findById(Long id) {
        String sql = SELECT_CLAUSE + WHERE_ID;
        try {
            BBCard card = jdbcTemplate.queryForObject(sql, new Object[]{id}, cardRowMapper());
            
            //get datatime
            
            
            return Optional.of(card); // Wrap the result in Optional
        } catch (EmptyResultDataAccessException e) {
            System.out.println("No card found with id: " + id);
            return Optional.empty(); // Return an empty Optional if no result is found
        }
    }
    
    public void gettime(){
        String sql = "SELECT created FROM cards";
    
        jdbcTemplate.query(sql, resultSet -> {
            try {
                if (resultSet.next()) {
                    Timestamp timestamp = resultSet.getTimestamp("created");
                    LocalDateTime localDateTime = timestamp.toLocalDateTime();
                    System.out.println("LocalDateTime: " + localDateTime);
                }
            } catch (SQLException ex) {
                Logger.getLogger(CardRepository.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    public Optional<List<BBCard>> findByListId(Long listId) 
    {
        System.out.println("findByListId");
        String sql = SELECT_CLAUSE + " WHERE list_id = ?";

        try {
            List<BBCard> cards = jdbcTemplate.query(sql, new Object[]{listId}, // Pass the parameter value for the placeholder
                    (rs, rowNum) -> new BBCard(
                        rs.getLong("id"),
                        rs.getLong("user_id"),
                        rs.getLong("list_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("color"),
                        rs.getString("type"),
                        rs.getInt("position"),
                        rs.getTimestamp("created").toLocalDateTime(),
                        rs.getTimestamp("updated").toLocalDateTime()
                    ));
            return Optional.of(cards); // Wrap the result in Optional
        } catch (EmptyResultDataAccessException e) {
            System.out.println("No card found with list_id: " + listId);
            return Optional.empty(); // Return an empty Optional if no result is found
        }
    }
    
    // RowMapper to map result set to BBCard object
    private RowMapper<BBCard> cardRowMapper() {
        return (rs, rowNum) -> {
            BBCard card = new BBCard();
            card.setId(rs.getLong("id"));
            card.setListId(rs.getLong("list_id"));
            card.setUserId(rs.getLong("user_id"));
            card.setTitle(rs.getString("title"));
            card.setDescription(rs.getString("description"));
            card.setColor(rs.getString("color"));
            card.setType(rs.getString("type"));
            card.setPosition(rs.getInt("position"));
            card.setCreated(rs.getTimestamp("created").toLocalDateTime());
            card.setUpdated(rs.getTimestamp("updated").toLocalDateTime());
            return card;
        };
    }
    
    public int update(BBCard e) {
        String sql = "UPDATE " + TABLE_NAME + " SET "
                + "user_id = ?, "
                + "list_id = ?, "
                + "description = ?, "
                + "title = ?, "
                + "color = ?, "
                + "type = ?, "
                + "position = ?, "
                + "updated = CURRENT_TIMESTAMP "
                + WHERE_ID;    
        System.out.println(sql);
        return jdbcTemplate.update(sql, e.getUserId(), e.getListId(), e.getDescription(), e.getTitle(), e.getColor(), e.getType(), e.getPosition(), e.getId());
    }
    
    public int delete(Long id) {
        String sql = "DELETE FROM " + TABLE_NAME + WHERE_ID;
        return jdbcTemplate.update(sql, id);
    }
    
}
