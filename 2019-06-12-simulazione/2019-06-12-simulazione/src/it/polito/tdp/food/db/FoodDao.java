package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import it.polito.tdp.food.model.Adiacenza;


public class FoodDao {

	public List<Food> listAllFood(){
		String sql = "SELECT * FROM food" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Food> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Food(res.getInt("food_id"),
							res.getInt("food_code"),
							res.getString("display_name"), 
							res.getInt("portion_default"), 
							res.getDouble("portion_amount"),
							res.getString("portion_display_name"),
							res.getDouble("calories")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Condiment> listAllCondiment(){
		String sql = "SELECT * FROM condiment" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Condiment> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Condiment(res.getInt("condiment_id"),
							res.getInt("food_code"),
							res.getString("display_name"), 
							res.getString("condiment_portion_size"), 
							res.getDouble("condiment_calories")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}

	}

	public List<Condiment> getVertex(Double calorie, Map<Integer, Condiment> idMap) {
		String sql = "SELECT * " + 
				"FROM condiment c " + 
				"WHERE c.condiment_calories<? " ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setDouble(1, calorie);
			List<Condiment> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					Condiment c= new Condiment(res.getInt("condiment_id"),
							res.getInt("food_code"),
							res.getString("display_name"), 
							res.getString("condiment_portion_size"), 
							res.getDouble("condiment_calories")
							);
					
					list.add(c);
					idMap.put(c.getCondiment_id(), c);
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}

	}

	public List<Adiacenza> getEdges(Double calorie) {
		String sql = "SELECT c1.condiment_id, c2.condiment_id, COUNT(DISTINCT fc1.food_code) as cnt " + 
				"FROM food_condiment fc1, food_condiment fc2, condiment c1, condiment c2 " + 
				"WHERE fc1.condiment_food_code=c1.food_code AND fc2.condiment_food_code=c2.food_code " + 
				"AND c1.condiment_id>c2.condiment_id AND fc1.food_code=fc2.food_code " + 
				"AND c1.condiment_calories<? AND c2.condiment_calories<? " + 
				"GROUP BY c1.condiment_id, c2.condiment_id " + 
				" " ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setDouble(1, calorie);
			st.setDouble(2, calorie);
			List<Adiacenza> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					Adiacenza a= new Adiacenza(res.getInt("c1.condiment_id"),res.getInt("c2.condiment_id"),
							res.getInt("cnt"));
					list.add(a);

				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
}
