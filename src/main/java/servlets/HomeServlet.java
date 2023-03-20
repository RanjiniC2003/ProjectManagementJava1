package servlets;

import jdbc.HomeJDBC;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.mysql.cj.xdevapi.Result;

import classess.Company;
import classess.User;
import common.Application;
import common.UserSingleton;
import common.getCompanyId;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Servlet implementation class HomeServlet
 */
@WebServlet("/home/*")
public class HomeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public HomeServlet() {
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

        String path=request.getPathInfo();
//        System.out.println(path);
        int userId = (int) request.getAttribute("userId");
//        System.out.println(userId);

        try {
            if(path.equals("/getHomeDetails")) {
             
//            	System.out.println(HomeJDBC.getDetails(userId).toString());
                response.getWriter().write(HomeJDBC.getDetails(userId).toString());
            }
            else if(path.equals("/updateDetails")) {
            	String firstName=request.getParameter("firstName");
            	String lastName=request.getParameter("lastName");
            	String phoneNumber=request.getParameter("phoneNumber");
            	System.out.println(phoneNumber);
            	String user=request.getParameter("userId");
            	
            	PreparedStatement psmt=Application.dbConnection.prepareStatement("update users set firstName=?,lastName=?,phoneNumber=? where userId=?");
            	psmt.setString(1,firstName);
            	psmt.setString(2, lastName);
            	psmt.setBigDecimal(3, new BigDecimal(phoneNumber));
            	psmt.setInt(4, userId);
            	psmt.executeUpdate();
            	
            	
            	
            	
            	
            	JSONObject json=new JSONObject();
            	
                
                
                json.put("userDetails",firstName+" "+lastName);
                
            	json.put("statusCode", 200);
            	json.put("message","SUCCESS");
            	json.put("detailedMessage", "Successfully updated");
            	response.getWriter().write(json.toString());
            }
            
            else if(path.equals("/changePassword")) {
//            	System.out.println("sbj");
            	String oldPassword=request.getParameter("oldPassword");
            	String newPassword=request.getParameter("newPassword");
            	String user=request.getParameter("userId");
            	
            	JSONObject json=new JSONObject();
            	
            	PreparedStatement psmt=Application.dbConnection.prepareStatement("select * from users where password=? and userId=?");
            	psmt.setString(1,oldPassword);
            	psmt.setInt(2,userId);
            	ResultSet rs=psmt.executeQuery();
            	if(!rs.next()) {
            		
                	json.put("statusCode", 500);
                	json.put("message","FAILED");
                	json.put("detailedMessage", "Wrong old password.please enter correct old password");
                	response.getWriter().write(json.toString());
                	return;
            	}
            	else {
            		PreparedStatement psmt2=Application.dbConnection.prepareStatement("update users set password=? where userId=?");
            		psmt2.setString(1,newPassword);
            		psmt2.setInt(2, userId);
            		psmt2.executeUpdate();
            		
            		json.put("statusCode", 200);
                	json.put("message","SUCCESS");
                	json.put("detailedMessage", "Successfully password changed");
                	response.getWriter().write(json.toString());
                
            	}
            }
            
            
            
            
        } catch (Exception e) {
//        	System.out.println("hjkl");
        	e.printStackTrace();
            response.setStatus(500);
            response.getWriter().write("Internal server error");
        }
    }
}
