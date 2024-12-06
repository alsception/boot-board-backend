package org.alsception.bootboard.controllers;

import java.util.List;
import org.alsception.bootboard.error.BadRequestException;
import org.alsception.bootboard.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import org.alsception.bootboard.repositories.UserRepository;

@RestController
@RequestMapping("/api/v1/users")
//@Tag(name = "User management API", description = "Endpoints for CRUD operations")
public class UsersController {

    @Autowired
    private UserRepository repository;
    
    @PostMapping
    //@Operation(summary = "Create user", description = "Creates a new user")
    public int createUser(@RequestBody User user) throws BadRequestException 
    {
        if(user.getUsername() == null || "".equals(user.getUsername()))
        {
            throw new BadRequestException("Username is mandatory");
        }
        user.setActive(true);
        try
        {
            int result = repository.create(user);
            return result;
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
            throw new BadRequestException(ex.getMessage());
        }  
    }
/*
    @GetMapping("/{id}")
    public Optional<User> getUser(@PathVariable Long id) 
    {
       // return repository.getUserById(id)/*.orElseThrow();
    }*/

    @GetMapping
    public List<User> getUsers( // todo pageable
            @RequestParam(required = false) String name
            //@RequestParam(defaultValue = "0") int page,
            //@RequestParam(defaultValue = "10") int size
            ) 
    {
        //If name is null or empty -> return all users
        //Pageable pageable = PageRequest.of(page, size);
        return repository.findAll();
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

  /*  @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) 
    {
        repository.deleteUser(id);
    }*/
    
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
