package org.alsception.bootboard.repositories;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.HashMap;
import org.alsception.bootboard.entities.BBCard;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import org.alsception.bootboard.entities.BBBoard;
import org.alsception.bootboard.entities.BBList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

@Repository
public class BoardRepository {

    private final JdbcTemplate jdbcTemplate;
    
    private static final String TABLE_NAME = "`boards`";
    private static final String SELECT_CLAUSE = "SELECT * FROM "+TABLE_NAME+ " ";
    private static final String WHERE_ID = " WHERE `id` = ?";
    private static final String ORDER_BY = " ORDER BY CASE WHEN `position` > 0 THEN 0 ELSE 1 END ASC, `position` ASC, `id` ASC";
    
    @Autowired
    private CardRepository cardRepository;
    
    @Autowired
    private ListRepository listRepository;

    public BoardRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }   

    public BBBoard create(BBBoard board) throws Exception
    {
        String sql = "INSERT INTO " + TABLE_NAME + " (user_id, title, color, type, position) VALUES (?, ?, ?, ?, ?)";
        System.out.println(sql);        
        // Using KeyHolder to capture the generated key
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, board.getUserId());
            ps.setString(2, board.getTitle());
            ps.setString(3, board.getColor().toLowerCase());
            ps.setString(4, board.getType());
            ps.setInt(5, board.getPosition());
            return ps;
        }, keyHolder);

        // Retrieve the generated ID
        long generatedId = keyHolder.getKey().longValue();

        // Fetch the complete object from the database
        BBBoard createdBoard = findById(generatedId).orElseThrow(() -> new Exception("Error creating board. Could not load new board from database ERR60"));

        //addCards(createdBoard);
        
        //again, with cards, if requested
        if(createdBoard.getType().startsWith("ADD")){
            //createdBoard = findById(generatedId).orElseThrow(() -> new Exception("Error creating board. Could not load new card from database ERR66"));
        }
        
        return createdBoard;
        
    }

    private void addCards(BBBoard createdBoard) {
        //now if requested by client:
        //create n cards.

        String type = createdBoard.getType();
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
//                            card.setListId(createdList.getId());
//                            card.setUserId(createdList.getUserId());
//                            card.setTitle(createdList.getTitle());
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
        if (input.startsWith("ADD")) {
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
    
    //1. Load board without lists
    public Optional<BBBoard> findById(Long id) {
        String sql = SELECT_CLAUSE + WHERE_ID;
        try {
            BBBoard cboard = jdbcTemplate.queryForObject(sql, new Object[]{id}, cboardRowMapper());
            return Optional.of(cboard); // Wrap the result in Optional
        } catch (EmptyResultDataAccessException e) {
            System.out.println("No card board found with id: " + id);
            return Optional.empty(); // Return an empty Optional if no result is found
        }
    }
    
    //2. Load board with lists -- todo: fix
//    public Optional<BBBoard> findByIdWithCards(Long id) 
//    {
//        Optional<BBBoard> cardBoard = this.findById(id);
//        
//        if(cardBoard.isPresent())
//        {
//            try{
//                //Optional<List<BBCard>> cards = this.cardRepository.findByBoardId(id);
//                if(cards.isPresent())
//                {
//                   // cardBoard.get().setCards(cards.get());
//                }
//            }catch(Exception e){
//                System.out.println("Error loading cards for board "+id);
//                e.printStackTrace();
//            }            
//        }
//        return cardBoard;
//    }    
//    
    public List<BBCard> findCardsForList(Long id) 
    {
        Optional<BBList> cardList = null;//this.findById(id);
        
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
    
    public Optional<BBBoard> findByIdWithLists(Long id) 
    {
        Optional<BBBoard> board = this.findById(id);
        
        if(board.isPresent())
        {
            try{
                Optional<List<BBList>> lists = this.listRepository.findByBoardId(id);
                if(lists.isPresent())
                {
                    board.get().setLists(lists.get());
                }
            }catch(Exception e){
                System.out.println("Error loading lists for board "+id);
                e.printStackTrace();
            }            
        }
        return board;
    }    
    
    public Optional<List<BBBoard>> findAll() 
    {
        String sql = SELECT_CLAUSE + ORDER_BY;
        List<BBBoard> clc = jdbcTemplate.query(sql, (rs, rowNum) ->                                
            new BBBoard(
                rs.getLong("id"),                         
                rs.getLong("user_id"),                         
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
//        
//    public List<BBList> findAllWithCards() {
//        Optional<List<BBList>> cardList = this.findAll();
//        if(cardList.isPresent())
//        {
//            cardList.get().forEach(cl -> {
//                List<BBCard> cards = findCardsForList(cl.getId());
//                cl.setCards(cards);
//            });
//        }
//        return cardList.get();
//    }    
    
    
    // RowMapper to map result set to BBCard object
    private RowMapper<BBBoard> cboardRowMapper() {
        return (rs, rowNum) -> {
            BBBoard cboard = new BBBoard();
            cboard.setId(rs.getLong("id"));
            cboard.setUserId(rs.getLong("user_id"));
            cboard.setTitle(rs.getString("title"));
            cboard.setColor(rs.getString("color"));
            cboard.setType(rs.getString("type"));
            cboard.setPosition(rs.getInt("position"));
            return cboard;
        };
    }
    
    // Method to update an cboard by ID
    public int update(BBBoard e) {
        String sql = "UPDATE " + TABLE_NAME + " SET "
                + "user_id = ?, "
                + "title = ?, "
                + "color = ?, "
                + "type = ?, "
                + "position = ?, "
                + "updated_at = CURRENT_TIMESTAMP "
                + WHERE_ID;        
        return jdbcTemplate.update(sql, e.getUserId(), e.getTitle(), e.getColor(), e.getType(), e.getPosition(), e.getId());
    }
    
    //This can be generalized
    public int delete(Long id) {
        String sql = "DELETE FROM " + TABLE_NAME + WHERE_ID;
        return jdbcTemplate.update(sql, id);
    }
    
}
