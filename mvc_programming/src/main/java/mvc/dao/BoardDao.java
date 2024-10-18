package mvc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import mvc.dbcon.Dbconn;
import mvc.vo.BoardVo;
import mvc.vo.MemberVo;

public class BoardDao {
	
	private Connection conn;	// 전역적으로 쓴다. 연결객체를...
	private PreparedStatement pstmt;
	
	public BoardDao() {	// 생성자를 만드는 이유 : DB연결하는 Dbconn 객체 생성할려고...생성해야 mysql 접속하니까
		Dbconn db = new Dbconn();
		this.conn = db.getConnection();
	}
	
	public ArrayList<BoardVo> boardSelectAll() {
		
		ArrayList<BoardVo> alist = new ArrayList<BoardVo>();	// ArrayList 컬렉션 객체에 BoardVo을 담겠다. BoardVo는 컬럼값을 담겠다.
		String sql = "SELECT * FROM board ORDER BY originbidx DESC, depth ASC";
		ResultSet rs = null;	// db값을 가져오기위한 전용클래스
		BoardVo bv = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery(sql);
			
			while(rs.next()) {	// 커서가 다음으로 이동해서 첫 글이 있느냐 물어보고 true면 진행
				int originbidx = rs.getInt("originbidx");
				String subject = rs.getString("subject");
				String writer = rs.getString("writer");
				int viewCnt = rs.getInt("viewcnt");
				String writeday = rs.getString("writeday");
				
				bv = new BoardVo();	// 첫행부터 mv에 옮겨담기
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
				
				pstmt.close();
				conn.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		
		
		return alist;
	}
	
}
