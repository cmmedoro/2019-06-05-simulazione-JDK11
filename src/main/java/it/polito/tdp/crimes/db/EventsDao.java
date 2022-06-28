package it.polito.tdp.crimes.db;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javadocmd.simplelatlng.LatLng;

import it.polito.tdp.crimes.model.Event;
import it.polito.tdp.crimes.model.LatLonDistrict;



public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
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
	
	public List<Event> getEventiData(int anno, int mese, int giorno){
		String sql = "SELECT * "
				+ "FROM `EVENTS` "
				+ "WHERE YEAR(reported_date) = ? AND MONTH(reported_date) = ? AND DAY(reported_date) = ? "
				+ "ORDER BY HOUR(reported_date), MINUTE(reported_date)";
		List<Event> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection() ;
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
			st.setInt(2, mese);
			st.setInt(3, giorno);
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				result.add(new Event(res.getLong("incident_id"),res.getInt("offense_code"),res.getInt("offense_code_extension"), 
						res.getString("offense_type_id"), res.getString("offense_category_id"),	res.getTimestamp("reported_date").toLocalDateTime(),
						res.getString("incident_address"),	res.getDouble("geo_lon"),res.getDouble("geo_lat"),res.getInt("district_id"),
						res.getInt("precinct_id"), res.getString("neighborhood_id"),res.getInt("is_crime"),	res.getInt("is_traffic")));
			}
			conn.close();
			return result ;
		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Integer> getAnni(){
		String sql = "SELECT DISTINCT YEAR(e.reported_date) AS anno "
				+ "FROM `events` e "
				+ "ORDER BY YEAR(e.reported_date)";
		List<Integer> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection() ;
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				result.add(res.getInt("anno"));
			}
			conn.close();
			return result ;
		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Integer> getGiorni(){
		String sql = "SELECT DISTINCT DAY(reported_date) AS giorno "
				+ "FROM `EVENTS` "
				+ "ORDER BY DAY(reported_date)";
		List<Integer> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection() ;
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				result.add(res.getInt("giorno"));
			}
			conn.close();
			return result ;
		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Integer> getMesi(){
		String sql = "SELECT DISTINCT MONTH(reported_date) AS mese "
				+ "FROM `EVENTS` "
				+ "ORDER BY MONTH(reported_date)";
		List<Integer> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection() ;
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				result.add(res.getInt("mese"));
			}
			conn.close();
			return result ;
		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	

	public List<Integer> getVertici(int year){
		String sql = "SELECT DISTINCT district_id "
				+ "FROM `events` "
				+ "WHERE YEAR(reported_date) = ? "
				+ "ORDER BY district_id";
		List<Integer> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection() ;
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, year);
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				result.add(res.getInt("district_id"));
			}
			conn.close();
			return result ;
		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<LatLonDistrict> getAvgCentroDistretto(Integer year){
		String sql = "SELECT AVG(e.geo_lon) AS lon_avg, AVG(e.geo_lat) AS lat_avg, e.district_id AS id "
				+ "FROM `events` e "
				+ "WHERE YEAR(e.reported_date) = ? "
				+ "GROUP BY e.district_id";
		List<LatLonDistrict> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection() ;
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, year);
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				double latitude = res.getDouble("lat_avg");
				double longitude = res.getDouble("lon_avg");
				LatLng distanza = new LatLng(latitude, longitude);
				result.add(new LatLonDistrict(res.getInt("id"), longitude, latitude, distanza));
			}
			conn.close();
			return result ;
		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
		
	}
	
	public Integer getMinoreCriminalita(Integer anno) {
		String sql = "SELECT district_id "
				+ "FROM `events` "
				+ "WHERE YEAR(reported_date) = ? "
				+ "GROUP BY district_id "
				+ "ORDER BY COUNT(*) "
				+ "LIMIT 1";
		Integer result = null;
		Connection conn = DBConnect.getConnection() ;
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
			ResultSet res = st.executeQuery() ;
			if(res.first()) {
				result = res.getInt("district_id");
			}
			conn.close();
			return result ;
		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
}
