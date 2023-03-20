package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jdbc.TeamJDBC;

/**
 * Servlet implementation class team
 */
@WebServlet("/team/*")
public class TeamServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public TeamServlet() {
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
		String path=request.getPathInfo();
		if(path.equals("/addTeams")) {
			String teamName=request.getParameter("teamName");
			String teamLead=request.getParameter("teamLead");
			String teamMembers=request.getParameter("teamMembers");
			String[] teamUsers=teamMembers.split(",");

			
			response.getWriter().write(TeamJDBC.addTeam(teamName,teamLead,teamUsers,request).toString());
		}
		else if(path.equals("/getTeamDetails")) {

	          response.getWriter().write(TeamJDBC.getTeamDetails(request).toString());
		}
		else if(path.equals("/updateTeam")) {

			String teamId=request.getParameter("teamId");
			String teamLead=request.getParameter("teamLead");
			String teamMembers=request.getParameter("teamMembers");
			String[] teamUsers=teamMembers.split(",");

			response.getWriter().write(TeamJDBC.updateTeams(teamId, teamLead, teamUsers, request).toString());
		}
		else if(path.equals("/SaveUpdateDatasInTeams")) {
			String teamId=request.getParameter("teamId");
			response.getWriter().write(TeamJDBC.SaveUpdateDatasInTeam(Integer.parseInt(teamId),request).toString());
		}
		
	}

}
