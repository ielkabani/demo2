package com.example.demo;


import com.example.demo.entities.Address;
import com.example.demo.entities.User;

import com.example.demo.repositories.UserRepository;
import com.example.demo.services.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Demo2Application {

    public static void main(String[] args) {

      ApplicationContext context = SpringApplication.run(Demo2Application.class, args);
    //  var service = context.getBean(UserService.class);
    //    service.showEntityStates();
  //    service.showRelatedEntities();
      //var repository = context.getBean(UserRepository.class);
  //    service.persistRelated();
      //service.deleteRelated();

     // var user1 = User.builder()
     //           .name("John Doe")
     //           .email("jdoe@email.com")
     //           .password("password")
     //           .build();


    /*    System.out.println(user1);

        repository.save(user1);
        var address1 = Address.builder()
                .street("123 Main St")
                .city("Springfield")
                .state("IL")
                .zipCode("62701")
                .build(); */

     //   user1.addAddress(address1);
    //    var user1 = repository.findById(1L).orElseThrow();
    //    System.out.println(user1.getEmail());

     //   repository.findAll().forEach( u ->System.out.println(u.getEmail()));
    //  var users = repository.findAll();
   //   users.forEach(u -> System.out.println(u.getEmail()));
  //    repository.deleteById(1L);
//        System.out.println(user1);
    }
}
