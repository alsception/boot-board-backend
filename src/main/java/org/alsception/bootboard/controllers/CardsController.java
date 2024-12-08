package org.alsception.bootboard.controllers;

import java.util.List;
import java.util.Optional;
import org.alsception.bootboard.entities.Card;
import org.alsception.bootboard.error.BadRequestException;
import org.alsception.bootboard.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cards")
@CrossOrigin(origins = "*")//or 4200 for dev
public class CardsController {

    @Autowired
    private CardRepository repository;
    
    @PostMapping
    public String create(@RequestBody Card entry) throws BadRequestException 
    {
        int result = 0;
        try
        {
            result = repository.create(entry);
            
            if(result>0){
                return "Card created";
            }else{
                return "Error creating card";
            }
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
            throw new BadRequestException(ex.getMessage());
        }  
    }

    @GetMapping("/{id}")
    public Optional<Card> get(@PathVariable Long id) 
    {
        return repository.findById(id);
    }

    @GetMapping
    public List<Card> getAll() 
    {
        return repository.findAll();
    }
    
    //TODO: search by text and pagination
    
    @PutMapping("/{id}")
    public Optional<Card> update(@PathVariable Long id, @RequestBody Card entry) 
    {
        if(id!=entry.getId()){
            throw new BadRequestException("Wrong id");
        }
        try
        {
            repository.update(entry);
            return repository.findById(id);
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
            if(result == 1)
                return "Card deleted";
            else 
                return "No card found";
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
            throw new BadRequestException(ex.getMessage());
        }  
    }
    
    
    
}
