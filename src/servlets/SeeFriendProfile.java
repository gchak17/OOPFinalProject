package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Account;
import managers.AccountData;

/**
 * Servlet implementation class SeeFriendProfile
 */
@WebServlet("/SeeFriendProfile")
public class SeeFriendProfile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SeeFriendProfile() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		AccountData accountData = (AccountData) getServletContext().getAttribute("accountData");
		String friendName = request.getParameter("friendName");
		Account friend = accountData.getAccountByUsername(friendName);
		request.getSession().setAttribute("friend", friend);
		System.out.println(friendName);
		request.getRequestDispatcher("Profile.jsp").forward(request, response);
	}

}
