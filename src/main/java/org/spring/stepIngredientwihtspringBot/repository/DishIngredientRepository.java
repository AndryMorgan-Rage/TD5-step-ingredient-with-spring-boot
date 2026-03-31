package org.spring.stepIngredientwihtspringBot.repository;

import org.spring.stepIngredientwihtspringBot.entity.DishIngredient;

import java.util.List;

public interface DishIngredientRepository {
    public void attachIngredient(int dishId ,List<DishIngredient> dishIngredientList);
    public void detachIngredient(int id);
}
