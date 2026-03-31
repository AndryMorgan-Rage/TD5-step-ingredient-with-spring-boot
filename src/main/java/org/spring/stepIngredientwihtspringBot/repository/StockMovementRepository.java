package org.spring.stepIngredientwihtspringBot.repository;

import org.spring.stepIngredientwihtspringBot.entity.StockMovement;

import java.util.List;

public interface StockMovementRepository {
    public List<StockMovement> findOneByIngredientId(int id);
}
