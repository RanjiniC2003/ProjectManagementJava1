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
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import common.Application;

/**
 * Servlet Filter implementation class authendication
 */
//@SuppressWarnings("serial")
//@WebFilter("/authendication")
public class TeamFilter extends HttpFilter implements Filter {
       
    /**
     * @see HttpFilter#HttpFilter()
     */
    public TeamFilter() {
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
		
		int userId=(int) request.getAttribute("userId");
		String role=(String) request.getAttribute("companyRole");

		String path=((HttpServletRequest) request).getPathInfo();
//			System.out.println(path);
		try{
			if (path.equals("/addTeams")) {

				if(role.equals("CEO")) {
//					System.out.println("CEO");
					chain.doFilter(request, response);
				}
				else {
//					System.out.println("Member");
					JSONObject json=new JSONObject();
					json.put("statusCode",400);
					json.put("message", "Failed");
					json.put("detailedMessage","You are not company owner");
					response.getWriter().write(json.toString());
				}
			}
			else if(path.equals("/getTeamDetails")) {
				if(role.equals("CEO")) {

					HashSet<Integer> ceo=new HashSet<>();

					PreparedStatement psmt=Application.dbConnection.prepareStatement("select * from teamList where createBy=?");
					psmt.setInt(1, userId);
					ResultSet rs=psmt.executeQuery();
					while(rs.next()) {
						ceo.add(rs.getInt("teamId"));
					}

					request.setAttribute("owner", ceo);
					chain.doFilter(request, response);

				}
				else {
					PreparedStatement psmt=Application.dbConnection.prepareStatement("select * from teamList where teamLead=?");
					psmt.setInt(1, userId);
					ResultSet rs=psmt.executeQuery();
					HashSet<Integer> teamLead=new HashSet<>();
					while(rs.next()) {
						teamLead.add(rs.getInt("teamId"));
					}

					PreparedStatement psmt2=Application.dbConnection.prepareStatement("select * from teamMembers where userId=?");
					psmt2.setInt(1, userId);
					ResultSet rs2=psmt2.executeQuery();

					while(rs2.next()) {
						teamLead.add(rs2.getInt("teamId"));
					}

					request.setAttribute("teamLead", teamLead);
//						request.setAttribute("teamMember", teamMember);

					chain.doFilter(request, response);
				}

			} else if (path.equals("/updateTeam")) {
				int teamId = Integer.parseInt(request.getParameter("teamId"));
				PreparedStatement psmt=Application.dbConnection.prepareStatement("select * from teamList where teamId=? and (teamLead=? or createBy=?)");

				psmt.setInt(1, teamId);
				psmt.setInt(2, userId);
				psmt.setInt(3, userId);

				ResultSet rs = psmt.executeQuery();

				if (rs.next()) {
					chain.doFilter(request, response);
				} else {
					JSONObject json = new JSONObject();
					json.put("statusCode", 400);
					json.put("message", "Failed");
					json.put("detailedMessage", "Yor are not company owner or team lead");
					response.getWriter().write(json.toString());
				}

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			((HttpServletResponse) response).setStatus(500);
			response.getWriter().write("Internal server error");
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
