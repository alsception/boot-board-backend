package org.alsception.bootboard.controllers;

import java.util.List;
import java.util.Optional;
import org.alsception.bootboard.entities.Entry;
import org.alsception.bootboard.error.BadRequestException;
import org.alsception.bootboard.repositories.EntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/entries")
public class EntriesController {

    @Autowired
    private EntryRepository repository;
    
    @PostMapping
    public String create(@RequestBody Entry entry) throws BadRequestException 
    {
        int result = 0;
        try
        {
            result = repository.create(entry);
            
            if(result>0){
                return "Entry created";
            }else{
                return "Error creating entry";
            }
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
            throw new BadRequestException(ex.getMessage());
        }  
    }

    @GetMapping("/{id}")
    public Optional<Entry> get(@PathVariable Long id) 
    {
        return repository.findById(id);
    }

    @GetMapping
    public List<Entry> getAll() 
    {
        return repository.findAll();
    }
    
    //TODO: search by text and pagination
    
    @PutMapping("/{id}")
    public Optional<Entry> update(@PathVariable Long id, @RequestBody Entry entry) 
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
                return "Entry deleted";
            else return "Error deleting entry";
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
            throw new BadRequestException(ex.getMessage());
        }  
    }
    
    
/*
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) 
    {
        try
        {
            return repository.updateUser(id, user);
        }
        catch(Exception ex)
        {
            throw new BadRequestException(ex.getMessage());
        }
    }*/

  /*  */
    
//       
//    private PaginatedResponse<User> findAllWithPagination(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size)
//    {
//        List<User> users = userRedisRepository.findAll(page, size);
//        long totalItems = userRedisRepository.count();
//        int totalPages = (int) Math.ceil((double) totalItems / size);
//
//        return new PaginatedResponse<>(users, totalItems, totalPages, page);
//    }    
//    
//    private List<User> findAll()
//    {       
//        return userRedisRepository.findAll();
//    }
//    
//    @GetMapping
//    public Object findAll(
//            @RequestParam(required = false) Integer page,
//            @RequestParam(required = false) Integer size)
//    {       
//        if(page==null && size==null)
//            return findAll();
//        else
//            return findAllWithPagination(page, size);
//    }
    
//    @PostMapping
//    public BasicResponse save(@RequestBody User user)
//    {
//        if(user.getUsername()==null || "".equals(user.getUsername()))
//        {
//            throw new BadRequestException("Username is mandatory");
//        }
//        user.setActive(true);
//        try
//        {
//            System.out.println("saving user: "+user.toString());
//            userRedisRepository.save(user);
//            System.out.println("saved");
//            return new BasicResponse("User saved with ID: " + user.getId());
//            //return userRedisRepository.save(user);
//        }
//        catch(Exception ex)
//        {
//            throw new BadRequestException(ex.getMessage());
//        }         
//    }
    
}
