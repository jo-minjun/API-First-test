# OpenApi Generator

## OpenApi Generator란?

- API 클라이언트 라이브러리, 서버 코드 stub, API 문서 등을 생성하는 도구이다.
- 사용 편의성에 중점을 둔 API 주도 개발 지원 도구이다.
- 50개가 넘는 언어의 클라이언트 라이브러리 생성을 지원한다.
- 40개가 넘는 언어의 서버 코드 stub 생성을 지원한다.

## OpenApi Generator 사용법

1. 프로젝트 생성 및 OpenApi Generator 설치
   - Spring Boot 프로젝트 Gradle을 기준으로 문서를 작성했습니다.
     - OpenApi Generator는 CLI, Maven/Gradle 플러그인 등을 사용하는 경우에서 동일합니다.
     - 이 문서는 Gradle을 이용해서 기술합니다.
   - 다른 언어는 아래 문서를 참고하세요.
     - [사용 가능한 언어 목록](https://openapi-generator.tech/docs/generators)
     - [OpenApi Generator 설치](https://openapi-generator.tech/docs/installation/)
     - [사용법](https://openapi-generator.tech/docs/usage)
       - CLI 기준입니다.
2. yaml 또는 json으로 OpenApi Spec 문서 기술
3. 서버 stub 생성 및 클라이언트 라이브러리 생성
