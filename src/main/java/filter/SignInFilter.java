package filter;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
//import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jasper.tagplugins.jstl.core.Out;

import common.Application;
import common.CookieManagement;

/**
 * Servlet Filter implementation class signInFilter
 */
//@WebFilter("/signInFilter")
public class SignInFilter extends HttpFilter implements Filter {
       
    /**
     * @see HttpFilter#HttpFilter()
     */
    public SignInFilter() {
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
//			System.out.println("signInCheck");
		
			try {
				PreparedStatement psmt=Application.dbConnection.prepareStatement("select * from session where sessionId=?");
				psmt.setString(1,stringID);
				ResultSet rs=psmt.executeQuery();
				
				if(!rs.next()) {
					CookieManagement.removeCookie((HttpServletResponse) response, stringID);
//					System.out.println("CEO");
					
					chain.doFilter(request, response);	
				}
				else {
//					System.out.println("signInCheck");
					((HttpServletResponse) response).sendRedirect("/zohoProjectManagement/taskSummary.html");
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		else {
			chain.doFilter(request, response);
		}
		
		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
