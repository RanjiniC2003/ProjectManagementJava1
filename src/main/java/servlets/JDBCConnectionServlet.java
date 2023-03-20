package servlets;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.Application;
public class JDBCConnectionServlet extends HttpServlet {
	/**
	 * Servlet implementation class ConfigurationServlet
	 */

		private static final long serialVersionUID = 1L;
	       
	    /**
	     * @see HttpServlet#HttpServlet()
	     */
	    public JDBCConnectionServlet() {
	        super();
	        // TODO Auto-generated constructor stub
	    }

	    public void init(){
	   
	  
	    	String dbname = getServletConfig().getInitParameter("dbname");
	    	String dbURL = "jdbc:mysql://localhost:3306/"+dbname;
	    	String username = getServletConfig().getInitParameter("dbusername");
	    	String password = getServletConfig().getInitParameter("dbpassword");
	    	try {
	    		Class.forName("com.mysql.cj.jdbc.Driver");
	    		Application.dbConnection = DriverManager.getConnection(dbURL, username, password);
	    	}catch(SQLException ex) {
	    		ex.printStackTrace();
	    	}catch(Exception ex) {
	    		ex.printStackTrace();
	    	}
	    }
		/**
		 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
		 */
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			// TODO Auto-generated method stub
			response.getWriter().append("Served at: ").append(request.getContextPath());
		}

		/**
		 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
		 */
		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			// TODO Auto-generated method stub
			doGet(request, response);
		}

}







