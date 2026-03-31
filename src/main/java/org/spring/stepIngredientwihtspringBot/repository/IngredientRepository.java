package org.spring.stepIngredientwihtspringBot.repository;

import org.spring.stepIngredientwihtspringBot.entity.Ingredient;

import java.util.List;
import java.util.Optional;

public interface IngredientRepository {
    List<Ingredient> findAll();
    Optional<Ingredient> findOne(int id);
    boolean checkIfExist(int id);

}
