package filter;

import common.Application;
//import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.*;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

public class IssueFilter extends HttpFilter implements Filter {

    /**
     * @see HttpFilter#HttpFilter()
     */
    public IssueFilter() {
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
        String path = ((HttpServletRequest) request).getPathInfo();

        try {
            if (path.equals("/UpdateIssueData")) {
                BufferedReader br=request.getReader();
                StringBuilder sb=new StringBuilder();
                String line=null;

                while((line=br.readLine())!=null) {
                    sb.append(line);
                }

                String val=sb.toString();

                JSONParser parser=new JSONParser();

                JSONObject object=(JSONObject) parser.parse(val);

                String issueId = (String) object.get("issueId");
                System.out.println(issueId);

                PreparedStatement psmt = Application.dbConnection.prepareStatement(
                        "select * from issue inner join project on issue.projectId=project.projectId inner join teamList on project.teamId=teamList.teamId " +
                                "where issue.issueId=? and (issue.issueAssignTo=? or teamList.teamLead=? or teamList.createBy=?)"
                );

                psmt.setInt(1, Integer.parseInt(issueId));
                psmt.setInt(2, userId);
                psmt.setInt(3, userId);
                psmt.setInt(4, userId);

                ResultSet rs = psmt.executeQuery();

                if (rs.next()) {
                	request.setAttribute("issueId",object.get("issueId"));
                	request.setAttribute("issueName",object.get("issueName"));
                	request.setAttribute("description",object.get("description"));
//                	request.setAttribute("projectName",object.get("projectName"));
                	request.setAttribute("issueAssignTo",object.get("assignTo"));
                	request.setAttribute("dueDate",object.get("dueDate"));
                	request.setAttribute("severity",object.get("Severity"));
                	request.setAttribute("classification",object.get("classification"));
    				request.setAttribute("reproducible",object.get("reproducible"));
    				request.setAttribute("flag",object.get("flag"));
    				request.setAttribute("status",object.get("status"));
                    chain.doFilter(request, response);
                } else {
                    ((HttpServletResponse) response).setStatus(401);
                    response.getWriter().write("Permission denied");
                }

            }
            else if(path.equals("/IssueGet") || path.equals("/getSmallListIssue")) {
                String role=(String) request.getAttribute("companyRole");

                try {
                    if(role.equals("CEO")) {
                        HashSet<Integer> ceoIssue=new HashSet<>();


                        PreparedStatement psmt=Application.dbConnection.prepareStatement("select * from issue inner join project on issue.projectId=project.projectId where project.createdBy=?");
                        psmt.setInt(1, userId);
                        ResultSet rs=psmt.executeQuery();
                        while(rs.next()) {
                            ceoIssue.add(rs.getInt("issue.issueId"));
                        }
                        request.setAttribute("ownerIssueList", ceoIssue);
                        chain.doFilter(request, response);

                    }
                    else {
                        HashSet<Integer> teamLeadIssue=new HashSet<>();

                        PreparedStatement psmt=Application.dbConnection.prepareStatement(
                                "select * from issue inner join project on issue.projectId=project.projectId inner join teamList on project.teamId=teamList.teamId where teamList.teamLead=?;"
                        );
                        psmt.setInt(1, userId);
                        ResultSet rs=psmt.executeQuery();
                        while(rs.next()) {
                            teamLeadIssue.add(rs.getInt("issue.issueId"));
                        }


                        PreparedStatement psmt1=Application.dbConnection.prepareStatement("select * from issue inner join project on issue.projectId=project.projectId inner join teamMembers on project.teamId=teamMembers.teamId where teamMembers.userId=?");
                        psmt1.setInt(1, userId);
                        ResultSet rs1=psmt1.executeQuery();
                        while(rs1.next()) {
                            teamLeadIssue.add(rs1.getInt("issue.issueId"));
                        }

                        request.setAttribute("TeamLeadIssueList", teamLeadIssue);
                        chain.doFilter(request, response);

                    }

                }
                catch(SQLException e) {
                    e.printStackTrace();
                    response.getWriter().write("Internal server error");
                }

            }

            else {
                chain.doFilter(request, response);
            }

        } catch(SQLException | ParseException e){
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
