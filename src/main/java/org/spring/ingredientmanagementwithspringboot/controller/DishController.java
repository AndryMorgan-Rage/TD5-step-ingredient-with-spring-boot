package org.spring.ingredientmanagementwithspringboot.controller;


import org.spring.ingredientmanagementwithspringboot.entity.Dish;
import org.spring.ingredientmanagementwithspringboot.entity.DishIngredient;
import org.spring.ingredientmanagementwithspringboot.entity.Ingredient;
import org.spring.ingredientmanagementwithspringboot.service.DishService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/dishes")
public class DishController {
    private final DishService dishService;
    public DishController(DishService dishService) {
        this.dishService = dishService;
    }
    @GetMapping("")
    public ResponseEntity<List<Dish>> getAllDishes() {
        return ResponseEntity.ok(dishService.getAllDishes());
    }

    @PutMapping("/{id}/ingredients")
    public ResponseEntity<?> updateDishIngredient(@PathVariable int id,@RequestBody List<DishIngredient> dishIngredientList) {
        try{
            if(dishIngredientList == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request body is required");
            }
            return ResponseEntity.status(HttpStatus.OK).body(dishService.updateDishIngredient(id, dishIngredientList));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
