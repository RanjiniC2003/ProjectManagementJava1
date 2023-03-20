package filter;

import java.io.BufferedReader;
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
import javax.servlet.http.HttpServletRequest;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.http.HttpFilter;

import common.Application;

/**
 * Servlet Filter implementation class projectFilter
 */
//@WebFilter("/projectFilter")
public class ProjectFilter extends HttpFilter implements Filter {
       
    /**
     * @see HttpFilter#HttpFilter()
     */
    public ProjectFilter() {
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
		
		int userId=(int)request.getAttribute("userId");
		String companyRole=(String) request.getAttribute("companyRole");
		
		String path = ((HttpServletRequest) request).getPathInfo();
		
		if(path.equals("/addProduct")) {
			if (companyRole.equals("CEO")) {
				try {
					PreparedStatement psmt=Application.dbConnection.prepareStatement("select * from company where superUserId=?");
					psmt.setInt(1, userId);
					ResultSet rs=psmt.executeQuery();
					if (rs.next()) {
						chain.doFilter(request, response);
					}
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					response.getWriter().write("Internal server error");
				}
			}
			else {
				JSONObject json=new JSONObject();
				json.put("statusCode",400);
				json.put("message", "Failed");
				json.put("detailedMessage","You are not company owner");
				response.getWriter().write(json.toString());
			}
		}
		
		else if (path.equals("/updateProjectDetails") ) {
			BufferedReader br=request.getReader();
    		StringBuilder sb=new StringBuilder();
    		String line;
    		while((line=br.readLine())!=null) {
    			sb.append(line);
    		}
    		String json1=sb.toString();
    	
    		
    		
    		JSONParser parse=new JSONParser();
    		JSONObject obj=null;
    		try {
               obj=(JSONObject) parse.parse(json1);
    		} catch (ParseException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		
    		request.setAttribute("projectId",(String)obj.get("projectId"));
    		request.setAttribute("projectName", (String)obj.get("projectName"));
    		request.setAttribute("startDate",(String)obj.get("startDate"));
    		request.setAttribute("endDate", (String)obj.get("endDate"));
    		request.setAttribute("description",(String)obj.get("description"));
    		request.setAttribute("statusType",(String)obj.get("statusType"));
    		
    		String projectId=(String) obj.get("projectId");
			
			try {
				if (companyRole.equals("CEO")) {
					PreparedStatement psmt=Application.dbConnection.prepareStatement("select * from project where projectId=? and createdBy=?");
					psmt.setInt(1, Integer.parseInt(projectId));
					psmt.setInt(2, userId);
					
					ResultSet rs = psmt.executeQuery();
					
					if (rs.next()) {
						request.setAttribute("permission", "owner");
						chain.doFilter(request, response);
					} else {
						JSONObject json=new JSONObject();
						json.put("statusCode", 500);
						json.put("message", "failed");
						json.put("detailedMessage", "You are not company owner or teamLead");
						response.getWriter().write(json.toString());
					}
					
				} else {
					PreparedStatement psmt=Application.dbConnection.prepareStatement("select * from project inner join teamList on project.teamId=teamList.teamId where project.projectId=? and teamList.teamLead=?");
					psmt.setInt(1, Integer.parseInt(projectId));
					psmt.setInt(2, userId);
					
					ResultSet rs = psmt.executeQuery();
					
					if (rs.next()) {
						request.setAttribute("permission", "TeamLead");
						chain.doFilter(request, response);
					} else {
						request.setAttribute("permission", "user");
						JSONObject json=new JSONObject();
						json.put("statusCode", 500);
						json.put("message", "failed");
						json.put("detailedMessage", "You are not company owner or teamLead");
						response.getWriter().write(json.toString());
					}
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				response.getWriter().write("Internal server error");
			}
			
		} 
		else{
			
			if(companyRole.equals("CEO")) {
				
				try {
					HashSet<Integer> ceoProject=new HashSet<>();
					
					PreparedStatement psmt=Application.dbConnection.prepareStatement("select * from project where createdBy=?");
					psmt.setInt(1, userId);
					ResultSet rs=psmt.executeQuery();
					while(rs.next()) {
						ceoProject.add(rs.getInt("projectId"));
					}
					request.setAttribute("projectOwner", ceoProject);
					chain.doFilter(request, response);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					response.getWriter().write("Internal server error");
				}
			}
			else {
//				try {
////					HashSet<Integer> teamLeadProject=new HashSet<>();
//					HashSet<Integer> teamMemberProject=new HashSet<>();
//					
//					PreparedStatement psmt=Application.dbConnection.prepareStatement("select * from project");
//					ResultSet rs=psmt.executeQuery();
//					while(rs.next()) {
//						int teamId=rs.getInt("teamId");
//						PreparedStatement psmt2=Application.dbConnection.prepareStatement("select * from teamList where teamLead=? and teamId=?");
//						psmt2.setInt(1, userId);
//						psmt2.setInt(2, teamId);
//						ResultSet rs2=psmt2.executeQuery();
//						if (rs2.next()) {
//							teamMemberProject.add(rs.getInt("projectId"));
//						}
//						
//						
//						PreparedStatement psmt3=Application.dbConnection.prepareStatement("select * from teamMembers where userId=? and teamId=?");
//						psmt3.setInt(1, userId);
//						psmt3.setInt(2, teamId);
//						ResultSet rs3=psmt3.executeQuery();
//						if(rs3.next()) {
//							teamMemberProject.add(rs.getInt("projectId"));
//						}
//						
////						request.setAttribute("projectTeamLead",teamLeadProject );
//						request.setAttribute("projectNormalMember", teamMemberProject);
//						chain.doFilter(request, response);
//					}
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					response.getWriter().write("Internal server error");
//				}
//				
				
				
				
				try {
//					HashSet<Integer> teamLeadProject=new HashSet<>();
					HashSet<Integer> teamMemberProject=new HashSet<>();
					
					PreparedStatement psmt=Application.dbConnection.prepareStatement("select * from project inner join teamList on project.teamId=teamList.teamId where teamList.teamLead=?");
					psmt.setInt(1, userId);
					
					ResultSet rs=psmt.executeQuery();
					while(rs.next()) {
						teamMemberProject.add(rs.getInt("project.projectId"));
						
					}


					PreparedStatement psmt3=Application.dbConnection.prepareStatement("select * from project inner join teamMembers on project.teamId=teamMembers.teamId where teamMembers.userId=?");
					psmt3.setInt(1, userId);
					
					ResultSet rs3=psmt3.executeQuery();
					while(rs3.next()) {
						teamMemberProject.add(rs3.getInt("project.projectId"));
					}

					request.setAttribute("projectNormalMember", teamMemberProject);
					chain.doFilter(request, response);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					response.getWriter().write("Internal server error");
				}
			}
		}
		// pass the request along the filter chain
		
		
			
		
		
		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
