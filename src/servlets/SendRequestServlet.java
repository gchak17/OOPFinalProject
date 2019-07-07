package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Account;
import dao.Avatar;
import managers.AccountData;
import managers.AccountIDGenerator;
import managers.AvatarManager;

/**
 * Servlet implementation class SendRequestServlet
 */
@WebServlet("/SendRequestServlet")
public class SendRequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendRequestServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AccountData accountData = (AccountData) getServletContext().getAttribute("accountData");
		String friendUserName = request.getParameter("friendusername");
		Account user = (Account)request.getSession().getAttribute("user");

		PrintWriter out = response.getWriter(); 
		response.setContentType("text/html");
		if(user.getUsername().equals(friendUserName)) {
			out.append("<p> that's your username.</p>");
		}else if(accountData.nameInUse(friendUserName)){
			if(user.getFriendByUsername(friendUserName) != null){
				out.append("<p> " + friendUserName + " is already your friend.</p>");
			}else{
				//user.addFriend();
				out.append("<p> friend request to " + friendUserName + " is sent.</p>");
			}
		}else{
			out.append("<p>user with that name does not exist.</p>");
		}
	}
}