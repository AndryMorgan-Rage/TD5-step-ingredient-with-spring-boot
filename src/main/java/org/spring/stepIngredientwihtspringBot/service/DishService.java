package org.spring.stepIngredientwihtspringBot.service;

import org.spring.stepIngredientwihtspringBot.entity.Dish;
import org.spring.stepIngredientwihtspringBot.entity.DishIngredient;
import org.spring.stepIngredientwihtspringBot.exception.DishNotFoundException;
import org.spring.stepIngredientwihtspringBot.exception.IngredientNotFoundException;
import org.spring.stepIngredientwihtspringBot.repository.DishIngredientRepository;
import org.spring.stepIngredientwihtspringBot.repository.DishRepository;
import org.spring.stepIngredientwihtspringBot.repository.IngredientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishService {
    private final DishRepository dishRepository;
    private final DishIngredientRepository dishIngredientRepository;
    private final IngredientRepository ingredientRepository;
    public DishService(DishRepository dishRepository, DishIngredientRepository dishIngredientRepository, IngredientRepository ingredientRepository) {
        this.dishRepository = dishRepository;
        this.dishIngredientRepository = dishIngredientRepository;
        this.ingredientRepository = ingredientRepository;
    }
    public List<Dish> getAllDishes(){
        return dishRepository.findAll();
    }
    public Dish getDishById(int id){
        Dish dish = dishRepository.findOne(id);
        List<DishIngredient> dishIngredients = dishRepository.findIngredientsByDishId(id);
        dish.setDishIngredientList(dishIngredients);
        return dish;
    }

    public Dish updateDishIngredient(int id, List<DishIngredient> dishIngredientList) {
        if(!dishRepository.checkIfExist(id)){
            throw new DishNotFoundException(id);
        }
        if(dishIngredientList != null && !dishIngredientList.isEmpty()){
            for(DishIngredient di : dishIngredientList){
                if(di.getIngredient() == null || di.getIngredient().getId() == null){
                    throw new RuntimeException("Ingredient ID is required");
                }
                if(!ingredientRepository.checkIfExist(di.getIngredient().getId())){
                    throw new IngredientNotFoundException(di.getIngredient().getId());
                }
            }
        }
        dishIngredientRepository.detachIngredient(id);
        if(dishIngredientList != null && !dishIngredientList.isEmpty()){
            dishIngredientRepository.attachIngredient(id,dishIngredientList);
        }
        return this.getDishById(id);
    }
}
