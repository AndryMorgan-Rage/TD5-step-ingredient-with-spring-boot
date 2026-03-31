package org.spring.stepIngredientwihtspringBot.repository;

import org.spring.stepIngredientwihtspringBot.entity.Dish;
import org.spring.stepIngredientwihtspringBot.entity.DishIngredient;
import java.util.List;

public interface DishRepository {
    // Garde celle-ci pour le filtrage
    List<Dish> findAll(Double pMin, Double pMax, String name);

    // Garde les autres méthodes utiles
    List<Dish> findAll();
    Dish findOne(int id);
    List<DishIngredient> findIngredientsByDishId(int id);
    boolean checkIfExist(int id);

    Dish save(Dish dish);

    // SUPPRIME OU RENOMME CETTE LIGNE :
    // List<Dish> findAllFiltered(Double pMin, Double pMax, String name);

    boolean existsByName(String name);
}