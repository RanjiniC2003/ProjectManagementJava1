package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import classess.Task;
import common.TaskSingleton;
import jdbc.TaskJDBC;

/**
 * Servlet implementation class task
 */
@WebServlet("/task/*")
public class TaskServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TaskServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**desc invite;desc invite;
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
		if(path.equals("/addTask")) {
			String taskName = request.getParameter("newTask");
			String projectName=request.getParameter("projectName");
			String newDesciption = request.getParameter("description");
			String newtaskAssignedTo = request.getParameter("assignTo");
			String newTaskstartDate = request.getParameter("startDate");
			String newTaskDueDate = request.getParameter("dueDate");
			String PriorityLis = request.getParameter("priority");
			
			response.getWriter().write(TaskJDBC.addTask(taskName,projectName,newDesciption,newtaskAssignedTo,newTaskstartDate,newTaskDueDate,PriorityLis,request).toString());
		}

		else if(path.equals("/UpdateTaskData")) {
			String taskId=request.getParameter("taskId");
			String taskName = request.getParameter("newTask");
//			String projectName=request.getParameter("projectName");
			String newDesciption = request.getParameter("description");
			String taskStatus=request.getParameter("taskStatus");
			String newtaskAssignedTo = request.getParameter("assignTo");
			String PriorityLis = request.getParameter("priority");
			String newTaskstartDate = request.getParameter("startDate");
			String newTaskDueDate = request.getParameter("dueDate");
			String completePercentage=request.getParameter("completePercentage");
			
			response.getWriter().write(TaskJDBC.updateTaskData(taskId, taskName, newDesciption, taskStatus, newtaskAssignedTo, PriorityLis, newTaskstartDate, newTaskDueDate, completePercentage, request).toString());
		}

		else if(path.equals("/Taskget")) {
			try {
				JSONObject json=new JSONObject();
				String statusType=request.getParameter("Status");				
				response.getWriter().write(TaskJDBC.getAllTeamTasks(request, statusType).toString());
			} catch (Exception e) {
				response.setStatus(500);
				response.getWriter().write("Internal server error");
			}
		}
		
		else if(path.equals("/SaveUpdateDatas")) {
			String taskId=request.getParameter("taskId");
			int task=Integer.parseInt(taskId);
			response.getWriter().write(TaskJDBC.saveUpdateDatas(task,request).toString());
		}

		else if(path.equals("/getSmallLIstTask")) {
			try {
				int taskId=Integer.parseInt(request.getParameter("taskId"));
				Task task = TaskSingleton.getInstance().getTaskById(taskId);
				
				JSONObject json=new JSONObject();
				json.put("statusType", task.getStatus());
				json.put("tasks",TaskJDBC.getAllTeamTasks(request, task.getStatus()) );
				response.getWriter().write(json.toString());
			} catch (Exception e) {
				response.setStatus(500);
				response.getWriter().write("Internal server error");
			}
		}
		
	}

}
