package org.spring.ingredientmanagementwithspringboot.exception;

public class IngredientNotFoundException extends RuntimeException {
    public IngredientNotFoundException(int id) {
        super(String.format("Ingredient.id={%s} is not found", id));
    }
}
