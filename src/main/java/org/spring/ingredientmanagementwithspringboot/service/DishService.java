package org.spring.ingredientmanagementwithspringboot.service;

import org.spring.ingredientmanagementwithspringboot.entity.Dish;
import org.spring.ingredientmanagementwithspringboot.entity.DishIngredient;
import org.spring.ingredientmanagementwithspringboot.entity.Enum.UnitType;
import org.spring.ingredientmanagementwithspringboot.entity.Ingredient;
import org.spring.ingredientmanagementwithspringboot.exception.DishNotFoundException;
import org.spring.ingredientmanagementwithspringboot.exception.IngredientNotFoundException;
import org.spring.ingredientmanagementwithspringboot.repository.DishIngredientRepository;
import org.spring.ingredientmanagementwithspringboot.repository.DishRepository;
import org.spring.ingredientmanagementwithspringboot.repository.IngredientRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
