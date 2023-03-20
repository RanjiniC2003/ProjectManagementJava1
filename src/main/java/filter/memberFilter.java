package filter;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;

import common.Application;

/**
 * Servlet Filter implementation class memberFilter
 */
//@WebFilter("/memberFilter")
public class memberFilter extends HttpFilter implements Filter {
       
    /**
     * @see HttpFilter#HttpFilter()
     */
    public memberFilter() {
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
		
		String path=((HttpServletRequest)request).getPathInfo();
		String role=(String) request.getAttribute("companyRole");
		int userId=(int) request.getAttribute("userId");
		
//		if(path.equals("/inviteNew")) {
			if(path.equals("/getTheirTeamMembers") || path.equals("/dropDownMembers")) {
				
				HashSet<Integer> companyMembers=new HashSet<>();
				
				try {
					PreparedStatement psmt=Application.dbConnection.prepareStatement("select * from companyMembers where userId=?");
					psmt.setInt(1,userId );
					ResultSet rs=psmt.executeQuery();
					if(rs.next()) {
						PreparedStatement psmt2=Application.dbConnection.prepareStatement("select * from companyMembers where companyId=?");
						psmt2.setInt(1, rs.getInt("companyId"));
						ResultSet rs2=psmt2.executeQuery();
						while(rs2.next()) {
							if(path.equals("/getTheirTeamMembers")) {
								companyMembers.add(rs2.getInt("userId"));
							}
							else if(path.equals("/dropDownMembers")) {
								if(rs2.getInt("invitedBy")!=rs2.getInt("userId")) {
									companyMembers.add(rs2.getInt("userId"));
								}
							}
						}
						
						request.setAttribute("companyMembers", companyMembers);
						chain.doFilter(request, response);
					}
					
					
				} catch (SQLException e) {
					e.printStackTrace();
					response.getWriter().write("Internal server error");
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
