package org.alsception.bootboard.repositories;

import org.alsception.bootboard.entities.BBUser;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<BBUser> findAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, (rs, rowNum) ->                                
            new BBUser(
                rs.getLong("id"),                         
                rs.getString("username"), 
                rs.getString("password"),                         
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),                    
                rs.getBoolean("active"),
                rs.getTimestamp("created").toLocalDateTime(),
                rs.getTimestamp("updated").toLocalDateTime()
            ));
    }

    public int create(BBUser user) {
        String sql = "INSERT INTO users (username, password, first_name, last_name, email, active) VALUES (?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getEmail(), user.isActive());
    }
}
