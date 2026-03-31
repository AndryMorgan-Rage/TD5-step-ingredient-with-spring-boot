package org.spring.stepIngredientwihtspringBot.service;

import org.spring.stepIngredientwihtspringBot.entity.UnitType;
import org.spring.stepIngredientwihtspringBot.entity.Ingredient;
import org.spring.stepIngredientwihtspringBot.entity.StockMovement;
import org.spring.stepIngredientwihtspringBot.entity.StockValue;
import org.spring.stepIngredientwihtspringBot.exception.IngredientNotFoundException;
import org.spring.stepIngredientwihtspringBot.repository.IngredientRepository;
import org.spring.stepIngredientwihtspringBot.repository.StockMovementRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
