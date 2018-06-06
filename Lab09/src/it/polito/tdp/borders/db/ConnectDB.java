package it.polito.tdp.borders.db;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class ConnectDB {
	
	/*La tabella country riporta la lista di tutte le nazioni, ciascuna identificata da un numero, da un’abbreviazione
	univoca di 3 lettere e dal nome completo. La tabella contiguity rappresenta la presenza di un confine, per ogni
	coppia di stati (‘state1no’, ‘state2no’), a partire dall’anno ‘year’ (anni compresi tra il 1816 ed il 2006). Esistono
	tipi diversi di confine, ma considerare esclusivamente il confine via terra (‘conttype’=1).*/

	private static final String jdbcURL = "jdbc:mysql://localhost/countries";
	private static HikariDataSource ds;
	
	public static Connection getConnection() {
		
		if (ds == null) {
			HikariConfig config = new HikariConfig();
			config.setJdbcUrl(jdbcURL);
			config.setUsername("root");
			config.setPassword("gualtieri95");
			
			// configurazione MySQL
			config.addDataSourceProperty("cachePrepStmts", "true");
			config.addDataSourceProperty("prepStmtCacheSize", "250");
			config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
			
			ds = new HikariDataSource(config);
		}
		
		try {
			
			return ds.getConnection();

		} catch (SQLException e) {
			System.err.println("Errore connessione al DB");
			throw new RuntimeException(e);
		}
	}

}
