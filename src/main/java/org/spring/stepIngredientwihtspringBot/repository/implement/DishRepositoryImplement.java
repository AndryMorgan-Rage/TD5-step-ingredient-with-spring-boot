package org.spring.stepIngredientwihtspringBot.repository.implement;

import org.spring.stepIngredientwihtspringBot.datasource.Datasource;
import org.spring.stepIngredientwihtspringBot.entity.*;
import org.spring.stepIngredientwihtspringBot.repository.DishRepository;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DishRepositoryImplement implements DishRepository {
    private final Datasource datasource;

    public DishRepositoryImplement(Datasource datasource) {
        this.datasource = datasource;
    }

    // 1. Version demandée pour le filtrage (Sujet HEI)
    @Override
    public List<Dish> findAll(Double pMin, Double pMax, String name) {
        StringBuilder sql = new StringBuilder("SELECT id, name, price, dish_type FROM dish WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (name != null && !name.trim().isEmpty()) {
            sql.append(" AND name ILIKE ?");
            params.add("%" + name + "%");
        }
        if (pMin != null) {
            sql.append(" AND price >= ?");
            params.add(pMin);
        }
        if (pMax != null) {
            sql.append(" AND price <= ?");
            params.add(pMax);
        }

        List<Dish> results = new ArrayList<>();
        try (Connection conn = datasource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapSimpleDish(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du filtrage", e);
        }
        return results;
    }

    // 2. Version sans paramètres (Si ton interface l'exige encore)
    @Override
    public List<Dish> findAll() {
        return findAll(null, null, null);
    }

    @Override
    public Dish save(Dish dish) {
        // Le cast ::dish_type est important pour PostgreSQL
        String sql = "INSERT INTO dish (name, price, dish_type) VALUES (?, ?, ?::dish_type) RETURNING id";

        try (Connection conn = datasource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, dish.getName());
            if (dish.getPrice() == null) {
                pstmt.setNull(2, Types.DOUBLE);
            } else {
                pstmt.setDouble(2, dish.getPrice());
            }

            if (dish.getDishType() == null) {
                throw new RuntimeException("DishType est obligatoire");
            }
            pstmt.setString(3, dish.getDishType().name());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    dish.setId(rs.getInt(1));
                }
            }
            return dish;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'insertion", e);
        }
    }

    @Override
    public boolean existsByName(String name) {
        String sql = "SELECT 1 FROM dish WHERE name = ?";
        try (Connection conn = datasource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Dish findOne(int id) {
        String sql = "SELECT id, name, price, dish_type FROM dish WHERE id = ?";
        try (Connection conn = datasource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return mapSimpleDish(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public boolean checkIfExist(int id) {
        String sql = "SELECT 1 FROM dish WHERE id = ?";
        try (Connection conn = datasource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<DishIngredient> findIngredientsByDishId(int id) {
        String sql = """
                SELECT i.id id_ing, i.name ing_name, i.category ing_cat, i.price ing_price, 
                       di.id di_id, di.quantity_required, di.unit
                FROM dishingredient di
                JOIN ingredient i ON di.id_ingredient = i.id
                WHERE di.id_dish = ?
                """;
        List<DishIngredient> ingredients = new ArrayList<>();
        try (Connection conn = datasource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ingredients.add(mapDishIngredient(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ingredients;
    }

    // --- MAPPERS ---

    private Dish mapSimpleDish(ResultSet rs) throws SQLException {
        Dish d = new Dish();
        d.setId(rs.getInt("id"));
        d.setName(rs.getString("name"));
        d.setPrice(rs.getDouble("price"));
        String type = rs.getString("dish_type");
        if (type != null) {
            d.setDishType(DishTypeEnum.valueOf(type.toUpperCase()));
        }
        return d;
    }

    private DishIngredient mapDishIngredient(ResultSet rs) throws SQLException {
        DishIngredient di = new DishIngredient();
        di.setId(rs.getInt("di_id"));
        di.setQuantity_required(rs.getDouble("quantity_required"));
        di.setUnit(UnitType.valueOf(rs.getString("unit").toUpperCase()));

        Ingredient ing = new Ingredient();
        ing.setId(rs.getInt("id_ing"));
        ing.setName(rs.getString("ing_name"));
        ing.setCategory(CategoryEnum.valueOf(rs.getString("ing_cat").toUpperCase()));
        ing.setPrice(rs.getDouble("ing_price"));

        di.setIngredient(ing);
        return di;
    }
}