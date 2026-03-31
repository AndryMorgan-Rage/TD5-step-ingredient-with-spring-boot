package org.spring.ingredientmanagementwithspringboot.repository;

import org.spring.ingredientmanagementwithspringboot.entity.DishIngredient;

import java.util.List;

public interface DishIngredientRepository {
    public void attachIngredient(int dishId ,List<DishIngredient> dishIngredientList);
    public void detachIngredient(int id);
}
