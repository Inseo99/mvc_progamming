package mvc.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mvc.dao.MemberDao;
import mvc.vo.MemberVo;

import java.io.IOException;

@WebServlet("/MemberController")
public class MemberController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// response.getWriter().append("Served at: ").append(request.getContextPath());
		// 넘어온 모든 값은 여기에서 처리해서 분기한다. - controller 역할
		
		// System.out.println("값이 넘어오나요?");
		
		// 전체주소를 추출
		String uri = request.getRequestURI();
		// System.out.println("url: " + url);	// /mvc_programming/member/memberJoinAction.aws
		
		String[] location = uri.split("/");
		
		if (location[2].equals("memberJoinAction.aws")) {	// 4번째방의 값이 memberJoinAction.aws이면 
			
			String memberId = request.getParameter("memberid");
			String memberPwd = request.getParameter("memberpwd");
			String memberPwd2 = request.getParameter("memberpwd2");
			String memberName = request.getParameter("membername");;
			String memberEmail = request.getParameter("memberemail");
			String memberPhone = request.getParameter("memberphone");
			String memberAddr = request.getParameter("memberaddr");
			String memberGender = request.getParameter("membergender");
			String memberBirth = request.getParameter("memberbirth");
			String[] memberHobby = request.getParameterValues("memberhobby");
			String memberInHobby="";
			for(int i=0;i<memberHobby.length;i++){
				memberInHobby = memberInHobby + memberHobby[i] + ",";
					
			}

			MemberDao md = new MemberDao();
			int value = md.memberInsert(
					memberId, 
					memberPwd, 
					memberPwd2, 
					memberGender, 
					memberBirth, 
					memberAddr, 
					memberPhone, 
					memberEmail, 
					memberInHobby);

			String pageUrl = "";
			String msg = "";
			
			HttpSession session = request.getSession();	// 세션객체 활용
			
			if(value == 1) {							// index.jsp파일은 web.xml웹설정파일에 기본등록되어있음 - 생략가능
				msg = "회원가입되었습니다.";
				
				session.setAttribute("msg", msg);
				
				pageUrl = request.getContextPath() + "/";	//request.getContextPath() : 프로젝트 이름
				response.sendRedirect(pageUrl);		// 전송방식 sendRedirect는 요청받으면 다시 그쪽으로 가라고 지시하는 방법
			} else {
				msg = "회원가입 오류발생하였습니다.";
				
				session.setAttribute("msg", msg);
				pageUrl = request.getContextPath() + "/member/memberJointeacher.jsp";
				response.sendRedirect(pageUrl);
			}
			
		} else if (location[2].equals("memberJointeacher.aws")) {

			String uri2 = "/member/memberJointeacher.jsp";
			RequestDispatcher rd = request.getRequestDispatcher(uri2);
			rd.forward(request, response);	// 포워드방식
		} else if (location[2].equals("memberLogin.aws")) {

			String uri2 = "/member/memberLogin.jsp";
			RequestDispatcher rd = request.getRequestDispatcher(uri2);
			rd.forward(request, response);	// 포워드방식
		}
		
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
