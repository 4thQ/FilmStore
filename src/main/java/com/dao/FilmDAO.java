package com.dao;
import java.sql.Connection;
import java.util.ArrayList;

import com.entity.Film;

import java.sql.*;


public class FilmDAO {
	
	Film oneFilm = null;
	Connection conn = null;
    Statement stmt = null;
	String user = "gurungni";
    String password = "lorstwEn5";
    // Note none default port used, 6306 not 3306
    String url = "jdbc:mysql://mudfoot.doc.stu.mmu.ac.uk:6306/"+user;

	public FilmDAO() {}

	
	private void openConnection() throws Exception{
		// loading jdbc driver for mysql
		    Class.forName("com.mysql.cj.jdbc.Driver");
		// connecting to database
 			conn = DriverManager.getConnection(url, user, password);
		    stmt = conn.createStatement();	   
    }
	private void closeConnection() throws Exception{
			conn.close();
	}

	private Film getNextFilm(ResultSet rs) throws Exception{
    	Film thisFilm=null;
			thisFilm = new Film(
					rs.getInt("id"),
					rs.getString("title"),
					rs.getInt("year"),
					rs.getString("director"),
					rs.getString("stars"),
					rs.getString("review"));
    	return thisFilm;		
	}
	
	
	
   public ArrayList<Film> getAllFilms() throws Exception{
		ArrayList<Film> allFilms = new ArrayList<Film>();
		openConnection();
	    // Create select statement and execute it
		    String selectSQL = "select * from films"; 
		    ResultSet rs1 = stmt.executeQuery(selectSQL);
	    // Retrieve the results
		    while(rs1.next()){
		    	oneFilm = getNextFilm(rs1);
		    	allFilms.add(oneFilm);
		   }

		    stmt.close();
		    closeConnection();

	   return allFilms;
   }

   public Film getFilmByID(int id) throws Exception{
	   
		openConnection();
		oneFilm=null;
	    // Create select statement and execute it
		    String selectSQL = "select * from films where id="+id;
		    ResultSet rs1 = stmt.executeQuery(selectSQL);
	    // Retrieve the results
		    while(rs1.next()){
		    	oneFilm = getNextFilm(rs1);
		    }

		    stmt.close();
		    closeConnection();

	   return oneFilm;
   }

   public ArrayList<Film> searchFilms(String str) throws Exception{
		openConnection();
		ArrayList<Film> films = new ArrayList<Film>();
		
	    String q = String.format("""
	    			SELECT * FROM films WHERE title LIKE '%s' OR director LIKE '%s' OR stars LIKE '%s'
	    		""","%"+str+"%","%"+str+"%","%"+str+"%");
	    ResultSet rs1 = stmt.executeQuery(q);
	    while(rs1.next()){
	    	films.add(getNextFilm(rs1));
	   }
	    stmt.close();
	    closeConnection();
	   return films;
  }
   
   public void insertFilm(Film f) throws Exception { 
		openConnection();  
		String q = String.format("INSERT INTO films (title, year, director, stars, review) VALUES ('%s', %s, '%s', '%s', '%s')"
	    , f.getTitle(), f.getYear(), f.getDirector(), f.getStars(), f.getReview());
	  stmt.execute(q);
	  stmt.close(); 
	  closeConnection();
	  }
   
   public void updateFilm(Film f) throws Exception{
	   openConnection();
	   String q = String.format("UPDATE films SET title = '%s', year = %s, director = '%s', stars = '%s', review = '%s' WHERE id = %s",f.getTitle(),f.getYear(),f.getDirector(),f.getStars(),f.getReview(),f.getId());
	 
	   stmt.execute(q);
	   stmt.close();
	   closeConnection();
   }
   
   public void deleteFilm(int id) throws Exception{
	   openConnection();
	   String q = String.format("""
	   			DELETE FROM films WHERE id = %s
	   		""",id);
	   stmt.execute(q);
	   stmt.close();
	   closeConnection();
   }
   
}
