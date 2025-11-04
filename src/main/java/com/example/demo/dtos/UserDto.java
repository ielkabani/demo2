package com.example.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDto {

   private Long id;
   private String name;
   private String email;
   private Boolean active;
}
