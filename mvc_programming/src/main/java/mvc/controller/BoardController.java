package mvc.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mvc.dao.BoardDao;
import mvc.vo.BoardVo;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Servlet implementation class BoardController
 */
@WebServlet("/BoardController")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	String location;	// 멤버변수(전역) 초기화 => 이동할 페이지
	
	public BoardController(String location) {
		this.location = location;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String paramMethod = "";	// 전송방식이 sendRedirect면 S foward방식이면 F
		String url = "";
		
		if (location.equals("boardList.aws")) {	// 가상 경로
			// System.out.println("boardList.aws");
			
			BoardDao bd = new BoardDao();
			ArrayList<BoardVo> alist = bd.boardSelectAll();
			
			request.setAttribute("alist", alist);
			
			// System.out.println(alist);
			
			url = request.getContextPath() + "/board/boardList.jsp";	// 실제 내부 경로
			paramMethod = "F";

		} else if (location.equals("boardWrite.aws")) {	// 가상 경로
			 System.out.println("boardList.aws");

			
			url = request.getContextPath() + "/board/boardWrite.jsp";	// 실제 내부 경로
			paramMethod = "F";

		}
		
		
				
		
		if (paramMethod.equals("F")) {
			RequestDispatcher rd = request.getRequestDispatcher(url);
			rd.forward(request, response);		
		} else if (paramMethod.equals("S")) {
			response.sendRedirect(url);
		}
		
		
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
