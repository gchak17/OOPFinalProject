package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Account;
import managers.AccountData;
import managers.FriendRequestManager;

/**
 * Servlet implementation class AcceptFriendRequestServlet
 */
@WebServlet("/AcceptFriendRequestServlet")
public class AcceptFriendRequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AcceptFriendRequestServlet() {
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
		FriendRequestManager friendRequestManager = (FriendRequestManager) getServletContext().getAttribute("friendRequestManager");
		AccountData accountData = (AccountData)getServletContext().getAttribute("accountData");
		Account requestReciever = (Account) request.getSession().getAttribute("user");
		String requestSenderUsername = request.getParameter("requestSenderUsername");
		Account requestSender = accountData.getAccountByUsername(requestSenderUsername);
		accountData.addFriend(requestSender, requestReciever.getID());
		
		requestReciever.addFriend(requestSender.getID());
		requestSender.addFriend(requestReciever.getID());
		
		friendRequestManager.deleteFriendRequest(requestSender.getID(), requestReciever.getID());
		request.removeAttribute("requestSenderUsername");
		request.getRequestDispatcher("NotificationPage.jsp").forward(request, response);
	}
}
