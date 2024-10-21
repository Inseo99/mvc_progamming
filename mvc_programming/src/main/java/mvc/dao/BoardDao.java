package mvc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import mvc.dbcon.Dbconn;
import mvc.vo.BoardVo;
import mvc.vo.Criteria;
import mvc.vo.MemberVo;

public class BoardDao {
	
	private Connection conn;	// 전역적으로 쓴다. 연결객체를...
	private PreparedStatement pstmt;	// 쿼리를 실행하기 위한 구문객체
	
	public BoardDao() {	// 생성자를 만드는 이유 : DB연결하는 Dbconn 객체 생성할려고...생성해야 mysql 접속하니까
		Dbconn db = new Dbconn();
		this.conn = db.getConnection();
	}
	
	public ArrayList<BoardVo> boardSelectAll(Criteria cri) {
		
		int page = cri.getPage();	// 페이지번호
		int perPageNum = cri.getPerPageNum();	// 화면 노출 리스트 갯수
		
		ArrayList<BoardVo> alist = new ArrayList<BoardVo>();	// ArrayList 컬렉션 객체에 BoardVo을 담겠다. BoardVo는 컬럼값을 담겠다.
		String sql = "SELECT * FROM board ORDER BY originbidx DESC, depth ASC LIMIT ?, ?";
		ResultSet rs = null;	// db값을 가져오기위한 전용클래스
		
		try {
			pstmt = conn.prepareStatement(sql);			
			pstmt.setInt(1, (page-1)*perPageNum);
			pstmt.setInt(2, perPageNum);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {	// 커서가 다음으로 이동해서 첫 글이 있느냐 물어보고 true면 진행
				int originbidx = rs.getInt("originbidx");
				String subject = rs.getString("subject");
				String writer = rs.getString("writer");
				int viewCnt = rs.getInt("viewcnt");
				String writeday = rs.getString("writeday");
				
				BoardVo bv = new BoardVo();	// 첫행부터 bv에 옮겨담기
				bv.setOriginbidx(originbidx);
				bv.setSubject(subject);
				bv.setWriter(writer);
				bv.setViewcnt(viewCnt);
				bv.setWriteday(writeday);
				
				alist.add(bv);		// ArrayList객체에 하나씩 추가한다.
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try{
				rs.close();
				pstmt.close();
				conn.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return alist;
	}
	
	// 게시판 전체 갯수 구하기
	public int boardTatalCount() {
		
		int value = 0;
		// 1. 쿼리 만들기
		String sql ="SELECT COUNT(*) AS cnt FROM board WHERE delyn = 'N'";
		// 2. conn 객체 안에 있는 구문 클래스 호출하기
		// 3. DB 컬럼값을 받는 전용 클래스 ResultSet 호출 (ResultSet 특징은 데이털를 그대로 복사하기때문에 전달이 빠름)
		ResultSet rs = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {	// 커서를 이동시켜서 첫줄로 옮긴다
				value = rs.getInt("cnt");	// 지역변수 value 담아서 리턴해서 가져간다
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try{	// 각 개체도 소멸시키고 DB연결을 끊는다.
				rs.close();
				pstmt.close();
				// conn.close();
			} catch(Exception e) {
				e.printStackTrace();
			}		
		}
		return value;
	}
	
	public int boardInsert(BoardVo bv) {
		
		int value = 0;
		
		String subject = bv.getSubject();
		String contents = bv.getContents();
		String writer = bv.getWriter();
		String password = bv.getPassword();
		int midx = bv.getMidx();
		
		String sql = "INSERT INTO board(originbidx, depth, level_, subject, contents, writer, password, midx) VALUE(null, 0, 0, ?, ?, ?, ?, ?)";
		String sql2 = "UPDATE board SET originbidx =(SELECT A.maxbidx FROM (SELECT max(bidx) AS maxbidx FROM board) A) WHERE bidx= (SELECT A.maxbidx FROM (SELECT max(bidx) AS maxbidx FROM board) A)";

		
		try {
			conn.setAutoCommit(false);	// 수동커밋으로 하겠다.
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, subject);
			pstmt.setString(2, contents);
			pstmt.setString(3, writer);
			pstmt.setString(4, password);
			pstmt.setInt(5, midx);
			
			int exec = pstmt.executeUpdate();	// 실행되면 1 안되면 0
			
			pstmt = conn.prepareStatement(sql2);
			int exec2 = pstmt.executeUpdate();	// 실행되면 1 안되면 0
			
			conn.commit();	// 일괄처리 커밋
			
			value = exec + exec2;
			
		} catch (SQLException e) {			
			try {
				conn.rollback();	// 실행중 오류발생시 롤백처리
			} catch (SQLException e1) {
				e1.printStackTrace();
			}			
			e.printStackTrace();
		}  finally {
			try{	// 각 개체도 소멸시키고 DB연결을 끊는다.
				// rs.close();
				pstmt.close();
				conn.close();
			} catch(Exception e) {
				e.printStackTrace();
			}		
		}
		return value;	// 성공하면 리턴값 2
	}
	
}



















