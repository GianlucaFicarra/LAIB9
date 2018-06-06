package it.polito.tdp.borders.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.borders.model.Border;
import it.polito.tdp.borders.model.Country;

public class BordersDAO {

	public List<Country> loadAllCountries() {

		String sql = "SELECT ccode, StateAbb, StateNme FROM country ORDER BY StateAbb";
		List<Country> result = new ArrayList<Country>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				System.out.format("%d %s %s\n", rs.getInt("ccode"), rs.getString("StateAbb"), rs.getString("StateNme"));
			Country c= new Country(rs.getInt("ccode"), rs.getString("StateAbb"), rs.getString("StateNme"));
			result.add(c);
			}
			
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}


	public List<Border> getStatiConfinanti(List<Country> stati, Integer anno) {
		
		String sql = "SELECT state1no, state2no " + 
				"FROM contiguity " + 
				"WHERE conttype=1 AND state1no<state2no AND year<=?";
		
		List<Border> result = new ArrayList<Border>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet rs = st.executeQuery();

			while (rs.next()) { //dammi il continente che ha quel codice 
				
				//prendo i due stati confinanti
				Country c1= stati.get(stati.indexOf(new Country(rs.getInt("state1no"))));
				Country c2= stati.get(stati.indexOf(new Country(rs.getInt("state2no"))));
				
				//gli creo un arco
				Border b= new Border(c1, c2);
	
				result.add(b);
			}
			
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}
}
