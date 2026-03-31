package org.spring.stepIngredientwihtspringBot.exception;

public class DishNotFoundException extends RuntimeException {
    public DishNotFoundException(int id) {
        super("Dish with id " + id + " not found");
    }
}
