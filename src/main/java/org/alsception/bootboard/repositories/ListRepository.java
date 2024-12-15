package org.alsception.bootboard.repositories;

import org.alsception.bootboard.utils.UniqueIdGenerator;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.HashMap;
import org.alsception.bootboard.entities.BBCard;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import org.alsception.bootboard.entities.BBList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

@Repository
public class ListRepository {

    private final JdbcTemplate jdbcTemplate;
    
    private static final String TABLE_NAME = "lists ";
    private static final String SELECT_CLAUSE = "SELECT * FROM "+TABLE_NAME;
    private static final String WHERE_ID = " WHERE id = ?";
    private static final String ORDER_BY = " ORDER BY CASE WHEN position` > 0 THEN 0 ELSE 1 END ASC, `position` ASC, `id` ASC";
    
    @Autowired
    private CardRepository cardRepository;

    public ListRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }   

    public BBList create(BBList list) throws Exception
    {
        list.setId(UniqueIdGenerator.generateNanoId());
        
        String sql = "INSERT INTO " + TABLE_NAME + " (user_id, board_id, title, color, type, position, id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        System.out.println(sql);        
        
        // TODO see card repository
        // Using KeyHolder to capture the generated key
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, list.getUserId());
            ps.setLong(2, list.getBoardId());
            ps.setString(3, list.getTitle());
            ps.setString(4, list.getColor().toLowerCase());
            ps.setString(5, list.getType());
            ps.setInt(6, list.getPosition());
            ps.setLong(7, list.getId());
            return ps;
        }, keyHolder);

        // Fetch the complete object from the database
        BBList createdList = findById(list.getId()).orElseThrow(() -> new Exception("Error creating list. Could not load new card from database ERR60"));

        addCards(createdList);
        
        //again, with cards, if requested
        if(null!=createdList.getType() && createdList.getType().startsWith("ADD")){
            createdList = findById(list.getId()).orElseThrow(() -> new Exception("Error creating list. Could not load new card from database ERR66"));
        }
        
        return createdList;
        
    }

    private void addCards(BBList createdList) {
        //now if requested by client:
        //create n cards.

        String type = createdList.getType();
        if( null != type && !"".equals(type))
        {
            if(type.startsWith("ADD_CARDS")){
                
                stringExtractor(type);
                
                String numberPart = type.replaceAll("\\D+", ""); // Remove all non-digit characters
                try{
                    int numberParam = Integer.parseInt(numberPart);
                    
                    if(numberParam < 0 || numberParam > 999 ){
                        //-1 could be interesting....
                        throw new Exception("Invalid number of ADD_CARDS");
                    }else{
                        for(int i = 0; i < numberParam; i++)
                        {
                            //create card.
                            System.out.println("Creating card "+i);
                            BBCard card = new BBCard();
                            //card.setId(rs.getLong("id"));
                            card.setListId(createdList.getId());
                            card.setUserId(createdList.getUserId());
                            card.setTitle(createdList.getTitle());
                            card.setDescription("Description");
                            card.setColor("color");
                            card.setType("type");
                            card.setPosition(0);
                            this.cardRepository.create(card);
                        }
                    }
                }catch(Exception e){
                    System.out.println("Error extracting number part. ERR71");
                }
            }
        }
    }
    
    private HashMap<String,Object> stringExtractor(String input) 
    {
        //test input would be = "ADD_CARDS_10_DESCRIPTION_ASDASDASD_COLOR_RED_TYPE_X";
        
        HashMap<String,Object> hmParameters = new HashMap<>();
        
        // Ensure string starts with "ADD"
        if (input != null && input.startsWith("ADD")) {
            // Updated regex to match space-separated parts
            String regex = "(?:CARDS (\\d+))?(?: DESCRIPTION ([^ ]+))?(?: COLOR ([^ ]+))?(?: TYPE (\\w+))?";
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
            java.util.regex.Matcher matcher = pattern.matcher(input);

            if (matcher.find()) {
                // Extract the values (or set them to null if not present)
                String number = matcher.group(1);
                String description = matcher.group(2);
                String color = matcher.group(3);
                String type = matcher.group(4);
                
                hmParameters.put("number", number);
                hmParameters.put("description", description);
                hmParameters.put("color", color);
                hmParameters.put("type", type);

                // Print the extracted parts, handling nulls
                System.out.println("Number after 'CARDS': " + (number != null ? number : "Not present"));
                System.out.println("Description: " + (description != null ? description : "Not present"));
                System.out.println("Color: " + (color != null ? color : "Not present"));
                System.out.println("Type: " + (type != null ? type : "Not present"));
            } else {
                System.out.println("No match found.");
            }
        } else {
            System.out.println("String does not start with 'ADD'.");
        }
        
        return hmParameters;
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
    
    public Optional<List<BBList>> findByBoardId(Long boardId) 
    {
        String sql = SELECT_CLAUSE + " WHERE board_id = ?";
        
        try {
            List<BBList> lists = jdbcTemplate.query(sql, new Object[]{boardId}, 
                    (rs, rowNum) -> new BBList(
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
            return Optional.of(lists); // Wrap the result in Optional
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty(); // Return an empty Optional if no result is found
        }
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
