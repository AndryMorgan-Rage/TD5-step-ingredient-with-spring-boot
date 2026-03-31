package org.spring.ingredientmanagementwithspringboot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.spring.ingredientmanagementwithspringboot.entity.DishTypeEnum;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Dish {
    private Integer id;
    private Double price;
    private String name;
    private DishTypeEnum dishType;
    @JsonIgnore
    private List<DishIngredient> dishIngredientList;

    @JsonProperty("ingredients")
    public List<Ingredient> getIngredients() {
        return dishIngredientList.stream().map(DishIngredient::getIngredient).collect(Collectors.toList());
    }

    public void setDishIngredientList(List<DishIngredient> dishIngredientList) {
        if(this.dishIngredientList != null && !this.dishIngredientList.isEmpty()){
            this.dishIngredientList.clear();
        }
        this.dishIngredientList = dishIngredientList == null ? new ArrayList<>() : dishIngredientList;
        for (DishIngredient dishIngredient : this.dishIngredientList) {
            if(dishIngredient != null){
            dishIngredient.setDish(this);
            }
        }
    }
    @JsonIgnore
    public Double getDishCost() {
        double totalPrice = 0;
        for (DishIngredient dishIngredient : dishIngredientList) {
            totalPrice += dishIngredient.getIngredientCost();
        }
        return totalPrice;
    }
    @JsonIgnore
    public Double getGrossMargin() {
        if (price == null) {
            throw new RuntimeException("Price is null");
        }
        return price - getDishCost();
    }
}