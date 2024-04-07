# matdori
스프링 부트 _ 맛집 탐방 커뮤니티

## 🖥️ 프로젝트 소개
이 프로젝트는 Spring 기반의 맛집 탐방 커뮤니티 웹 애플리케이션입니다. <br>
사용자들은 맛집 정보를 공유하고 의견을 나눌 수 있는 플랫폼을 제공합니다.
<br>

## 🕰️ 개발 기간
* 팀편성 발표: 03/18(월)
* 팀빌딩 기간: 03/18(월) ~ 03/22(금)
* 팀확정 및 역활 확정: ~ 03/22(금)
* 프로젝트 일정: 03/25(월) ~ 04/08(월)
* 프로젝트 발표: 04/09(화)

### 🧑‍🤝‍🧑 멤버 구성
- 안태규 (팀장)
- 조창현 (팀원)
- 송재훈 (팀원)
- 하태민 (팀원) 

### ⚙️ 개발 환경
- **Java** : <img src = "https://img.shields.io/badge/Java 17-007396?&logo=java&logoColor=white">
- **IDE** : <img src = "https://img.shields.io/badge/Intellij Idea-000000?&logo=intellijidea&logoColor=white">
- **Framework** : <img src = "https://img.shields.io/badge/Springboot 3.2.4-6DB33F?&logo=springboot&logoColor=white">
- **Database** : <img src = "https://img.shields.io/badge/H2-4169E1?&logo=H2&logoColor=white">, RDS MySQL
- **Server** : <img src = "https://img.shields.io/badge/Amazon EC2-FF9900?&logo=amazonec2&logoColor=white">
- **WS/WAS** : <img src = "https://img.shields.io/badge/Apachetomcat-F8DC75?&logo=apachetomcat&logoColor=white">
- **Meeting** : <img src = "https://img.shields.io/badge/Discord-5865F2?&logo=discord&logoColor=white">, <a href="https://www.notion.so/oreumi/13-ad824ce6537d40ea965e67bef0a8988e?pvs=4"><img src = "https://img.shields.io/badge/Notion (Link)-000000?&logo=Notion&logoColor=white"> </a>
- **Front-end** : <img src = "https://img.shields.io/badge/HTML-E34F26?&logo=html5&logoColor=white">, <img src = "https://img.shields.io/badge/CSS3-1572B6?&logo=css3&logoColor=white">, <img src = "https://img.shields.io/badge/Javascript-F7DF1E?&logo=javascript&logoColor=white">, <img src = "https://img.shields.io/badge/Bootstrap-7952B3?&logo=bootstrap&logoColor=white">, ThymeLeaf


### 🔨 프로젝트 구조
```
.
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── estsoft13
│   │   │           └── matdori
│   │   │               ├── config              # 애플리케이션 설정 클래스들
│   │   │               ├── controller          # 컨트롤러 클래스들
│   │   │               ├── domain              # 도메인 모델 클래스들
│   │   │               ├── dto                 # 데이터 전송 객체 클래스들
│   │   │               ├── repository          # 데이터베이스 액세스를 위한 레포지토리 클래스들
│   │   │               ├── service             # 비즈니스 로직을 처리하는 서비스 클래스들
│   │   │               ├── util                # 유틸리티 클래스들
│   │   │               └── MatdoriApplication.java    # 애플리케이션의 메인 클래스
│   │   └── resources
│   │       ├── static          # 정적 파일(css, js 등)
│   │       ├── templates       # Thymeleaf 템플릿 파일들
│   │       ├── application.properties  # 애플리케이션 설정 파일
│   │       └── data.sql
│   └── test
│       └── java
│           └── com
│               └── estsoft13
│                   └── matdori
│                       └── controller     # 컨트롤러 테스트 클래스들
│                       └── repository     # 레포지토리 테스트 클레스들
│                       └── MatdoriApplicationTests
│  
├── build           # 빌드된 파일들이 생성되는 디렉터리
├── build.gradle    # Gradle 프로젝트 설정 파일
└── README.md       # 프로젝트 README 파일
```

## 📌 주요 기능
### 공통
- 회원 접근 가능
  - redirection -> 로그인 페이지 (비회원)
### 리뷰 게시판
- 리뷰 작성
  - 회원 접근 가능
  - 작성 버튼
    - redirection : 작성 페이지
    - 작성 페이지
      - 제목, 날짜, 내 점수, 식당, 내용, 방문 시간, 예상 대기 시간
      - 등록 버튼
        - redirection -> 해당 글의 상세 페이지
        - dialog : '등록이 완료되었습니다.'
- 리뷰 조회
  - 모두 접근 가능 -> 비회원은 메인페이지만 접근 가능
  - 리뷰 배치
    - 최신순
    - 평점순 
    - 조회순 
  - 리뷰의 "View Review" 버튼
    - redirection - > 해당 글의 상세 페이지
- 리뷰 수정
  - 작성자만 접근 가능
  - 해당 글의 상세 페이지에 버튼 구현
  - 수정 버튼
    - redirection -> 해당 글의 수정 페이지
    - dialog : '수정이 완료되었습니다.'
- 리뷰 삭제
  - 작성자 접근 가능
  - 해당 글의 상세 페이지에 버튼 구현
  - 삭제 버튼
    - redirection -> 리뷰 게시판
    - dialog : '삭제가 완료되었습니다.'
- 리뷰 검색
  - 조회시 키워드에 관한 리뷰 검색
### 모임 게시판
- 모임 글 작성
    - 회원 접근 가능
    - 작성 버튼
      - redirection : 작성 페이지
      - 작성 페이지
        - 제목, 날짜, 내용, 방문 시간, 예상 대기 시간
      - 등록 버튼
          - redirection -> 해당 글의 상세 페이지
          - dialog : '등록이 완료되었습니다.'
- 모임 글 조회
    - 모두 접근 가능
    - 모임 글 배치 : 시간 순
    - 제목, 날짜, 닉네임 -> 추천수
    - 게시글 제목
        - redirection - > 해당 글의 상세 페이지
- 모임 글 수정
  - 회원 접근 가능
  - 해당 글의 상세 페이지에 버튼 구현
  - 수정 버튼
      - redirection -> 해당 글의 수정 페이지
      - dialog : '수정이 완료되었습니다.'
- 모임 글 삭제
  - 회원 및 관리자 접근 가능
  - 해당 글의 상세 페이지에 버튼 구현
  - 삭제 버튼
    - redirection -> 리뷰 게시판
    - dialog : '삭제가 완료되었습니다.'
- 모임 글 검색
  - 날짜, 장소, 대기 시간
### 댓글 / 답글
- 공통 : 리뷰, 모임 글 상세 페이지에 구현
- 댓글 작성
- 댓글 작성 / 조회
  - 해당 댓글 box에 버튼 구현
- 댓글 수정
  - 해당 댓글 box에 버튼 구현
- 댓글 삭제
  - 해당 댓글 box에 버튼 구현
### 회원 관리
- 공통
  - 리뷰 게시판, 모임 게시판에 login/signup 버튼 구현
  - login
    - redirection -> 로그인 페이지
  - signup
    - redirection -> 회원가입 페이지
  - logout
     - 페이지 왼쪽 하단에 logout 버튼
- 회원 가입
  - 유저 이름
  - 이메일
  - 비밀번호
- 로그인
- 로그아웃
- 비밀번호 찾기
- 비밀번호 변경, 탈퇴
