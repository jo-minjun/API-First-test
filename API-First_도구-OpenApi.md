# API-First 도구 (OpenApi)

- 본 문서는 OpenApi의 가장 간단한 부분을 번역하여 기술했습니다.
- 자세한 정보는 [OpenApi Guide](https://swagger.io/docs/specification/about/)를 참조하세요.

## OpenApi 란?

- OpenApi Specification은 REST API를 위한 기술 도구이다.
- OpenAPI 파일은 아래 내용을 포함한 내용을 기술할 수 있다.
  - API endpoint와 HTTP method (GET /users, POST /users 등)
  - 각 API의 요청과 응답의 파라미터
  - 인증 method
  - 이용 방법, 라이센스, 연락처 등과 기타 정보
- OpenApi Specification은 YAML 또는 JSON으로 작성할 수 있다.
- Swagger와 redoc 등을 모두 지원한다.
  - Swagger Editor를 사용하면 OpenApi Specification의 문법 오류와 UI를 확인 할 수 있다.

## OpenApi의 장점

- OpenApi-generator를 사용하면 서버의 코드 stub과 클라이언트의 라이브러리를 자동으로 생성해준다.
  - 서버 stub은 각 API에 대한 기술없이, 구현만 해주면 된다.
  - JAVA 뿐만 아니라 PHP, GO, C++, C#, Python, Ruby, Typescript 등 대부분의 언어를 지원한다.
  - API Interface 뿐만 아니라 model도 구현해주고, 정규 표현식 또는 다른 제한을 validation 까지 해준다.
- 브라우저에서 사용자가 직접 호출해볼 수 있는 interactive API 문서를 만들어 준다.
  - Ex) Swagger
- API를 먼저 기술하여 API-First 개발을 할 수 있다.

## 기본 문법 구조

- YAML 또는 JSON으로 작성할 수 있다.
- 예시는 YAML이지만 JSON도 똑같이 적용할 수 있다.
- 모든 키워드 이름은 대소문자를 구분한다.

```yaml
openapi: 3.0.0

info:
  title: Sample API
  description: Optional multiline or single-line description in [CommonMark](http://commonmark.org/help/) or HTML.
  version: 0.1.9

servers:
  - url: http://api.example.com/v1
    description: Optional server description, e.g. Main (production) server
  - url: http://staging-api.example.com
    description: Optional server description, e.g. Internal staging server for testing

paths:
  /users:
    get:
      summary: Returns a list of users.
      description: Optional extended description in CommonMark or HTML.
      responses:
        '200': # status code
          description: A JSON array of user names
          content:
            application/json:
              schema:
                type: array
                items:
                  type: string
```

> - OpenApi Spec 예시

### 메타데이터

```yaml
openapi: 3.0.0
```

- OpenApi Spec 파일은 위 형식을 가진 버전 정보를 포함해야 한다.

```yaml
info:
  title: Sample API
  description: Optional multiline or single-line description in [CommonMark](http://commonmark.org/help/) or HTML.
  version: 0.1.9
```

- info 는 API의 정보를 포함한다. title과 description은 선택 필드이다.
- title: API의 이름
- description: API에 대한 추가적인 정보, 여러줄도 작성 가능하다.
  - MD 형식을 지원한다.
- version: API의 버전을 기술한다.
  - OpenApi Spec 파일의 버전과는 다르다. 이 필드는 API의 버전이다.

### Servers

```yaml
servers:
  - url: http://api.example.com/v1
    description: Optional server description, e.g. Main (production) server
  - url: http://staging-api.example.com
    description: Optional server description, e.g. Internal staging server for testing
```

- 모든 API 경로는 servers.url에 상대적이다.
  - 위 예제에서 /users는 http://api.example.com/v1/users 또는 http://staging-api.example.com/users를 의미한다.
- 자세한 정보는 [API Server and Base Path](https://swagger.io/docs/specification/api-host-and-base-path/)를 참고

### Path

```yaml
paths:
  /users:
    get:
      summary: Returns a list of users.
      description: Optional extended description in CommonMark or HTML
      responses:
        '200':
          description: A JSON array of user names
          content:
            application/json:
              schema:
                type: array
                items:
                  type: string
```

- path는 parameters, request body, 응답될 수 있는 모든 status code, 응답 content를 포함할 수 있다.
- 자세한 정보는 [Paths and Operations](https://swagger.io/docs/specification/paths-and-operations/)를 참고

### Parameters

```yaml
paths:
  /users/{userId}:
    get:
      summary: Returns a user by ID.
      parameters:
        - name: userId
          in: path
          required: true
          description: Parameter description in CommonMark or HTML.
          schema:
            type: integer
            format: int64
            minimum: 1
      responses:
        '200':
          description: OK
```

- path parameter 또는 query string, header, cookies 를 가질 수 있다.
- 각 parameter가 선택인지 필수인지와 데이터 타입, format 등을 정의할 수 있다.
- 자세한 정보는 [Describing Parameters](https://swagger.io/docs/specification/describing-parameters/)를 참고

### Request Body

```yaml
paths:
  /users:
    post:
      summary: Creates a user.
      requestBody:
        required: true
        content:
          application/json:
            schema:
              type: object
              properties:
                username:
                  type: string
      responses:
        '201':
          description: Created
```

- 요청의 content를 정의하기 위해서 requestBody를 사용할 수 있다.
- 자세한 정보는 [Describing Request Body](https://swagger.io/docs/specification/describing-request-body/)를 참고

### Responses

```yaml
paths:
  /users/{userId}:
    get:
      summary: Returns a user by ID.
      parameters:
        - name: userId
          in: path
          required: true
          description: The ID of the user to return.
          schema:
            type: integer
            format: int64
            minimum: 1
      responses:
        '200':
          description: A user object.
          content:
            application/json:
              schema:
                type: object
                properties:
                  id:
                    type: integer
                    format: int64
                    example: 4
                  name:
                    type: string
                    example: Jessica Smith
        '400':
          description: The specified user ID is invalid (not a number).
        '404':
          description: A user with the specified ID was not found.
        default:
          description: Unexpected error
```

- schema를 이용해서 response의 body를 정의할 수 있고, 가능한 status code를 정의할 수 있다.
- $ref를 이용해서 미리 정의 해둔 schema를 가리킬 수 있다.
- 자세한 정보는 [Describing Responses](https://swagger.io/docs/specification/describing-responses/)를 참고

### Models

- components/schema 에는 API에서 사용되는 데이터 구조를 정의할 수 있다.

```json
{
  "id": 4,
  "name": "Arthur Dent"
}
```

- 위 구조는 아래와 같이 정의할 수 있다.

```yaml
components:
  schemas:
    User:
      type: object
      properties:
        id:
          type: integer
          example: 4
        name:
          type: string
          example: Arthur Dent
      # Both properties are required
      required:
        - id
        - name
```

- 또한 위 데이터 구조를 참조하여 request body schema 또는 response body schema로 사용할 수 있다.

```yaml
paths:
  /users/{userId}:
    get:
      summary: Returns a user by ID.
      parameters:
        - in: path
          name: userId
          required: true
          schema:
            type: integer
            format: int64
            minimum: 1
      responses:
        '200':
          description: OK
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/User' # <-------
  /users:
    post:
      summary: Creates a new user.
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/User' # <-------
      responses:
        '201':
          description: Created
```

### Authentication

```yaml
components:
  securitySchemes:
    BasicAuth:
      type: http
      scheme: basic
security:
  - BasicAuth: []
```

- securitySchemas와 security 키워드는 API에서 인증 방법을 기술할 때 사용된다.
- 지원되는 인증 방법은 다음과 같다.
  - HTTP authentication: Basic, Bearer
  - header 또는 query string 또는 Cookies에 있는 API Key
  - OAuth2
  - OpenId Connection Discovery
- 자세한 정보는 [Authentication](https://swagger.io/docs/specification/authentication/)를 참고

### 기타

- 위 내용뿐만 아니라 다른 많은 기능을 가지고 있습니다.
- 아래 내용은 읽어보시길 추천 드립니다.
  - [Data Types](https://swagger.io/docs/specification/data-models/data-types/)
  - [Enums](https://swagger.io/docs/specification/enums/)
  - [oneOf, anyOf, allOf, not](https://swagger.io/docs/specification/data-models/oneof-anyof-allof-not/)
  - [Adding Examples](https://swagger.io/docs/specification/adding-examples/)
