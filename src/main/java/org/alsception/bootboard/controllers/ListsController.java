package org.alsception.bootboard.controllers;

import java.util.List;
import java.util.Optional;
import org.alsception.bootboard.entities.BBList;
import org.alsception.bootboard.error.BadRequestException;
import org.alsception.bootboard.repositories.ListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/lists")
@CrossOrigin(origins = "*")//or 4200 for dev
public class ListsController {

    @Autowired
    private ListRepository listRepository;
    
    @PostMapping
    public String create(@RequestBody BBList entry) throws BadRequestException 
    {
        int result = 0;
        try
        {
            result = listRepository.create(entry);
            
            if(result>0){
                return "List created";
            }else{
                return "Error creating list";
            }
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
            throw new BadRequestException(ex.getMessage());
        }  
    }

    @GetMapping("/{id}")
    public Optional<BBList> get(@PathVariable Long id) 
    {
        return listRepository.findById(id);
    }
    
    @GetMapping("/{id}/cards")
    public Optional<BBList> getWithCards(@PathVariable Long id) 
    {
        return listRepository.findByIdWithCards(id);
    }
    
    @GetMapping()
    public Optional<List<BBList>> getAll() 
    {
        return listRepository.findAll();
    }
    
    @GetMapping("/cards")
    public List<BBList> getAllWithCards() 
    {
        //TODO: load cards
        System.out.println("get all with cards");
        return listRepository.findAllWithCards();
    }
    
    //TODO: search by text and pagination
    
    @PutMapping("/{id}")
    public Optional<BBList> update(@PathVariable Long id, @RequestBody BBList list) 
    {
        if(id!=list.getId()){
            throw new BadRequestException("Wrong id");
        }
        try
        {
            listRepository.update(list);
            return listRepository.findById(id);
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
            int result = listRepository.delete(id);
            if(result == 1)
                return "List deleted";
            else 
                return "No list found";
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
            throw new BadRequestException(ex.getMessage());
        }  
    }
    
    
    
}
