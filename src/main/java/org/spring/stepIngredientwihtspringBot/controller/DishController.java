package org.spring.stepIngredientwihtspringBot.controller;

import org.spring.stepIngredientwihtspringBot.entity.Dish;
import org.spring.stepIngredientwihtspringBot.entity.DishIngredient;
import org.spring.stepIngredientwihtspringBot.service.DishService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/dishes")
public class DishController {
    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @GetMapping
    public ResponseEntity<List<Dish>> getDishes(
            @RequestParam(required = false) Double pMin,
            @RequestParam(required = false) Double pMax,
            @RequestParam(required = false, name = "n") String n) {
        List<Dish> dishes = dishService.getAllDishes(pMin, pMax, n);
        return ResponseEntity.ok(dishes);
    }

    @PostMapping
    public ResponseEntity<?> createDishes(@RequestBody List<Dish> dishes) {
        try {
            List<Dish> createdDishes = dishService.createDishes(dishes);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDishes);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/ingredients")
    public ResponseEntity<?> updateDishIngredient(
            @PathVariable int id,
            @RequestBody List<DishIngredient> dishIngredientList) {

        if (dishIngredientList == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request body is required");
        }

        try {
            Dish updatedDish = dishService.updateDishIngredient(id, dishIngredientList);
            return ResponseEntity.ok(updatedDish);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}