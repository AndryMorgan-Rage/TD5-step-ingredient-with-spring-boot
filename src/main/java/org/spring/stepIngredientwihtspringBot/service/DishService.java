package org.spring.stepIngredientwihtspringBot.service;

import org.spring.stepIngredientwihtspringBot.entity.Dish;
import org.spring.stepIngredientwihtspringBot.entity.DishIngredient;
import org.spring.stepIngredientwihtspringBot.exception.DishNotFoundException;
import org.spring.stepIngredientwihtspringBot.exception.IngredientNotFoundException;
import org.spring.stepIngredientwihtspringBot.repository.DishIngredientRepository;
import org.spring.stepIngredientwihtspringBot.repository.DishRepository;
import org.spring.stepIngredientwihtspringBot.repository.IngredientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class DishService {
    private final DishRepository dishRepository;
    private final DishIngredientRepository dishIngredientRepository;
    private final IngredientRepository ingredientRepository;

    public DishService(DishRepository dishRepository,
                       DishIngredientRepository dishIngredientRepository,
                       IngredientRepository ingredientRepository) {
        this.dishRepository = dishRepository;
        this.dishIngredientRepository = dishIngredientRepository;
        this.ingredientRepository = ingredientRepository;
    }

    // --- TD PARTIE 1 : RÉCUPÉRATION & FILTRAGE ---

    public List<Dish> getAllDishes(Double pMin, Double pMax, String name) {
        // On utilise la méthode findAll qui gère maintenant les filtres
        return dishRepository.findAll(pMin, pMax, name);
    }

    public Dish getDishById(int id) {
        Dish dish = dishRepository.findOne(id);
        if (dish == null) {
            throw new DishNotFoundException(id);
        }
        List<DishIngredient> dishIngredients = dishRepository.findIngredientsByDishId(id);
        dish.setDishIngredientList(dishIngredients);
        return dish;
    }


    public List<Dish> createDishes(List<Dish> dishes) {
        List<Dish> savedDishes = new ArrayList<>();

        for (Dish dish : dishes) {
            // Règle métier : Vérifier si le nom existe déjà
            if (dishRepository.existsByName(dish.getName())) {
                // Utilisation de ResponseStatusException pour renvoyer le code 400
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Dish.name=" + dish.getName() + " already exists"
                );
            }
            savedDishes.add(dishRepository.save(dish));
        }
        return savedDishes;
    }

    // --- TD PARTIE 3 : MISE À JOUR DES INGRÉDIENTS ---

    public Dish updateDishIngredient(int id, List<DishIngredient> dishIngredientList) {
        if (!dishRepository.checkIfExist(id)) {
            throw new DishNotFoundException(id);
        }

        if (dishIngredientList != null && !dishIngredientList.isEmpty()) {
            for (DishIngredient di : dishIngredientList) {
                if (di.getIngredient() == null || di.getIngredient().getId() == 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ingredient ID is required");
                }
                if (!ingredientRepository.checkIfExist(di.getIngredient().getId())) {
                    throw new IngredientNotFoundException(di.getIngredient().getId());
                }
            }
        }

        // Suppression des anciens liens et création des nouveaux
        dishIngredientRepository.detachIngredient(id);
        if (dishIngredientList != null && !dishIngredientList.isEmpty()) {
            dishIngredientRepository.attachIngredient(id, dishIngredientList);
        }

        return this.getDishById(id);
    }


}