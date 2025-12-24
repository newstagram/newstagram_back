# Newstagram Backend

# 📋 프로젝트 개요

### 1-1. 프로젝트 소개

---

Newstagram은 사용자의 활동 로그를 분석하여 RSS 뉴스 피드를 선별 제공하고, 개인화된 기사를 추천하는 지능형 뉴스 서비스이다.

### 1-2. 주요 목적

---

- 여러 언론사의 뉴스를 한곳에서 손쉽게 통합 조회
- 최신 핵심 현안(실시간, 주간)을 빠르게 파악
- 사용자의 구독 성향을 분석한 맞춤형 콘텐츠 제공
- 사용자가 직접 정의하는 고유의 주제별 뉴스 분류 기능 제공

## 2. 주요 기능

### 2-1. 핫 이슈 뉴스 피드

---

여러 언론사에서 쏟아지는 기사 중 **현재 가장 많이 다뤄지고 있는 이슈의 기사**를 한눈에 확인할 수 있다.

**카테고리(정치·경제·사회·스포츠 등)별**로 어떤 이슈가 뜨겁게 다뤄지고 있는지 보여주며, **실시간 · 일간 · 주간** 단위로 트렌드를 선택해서 확인할 수 있다.

### 2-2. 개인 맞춤 뉴스 피드

---

사용자는 별도의 설정 없이 **자신의 읽기 패턴에 기반한 개인 맞춤 뉴스**를 자동으로 받아볼 수 있다.

내가 어떤 기사를 자주 읽는지, 어떤 분야에 관심이 많은지를 **AI가 분석**해 나에게 알맞은 뉴스를 추천한다.

### 2-3. 문장으로 만드는 나만의 뉴스 피드

---

사용자는 원하는 뉴스의 스타일이나 주제를 **문장으로 자연스럽게 입력**하여 나에게 맞는 뉴스를 추천받을 수 있다.

예) “요즘 KBO에서 기아 타이거즈 관련한 소식이 궁금해.”

→ 해당 의도에 맞는 최신 스포츠·야구·기아팀 관련 기사 추천

예) “최근 인공지능 산업 동향 알려줘.”

→ 기술·산업·AI 관련 트렌드 기사 제공

## 3. 기능 요구사항

| 요구사항명                          | 요구사항 상세                                                   | 우선순 |
| ----------------------------------- | --------------------------------------------------------------- | ------ |
| 기사 수집                           | 일정 시간을 주기로 RSS를 통해 언론사별 기사를 수집              | 상     |
| 유사 내용 기사 그룹핑               | 유사한 내용의 기사들은 하나의 이슈로 그룹핑                     | 상     |
| 각 이슈 그룹의 대표 기사 선정       | 화면에 표시할 대표 기사 선정                                    | 중     |
| 카테고리별 기사 분류                | 카테고리별로 기사 필터링                                        | 중     |
| 실시간/일간/주간 이슈 기사 구분     | 기사 작성일을 기준으로 관리                                     | 중     |
| 사용자 기사 클릭 로그 저장          | 사용자가 기사를 클릭할 때마다 로그로 저장                       | 상     |
| 사용자 관심사와 유사한 기사 추천    | 클릭한 기사 기반으로 사용자의 관심사를 분석 후 유사한 기사 추천 | 상     |
| 사용자 입력 문장과 유사한 기사 추천 | 사용자가 입력한 자연어 문장과 유사한 기사 추천                  | 중     |
| 회원관리                            | 회원가입, 수정, 조회, 탈퇴                                      | 상     |
| 로그인 관리                         | 로그인, 로그아웃, 비밀번호 찾기                                 | 상     |
| 프로필 정보 수정                    | 기사 조회 기록 삭제                                             | 하     |

## 4. 기대효과

- **정보 탐색 시간의 획기적 단축**
  - 분산된 언론사 사이트를 일일이 방문하는 대신, 한곳에서 실시간으로 통합된 뉴스를 확인함으로써 정보 수집에 드는 시간과 노력을 최소화한다.
- **사용자 편의성 및 정보 접근성 극대화**
  - 명확하고 간결한 인터페이스(UI) 설계를 통해, 사용자가 원하는 정보에 즉각적으로 도달할 수 있는 편리한 이용 환경을 제공한다.
- **최신 트렌드 및 이슈 신속 파악**
  - 시의성 높은 실시간/주간 인기 기사를 전면에 배치하여, 사용자가 사회의 주요 동향과 핵심 이슈를 놓치지 않도록 돕는다.
- **사용자 주도형 정보 큐레이션**
  - 자연어 프롬프트 기능을 활용해 자신만의 관심 주제를 설정함으로써, 수동적으로 정보를 소비하던 방식에서 벗어나 능동적으로 정보를 관리할 수 있게 된다.
- **언론사별 시각 비교 분석 용이**
  - 동일한 사건을 다루는 여러 언론사의 보도를 한 화면에서 비교할 수 있어, 사안에 대한 다각적이고 균형 잡힌 시각을 갖추는 데 도움이 된다.

---

## 5. 화면 설계

![화면 페이지1](docs/images/Page1.png)

![화면 페이지2](docs/images/Page2.png)
---


<br><br><br>

# 📋 프로젝트 시스템

## 시스템 아키텍처

### 전체 시스템 아키텍처

![아키텍처1](docs/images/FullSystemArchitecture.png)
### AWS 시스템 아키텍처

![아키텍처2](docs/images/AwsSystemArchitecture.png)

## Backend 전체 기술 스택 
### 공통 모듈 
- Java 17
- Spring Boot 3.5.8
- Gradle
- Lombok
- JUnit Platform
- Spring Boot Devtools
- Spring Boot Starter Web
- Spring Boot Starter Validation
- Spring Boot Starter Actuator (Health check)
- Spring Boot Starter Test

### DB
- PostgreSQL
- Spring Data JPA
- MyBatis
- Spring Data Redis
- Kafka (KRaft mode 포함)

### 인증 인가
- Spring Security
- JWT (io.jsonwebtoken)
- OAuth2 Client
- Spring Boot Starter Mail (메일)
- SMS (Solapi SDK)

### 문서화
- Swagger/OpenAPI (springdoc-openapi 문서)

### 라이브러리
- Komoran (한국어 형태소 분석기)

### 대용량 데이터 처리
- Spring Batch
- Quartz
- Rome/Rome-modules (RSS Feed Parser, 썸네일 추출)
- Jsoup (HTML Parsing)
- Smile-core (Java 머신러닝 라이브러리)

### 로컬 개발 환경 CI/CD
- Docker, docker-compose 


## 데이터베이스 설계 
### ERD 설계도
![ERD 설계도](docs/images/ERD.png)

## API  
### Swagger 사용
![스웨거 화면1](docs/images/Swagger1.png)
![스웨거 화면2](docs/images/Swagger2.png)
![스웨거 화면3](docs/images/Swagger3.png)
![스웨거 화면4](docs/images/Swagger4.png)
![스웨거 화면5](docs/images/Swagger5.png)
![스웨거 화면6](docs/images/Swagger6.png)

## 시스템 흐름도

### Use Case
---
![유스 케이스](docs/images/UseCase.png)


### 시퀸스 다이어그램
---
#### 소셜 로그인

![시퀀스 다이어그램](docs/images/SocialLogin.png)

#### 일반 로그인

![시퀀스 다이어그램](docs/images/GeneralLogin.png)

#### 문자 인증

![시퀀스 다이어그램](docs/images/LetterAuthentication.png)

#### 이메일 인증

![시퀀스 다이어그램](docs/images/EmailAuthentication.png)

#### 공용 서비스 (콜드 스타트 및 기사 클릭 로깅에서 사용)

![시퀀스 다이어그램](docs/images/LoggingPublicService.png)

#### 콜드 스타트

![시퀀스 다이어그램](docs/images/ColdStart.png)

#### 기사 클릭 로깅

![시퀀스 다이어그램](docs/images/ClickArticleLogging.png)

#### 이슈 기사 조회

![시퀀스 다이어그램](docs/images/IssueArticleInquiry.png)

#### 프롬프트 검색

![시퀀스 다이어그램](docs/images/SearchPrompt.png)

#### RSS기사수집

![시퀀스 다이어그램](docs/images/RssArticleCollection.png)

#### 클러스터링

![시퀀스 다이어그램](docs/images/Clustering.png)

---

## 📁 폴더 구조 예시

```
newstagram_back/
├── api-server/           # 메인 API 서버
│   ├── src/
│   │   └── main/
│   │       ├── java/com/ssafy/newstagram/api/...
│   │       └── resources/
│   │           ├── application.properties
│   │           ├── application-dev.properties
│   │           └── application-prod.properties
│   ├── build.gradle
│   └── Dockerfile
├── logging-server/       # 로깅 서버
│   ├── src/
│   │   └── main/
│   │       ├── java/com/ssafy/newstagram/logging/...
│   │       └── resources/
│   │           ├── application.properties
│   │           ├── application-dev.properties
│   │           └── application-prod.properties
│   ├── build.gradle
│   └── Dockerfile
├── rss-collector/        # RSS 수집 서버
│   ├── src/
│   │   └── main/
│   │       ├── java/com/ssafy/newstagram/rss/...
│   │       └── resources/
│   │           ├── application.properties
│   │           ├── application-dev.properties
│   │           └── application-prod.properties
│   ├── build.gradle
│   └── Dockerfile
├── newstagram-domain/    # 공통 도메인 모듈
│   ├── src/
│   │   └── main/
│   │       └── java/com/ssafy/newstagram/domain/...
│   └── build.gradle
├── docker-compose.yml
├── .env
├── README.md
├── TECH_STACK.md
├── sql/                  # DB 초기화 및 데이터 스크립트
│   ├── 00_init.sql
│   └── 01_AddData.sql
└── docs/                 # 문서 및 이미지
    └── images/
```
<br><br><br>

# Newstagram 백엔드: 개발 환경 설정 가이드

## 사전 요구사항

- **Docker & Docker Compose**: 데이터베이스와 Redis 등 외부 서비스를 컨테이너 환경에서 실행하기 위해 필요
- **JDK 17 이상**: 프로젝트의 컴파일 및 실행을 위한 Java Development Kit
- **Git**: 소스 코드를 관리하고 저장소에서 클론하기 위해 필요

---

## Git 저장소 클론

```bash
# CLI 
git clone <repository-url>
cd newstagram_back
```

## 의존성 빌드

```bash
# CLI 
./gradlew clean build 
```

## 환경 변수 설정

### **일반적인 콘솔 환경**

1. 프로젝트의 루트 디렉터리에 .env 파일을 생성
2. 아래 템플릿을 복사하여 파일에 붙여넣고, 필요시 값을 수정

```java
## PostgreSQL Configuration
POSTGRES_DB=newstagram
POSTGRES_USER=newstagram
POSTGRES_PASSWORD=ssafy
POSTGRES_PORT=5432
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/newstagram?currentSchema=public

# Redis (필요 시 추가)
REDIS_PASSWORD=
REDIS_PORT=6379

# Kafka Configuration
KAFKA_PORT=9094

# JWT Secret (보안을 위해 고유한 값으로 변경 권장)
JWT_SECRET=

# GMS AI API 키 
GMS_BASE_URL=
GMS_SECRET=
GMS_LLM_BASE_URL=

# Google OAuth
GOOGLE_CLIENT_ID=
GOOGLE_CLIENT_SECRET=
GOOGLE_REDIRECT_URI=(http://localhost:8080/api/login/oauth2/code/google)
#GOOGLE_REDIRECT_URI=https://localhost:8080/api/login/oauth2/code/google

# Gmail
GMAIL_USERNAME=dabinchi9969@gmail.com
GMAIL_APP_PASSWORD=fbnsevwojrhixiuy

BASE_URL=http://localhost:8080

FRONT_URL=http://localhost:5173

# SOLAPI
SOLAPI_KEY=
SOLAPI_SECRET=
SOLAPI_FROM_NUMBER=01033295596
```

### **IntelliJ IDEA 환경**

**플러그인(EnvFile**) **설치 방법**:

- File > Settings > Plugins로 이동하여 EnvFile을 검색 후 설치하고 IntelliJ를 재시작합니다.

**실행 구성(Run Configuration) 설정**:

- Run > Edit Configurations를 엽니다.
- ApiServerApplication을 선택한 후, EnvFile 탭을 클릭합니다.
- Enable EnvFile을 체크하고 + 버튼을 눌러 프로젝트 루트의 .env 파일을 추가합니다.

**실행 구성(Run Configuration) 설정** :

- Run > Edit Configurations에서 ApiServerApplication을 선택합니다.

**환경 변수 입력**:

- Environment variables 필드에 아래 값을 복사하여 붙여넣습니다.
    
    ```java
    POSTGRES_DB=newstagram;POSTGRES_USER=newstagram;POSTGRES_PASSWORD=ssafy;POSTGRES_PORT=5432;SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/newstagram;REDIS_PASSWORD=;REDIS_PORT=6379;JWT_SECRET=KJWoxj9BDq5NruIHfdFmon5L6vUYvpkHNrlIk1hqQzF
    ```
    

### Docker Compose 실행

```java
docker-compose up -d
```

이 명령어를 통해 실행되는 서비스는 다음과 같음

- **PgVector(Postgre)** : 5432 포트
- **Redis 7**: 6379 포트
- **Kafka** : 9092 ~ 9094 포트

### 데이터베이스 초기화 확인

Docker Compose가 처음 실행될 때, sql/test-connection.sql 스크립트가 자동으로 실행되어 아래 항목들이 생성

- 기본 데이터베이스 테이블
- 샘플 카테고리 데이터
- 연결 테스트용 데이터

### 애플리케이션 실행

```java
CLI 사용

# API 서버
./gradlew api-server:bootRun

# 로깅 서버
./gradlew logging-server:bootRun

# RSS 수집기
./gradlew rss-collector:bootRun

다른 STS 및 인텔리제이 사용
```

---

## 주요 접속 정보

### 데이터베이스 (Postgre : PgVector)

- **Host**: localhost
- **Port**: 5432
- **Database**: newstagram
- **Username**: newstagram
- **Password**: ssafy (또는 .env 파일에 설정한 값)

### Redis

- **Host**: localhost
- **Port**: 6379
- **Password**: 설정되지 않음

### Kafka

- **Host** : localhost
- **Port** : 9092, 9093, 9094
- **Password** : 설정되지 않음

### 애플리케이션

- **API Server**: http://localhost:8080/api
- **API Swagger**: http://localhost:8080/api/swagger-ui.html
- **API Server health check** : http://localhost:8080/api/actuator/health
- **Logging Server**: http://localhost:8081
- **Logging Server health check** : http://localhost:8081/actuator/health
- **RSS Collector**: http://localhost:8082
- **RSS Collector health check** : http://localhost:8082/actuator/health

---

## 개발 팁 및 도구

### Docker 컨테이너 관리

- **상태 확인**: `docker-compose ps`
- **로그 실시간 확인**: `docker-compose logs -f mysql` 또는 `docker-compose logs -f redis`
- **서비스 중지**: `docker-compose down`
- **서비스 완전 삭제 (데이터 포함)**: `docker-compose down -v`
- **서비스 재시작**: `docker-compose restart`

### 데이터베이스 초기화

데이터베이스를 초기 상태로 되돌리려면 아래 명령어를 순서대로 실행/*

```java
# 1. 컨테이너와 볼륨(데이터)을 모두 삭제
docker-compose down -v
f
# 2. 컨테이너를 다시 생성하고 실행
docker-compose up -d
```

---

## 트러블슈팅

### 포트 충돌 문제

**문제: Postgre 포트 3306이 이미 사용 중입니다.**

- **해결책**: .env 파일에서 호스트 포트를 변경
    
    ```java
    POSTGRES_PORT=5433 // 호스트 포트를 5433로 변경
    SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5433/newstagram
    ```
    

**문제: Redis 포트 6379가 이미 사용 중입니다.**

- **해결책**: .env 파일에서 호스트 포트를 변경
    
    ```java
    REDIS_PORT = 6380 // 호스트 포트를 6380으로 변경
    ```