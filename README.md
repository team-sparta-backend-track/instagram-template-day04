# Day 4: Member 엔터티와 회원가입 - 라이브 코딩 커밋 계획

이 문서는 `day4` 브랜치에서 진행할 라이브 코딩의 커밋 순서와 상세 내용을 가이드합니다. 이미 작성된 소스 코드(`service`, `controller`, `repository` 등)를 단계별로 어떻게 구현해 나갈지 설명합니다.

## 1. Member 엔터티 및 Repository 설계 (Entity & DDL)
**목표**: 데이터베이스 테이블과 매핑되는 엔터티 클래스를 정의하고, DB 접근 계층을 만듭니다.

- **작업 파일**:
  - `src/main/java/com/example/instagramclone/domain/member/entity/Member.java`
  - `src/main/java/com/example/instagramclone/repository/MemberRepository.java`
- **주요 구현 내용**:
  - **Member.java**:
    - JPA 어노테이션: `@Entity`, `@Table(name="members")`
    - 필드 정의: `username`, `password`, `email`, `phone`, `name`
    - 제약 조건: `@Column(unique = true)` (이메일, 사용자이름, 전화번호)
    - 빌더 패턴: `@Builder`, `@NoArgsConstructor(access = Protected)`
    - *Tip*: `role` 필드의 기본값(`ROLE_USER`) 설정도 함께 다룹니다.
  - **MemberRepository.java**:
    - `JpaRepository<Member, Long>` 상속.
    - 쿼리 메소드 정의:
      - `Optional<Member> findByEmail(String email);`
      - `boolean existsByEmail(String email);` 등 중복 체크용 메소드.
- **Commit Message**: `feat: Define Member entity and repository interface`

## 2. Repository 단위 테스트 (Test)
**목표**: 엔터티 매핑과 제약 조건이 DB에 잘 반영되는지, Repository 쿼리가 정상 동작하는지 검증합니다.

- **작업 파일**:
  - `src/test/java/com/example/instagramclone/repository/MemberRepositoryTest.java` (생성 필요)
- **주요 구현 내용**:
  - `@DataJpaTest` 슬라이스 테스트 환경 설정.
  - **Save Test**: 정상적인 회원가입 시나리오 테스트.
  - **Unique Constraints Test**: 중복된 `email` 저장 시 `DataIntegrityViolationException` 발생 확인.
  - **Exists Test**: `existsBy...` 메소드가 `true`/`false`를 정확히 반환하는지 확인.
- **Commit Message**: `test: Add unit tests for MemberRepository`

## 3. 회원가입 기능 구현 (1단계: Try-Catch 패턴) (Business & API)
**목표**: Service에서 비즈니스 로직(중복 검사, 암호화, 저장)을 구현하고, Controller에서 이를 호출합니다. 단, 예외 처리는 **표준 자바 예외(`IllegalStateException`)**와 **Controller 내 `try-catch`**로 처리하여, 코드의 복잡성과 중복 문제를 직접 체험합니다.

- **작업 파일**:
  - `src/main/java/com/example/instagramclone/service/MemberService.java`
  - `src/main/java/com/example/instagramclone/controller/rest/AuthController.java`
  - `src/main/java/com/example/instagramclone/domain/member/dto/request/SignUpRequest.java`
  - `src/main/java/com/example/instagramclone/config/SecurityConfig.java` (API 오픈용)
- **주요 구현 내용**:
  - **SignUpRequest**: `@NotBlank`, `@Email`, `@Pattern` 등 유효성 검증 어노테이션 추가.
  - **MemberService**:
    - `signUp(SignUpRequest)`:
      - `repository.existsByEmail(...)` 체크 -> 중복 시 `throw new IllegalStateException("이미 존재하는 이메일입니다.");`
      - 비밀번호 암호화 (`BCryptPasswordEncoder` 빈 등록 필요).
      - `repository.save()` 호출.
  - **AuthController**:
    - `@PostMapping("/signup")` 엔드포인트 구현.
    - **Try-Catch 블록**: `service.signUp()` 호출을 `try-catch`로 감싸고, 예외 발생 시 `ResponseEntity.badRequest().body(e.getMessage())` 반환.
  - **SecurityConfig**: `/api/auth/**` 경로 `.permitAll()`.
- **학습 포인트**: "매번 Controller마다 `try-catch`를 써야 하나요? API 마다 에러 응답 형식이 제각각이면 프론트엔드는 어떻게 하나요?"라는 의문을 제기합니다.
- **Commit Message**: `feat: Implement basic sign-up logic with try-catch error handling`

## 4. 커스텀 예외와 전역 핸들러 리팩토링 (2단계: Global Exception) (Refactor)
**목표**: 앞서 작성한 `try-catch` 코드의 문제점을 해결하기 위해, **커스텀 예외**와 **`@RestControllerAdvice`**를 도입하여 에러 처리를 중앙화하고 표준화합니다.

- **작업 파일**:
  - `src/main/java/com/example/instagramclone/exception/ErrorCode.java`
  - `src/main/java/com/example/instagramclone/exception/MemberException.java`
  - `src/main/java/com/example/instagramclone/exception/GlobalExceptionHandler.java`
  - `src/main/java/com/example/instagramclone/service/MemberService.java` (수정)
  - `src/main/java/com/example/instagramclone/controller/rest/AuthController.java` (수정)
- **주요 구현 내용**:
  - **ErrorCode & Custom Exception**:
    - `DUPLICATE_EMAIL(409, ...)` 정의.
    - `MemberException` 생성.
  - **Refactoring Service**:
    - `throw new IllegalStateException(...)` -> `throw new MemberException(ErrorCode.DUPLICATE_EMAIL);` 로 변경.
  - **GlobalExceptionHandler**:
    - `@ExceptionHandler(MemberException.class)` 구현.
    - 표준 `ErrorResponse` 객체(code, message) 반환.
  - **Refactoring Controller**:
    - 지저분한 `try-catch` 블록 **완전 제거**.
    - 코드가 매우 깔끔해짐을 확인 (`HAPPY PATH`만 남음).
- **Commit Message**: `refactor: Introduce global exception handling and custom exceptions`
