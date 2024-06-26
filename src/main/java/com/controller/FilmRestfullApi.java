package com.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.FilmDAO;
import com.entity.Film;
import com.entity.FilmList;
import com.entity.Response;
import com.utils.Helper;



 
@WebServlet("/FilmCity")
public class FilmRestfullApi extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public FilmRestfullApi() {
        super();
        // TODO Auto-generated constructor stub
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	System.out.println("I am inside DOGET method");
        String format = "json";
        Helper helper = new Helper();
        FilmDAO dao = new FilmDAO();
        PrintWriter writer = response.getWriter();
        String id = request.getParameter("id");
        String searchQuery = request.getParameter("query");
        String accept = request.getHeader("Accept");

        try {
            if (accept != null) {
                if (accept.contains("xml")) format = "xml";
                else if (accept.contains("text")) format = "text";
                else if (!accept.contains("json")) throw new IllegalArgumentException("Unsupported Accept Type!");
            }

            if (searchQuery != null && !searchQuery.isEmpty()) {
                writer.write(helper.convertObject(format, new FilmList(dao.searchFilms(searchQuery))));
            } else if (id != null && !id.isEmpty() && Integer.parseInt(id) > 0) {
                writer.write(helper.convertObject(format, dao.getFilmByID(Integer.parseInt(id))));
            } else {
                writer.write(helper.convertObject(format, new FilmList(dao.getAllFilms())));
            }
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            try {
				writer.write(helper.convertObject(format, new Response(e.getMessage())));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try {
				writer.write(helper.convertObject(format, new Response("Internal Server Error!")));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        } finally {
            writer.close();
        }
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	System.out.println("I am inside DOPOST method");

        Helper helper = new Helper();
        FilmDAO dao = new FilmDAO();
        PrintWriter writer = response.getWriter();
        String acceptFormat = "json";
        String contentFormat = "json";
        String accept = request.getHeader("Accept");
        String contentType = request.getHeader("Content-Type");

        try {
 
            if (contentType != null) {
                if (contentType.toLowerCase().contains("json")) contentFormat = "json";
                else if (contentType.toLowerCase().contains("xml")) contentFormat = "xml";
                else if (contentType.toLowerCase().contains("text")) contentFormat = "text";
                else throw new IllegalArgumentException("This is not a valid content type!");
            }

 
            if (accept != null) {
                if (accept.toLowerCase().contains("json")) acceptFormat = "json";
                else if (accept.toLowerCase().contains("xml")) acceptFormat = "xml";
                else if (accept.toLowerCase().contains("text")) acceptFormat = "text";
                else throw new IllegalArgumentException("This not a valid accept type!");
            }


            StringBuilder content = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line = reader.readLine();
            while (line != null) {
                content.append(line);
                line = reader.readLine();
            }

    
            Film film = helper.convertToFilm(contentFormat, content.toString());

   
            if (dao.getFilmByID(film.getId()) != null) {
                throw new Exception("Film Exists!");
            }

            dao.insertFilm(film);
            response.setStatus(HttpServletResponse.SC_OK);
            writer.write(helper.convertObject(acceptFormat, new Response("YAY! Film has been Inserted!")));
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            try {
				writer.write(helper.convertObject(acceptFormat, new Response(e.getMessage())));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try {
				writer.write(helper.convertObject(acceptFormat, new Response(e.getMessage())));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        } finally {
            writer.close();
        }
    }


    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	System.out.println("I am inside DOGET method");
  
        Helper helper = new Helper();
        FilmDAO dao = new FilmDAO();
        PrintWriter writer = response.getWriter();
        
  
        String acceptFormat = "json";
        String contentFormat = "json";
        

        String accept = request.getHeader("Accept");
        String contentType = request.getHeader("Content-Type");

        try {
       
            if (contentType != null) {
                if (contentType.contains("json")) contentFormat = "json";
                else if (contentType.contains("xml")) contentFormat = "xml";
                else if (contentType.contains("text")) contentFormat = "text";
                else throw new IllegalArgumentException("Invalid Content Type!");
            }

   
            if (accept != null) {
                if (accept.contains("json")) acceptFormat = "json";
                else if (accept.contains("xml")) acceptFormat = "xml";
                else if (accept.contains("text")) acceptFormat = "text";
                else throw new IllegalArgumentException("Invalid Accept Type!");
            }

         
            StringBuilder content = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }

         
            Film film = helper.convertToFilm(contentFormat, content.toString());

   
            if (dao.getFilmByID(film.getId()) == null) {
                throw new IllegalArgumentException(":( This film doesn't exist!");
            }

        
            dao.updateFilm(film);
            response.setStatus(HttpServletResponse.SC_OK);
            writer.write(helper.convertObject(acceptFormat, new Response("Film has been updated!")));
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            try {
				writer.write(helper.convertObject(acceptFormat, new Response(e.getMessage())));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        } catch (Exception ex) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
        } finally {
            writer.close();
        }
    }

    
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("I am inside doDelete method");

        // Initialize objects
        FilmDAO dao = new FilmDAO();
        PrintWriter writer = response.getWriter();
        response.setContentType("application/json"); 

       
        String id = request.getParameter("id");
        if (id == null || id.isEmpty() || Integer.parseInt(id) <= 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            writer.write("{\"error\": \"Invalid Film ID!\"}"); 
            writer.close();
            return;
        }

        try {
            // delete film
            dao.deleteFilm(Integer.parseInt(id));
            response.setStatus(HttpServletResponse.SC_OK);
            writer.write("{\"success\": \"Film with ID " + id + " has been deleted successfully.\"}"); // success message 
        } catch (Exception e) {
        
            e.printStackTrace(); 
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            writer.write("{\"error\": \"An error occurred while deleting the film.\"}"); //  error message 
        }

        writer.close();
    }

}

      
    
