package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import managers.AccountData;
import managers.ReviewsManager;

/**
 * Servlet implementation class WriteReview
 */
@WebServlet("/WriteReview")
public class WriteReview extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WriteReview() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		AccountData accountData = (AccountData) getServletContext().getAttribute("accountData");
		ReviewsManager reviewsManager = (ReviewsManager) getServletContext().getAttribute("reviewsManager");
		String point = request.getParameter("point");
		String reviewRecieverUsername = request.getParameter("reviewRecieverUsername");
		reviewsManager.addReview(accountData.getAccountByUsername(reviewRecieverUsername).getID(), Integer.parseInt(point));
		request.getRequestDispatcher("Profile.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
