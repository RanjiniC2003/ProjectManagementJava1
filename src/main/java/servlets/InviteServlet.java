package servlets;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Date;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import common.Application;
import common.CookieManagement;
import common.getCompanyId;

/**
 * Servlet implementation class invite
 */
@WebServlet("/invite/*" )
public class InviteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final Pattern namePattern = Pattern.compile("^[A-Za-z. ]+$");
	/* private final Pattern comPattern = Pattern.compile("^[A-Za-z ]+$"); */
    private final Pattern lastPattern = Pattern.compile("^[A-Za-z]+$");
    private final Pattern emailPattern = Pattern.compile("^[a-z0-9]+(?:\\.[a-z0-9]+)*@[a-z]+(?:\\.[a-z]+)*$");
    private final Pattern passPattern = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&* )(+=._-])(?=.*[0-9])[A-Za-z0-9!@#$%^&* )(+=._-]{8,}$");
    private final Pattern phoneNumPattern = Pattern.compile("^[0-9]{10}$");
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InviteServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest  request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String getPath=request.getPathInfo();
//		System.out.println(getPath);
		if(getPath.equals("/inviteNew")) {
			String email=request.getParameter("email");
			
			
			
			int userId=(int) request.getAttribute("userId");
			
			
//			String stringID = CookieManagement.getCookie(request, "user-id");
//			int userId=0;
//			
//			if (!stringID.isEmpty()) {
//				userId = Integer.parseInt(stringID);
//			}
			 
	
			/* int userId=Integer.parseInt(value); */
			
			
			
//			String userId=getCookieValuesInJava.getCookie(request,"userId"); 
//  		System.out.println("userId"+userId);
			
				try {
					Statement st=Application.dbConnection.createStatement();
					ResultSet rs=st.executeQuery("select * from invite where email='"+email+"' and companyId="+getCompanyId.getCompanyId(userId));
					if(rs.next()) {
						JSONObject responseJSON = new JSONObject();
						responseJSON.put("statusCode", 200);
						responseJSON.put("statusMessage", "massage failure");
						responseJSON.put("detailedMessage", "already this user was already invited");
						response.getWriter().write(responseJSON.toString());
						return;
					}
					
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
				try {
					PreparedStatement psmt3=Application.dbConnection.prepareStatement("select * from users where email=?");
				    psmt3.setString(1, email);
					ResultSet rs3=psmt3.executeQuery();
					if(rs3.next()) {
						
						JSONObject responseJSON = new JSONObject();
						responseJSON.put("statusCode", 200);
						responseJSON.put("statusMessage", "massage failure");
						responseJSON.put("detailedMessage", "already this user was already in user");
						return;
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				

				
				
				
				
				
				String to = email;
				/*
				 * common.MailService.getInstance().sendText(to, "Mail notification from Java",
				 * "Checking text type");
				 */
				String url = "http://ranjini-zstk321:8080/zohoProjectManagement/invite.html?email=" + URLEncoder.encode(email, StandardCharsets.UTF_8);

				common.MailService.getInstance().sendHTML(to, "Invite notification", "<h1>Projects invitation</h1><p>Click <a href=\"" + url + "\">here</a> to accept the invite</p>");

				
				
				
				
				try {
					PreparedStatement psmt=Application.dbConnection.prepareStatement("insert into invite(invitedBy,email,companyId) values(?,?,?)");
//					System.out.println(userId);
//					System.out.println("email : "+email);
					/* int invitedId= */
//					System.out.println("company id : "+getCompanyId.getCompanyId(userId));
					psmt.setInt(1,userId);
					psmt.setString(2, email);
					psmt.setInt(3, getCompanyId.getCompanyId(userId));
					psmt.executeUpdate();
					JSONObject responseJSON = new JSONObject();
					responseJSON.put("statusCode", 200);
					responseJSON.put("statusMessage", "SUCCESS");
					responseJSON.put("detailedMessage", "invitation send successfully");
				
					
//					  String url1="http://localhost:8080/javaConsoleFinalProject/invite.html";
//					  String params="email="+URLEncoder.encode(email,StandardCharsets.UTF_8);
//					 
//					System.out.println(url1+"?"+params); 
					
					
					 
					
					response.getWriter().write(responseJSON.toString());
				} 
				catch (SQLException e) {
					e.printStackTrace();
				}
			
			
		}
		else if(getPath.equals("/accept")) {
			

	        String firstName=request.getParameter("firstName");
	        String lastName=request.getParameter("lastName");
	      
	        String password=request.getParameter("password");
	        String phoneNumber=request.getParameter("phoneNumber");
	        String email=request.getParameter("email");
	        

	        JSONObject responseJSON = new JSONObject();

	        if (!namePattern.matcher(firstName).find()) {
	            responseJSON.put("statusCode", 200);
	            responseJSON.put("statusMessage", "FAILURE");
	            responseJSON.put("detailedMessage", "First name");
	            response.getWriter().write(responseJSON.toString());
	            return;
	        }

	        if (!lastPattern.matcher(lastName).find()) {
	            responseJSON.put("statusCode", 200);
	            responseJSON.put("statusMessage", "FAILURE");
	            responseJSON.put("detailedMessage", "last name");
	            response.getWriter().write(responseJSON.toString());
	            return;
	        }

	     

	        if (!emailPattern.matcher(email).find()) {
	            responseJSON.put("statusCode", 200);
	            responseJSON.put("statusMessage", "FAILURE");
	            responseJSON.put("detailedMessage", "email");
	            response.getWriter().write(responseJSON.toString());
	            return;
	        }

	        if (!passPattern.matcher(password).find()) {
	            responseJSON.put("statusCode", 200);
	            responseJSON.put("statusMessage", "FAILURE");
	            responseJSON.put("detailedMessage", "Password");
	            response.getWriter().write(responseJSON.toString());
	            return;
	        }
	        
	        if (!phoneNumPattern.matcher(phoneNumber).find()) {
	            responseJSON.put("statusCode", 200);
	            responseJSON.put("statusMessage", "FAILURE");
	            responseJSON.put("detailedMessage", "Phone number");
	            response.getWriter().write(responseJSON.toString());
	            return;
	        }
	        
	        int invitedBy=0;
	        int companyId=0;
	        int userId=0;
	        
	       try {
			PreparedStatement psmt1=Application.dbConnection.prepareStatement("select * from invite where email=?");
			psmt1.setString(1, email);
			
			ResultSet rs2=psmt1.executeQuery();
			if(!rs2.next()) {
				JSONObject json=new JSONObject();
				json.put("statusCode", 200);
	            json.put("statusMessage", "failure");
	            json.put("detailedMessage", "There is no invitation for this email ");
	            response.getWriter().write(json.toString());
	            return;
			}
			else {
				companyId=rs2.getInt("companyId");
				invitedBy=rs2.getInt("invitedBy");
		        try {
		            PreparedStatement psmt=Application.dbConnection.prepareStatement("insert into users(firstName,lastName,email,password,phoneNumber) values(?,?,?,?,?)");
//				    psmt.setString(1,companyname);
		            psmt.setString(1,firstName );//		System.out.println(email);
//		            System.out.print(String.valueOf(email));
//		            System.out.println(email);
		            psmt.setString(2, lastName);
		            psmt.setString(3, email);
		            psmt.setString(4,password);
		            psmt.setBigDecimal(5, new BigDecimal(phoneNumber));
		            psmt.executeUpdate();

		        } catch (SQLException e) {
		            System.out.println(e.getMessage());
		        }
		        
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
		        
			}
			} catch (SQLException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
		       
	        
	       
	       
	       UUID sessionId=UUID.randomUUID();
			CookieManagement.setCookie(response, "SESSIONID", String.valueOf(sessionId));
	        
			
			
//	        System.out.println(userId);
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
	        
	      
		/*
		 * int companyId=getCompanyId.getCompanyId(userId);
		 */
	        
	        try {
				PreparedStatement pst=Application.dbConnection.prepareStatement("insert into companyMembers values(?,?,?,?,?)");
	            pst.setInt(1,companyId );
	            pst.setInt(2,userId);
	           
	            
	            Date joinDate=new Date();
	            java.sql.Timestamp stDate = new java.sql.Timestamp(joinDate.getTime());
				/* System.out.println(stDate); */
				pst.setTimestamp(3, stDate);
				
				pst.setTimestamp(4,stDate);
				pst.setInt(5, invitedBy);
				pst.executeUpdate();
				
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
	        
	        
	        try {
				PreparedStatement psmt4=Application.dbConnection.prepareStatement("delete from invite where email=?");
				psmt4.setString(1, email);
				psmt4.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*
			 * Cookie cookie = new Cookie("userId",String.valueOf(userId));
			 * response.addCookie(cookie);
			 */
	        
	        responseJSON.put("statusCode", 200);
	        responseJSON.put("statusMessage", "SUCCESS");
	        responseJSON.put("detailedMessage", "You joined successfully");
	        response.getWriter().write(responseJSON.toString());
		}
		
		
		
		
	}

}
