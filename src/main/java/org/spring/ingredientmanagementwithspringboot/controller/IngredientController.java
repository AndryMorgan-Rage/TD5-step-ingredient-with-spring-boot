package org.spring.ingredientmanagementwithspringboot.controller;

import org.spring.ingredientmanagementwithspringboot.entity.Enum.UnitType;
import org.spring.ingredientmanagementwithspringboot.entity.Ingredient;
import org.spring.ingredientmanagementwithspringboot.service.IngredientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {
    private final IngredientService ingredientService;
    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping
    public ResponseEntity<List<Ingredient>> getAllIngredients() {
        return ResponseEntity.ok(ingredientService.getAllIngredients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ingredient> getIngredientById(@PathVariable(required = true) int id) {
            return ResponseEntity.ok(ingredientService.getIngredientById(id));
    }

    @GetMapping("/{id}/stock")
    public ResponseEntity<?> getStockValueAt(@PathVariable(required = true) int id, @RequestParam(required = false) Instant at, @RequestParam(required = false) UnitType unit){
        if(at == null || unit == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Either mandatory query parameter `at` or `unit` is not provided.");
        }
        return ResponseEntity.ok(ingredientService.getStockMovementAt(id, at, unit));
    }
}
