package org.spring.ingredientmanagementwithspringboot.service;

import org.spring.ingredientmanagementwithspringboot.entity.Enum.UnitType;
import org.spring.ingredientmanagementwithspringboot.entity.Ingredient;
import org.spring.ingredientmanagementwithspringboot.entity.StockMovement;
import org.spring.ingredientmanagementwithspringboot.entity.StockValue;
import org.spring.ingredientmanagementwithspringboot.exception.IngredientNotFoundException;
import org.spring.ingredientmanagementwithspringboot.repository.IngredientRepository;
import org.spring.ingredientmanagementwithspringboot.repository.StockMovementRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.Temporal;
import java.util.List;

@Service
public class IngredientService {
    private final IngredientRepository ingredientRepository;
    private final StockMovementRepository stockMovementRepository;

    public IngredientService(IngredientRepository ingredientRepository, StockMovementRepository stockMovementRepository) {
        this.ingredientRepository = ingredientRepository;
        this.stockMovementRepository = stockMovementRepository;
    }
    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }
    public Ingredient getIngredientById(int id) {
        return ingredientRepository.findOne(id)
                .orElseThrow(() -> new IngredientNotFoundException(id));
    }

    public StockValue getStockMovementAt(int id, Instant at, UnitType unit){
        Ingredient ingredient = getIngredientById(id);
        List<StockMovement> stockMovementList = stockMovementRepository.findOneByIngredientId(id);
        ingredient.setStockMovementList(stockMovementList);
        return ingredient.getStockValueAt(at, unit);
    }

}
