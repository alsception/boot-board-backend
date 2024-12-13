package org.alsception.bootboard.controllers;

import java.util.List;
import java.util.Optional;
import org.alsception.bootboard.entities.BBCard;
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
    public BBCard create(@RequestBody BBCard entry) throws BadRequestException 
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
    public Optional<BBCard> get(@PathVariable Long id) 
    {
        return repository.findById(id);
    }

    @GetMapping
    public List<BBCard> getAll() 
    {
        return repository.findAll();
    }
    
    //TODO: search by text and pagination
    
    @PutMapping(/*"/{id}"*/)
    public Optional<BBCard> update(/*@PathVariable Long id, */@RequestBody BBCard card) 
    {
        if(card==null)
            System.out.println("card is null...");
        
        System.out.println("Update card:");
        System.out.println("Received item: ");
        System.out.println(card);
        
        
        if(null==card.getId()){
            throw new BadRequestException("Missing id");
        }
        try
        {
            repository.update(card);
            return repository.findById(card.getId());
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
