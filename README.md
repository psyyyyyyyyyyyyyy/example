# 📸 Media Board Backend

미디어 중심의 게시판 백엔드 API 서버입니다. Spring Boot와 H2 데이터베이스를 사용하여 구축되었습니다.

## 🚀 주요 기능

- **게시글 관리**: CRUD 기능 (생성, 조회, 수정, 삭제)
- **미디어 업로드**: 이미지/동영상 파일 업로드 (최대 50MB)
- **댓글 시스템**: 게시글별 댓글 작성/관리
- **좋아요 기능**: IP 기반 좋아요/취소 (중복 방지)
- **카테고리 필터링**: 풍경, 음식, 인물, 여행, 일상
- **미디어 타입 필터링**: 사진/동영상 분류
- **검색 기능**: 제목 기반 검색
- **조회수 추적**: 게시글별 조회 통계
- **API 문서화**: Swagger UI 제공

## 🔧 기술 스택

- **Java**: 17+
- **Spring Boot**: 3.2.0
- **Spring Data JPA**: 데이터베이스 ORM
- **H2 Database**: 인메모리 데이터베이스
- **Swagger**: API 문서화 (springdoc-openapi)
- **Maven**: 빌드 도구

## 📁 파일 저장 방식

> **중요**: 실제 이미지/동영상 파일은 H2 데이터베이스에 저장되지 않습니다!

```
📦 파일 저장 구조
├── 파일 시스템 (uploads/) 👉 실제 파일 저장
└── H2 데이터베이스 👉 파일 메타데이터만 저장
    ├── 파일명, 경로, 크기
    ├── MIME 타입, URL
    └── 업로드 시간 등
```

**장점**:

- 💾 **DB 용량 절약**: 바이너리 데이터를 DB에 저장하지 않음
- 🚀 **빠른 성능**: 파일 서빙 최적화
- 🔍 **효율적 쿼리**: 메타데이터만 검색

## 🏃‍♂️ 실행 방법

### 1. 필수 요구사항

```bash
# Java 17 이상 설치 확인
java -version

# Git으로 프로젝트 클론 (선택사항)
git clone <repository-url>
cd backend
```

### 2. 서버 실행

```bash
# Maven Wrapper 사용 (권장)
./mvnw spring-boot:run

# 또는 Maven이 설치된 경우
mvn spring-boot:run

# Windows에서는
mvnw.cmd spring-boot:run
```

### 3. 서버 확인

- **API 서버**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **H2 콘솔**: http://localhost:8080/h2-console

## 🗃️ 데이터베이스 설정

### H2 콘솔 접속 정보

```properties
JDBC URL: jdbc:h2:mem:mediaboard
Username: sa
Password: password
```

### 자동 생성되는 샘플 데이터

서버 시작 시 다음 데이터가 자동으로 생성됩니다:

- 📝 **7개의 샘플 게시글** (다양한 카테고리)
- 💬 **20여개의 샘플 댓글**
- 📊 **좋아요, 조회수 통계**

## 📚 API 문서

### 🔗 Swagger UI 접속

서버 실행 후 **http://localhost:8080/swagger-ui.html** 에서 대화형 API 문서를 확인할 수 있습니다.

### 🎯 주요 API 엔드포인트

#### 📝 게시글 API

| Method | Endpoint               | 설명                           |
| ------ | ---------------------- | ------------------------------ |
| GET    | `/api/posts`           | 게시글 목록 조회 (필터링 지원) |
| GET    | `/api/posts/{id}`      | 특정 게시글 상세 조회          |
| POST   | `/api/posts`           | 새 게시글 작성                 |
| PUT    | `/api/posts/{id}`      | 게시글 수정                    |
| DELETE | `/api/posts/{id}`      | 게시글 삭제                    |
| PUT    | `/api/posts/{id}/like` | 좋아요 토글                    |

#### 💬 댓글 API

| Method | Endpoint                       | 설명           |
| ------ | ------------------------------ | -------------- |
| GET    | `/api/posts/{postId}/comments` | 댓글 목록 조회 |
| POST   | `/api/posts/{postId}/comments` | 댓글 작성      |
| PUT    | `/api/comments/{commentId}`    | 댓글 수정      |
| DELETE | `/api/comments/{commentId}`    | 댓글 삭제      |

#### 📁 파일 API

| Method | Endpoint                    | 설명               |
| ------ | --------------------------- | ------------------ |
| POST   | `/api/posts/{postId}/files` | 파일 업로드        |
| GET    | `/api/files/{fileName}`     | 파일 다운로드/조회 |
| DELETE | `/api/files/{fileId}`       | 파일 삭제          |

### 🔍 필터링 옵션

#### 카테고리 필터

```
GET /api/posts?category=풍경
GET /api/posts?category=음식
GET /api/posts?category=인물
GET /api/posts?category=여행
GET /api/posts?category=일상
```

#### 미디어 타입 필터

```
GET /api/posts?category=여행&mediaType=사진
GET /api/posts?category=음식&mediaType=동영상
```

#### 검색

```
GET /api/posts?search=제목검색어
```

## 🔧 설정 파일

### application.properties

```properties
# 서버 포트
server.port=8080

# H2 데이터베이스
spring.datasource.url=jdbc:h2:mem:mediaboard
spring.h2.console.enabled=true

# 파일 업로드 설정
spring.servlet.multipart.max-file-size=50MB
spring.servlet.multipart.max-request-size=50MB

# CORS 설정 (React 연동)
spring.web.cors.allowed-origins=http://localhost:5173

# Swagger UI
springdoc.swagger-ui.path=/swagger-ui.html
```

## 🔐 보안 및 제한사항

- **파일 크기**: 최대 50MB
- **허용 파일**: 이미지/동영상만 업로드 가능
- **좋아요 중복 방지**: IP 기반 체크
- **CORS**: React 개발 서버(`localhost:5173`) 허용

## 🚨 주의사항

1. **H2 인메모리 DB**: 서버 재시작 시 데이터 초기화
2. **파일 저장**: `uploads/` 폴더에 저장 (서버 재시작 시 보존)
3. **프로덕션 사용 시**: MySQL/PostgreSQL 등으로 DB 변경 필요

## 🐛 트러블슈팅

### Java 버전 확인

```bash
java -version
# Java 17 이상 필요
```

### 포트 충돌 시

```bash
# application.properties에서 포트 변경
server.port=8081
```

### 파일 업로드 실패 시

- 파일 크기 50MB 이하 확인
- 허용된 파일 타입인지 확인 (이미지/동영상)

## 📞 지원

문제가 발생하거나 문의사항이 있으시면 언제든지 연락주세요!

---

**Made with ❤️ by Media Board Team**
