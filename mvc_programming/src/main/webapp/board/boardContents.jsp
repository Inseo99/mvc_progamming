<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글내용</title>
<link href="../css/style2.css" rel="stylesheet">
<script> 

function check() {
	  
	  // 유효성 검사하기
	  let fm = document.frm;
	  
	  if (fm.content.value == "") {
		  alert("내용을 입력해주세요");
		  fm.content.focus();
		  return;
	  }
	  
	  let ans = confirm("저장하시겠습니까?");
	  
	  if (ans == true) {
		  fm.action="./detail.html";
		  fm.method="post";
		  fm.submit();
	  }	  
	  
	  return;
}

</script>
</head>
<body>
<header>
	<h2 class="mainTitle">글내용</h2>
</header>

<article class="detailContents">
	<h2 class="contentTitle">장애학생들을 위한 특별한 피아노 (조회수:7)</h2>
	<p class="write">관리자 (2024-10-12)</p>
	<div class="content">
		link: http://www.naver.com
		<br>
		네이버 홈페이지 입니다.
	</div>
	<a href="#" class="fileDown"><img src="https://cdn.pixabay.com/photo/2024/03/12/09/28/ai-generated-8628373_1280.png" width="30px" height="20px">첨부파일입니다.</a>
</article>
	
<div class="btnBox">
	<a class="btn aBtn" href="./update.html">수정</a>
	<a class="btn aBtn" href="./delete.html">삭제</a>
	<a class="btn aBtn" href="./comment.html">답변</a>
	<a class="btn aBtn" href="./list.html">목록</a>
</div>

<article class="commentContents">
	<form name="frm">
		<p class="commentWriter">admin</p>	
		<input type="text" name="content">
		<button type="button" class="replyBtn" onclick="check();">댓글쓰기</button>
	</form>
	
	
	<table class="replyTable">
		<tr>
			<th>번호</th>
			<th>작성자</th>
			<th>내용</th>
			<th>날짜</th>
			<th>DEL</th>
		</tr>
		<tr>
			<td>1</td>
			<td>홍길동</td>
			<td class="content">댓글입니다</td>
			<td>2024-10-18</td>
			<td>sss</td>
		</tr>
	</table>
</article>

</body>
</html>