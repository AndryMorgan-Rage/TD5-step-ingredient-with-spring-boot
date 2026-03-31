package org.spring.ingredientmanagementwithspringboot.repository.impl;

import org.spring.ingredientmanagementwithspringboot.datasource.Datasource;
import org.spring.ingredientmanagementwithspringboot.entity.CategoryEnum;
import org.spring.ingredientmanagementwithspringboot.entity.Ingredient;
import org.spring.ingredientmanagementwithspringboot.repository.IngredientRepository;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class IngredientRepositoryImpl implements IngredientRepository {
    private final Datasource datasource;
    public IngredientRepositoryImpl(Datasource datasource) {
        this.datasource = datasource;
    }

    @Override
    public List<Ingredient> findAll() {
        String ingSql = """
                select i.id, i.name, i.category,i.price from ingredient i
                """;
       // String stockMovementSql = """
         //       select id, id_ingredient, quantity, type, unit, creation_datetime from stockmovement where id_ingredient = ?
           //     """;
        List<Ingredient> ingredients = new ArrayList<>();
        try(Connection connection = datasource.getConnection();
            PreparedStatement ingPs = connection.prepareStatement(ingSql);
            //PreparedStatement stockMovementPs = connection.prepareStatement(stockMovementSql);
            ResultSet ingRs= ingPs.executeQuery();
            //ResultSet stockMovementRs = stockMovementPs.executeQuery();
        ){
            if(ingRs.next()){
                ingredients = mapResultSetToIngredients(ingRs);
            }
            return ingredients;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Ingredient> findOne(int id) {
        try(Connection connection = datasource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        """
                                select ingredient.id, ingredient.name, ingredient.price, ingredient.category
                                from ingredient
                                where ingredient.id = ?;
                                """);
                ){
            preparedStatement.setInt(1, id);
            try(
                ResultSet resultSet = preparedStatement.executeQuery();){
                if (resultSet.next()) {
                    Ingredient ingredient = new Ingredient();
                    ingredient.setId(resultSet.getInt("id"));
                    ingredient.setName(resultSet.getString("name"));
                    ingredient.setPrice(resultSet.getDouble("price"));
                    ingredient.setCategory(CategoryEnum.valueOf(resultSet.getString("category")));

                    return Optional.of(ingredient);
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public List<Ingredient> mapResultSetToIngredients(ResultSet resultSet) throws SQLException {
        List<Ingredient> ingredients = new ArrayList<>();
        while (resultSet.next()) {
            Ingredient ingredient = new Ingredient();
            ingredient.setId(resultSet.getInt(1));
            ingredient.setName(resultSet.getString(2));
            ingredient.setCategory(CategoryEnum.valueOf(resultSet.getString(3)));
            ingredient.setPrice(resultSet.getDouble(4));

            ingredients.add(ingredient);
        }
        return ingredients;
    }

    @Override
    public boolean checkIfExist(int id) {
        String sql = """
                select 1
                from ingredient
                where ingredient.id = ?
                """;
        try(Connection connection = datasource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ){
            preparedStatement.setInt(1, id);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
