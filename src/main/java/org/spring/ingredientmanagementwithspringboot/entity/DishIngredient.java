package org.spring.ingredientmanagementwithspringboot.entity;

import lombok.*;
import org.spring.ingredientmanagementwithspringboot.entity.Enum.UnitType;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DishIngredient {
    private Integer id;
    private double quantity_required;
    private UnitType unit;
    private Dish dish;
    private Ingredient ingredient;

    public double getIngredientCost(){
        if(ingredient.getPrice() == null) throw new RuntimeException("ingredient price is null");
        return ingredient.getPrice() * getQuantity_required();
    }
}
