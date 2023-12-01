package com.goldenticket.service;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.goldenticket.DTO.Article;
import com.goldenticket.DTO.Member;
import com.goldenticket.DTO.Movie;
import com.goldenticket.DTO.Reply;
import com.goldenticket.DTO.Review;
import com.goldenticket.mapper.MemberMapper;

@Service
public class MemberService {
	
	@Autowired
	private MemberMapper memberMapper;
	
	//로그인 기능
	public int loginCheck(String email,String password) throws Exception {//보안적으로 문제가없는가?
		//DB에서 비밀번호가일치하는지 확인하기 vs DB에서 받아온값을 Service에서 비교해서 비밀번호가일치하는지확인
		String pwfromdb=memberMapper.logincheck(email);
		if(password.equals(pwfromdb)) {//DB에서 받아온 비밀번호가 사용자가 전송한 비밀번호와 일치한다면
			return 1;
		}else {//DB에서 받아온 비밀번호가 사용자가 전송한 비밀번호와 일치하지않다면
			return 0;
		}
	}
	
	
	//닉네임,회원번호 가져오기
	public Member getMember(String email) throws Exception {
		return memberMapper.getMember(email);
	}
	
	//회원가입하기 1
	public int createMember(Member member) throws Exception {
		return memberMapper.createMember(member);
	}
	//회원가입하기 2 (회원이 선호하는장르 )
	public int setMemberGenre(int genre_id,int id) throws Exception {
		return memberMapper.setMemberGenre(genre_id, id);
	}
	
	//회원정보 전부 가져오기
		public Member getMemberinfo(int id) throws Exception { //예외처리 나중에 하기
			return memberMapper.getMemberinfo(id);
		}
		
    //회원정보 수정페이지에서 회원이 선호하는 장르 목록 가져오기
		public List<Integer> getMemGenreList(int id) throws Exception {
			return memberMapper.getMemGenreList(id);
		}
	//회원정보 수정하기
		public int updateMember(Member member,List<Integer> member_genres) {
			try {
			int mem_id=member.getId();
			memberMapper.deleteMember(member.getId());//회원의 선호장르 레코드 모두 삭제
			for(int genre:member_genres) {//회원이 선택한 선호 장르 갯수와 똑같은 횟수로 선호장르 입력 시킴 
				memberMapper.setMemberGenre(genre,mem_id);
				System.out.println("genre=>"+genre+",mem_id=>"+mem_id);
			}
		     return memberMapper.updateMember(member);//위의 과정이 모두 성공할경우 1을 반환
			}catch(Exception e) {
				e.printStackTrace();
				System.out.println("service 실패");
				return 0;//위의 과정이 한개라도 실패할경우 0 반환
			}
		}
	
	//회원이 북마크한 영화 갯수 가져오기
	public int getTotalbookmark(int id) throws Exception{
		return memberMapper.getTotalbookmark(id);
	}
	
	//회원의 북마크 목록 가져오기
	public List<Movie> getBookmark(RowBounds rowBounds, int id) throws Exception{
		return memberMapper.getBookmark(rowBounds, id);
	};
	
	//회원이 작성한 리뷰 갯수 가져오기
	public int getMytotalreviews(int id) throws Exception{
		return memberMapper.getMytotalreplies(id);
	}
	
	//회원의 리뷰 목록 가져오기
	public List<Review> getMyreviews(RowBounds rowBounds, int id) throws Exception{
		return memberMapper.getMyreviews(rowBounds, id);
	}
	
	//회원이 작성한 게시글 갯수 가져오기
	public int getMytotalarticles(int id) throws Exception {
		return memberMapper.getMytotalarticles(id);
	}
	
	//회원의 게시글 목록 가져오기
	public List<Article> getMyarticles(RowBounds rowBounds, int id) throws Exception{
		return memberMapper.getMyarticles(rowBounds, id);
	}
	
	//회원이 작성한 댓글 갯수 가져오기
	public int getMytotalreplies(int id) throws Exception{
		return memberMapper.getMytotalreplies(id);
	}
	
	//회원의 댓글 목록 가져오기
	public List<Reply> getMyreplies(RowBounds rowBounds, int id) throws Exception{
		return memberMapper.getMyreplies(rowBounds, id);
	}
}
