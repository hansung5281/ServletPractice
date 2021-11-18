import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/calc")
public class calc extends HttpServlet {
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		request.setCharacterEncoding("UTF-8");

		String x_ = request.getParameter("x");
		String y_ = request.getParameter("y");
		int x = 0;
		int y = 0;
		if (x_ != null && !x_.equals("")) {
			x = Integer.parseInt(x_);
		}
		if (y_ != null && !y_.equals("")) {
			y = Integer.parseInt(y_);
		}
		int result = 0;
		String op = request.getParameter("operator");
		if (op.equals("µ¡¼À")) {
			result = x + y;
		}else {
			result = x - y;
		}
		
		response.getWriter().printf("result = %d", result);
	}
}
