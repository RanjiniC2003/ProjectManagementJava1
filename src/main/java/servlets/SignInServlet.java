package servlets;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import common.Application;
import common.CookieManagement;

/**
 * Servlet implementation class signIn
 */
//@WebServlet("/signIn")
public class SignInServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignInServlet() {
        super();
        // TODO Auto-generated constructor stub
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
		String email=request.getParameter("email");
		String password=request.getParameter("password");
		JSONObject responseJSON = new JSONObject();
		Statement st = null;
		try {
			st = Application.dbConnection.createStatement();
			PreparedStatement psmt=Application.dbConnection.prepareStatement("select * from users where email=?");
			psmt.setString(1, email);
			ResultSet rs=psmt.executeQuery();
			if(rs.next()) {
				if(String.valueOf(rs.getString("email")).equals(email) && String.valueOf(rs.getString("password")).equals(password)) {
					
					int userId=rs.getInt("userId");
					UUID sessionId=UUID.randomUUID();
					CookieManagement.setCookie(response, "SESSIONID", String.valueOf(sessionId));

					PreparedStatement psmt2=Application.dbConnection.prepareStatement("insert into session values(?,?)");
					psmt2.setString(1, String.valueOf(sessionId));
					psmt2.setInt(2, userId);
					psmt2.executeUpdate();
					
					responseJSON.put("statusCode", 200);
					responseJSON.put("statusMessage", "SUCCESS");
					responseJSON.put("detailedMessage", "sign in Successful");
					
				}
			}
			else {
				System.out.print("Invalid email");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
//			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		
		
		
		response.getWriter().write(responseJSON.toString());
		
	}

}
