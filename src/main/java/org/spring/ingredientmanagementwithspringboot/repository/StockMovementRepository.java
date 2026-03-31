package org.spring.ingredientmanagementwithspringboot.repository;

import org.spring.ingredientmanagementwithspringboot.entity.StockMovement;

import java.util.List;

public interface StockMovementRepository {
    public List<StockMovement> findOneByIngredientId(int id);
}
