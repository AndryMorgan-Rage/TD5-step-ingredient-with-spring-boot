package org.spring.stepIngredientwihtspringBot.repository.implement;

import org.spring.stepIngredientwihtspringBot.datasource.Datasource;
import org.spring.stepIngredientwihtspringBot.entity.*;
import org.spring.stepIngredientwihtspringBot.entity.CategoryEnum;
import org.spring.stepIngredientwihtspringBot.entity.DishTypeEnum;
import org.spring.stepIngredientwihtspringBot.entity.UnitType;
import org.spring.stepIngredientwihtspringBot.repository.DishRepository;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DishRepositoryImplement implements DishRepository {
    private final Datasource datasource;
    public DishRepositoryImplement(Datasource datasource) {
        this.datasource = datasource;
    }
    @Override
    public List<Dish> findAll() {
        String sql = """
                select d.id, d.name,d.dish_type,  d.price , i.id id_ing, i.name ing_name, i.category ing_cat , i.price ing_price,di.id di_id, di.quantity_required, di.unit
                 from dish d
                  join dishingredient di on d.id = di.id_dish
                  join ingredient i on di.id_ingredient = i.id
                  order by d.id
                """;
        Map<Integer, Dish> dishMap = new HashMap<>();
        try(Connection conn = datasource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
        ){
            while(rs.next()){
                Dish dish = dishMap.get(rs.getInt("id"));

                if(dish == null){
                    dish = new Dish();
                    dish.setId(rs.getInt("id"));
                    dish.setName(rs.getString("name"));
                    dish.setDishType(DishTypeEnum.valueOf(rs.getString("dish_type")));
                    dish.setPrice(rs.getDouble("price"));
                    dish.setDishIngredientList(new ArrayList<>());
                    dishMap.put(rs.getInt("id"), dish);
                }
                DishIngredient di = new DishIngredient();
                Ingredient ing  = new Ingredient();
                di.setId(rs.getInt("di_id"));
                di.setUnit(UnitType.valueOf(rs.getString("unit")));
                di.setQuantity_required(rs.getDouble("quantity_required"));

                ing.setId(rs.getInt("id_ing"));
                ing.setName(rs.getString("ing_name"));
                ing.setCategory(CategoryEnum.valueOf(rs.getString("ing_cat")));
                ing.setPrice(rs.getDouble("ing_price"));

                di.setIngredient(ing);
                dish.getDishIngredientList().add(di);
            }
            return List.copyOf(dishMap.values());
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public Dish findOne(int id) {
        String sql = """
                select d.id,d.name,d.price, d.dish_type
                from dish d
                where d.id = ?
                """;
        try(Connection conn = datasource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ){
            pstmt.setInt(1 , id);
            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    Dish dish = new Dish();
                    dish.setId(rs.getInt("id"));
                    dish.setName(rs.getString("name"));
                    dish.setPrice(rs.getDouble("price"));
                    dish.setDishType(DishTypeEnum.valueOf(rs.getString("dish_type")));
                    return dish;
                }
            }
            return null;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<DishIngredient> findIngredientsByDishId(int id) {
        String sql = """
                select i.id id_ing, i.name ing_name, i.category ing_cat , i.price ing_price,di.id di_id, di.quantity_required, di.unit
                 from dish d
                  join dishingredient di on d.id = di.id_dish
                  join ingredient i on di.id_ingredient = i.id
                  where d.id = ?
                """;
        List<DishIngredient> dishIngredients = new ArrayList<>();
        try(Connection conn = datasource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.setInt(1, id);
            try(ResultSet rs = pstmt.executeQuery()){
                while(rs.next()){
                    DishIngredient di = new DishIngredient();
                    Ingredient ing  = new Ingredient();
                    di.setId(rs.getInt("di_id"));
                    di.setUnit(UnitType.valueOf(rs.getString("unit")));
                    di.setQuantity_required(rs.getDouble("quantity_required"));

                    ing.setId(rs.getInt("id_ing"));
                    ing.setName(rs.getString("ing_name"));
                    ing.setCategory(CategoryEnum.valueOf(rs.getString("ing_cat")));
                    ing.setPrice(rs.getDouble("ing_price"));

                    di.setIngredient(ing);
                    dishIngredients.add(di);
                }
            }
            return dishIngredients;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean checkIfExist(int id) {
        String sql = """
                select 1
                from dish d
                where d.id = ?
                """;
        try(Connection conn = datasource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.setInt(1, id);
            try(ResultSet rs = pstmt.executeQuery()){
                return rs.next();
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

}
