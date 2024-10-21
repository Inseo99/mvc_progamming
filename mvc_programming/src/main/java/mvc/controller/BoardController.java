package mvc.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mvc.dao.BoardDao;
import mvc.vo.BoardVo;
import mvc.vo.Criteria;
import mvc.vo.PageMaker;

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
			
			String page = request.getParameter("page");
			if (page == null) page = "1";
			int pageInt = Integer.parseInt(page);
			
			Criteria cri = new Criteria();
			cri.setPage(pageInt);			// <--------- PageMaker에 Criteria 담아서 가지고 다닌다.
			
			PageMaker pm = new PageMaker();
			pm.setCri(cri);
			
			BoardDao bd = new BoardDao();
			// 페이징 처리하기 위한 전체 데이터 갯수 가져오기
			int boardCnt = bd.boardTatalCount();
			// System.out.println("게시물수는? " + boardCnt);
			
			pm.setTotalCount(boardCnt);			// <--------- PageMaker에 전체게시물수를 담아서 페이지계산
			
			ArrayList<BoardVo> alist = bd.boardSelectAll(cri);
			
			request.setAttribute("alist", alist);	// 화면까지 가지고 가기위해 request객체에 담는다.
			request.setAttribute("pm",pm);			// forward방식으로 넘기기 때문에 공유가 가능하다.
			
			// System.out.println(alist);
			
			paramMethod = "F";
			url = "/board/boardList.jsp";	// 실제 내부 경로

		} else if (location.equals("boardWrite.aws")) {	// 가상 경로
			// System.out.println("boardWrite.aws");
			
			paramMethod = "F";	// 포워드 방식은 내부에서 내부에서 공유하는 것이기때문에 내부에서 활동하고 이동한다.
			url = "/board/boardWrite.jsp";	// 실제 내부 경로

		} else if (location.equals("boardWriteAction.aws")) {	// 가상 경로
			//System.out.println("boardWriteAction.aws");
			
			// 1. 파라미터 값을 넘겨받는다.
			String subject = request.getParameter("subject");
			String contents = request.getParameter("contents");
			String writer = request.getParameter("writer");
			String password = request.getParameter("password");
			
			HttpSession session = request.getSession();	// 세션 객체를 불러와서
			int midx = Integer.parseInt(session.getAttribute("midx").toString());	// 로그인할때 담았던 세션변수 midx값을 꺼낸다.
			
			BoardVo bv = new BoardVo();
			bv.setSubject(subject);
			bv.setContents(contents);
			bv.setWriter(writer);
			bv.setPassword(password);
			bv.setMidx(midx);
			
			// 2. DB처리한다.
			BoardDao bd = new BoardDao();
			
			int value = bd.boardInsert(bv);
			
			if(value == 2) {	// 입력성공
				
				paramMethod = "S";
				url = request.getContextPath() + "/board/boardList.aws";	
				
				
			} else {	// 입력실패
				paramMethod = "S";
				url = request.getContextPath() + "/board/boardWrite.aws";	
			}
			
			// 3. 처리 후 이동한다. sendRedirect
			
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
