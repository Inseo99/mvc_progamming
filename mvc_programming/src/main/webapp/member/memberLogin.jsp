<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
 <HEAD>
  <TITLE> 회원로그인 </TITLE>
  <style>
header {
	width:100%
	height:100px;
	text-align:center;
}
nav {
	width:15%;
	height:300px;
	float:left;
	--background-color:blue;
}
section { 
	width: 70%;	
	height: 400px;
	float: left; 
	--background: olivedrab; 
}
aside { 
	width: 15%; 
	height: 300px; 
	float: left; 
	--background: orange; 
		}
footer {
	width: 100%;
	height: 100px; 
	clear: both; 
	--background: plum; 
	text-align:center;
		}

</style>
 </HEAD>
 <BODY>
<header>로그인 페이지</header>
<nav></nav>
<section>
	 <article>
		<form name="frm" action=".test0920_result.html" method="post">
			<table border=1 style="width:500px">
				<tr>
					<td>아이디</td>
					<td><input type="text"name="memberId"maxlength="30"style="width:150px"value=""placeholder="아이디를 입력하세요"></td>
				</tr>
				<tr>
					<td>비밀번호</td>
					<td><input type="password"name="memberPwd"maxlength="30"style="width:150px"placeholder="비밀번호를 입력하세요"></td>
				</tr>
				<tr>
					<td colspan=2 style="text-align:center">
					<input type="button"name="btn"value="로그인">
					</td>
				</tr>
			</table>
		</form>
	</article>
</section>
<aside>
</aside>
<footer>
made by kis
</footer>
 </BODY>
</HTML>
