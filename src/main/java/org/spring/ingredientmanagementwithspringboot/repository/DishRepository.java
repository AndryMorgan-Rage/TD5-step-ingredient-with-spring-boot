package org.spring.ingredientmanagementwithspringboot.repository;

import org.spring.ingredientmanagementwithspringboot.entity.Dish;
import org.spring.ingredientmanagementwithspringboot.entity.DishIngredient;

import java.util.List;

public interface DishRepository {
    List<Dish> findAll();
    Dish findOne(int id);
    List<DishIngredient> findIngredientsByDishId(int id);
    boolean checkIfExist(int id);
}
