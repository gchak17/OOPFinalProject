package servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Account;
import dao.Avatar;
import managers.AccountData;
import managers.AvatarManager;
import managers.AccountIDGenerator;

/**
 * Servlet implementation class AccountCreationServlet
 */
@WebServlet("/AccountCreationServlet")
public class AccountCreationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AccountCreationServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		AccountData accountData = (AccountData) getServletContext().getAttribute("accountData");
		AvatarManager avatarManager = (AvatarManager) getServletContext().getAttribute("avatarManager");
		AccountIDGenerator generator = (AccountIDGenerator) getServletContext().getAttribute("idGenerator");

		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		long avatar_id = Long.valueOf(request.getParameter("avatar_id"));
		Avatar avatar = avatarManager.getAvatarByID(avatar_id);

		if (!accountData.nameInUse(userName)) {
			Account account = new Account(generator.generateID(), userName, password, avatar, new ArrayList<Long>());
			accountData.addAccount(account);
			request.getSession().setAttribute("user", account);
			request.getRequestDispatcher("Main.jsp").forward(request, response);
		} else {
			request.getRequestDispatcher("NameInUse.jsp").forward(request, response);
		}
	}

}
