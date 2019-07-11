package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FoodDao {

	public List<Food> listAllFood(Map<Integer,Food>m){
		String sql = "SELECT * FROM food" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Food> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					Food f=new Food(res.getInt("food_id"),
							res.getInt("food_code"),
							res.getString("display_name"), 
							res.getInt("portion_default"), 
							res.getDouble("portion_amount"),
							res.getString("portion_display_name"),
							res.getDouble("calories"));
					list.add(f);
					m.put(res.getInt("food_id"),f );
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
	
	public List<Condiment> listAllCondiment(Map<Integer,Condiment>m){
		String sql = "SELECT * FROM condiment" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Condiment> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					Condiment c=new Condiment(res.getInt("condiment_id"),
							res.getInt("food_code"),
							res.getString("display_name"), 
							res.getString("condiment_portion_size"), 
							res.getDouble("condiment_calories")
							);
					list.add(c);
					m.put(c.getCondiment_id(), c);
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
	
	public List<Condiment> listCondimentCalories(int calorie,Map<Integer,Condiment>m){
		String sql = "SELECT DISTINCT condiment_id FROM condiment WHERE condiment_calories < ? " ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setInt(1, calorie);
			
			List<Condiment> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(m.get(res.getInt("condiment_id")));
					
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
	
	public Double getPesoVicini(Condiment c,Condiment c1){
		String sql = "SELECT COUNT(fc2.food_code) FROM condiment AS c,condiment AS c1,food_condiment AS fc1, food_condiment AS fc2 WHERE c.condiment_id= ? AND c1.condiment_id= ? AND  fc1.food_code=fc2.food_code AND fc1.condiment_food_code=c.food_code AND fc2.condiment_food_code=c1.food_code GROUP BY c.condiment_id" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setInt(1, c.getCondiment_id());
			st.setInt(2, c1.getCondiment_id());
			
			
			double peso=0;
			
			ResultSet res = st.executeQuery() ;
			
			if(res.next()) {
				try {
					peso=res.getDouble("COUNT(fc2.food_code)");
					
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return peso ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Condiment> getViciniVertice(Condiment c,Map<Integer,Condiment> m,int calorie){
		String sql = "SELECT DISTINCT c1.condiment_id FROM condiment AS c,condiment AS c1,food_condiment AS fc1, food_condiment AS fc2 WHERE c.condiment_id= ? AND c1.condiment_calories < ? AND c1.condiment_id!=c.condiment_id AND  fc1.food_code=fc2.food_code AND fc1.condiment_food_code=c.food_code AND fc2.condiment_food_code=c1.food_code";
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setInt(1, c.getCondiment_id());
			st.setInt(2, calorie);
			
			List<Condiment> list=new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					
					list.add(m.get(res.getInt("condiment_id")));
					
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
	
	public int getCibiContenuti(Condiment c){
		String sql = "SELECT COUNT( fc.food_code) FROM food_condiment AS fc,condiment AS c WHERE fc.condiment_food_code=c.food_code AND c.condiment_id=? " ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setInt(1, c.getCondiment_id());
			
			ResultSet res = st.executeQuery() ;
			
			int count=0;
			
			if(res.next()) {
				try {
					count=res.getInt("COUNT( fc.food_code)");
					
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return count ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1 ;
		}

	}
}
