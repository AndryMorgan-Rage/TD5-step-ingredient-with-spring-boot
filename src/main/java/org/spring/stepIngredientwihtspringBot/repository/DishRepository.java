package org.spring.stepIngredientwihtspringBot.repository;

import org.spring.stepIngredientwihtspringBot.entity.Dish;
import org.spring.stepIngredientwihtspringBot.entity.DishIngredient;

import java.util.List;

public interface DishRepository {
    List<Dish> findAll();
    Dish findOne(int id);
    List<DishIngredient> findIngredientsByDishId(int id);
    boolean checkIfExist(int id);
}
