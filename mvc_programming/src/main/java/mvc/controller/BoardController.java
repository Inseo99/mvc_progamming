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
			
			// 3. 처리 후 이동한다. sendRedirect			
			if(value == 2) {	// 입력성공				
				paramMethod = "S";
				url = request.getContextPath() + "/board/boardList.aws";					
			} else {	// 입력실패
				paramMethod = "S";
				url = request.getContextPath() + "/board/boardWrite.aws";	
			}			
		} else if (location.equals("boardContents.aws")) {
			// System.out.println("boardContents.aws");
			// 1. 넘어온 값 받기
			String bidx = request.getParameter("bidx");
			// System.out.println("bidx:" + bidx);
			int bidxInt = Integer.parseInt(bidx);
			
			// 2. 처리하기
			BoardDao bd = new BoardDao();
			BoardVo bv = bd.boardSelectOne(bidxInt);
			
			request.setAttribute("bv", bv);	// 포워드 방식이라 같은 영역안에 있어서 공유해서 jsp페이지에서 꺼내쓸 수 있다.
			
			// 3. 이동해서 화면 보여주기
			paramMethod = "F";
			url = "/board/boardContents.jsp";			
		} else if (location.equals("boardModify.aws")) {
			// System.out.println("boardModify.aws");
			
			String bidx = request.getParameter("bidx");
			BoardDao bd = new BoardDao();
			BoardVo bv = bd.boardSelectOne(Integer.parseInt(bidx));
			request.setAttribute("bv", bv);
			
			paramMethod = "F";
			url = "/board/boardModify.jsp";
		
		} else if (location.equals("boardModifyAction.aws")) {
			//System.out.println("boardModifyAction.aws");
			
			String subject = request.getParameter("subject");
			String contents = request.getParameter("contents");
			String writer = request.getParameter("writer");
			String password = request.getParameter("password");
			String bidx = request.getParameter("bidx");

			BoardDao bd = new BoardDao();
			int bidxInt = Integer.parseInt(bidx);
			BoardVo bv = bd.boardSelectOne(bidxInt);
			
			// 비밀번호 체크
			if (password.equals(bv.getPassword())) {	// 비밀번호 같으면
				
				BoardDao bd2 = new BoardDao();
				BoardVo bvnew = new BoardVo();

				bvnew.setSubject(subject);
				bvnew.setContents(contents);
				bvnew.setWriter(writer);
				bvnew.setPassword(password);
				bvnew.setBidx(bidxInt);
				
				int value = bd2.boardUpdate(bvnew); 
				
				if(value == 1) {	// 입력성공				
					paramMethod = "S";
					url = request.getContextPath() + "/board/boardContents.aws?bidx=" + bidx;					
				} else {	// 입력실패
					paramMethod = "S";
					url = request.getContextPath() + "/board/boardModify.aws?bidx=" + bidx;	
				}
			} else {	// 비밀번호 다르면				
				paramMethod = "S";
				url = request.getContextPath() + "/board/boardModify.aws?bidx=" + bidx;
			}

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
