package filter;

import java.io.IOException;
import java.security.Principal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.Application;
import common.CookieManagement;

/**
 * Servlet Filter implementation class Autherazation
 */
//@WebFilter("/Autherazation")
public class AuthenticationFilter extends HttpFilter implements Filter {
       
    /**
     * @see HttpFilter#HttpFilter()
     */
    public AuthenticationFilter() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		
		String stringID = CookieManagement.getCookie((HttpServletRequest)request, "SESSIONID");
		if (!stringID.isEmpty() && !stringID.isBlank()) {
			try {
				int userId=0;
				PreparedStatement psmt=Application.dbConnection.prepareStatement("select * from session where sessionId=?");
				psmt.setString(1,stringID);
				ResultSet rs=psmt.executeQuery();
				if (rs.next()) {
					userId=rs.getInt("userId");
					request.setAttribute("userId", userId);
					
					PreparedStatement psmt2=Application.dbConnection.prepareStatement("select * from company where superUserId=?");
					psmt2.setInt(1, userId);
					ResultSet rs2=psmt2.executeQuery();
					if(rs2.next()) {
						request.setAttribute("companyRole","CEO");

						chain.doFilter(request, response);
					}
					else {
						request.setAttribute("companyRole", "employee");
						chain.doFilter(request, response);
					}
			    }
				else {
					((HttpServletResponse)response).setStatus(301);
					response.getWriter().write("Permission denied");
				}
			} catch (SQLException e) {
				e.printStackTrace();

				((HttpServletResponse)response).setStatus(401);
				response.getWriter().write("Permission denied");
			}
		}
		else {
			((HttpServletResponse)response).setStatus(301);
			
		}
		
		
		



}


	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
