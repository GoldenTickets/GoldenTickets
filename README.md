# 🗒️ GoldenTicket
프로젝트 기간 : 2023.09.24 ~ 2023.11.30

# 🧗 프로젝트 소개
- 각종 영화 정보를 확인하고 리뷰를 달 수 있습니다.<br>
- 영화에 매겨진 평점을 통해 영화의 순위를 확인할 수 있습니다.<br>
- 마음에 드는 영화를 북마크하여 저장해 둘 수 있습니다.<br>
- 커뮤니티를 통해 소통이 가능합니다.

# 😼 팀원 소개
|건하|명준|
|------|---|
|![gh](https://github.com/GoldenTickets/GoldenTickets/assets/133292703/2adad278-4738-4e0f-9a40-335e44d395da)|![mj](https://github.com/GoldenTickets/GoldenTickets/assets/133292703/94e74c3b-5a07-4c38-a53a-bf360e67e316)|
# 🛠️ 개발환경
- Server
  
`Elastic Beanstalk`
- DB
  
`MySQL 8.0.34`
- SCM
  
`Git(GitHub)`
- BackEnd(Language & Framework & Library)
  
Language : `Java 8` `JDK 1.8.0`

Framework : `Spring Boot 2.7.17`

IDE : `STS-4.20.1.RELEASE`

Build Tools : `Maven 4.0.0`

ORM : `Mybatis 2.3.1`

Library : `Springdoc 1.6.14` 
  
# 🗂 디렉토리 구조
```
📦src/
 ┣ 📂apis
 ┣ 📂assets
 ┃ ┣ 📂images
 ┃ ┗ 📂styles
 ┣ 📂components
 ┃ ┗ 📂common
 ┣ 📂hooks
 ┣ 📂pages
 ┃ ┣ 📜ErrorPage.tsx
 ┃ ┣ 📜FindProjectPage.tsx
 ┃ ┣ 📜LoadingPage.tsx
 ┃ ┣ 📜MainPage.tsx
 ┃ ┣ 📜MyPage.tsx
 ┃ ┣ 📜ProjectDetailPage.tsx
 ┃ ┣ 📜ProjectEditPage.tsx
 ┃ ┣ 📜ProjectWritePage.tsx
 ┃ ┣ 📜PublicProfilePage.tsx
 ┃ ┗ 📜Root.tsx
 ┣ 📂recoil
 ┣ 📂routes
 ┣ 📂types
 ┣ 📂utils
 ┣ 📜App.tsx
 ┣ 📜index.tsx
 ┗ 📜react-app-env.d.ts
```
# ⭐️ 주요 기능
### ✔️ 메인
### ✔️ 장르별 보기
### ✔️ 북마크
### ✔️ 리뷰 작성


# 구현 기능
#### 로그인/회원가입
- 회원가입시 선호하는 장르 설정
- 로그인/회원가입 유효성 검사
#### 헤더
- 측면에서 나타나는 오프캔버스 기능으로 로그인, 마이페이지 이동 가능
#### 메인
- 신작 영화 테이블에서 저장된 4개의 신작 영화를 랜덤으로 사진을 하나 골라 평점, 장르, 국가와 함께 보여줌
- 오늘의 영화 테이블에 저장된 5개의 영화를 보여줌
- 세션에 저장된 로그인 정보를 확인 후 로그인 됐을 경우 선호하는 장르에 맞는 영화를 10개 보여주고 아닐 경우 조회수가 높은 순으로 10개의 영화를 보여줌
- 평점 순으로 10개의 영화를 보여줌
- 영화 장르 버튼을 누르면 그 장르의 영화를 볼 수 있는 페이지로 이동
#### 게시판 (라운지)
- 카테고리 별로 게시물 확인 가능
- 페이지 당 게시물 갯수 조정 가능
- 제목, 닉네임, 제목 + 닉네임으로 검색 가능
- 게시물 생성, 수정, 삭제, 댓글 작성, 삭제 유효성 검사
#### 영화 정보
- 장르별로 보기 가능
- 개봉순, 조회수 순으로 정렬 가능
#### 영화 랭킹
- 장르별 랭킹 보기 가능
- 우측의 버튼을 통해 북마크 추가, 삭제 가능
#### 마이페이지
- 회원 정보 수정
- 
