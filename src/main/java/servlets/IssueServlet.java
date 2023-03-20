package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import classess.Issue;
import common.IssueSingleton;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import jdbc.IssueJDBC;


/**
 * Servlet implementation class IssueServlet
 */
@WebServlet("/issue/*")
public class IssueServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public IssueServlet() {
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
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String path=request.getPathInfo();

		try {
			if(path.equals("/addIssue")) {

				BufferedReader br=request.getReader();
	    		StringBuilder sb=new StringBuilder();
	    		String line;
	    		while((line=br.readLine())!=null) {
	    			sb.append(line);
	    		}
	    		String json1=sb.toString();
	    	
	    		
	    		
	    		JSONParser parse=new JSONParser();
	    		JSONObject object=null;
	    		try {
	               object=(JSONObject) parse.parse(json1);
	    		} catch (ParseException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}

				String issueName=(String) object.get("issueName");
				String description=(String) object.get("description");
				String projectName=(String) object.get("projectName");
				String issueAssignTo=(String) object.get("assignTo");
				String startDate=(String) object.get("startDate");
				String dueDate=(String) object.get("dueDate");
				String severity=(String) object.get("Severity");
				String classification=(String) object.get("classification");
				String reproducible=(String) object.get("reproducible");
				String flag=(String) object.get("flag");

				response.getWriter().write(IssueJDBC.addIssue(issueName,description,projectName,issueAssignTo,startDate,dueDate,severity,classification,reproducible,flag,request).toString());

			}

			else if(path.equals("/saveUpdateDatasInIssue")) {
				String issueId=request.getParameter("issueId");
				response.getWriter().write(IssueJDBC.saveUpdateDatasInIssue(Integer.parseInt(issueId), request).toString());
			}

			else if(path.equals("/UpdateIssueData")) {

				String issueId=(String)request.getAttribute("issueId");
				String issueName=(String) request.getAttribute("issueName");
				String description=(String)request.getAttribute("description");
//				String projectName=(String)request.getAttribute("projectName");
				String issueAssignTo=(String)request.getAttribute("issueAssignTo");
//				String startDate=(String)request.getAttribute("startDate");
				String dueDate=(String)request.getAttribute("dueDate");
				String severity=(String) request.getAttribute("severity");
				String classification=(String)request.getAttribute("classification");
				String reproducible=(String) request.getAttribute("reproducible");
				String flag=(String)request.getAttribute("flag");
				String status=(String)request.getAttribute("status");
			
				
			

				response.getWriter().write(IssueJDBC.updateIssueData(issueId, issueName, description, issueAssignTo, dueDate, severity, classification, reproducible, flag, status,request).toString());
			}

			else if(path.equals("/IssueGet")) {

				String classification=request.getParameter("classification");
				response.getWriter().write(IssueJDBC.getAllTeamIssues(request, classification).toString());

			} else if(path.equals("/getSmallListIssue")) {

				int issueId=Integer.parseInt(request.getParameter("issueId"));
				Issue issue = IssueSingleton.getInstance().getIssueById(issueId);

				JSONObject json=new JSONObject();
				json.put("classification", issue.getClassification());
				json.put("issues", IssueJDBC.getAllTeamIssues(request, issue.getClassification()));
				response.getWriter().write(json.toString());

			}

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(500);
			response.getWriter().write("Internal server error");
		}
	}
}
//
//	private JSONObject getJSON(HttpServletRequest request) throws IOException {
//		BufferedReader br=request.getReader();
//		StringBuilder sb=new StringBuilder();
//		String line=null;
//
//		while((line=br.readLine())!=null) {
//			sb.append(line);
//		}
//
//		String val=sb.toString();
//
//		JSONObject object=null;
//		JSONParser parser=new JSONParser();
//
//		try {
//			object=(JSONObject) parser.parse(val);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		return object;
//	}
//}
