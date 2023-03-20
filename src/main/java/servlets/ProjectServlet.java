package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.apache.tomcat.util.json.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
//import com.mysql.cj.xdevapi.JsonParser;

import classess.Team;
import common.ProjectSingleton;
import jdbc.ProjectJDBC;

/**
 * Servlet implementation class project
 */
@WebServlet("/project/*")
public class ProjectServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProjectServlet() {
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        String path=request.getPathInfo();
        if(path.equals("/addProduct")) {
            String projectName=request.getParameter("projectName");
            String team=request.getParameter("team");
            String startDate=request.getParameter("startDate");
            String endDate=request.getParameter("dueDate");
            String strict=request.getParameter("strict");
            String publicType=request.getParameter("publicType");
            String description=request.getParameter("description");
            String status="active";
         
            response.getWriter().write(ProjectJDBC.addProject(projectName, team, startDate, endDate, strict, publicType, description, status, request).toString());
        }
        else if(path.equals("/getProjectDetails")) {
            response.getWriter().write(ProjectJDBC.getProjectDetails(request).toString());
        }
        else if(path.equals("/getSmallListProject")) {
            String projectId=request.getParameter("projectId");
            response.getWriter().write(ProjectJDBC.getSmallListProject(projectId, request).toString());
        }
        else if(path.equals("/saveUpdateProject")) {
            String projectId=request.getParameter("projectId");
            response.getWriter().write(ProjectJDBC.saveUpdateProject(Integer.parseInt(projectId), request).toString());
        }
        else if(path.equals("/updateProjectDetails")) {

        	
        	
        	
        	String projectId=(String)request.getAttribute("projectId");
//        	System.out.println(projectId);
            String projectName=(String)request.getAttribute("projectName");
//            Team team=ProjectSingleton.getInstance().getProjectById(Integer.parseInt(projectId)).getTeam();
            String startDate=(String)request.getAttribute("startDate");
            String endDate=(String)request.getAttribute("endDate");
//            String strict=request.getParameter("strict");
//            String publicType=request.getParameter("publicType");
            String description=(String)request.getAttribute("description");
            String status=(String)request.getAttribute("statusType");
//            String modifiedOn=request.getParameter("modifiedOn");

            response.getWriter().write(ProjectJDBC.updateProjectDetails(Integer.parseInt(projectId),projectName,startDate,endDate,description,status,request).toString());
        }
    }

}