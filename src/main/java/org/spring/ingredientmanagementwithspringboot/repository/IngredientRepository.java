package org.spring.ingredientmanagementwithspringboot.repository;

import org.spring.ingredientmanagementwithspringboot.entity.Ingredient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface IngredientRepository {
    List<Ingredient> findAll();
    Optional<Ingredient> findOne(int id);
    boolean checkIfExist(int id);

}
