package filter;

import common.Application;

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
//import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jasper.tagplugins.jstl.core.Out;

/**
 * Servlet Filter implementation class taskFilter
 */
//@WebFilter(urlPatterns = {""})
public class TaskFilter extends HttpFilter implements Filter {
       
    /**
     * @see HttpFilter#HttpFilter()
     */
    public TaskFilter() {
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
		int userId = (int) request.getAttribute("userId");
//		System.out.println(userId);
		String path = ((HttpServletRequest) request).getPathInfo();
//		System.out.println(path);

		try {
			if (path.equals("/UpdateTaskData")) {
//				System.out.println("sdfg");
				int taskId = Integer.parseInt(request.getParameter("taskId"));
//				System.out.println("taskId : "+taskId);
//				System.out.println("userId : "+userId);

				PreparedStatement psmt = Application.dbConnection.prepareStatement(
					"select * from taskSummary inner join teamList on taskSummary.teamId=teamList.teamId " +
					"where taskSummary.taskId=? and (taskSummary.taskAssignedTo=? or teamList.teamLead=? or teamList.createBy=?)"
				);

				psmt.setInt(1, taskId);
				psmt.setInt(2, userId);
				psmt.setInt(3, userId);
				psmt.setInt(4, userId);

				ResultSet rs = psmt.executeQuery();

				if (rs.next()) {
//					System.out.println("hjkl");
					chain.doFilter(request, response);
				} else {
//					System.out.println("bnm");
					((HttpServletResponse) response).setStatus(401);
					response.getWriter().write("Permission denied");
				}

			}
			else if(path.equals("/Taskget") || path.equals("/getSmallLIstTask")) {
				String role=(String) request.getAttribute("companyRole");
				
				try {
				     if(role.equals("CEO")) {
						HashSet<Integer> ceoTask=new HashSet<>();
						
						
						PreparedStatement psmt=Application.dbConnection.prepareStatement("select * from taskSummary inner join project on taskSummary.projectId=project.projectId where project.createdBy=?");
						psmt.setInt(1, userId);
						ResultSet rs=psmt.executeQuery();
						while(rs.next()) {
//							System.out.println("ceo");
							ceoTask.add(rs.getInt("taskSummary.taskId"));
						}
						request.setAttribute("ownerTaskList", ceoTask);
						chain.doFilter(request, response);
						
					}
				     else {
//				    	 System.out.println("taskGet");
				    	 HashSet<Integer> teamLeadTask=new HashSet<>();
//				    	 HashSet<Integer> teamMemberTask=new HashSet<>();
				    	 
				    	 PreparedStatement psmt=Application.dbConnection.prepareStatement("select * from teamList inner join taskSummary on taskSummary.teamId=teamList.teamId where teamList.teamLead=?;");
				    	 psmt.setInt(1, userId);
				    	 ResultSet rs=psmt.executeQuery();
				    	 while(rs.next()) {
//				    		 System.out.println("teamLead");
				    		 teamLeadTask.add(rs.getInt("taskSummary.taskId"));
				    	 }
				    	 
				    	 
				    	 PreparedStatement psmt1=Application.dbConnection.prepareStatement("select * from teamMembers inner join taskSummary on taskSummary.teamId=teamMembers.teamId where teamMembers.userId=?");
				    	 psmt1.setInt(1, userId);
				    	 ResultSet rs1=psmt1.executeQuery();
				    	 while(rs1.next()) {
//				    		 System.out.println("member");
				    		 teamLeadTask.add(rs1.getInt("taskSummary.taskId"));
				    	 }
				    	 
				    	 request.setAttribute("TeamLeadTaskList", teamLeadTask);
//				    	 request.setAttribute("TeamMemberTaskList", teamMemberTask);
					     chain.doFilter(request, response);
				    	 
				     }
					
				}
				catch(SQLException e) {
					e.printStackTrace();
					response.getWriter().write("Internal server error");
				}
				
			}
//			else if(path.equals("/getSmallLIstTask")) {
//				String role=(String) request.getAttribute("companyRole");
////				System.out.println("taskGet");
//				try {
//				     if(role.equals("CEO")) {
//						HashSet<Integer> ceoTask=new HashSet<>();
//						
//						
//						PreparedStatement psmt=Application.dbConnection.prepareStatement("select * from taskSummary where taskCreatedBy=?");
//						psmt.setInt(1, userId);
//						ResultSet rs=psmt.executeQuery();
//						while(rs.next()) {
////							System.out.println("ceo");
//							ceoTask.add(rs.getInt("taskId"));
//						}
//						request.setAttribute("ownerTasksmallList", ceoTask);
//						chain.doFilter(request, response);
//						
//					}
//				     else {
//				    	 
//				    	 HashSet<Integer> teamLeadTask=new HashSet<>();
//				    	 HashSet<Integer> teamMemberTask=new HashSet<>();
//				    	 
//				    	 PreparedStatement psmt=Application.dbConnection.prepareStatement("select * from teamList inner join taskSummary on taskSummary.teamId=teamList.teamId where teamList.teamLead=?;");
//				    	 psmt.setInt(1, userId);
//				    	 ResultSet rs=psmt.executeQuery();
//				    	 while(rs.next()) {
////				    		 System.out.println("teamLead");
//				    		 teamLeadTask.add(rs.getInt("taskSummary.taskId"));
//				    	 }
//				    	 
//				    	 
//				    	 PreparedStatement psmt1=Application.dbConnection.prepareStatement("select * from teamMembers inner join taskSummary on taskSummary.teamId=teamMembers.teamId where teamMembers.userId=?");
//				    	 psmt1.setInt(1, userId);
//				    	 ResultSet rs1=psmt1.executeQuery();
//				    	 while(rs1.next()) {
////				    		 System.out.println("member");
//				    		 teamMemberTask.add(rs.getInt("taskSummary.taskId"));
//				    	 }
//				    	 
//				    	 request.setAttribute("TeamLeadsmallTaskList", teamLeadTask);
//				    	 request.setAttribute("TeamMembersmallTaskList", teamMemberTask);
//					     chain.doFilter(request, response);
//				    	 
//				     }
//					
//				}
//				catch(SQLException e) {
//					e.printStackTrace();
//					response.getWriter().write("Internal server error");
//				}
//				
//			}
			else {
				chain.doFilter(request, response);
			}

		} catch(SQLException e){
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
