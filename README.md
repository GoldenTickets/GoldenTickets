# 🗒️ GoldenTicket
![logo](https://github.com/GoldenTickets/GoldenTickets/assets/133292703/675b64a7-6990-4730-a9d1-cb07556876e3)

프로젝트 기간 : 2023.09.24 ~ 2023.11.30
<br/>
<br/>

# 🧗 프로젝트 소개
- 각종 영화 정보를 확인하고 리뷰를 남길 수 있습니다.<br>
- 영화에 매겨진 평점을 통해 영화의 순위를 확인할 수 있습니다.<br>
- 마음에 드는 영화를 북마크하여 저장해 둘 수 있습니다.<br>
- 커뮤니티를 통해 소통이 가능합니다.
<br/>
<br/>

# 😼 팀원 소개

|건하|명준|
|------|---|
|![gh](https://github.com/GoldenTickets/GoldenTickets/assets/133292703/2adad278-4738-4e0f-9a40-335e44d395da)|![mj](https://github.com/GoldenTickets/GoldenTickets/assets/133292703/94e74c3b-5a07-4c38-a53a-bf360e67e316)|
<br/>
<br/>

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

Library : `springdoc-openapi 1.6.14` 
<br/>
<br/>

# 📱 ERD
![image](https://github.com/GoldenTickets/GoldenTickets/assets/133292703/2bff9855-ad87-4efd-834b-8a3409588c4a)
<br/>
<br/>

# 💡 REST API 명세
![image](https://github.com/GoldenTickets/GoldenTickets/assets/133292703/72c89792-abf7-4889-882b-db53d06c2fd9)
<br/>
<br/>

# 🗂 디렉토리 구조
```
📦src/main
 ┣ 📂java/com/goldenticket
 ┃ ┣ 📂config
 ┃ ┣ 📂controller
 ┃ ┣ 📂DTO
 ┃ ┣ 📂mapper
 ┃ ┗ 📂service
 ┣ 📂resources
 ┃ ┣ 📂static
 ┃ ┃ ┣ 📂css
 ┃ ┃ ┣ 📂images
 ┃ ┃ ┗ 📂js
 ┃ ┣ 📂templates
 ┃ ┃ ┣ 📂fragments
 ┃ ┃ ┗ 📂layout
```
<br/>
<br/>

# ⭐️ 주요 기능
### ✔️ 메인
![Honeycam_2023-12-01_01-08-10](https://github.com/GoldenTickets/GoldenTickets/assets/133292703/4a72d4b6-60aa-4efd-833c-38fa372238d3)

### ✔️ 장르별 보기
![Honeycam_2023-12-01_01-10-07](https://github.com/GoldenTickets/GoldenTickets/assets/133292703/92c5a16e-8fad-4a17-9cba-f9fb610745dc)
![Honeycam_2023-12-01_01-12-40](https://github.com/GoldenTickets/GoldenTickets/assets/133292703/de291a2f-7e06-45df-8191-f3f364bff9b5)

### ✔️ 북마크
![Honeycam_2023-12-01_01-16-16](https://github.com/GoldenTickets/GoldenTickets/assets/133292703/28ad2846-659e-4cda-8fb8-dcd0ffc7589e)

### ✔️ 리뷰 작성
![Honeycam_2023-12-01_01-19-13](https://github.com/GoldenTickets/GoldenTickets/assets/133292703/316dbbfa-acab-4e58-aae9-91d5d0683bdb)
<br/>
<br/>


# 🖥️ 구현 기능
#### 로그인/회원가입
- 회원가입시 선호하는 장르 설정
- 로그인/회원가입 유효성 검사
#### 헤더
- 측면에서 나타나는 오프캔버스를 통해 로그인, 마이페이지 이동
#### 메인
- 신작 영화 테이블에서 저장된 4개의 신작 영화를 랜덤으로 사진을 하나 골라 평점, 장르, 국가와 함께 조회
- 오늘의 영화 테이블에 저장된 5개의 영화를 조회
- 세션에 저장된 로그인 정보를 확인 후 로그인 됐을 경우 선호하는 장르에 맞는 영화를 10개 조회하고 아닐 경우 조회수가 높은 순으로 10개의 영화를 조회
- 평점 순으로 10개의 영화를 조회
- 영화 장르 버튼을 누르면 그 장르의 영화를 볼 수 있는 페이지로 이동
#### 게시판 (라운지)
- 카테고리 별로 게시물 조회
- 페이지 당 게시물 조회 갯수 조정 기능
- 제목, 닉네임, 제목 + 닉네임으로 검색 기능
- 게시물 생성, 수정, 삭제, 댓글 작성, 삭제 유효성 검사
#### 영화 정보
- 장르별 영화 리스트 조회
- 개봉순, 조회수 순으로 리스트 조회
- 특정 영화 조회시 북마크 추가, 삭제 기능
- 영화 시놉시스, 개봉일, 평점, 장르, 배우 등의 영화 정보 확인 기능
- 리뷰 조회 및 작성, 삭제 기능
- 영화 평점은 각 리뷰에 매겨진 평점들의 평균
- 리뷰 작성, 삭제, 북마크 추가, 삭제 유효성 검사
#### 영화 랭킹
- 장르별 랭킹 조회 기능
- 우측의 버튼을 통해 북마크 추가, 삭제
#### 마이페이지
- 회원 정보 수정
- 북마크 조회
- 작성 리뷰 확인 및 삭제
- 작성 게시물 확인 및 수정, 삭제
- 작성 댓글 확인 및 삭제
- 언급한 기능들 유효성 검사
<br/>
