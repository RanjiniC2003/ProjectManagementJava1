package servlets;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import common.Application;
import common.CookieManagement;
import common.getCompanyId;

/**
 * Servlet implementation class signUp
 */
//@WebServlet("/signUp")
public class SignUpServlet extends HttpServlet {

	
	
	
	private static final long serialVersionUID = 1L;

    private final Pattern namePattern = Pattern.compile("^[A-Za-z. ]+$");
    private final Pattern comPattern = Pattern.compile("^[A-Za-z ]+$");
    private final Pattern lastPattern = Pattern.compile("^[A-Za-z]+$");
    private final Pattern emailPattern = Pattern.compile("^[a-z0-9]+(?:\\.[a-z0-9]+)*@[a-z]+(?:\\.[a-z]+)*$");
    private final Pattern passPattern = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&* )(+=._-])(?=.*[0-9])[A-Za-z0-9!@#$%^&* )(+=._-]{8,}$");
    private final Pattern phoneNumPattern = Pattern.compile("^[0-9]{10}$");
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignUpServlet() {
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

        String companyname=request.getParameter("companyname");
        String firstName=request.getParameter("firstName");
        String lastName=request.getParameter("lastName");
        String email=request.getParameter("email");
        String password=request.getParameter("password");
        String phoneNumber=request.getParameter("phoneNumber");

        JSONObject responseJSON = new JSONObject();

        if (!namePattern.matcher(firstName).find()) {
            responseJSON.put("statusCode", 400);
            responseJSON.put("statusMessage", "FAILURE");
            responseJSON.put("detailedMessage", "First name");
            response.getWriter().write(responseJSON.toString());
            return;
        }

        if (!lastPattern.matcher(lastName).find()) {
            responseJSON.put("statusCode", 400);
            responseJSON.put("statusMessage", "FAILURE");
            responseJSON.put("detailedMessage", "last name");
            response.getWriter().write(responseJSON.toString());
            return;
        }

        if (!comPattern.matcher(companyname).find()) {
            responseJSON.put("statusCode", 400);
            responseJSON.put("statusMessage", "FAILURE");
            responseJSON.put("detailedMessage", "company name");
            response.getWriter().write(responseJSON.toString());
            return;
        }

        if (!emailPattern.matcher(email).find()) {
            responseJSON.put("statusCode", 400);
            responseJSON.put("statusMessage", "FAILURE");
            responseJSON.put("detailedMessage", "email");
            response.getWriter().write(responseJSON.toString());
            return;
        }

        if (!passPattern.matcher(password).find()) {
            responseJSON.put("statusCode", 400);
            responseJSON.put("statusMessage", "FAILURE");
            responseJSON.put("detailedMessage", "Password");
            response.getWriter().write(responseJSON.toString());
            return;
        }
        
        if (!phoneNumPattern.matcher(phoneNumber).find()) {
            responseJSON.put("statusCode", 400);
            responseJSON.put("statusMessage", "FAILURE");
            responseJSON.put("detailedMessage", "Phone number");
            response.getWriter().write(responseJSON.toString());
            return;
        }
        try {
            PreparedStatement psmt=Application.dbConnection.prepareStatement("insert into users(firstName,lastName,email,password,phoneNumber) values(?,?,?,?,?)");
//		    psmt.setString(1,companyname);
            psmt.setString(1,firstName );//		System.out.println(email);
//            System.out.print(String.valueOf(email));
//            System.out.println(email);
            psmt.setString(2, lastName);
            psmt.setString(3, email);
            psmt.setString(4,password);
            psmt.setBigDecimal(5, new BigDecimal(phoneNumber));
            psmt.executeUpdate();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
//			e.printStackTrace();
            System.out.println(e.getMessage());
        }
        int userId=0;
        Statement st = null;
        try {
            st = Application.dbConnection.createStatement();
        } catch (SQLException e2) {
            System.out.println(e2.getMessage());
        }
        try {
            ResultSet rs=st.executeQuery("select * from users where phoneNumber="+phoneNumber);
            if(rs.next()) {
                rs.getInt("userId");
                userId=rs.getInt("userId");
            }

        } catch (SQLException e1) {
            System.out.println(e1.getMessage());
        }
        

        try {
            PreparedStatement psmt1=Application.dbConnection.prepareStatement("insert into company(companyName,superUserId) values(?,?)");
            psmt1.setString(1, companyname);
            psmt1.setInt(2,userId);
            psmt1.executeUpdate();

        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        

        
        
		UUID sessionId=UUID.randomUUID();
		CookieManagement.setCookie(response, "SESSIONID", String.valueOf(sessionId));
        
		

		PreparedStatement psmt2;
		try {
			psmt2 = Application.dbConnection.prepareStatement("insert into session values(?,?)");
			psmt2.setString(1, String.valueOf(sessionId));
			psmt2.setInt(2, userId);
			psmt2.executeUpdate();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		
        
        
        int companyId=getCompanyId.getCompanyId(userId);
		/* System.out.println("company Id : "+companyId); */
        try {
			PreparedStatement pst=Application.dbConnection.prepareStatement("insert into companyMembers values(?,?,?,?,?)");
            pst.setInt(1,companyId );
            pst.setInt(2,userId);
//            SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
            
            Date joinDate=new Date();
            java.sql.Timestamp stDate = new java.sql.Timestamp(joinDate.getTime());
			/* System.out.println(stDate); */
			pst.setTimestamp(3, stDate);
			
			pst.setTimestamp(4, stDate);
			pst.setInt(5, userId);
			pst.executeUpdate();
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		/* System.out.println("user Id : "+userId); */
        
        responseJSON.put("statusCode", 200);
        responseJSON.put("statusMessage", "SUCCESS");
        responseJSON.put("detailedMessage", "sign up Successful");
        response.getWriter().write(responseJSON.toString());
	}
	
	
	

}
