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
//import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import common.Application;

/**
 * Servlet Filter implementation class memberFilter
 */
//@WebFilter("/memberFilter")
public class InviteFilter extends HttpFilter implements Filter {
       
    /**
     * @see HttpFilter#HttpFilter()
     */
    public InviteFilter() {
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
		// TODO Auto-generated method stub
		// place your code here

		// pass the request along the filter chain
		int userId=(int) request.getAttribute("userId");
		String path=((HttpServletRequest)request).getPathInfo();
		String role=(String)request.getAttribute("companyRole");
		
		if(path.equals("/inviteNew")) {
			if(role.equals("CEO")) {
				try {
					PreparedStatement psmt=Application.dbConnection.prepareStatement("select * from company where superUserId=?");
					psmt.setInt(1, userId);
					ResultSet rs=psmt.executeQuery();
					if (rs.next()) {
						chain.doFilter(request, response);
					}
					else {
						((HttpServletResponse)response).setStatus(401);
						response.getWriter().write("Permission denied");
					}
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					((HttpServletResponse)response).setStatus(500);
					response.getWriter().write("Internal server error");
				}
			}
			else {
				JSONObject json=new JSONObject();
				json.put("statusCode",200);
				json.put("Message", "Failed");
				json.put("detailedMessage", "Yor are not company owner");
				
				response.getWriter().write(json.toString());
			}
		}
		
		
		
		
		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
