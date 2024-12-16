package org.alsception.bootboard.controllers;

import java.util.List;
import java.util.Optional;
import org.alsception.bootboard.entities.BBBoard;
import org.alsception.bootboard.error.BadRequestException;
import org.alsception.bootboard.repositories.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/boards")
public class BoardsController {

    @Autowired
    private BoardRepository repository;
    
    @PostMapping
    public BBBoard create(@RequestBody BBBoard entry) throws BadRequestException 
    {
        try
        {
            return repository.create(entry);            
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
            throw new BadRequestException(ex.getMessage());
        }  
    }

    @GetMapping("/{id}")
    public Optional<BBBoard> get(@PathVariable Long id) 
    {
        return repository.findById(id);
    }
    
    @GetMapping("/{id}/lists")
    public Optional<BBBoard> getWithLists(@PathVariable Long id) 
    {
        return repository.findByIdWithLists(id);
    }
    
    @GetMapping()
    public Optional<List<BBBoard>> getAll() 
    {
        return repository.findAll();
    }
    
    @GetMapping("/lists")
    public /*List<BBBoard>*/void getAllWithLists() 
    {
        System.out.println("get all with lists");
        //return repository.findAllWithCards();
    }
    
    //TODO: search by text and pagination
    
    @PutMapping()
    public Optional<BBBoard> update(@RequestBody BBBoard board) 
    {
        if(board==null)
            System.out.println("board is null...");
        
        if(null==board.getId()){
            throw new BadRequestException("Missing id");
        }
        try
        {
            repository.update(board);
            return repository.findById(board.getId());
        }
        catch(Exception ex)
        {
            throw new BadRequestException(ex.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) 
    {
        try
        {
            int result = repository.delete(id);
            if(result >= 1)
                return "Board deleted and "+ (--result)+" children lists and cards";
            else 
                return "No board found";
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
            throw new BadRequestException(ex.getMessage());
        }  
    }       
    
}
