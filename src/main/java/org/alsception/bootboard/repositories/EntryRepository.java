package org.alsception.bootboard.repositories;

import org.alsception.bootboard.entities.Entry;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;

@Repository
public class EntryRepository {

    private final JdbcTemplate jdbcTemplate;
    
    private String TABLE_NAME = "entries ";
    private String SELECT_CLAUSE = "SELECT * FROM "+TABLE_NAME;
    private String WHERE_ID = " WHERE id = ?";

    public EntryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }   

    public int create(Entry entry) {
        String sql = "INSERT INTO " + TABLE_NAME + " (user_id, list_id, text, title, color, position) VALUES (?, ?, ?, ?, ?, ?)";
        System.out.println(sql);
        return jdbcTemplate.update(sql, entry.getUserId(), entry.getListId(), entry.getText(), entry.getTitle(), entry.getColor(), entry.getPosition());
    }
    
    public List<Entry> findAll() {
        String sql = SELECT_CLAUSE;
        return jdbcTemplate.query(sql, (rs, rowNum) ->                                
            new Entry(
                rs.getLong("id"),                         
                rs.getLong("user_id"),                         
                rs.getLong("list_id"),                         
                rs.getString("text"), 
                rs.getString("title"),                         
                rs.getString("color"),
                rs.getInt("position")
            ));
    }
    
    public Optional<Entry> findById(Long id) {
        String sql = SELECT_CLAUSE + WHERE_ID;
        try {
            Entry entry = jdbcTemplate.queryForObject(sql, new Object[]{id}, entryRowMapper());
            return Optional.of(entry); // Wrap the result in Optional
        } catch (EmptyResultDataAccessException e) {
            System.out.println("No entry found with id: " + id);
            return Optional.empty(); // Return an empty Optional if no result is found
        }
    }
    
    // RowMapper to map result set to Entry object
    private RowMapper<Entry> entryRowMapper() {
        return (rs, rowNum) -> {
            Entry entry = new Entry();
            entry.setId(rs.getLong("id"));
            entry.setListId(rs.getLong("list_id"));
            entry.setUserId(rs.getLong("user_id"));
            entry.setText(rs.getString("text"));
            entry.setTitle(rs.getString("title"));
            entry.setColor(rs.getString("color"));
            entry.setPosition(rs.getInt("position"));
            return entry;
        };
    }
    
    // Method to update an entry by ID
    public int update(Entry e) {
        String sql = "UPDATE " + TABLE_NAME + " SET "
                + "user_id = ?, "
                + "list_id = ?, "
                + "text = ?, "
                + "title = ?, "
                + "color = ?, "
                + "position = ?, "
                + "updated_at = CURRENT_TIMESTAMP "
                + WHERE_ID;        
        return jdbcTemplate.update(sql, e.getUserId(), e.getListId(), e.getText(), e.getTitle(), e.getColor(), e.getPosition(), e.getId());
    }
    
    public int delete(Long id) {
        String sql = "DELETE FROM " + TABLE_NAME + WHERE_ID;
        return jdbcTemplate.update(sql, id);
    }
    
}
