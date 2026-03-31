package org.spring.stepIngredientwihtspringBot.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.spring.stepIngredientwihtspringBot.entity.Enum.UnitType;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@EqualsAndHashCode
public class Ingredient {
    private Integer id;
    private String name;
    private org.spring.stepIngredientwihtspringBot.entity.CategoryEnum category;
    private Double price;
    /* Ignore the stock movement for now */
    @JsonIgnore
    private List<StockMovement> stockMovementList;

    /* getStockValueAt method */
    public StockValue getStockValueAt(Instant time, UnitType unitType) {
        List<StockMovement> concerned =  stockMovementList.stream().filter(stockMovement -> stockMovement.getCreationDatetime().isBefore(time)
        || stockMovement.getCreationDatetime().equals(time))
                .toList();
        double quantity = 0.0;
        for (StockMovement sm : concerned) {
            double quantitySm = sm.getValue().getQuantity();
            if(sm.getType() == org.spring.stepIngredientwihtspringBot.entity.MovementTypeEnum.IN){
                quantity += quantitySm;
            } else {
                quantity -= quantitySm;
            }
        }
        return new StockValue(quantity,unitType);
    };

}