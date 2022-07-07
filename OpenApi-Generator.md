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

- 참고: [Github](https://github.com/OpenAPITools/openapi-generator)

## 프로젝트 생성 및 OpenApi Generator 설치

- gradle을 이용할 것이기 때문에 OpenApi Generator를 따로 설치하지 않아도 된다.
   - 다른 방법은 상술한 내용 참고 (**OpenApi Generator 사용법**)
- 프로젝트는 일반 프로젝트와 똑같이 만들어 주고, 관련 Dependency를 추가해주면 된다.
- build.gradle에 OpenApi 관련 Dependency 들을 모두 추가해준다.

```java
**file: build.gradle**

plugins {
    id 'org.springframework.boot' version '2.7.1'
    id 'io.spring.dependency-management' version '1.0.11.RELEASE'
    id 'java'

    // OpenApi Generator
    id "org.openapi.generator" version "6.0.1"
}

// OpenApi Generator
apply plugin: 'org.openapi.generator'
// OpenApi 설정을 gradle/openApi.gradle에 두었음 (import)
apply from: 'gradle/openApi.gradle'

group = 'minjun'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '11'

configurations {
    compileOnly {
        extendsFrom annotationProcessor
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'

    // OpenApi Generator
    // OpenApi Stub에서 사용되는 annotation 추가
    implementation 'org.openapitools:jackson-databind-nullable:0.2.3'
    implementation 'org.springdoc:springdoc-openapi-ui:1.6.9'
}

tasks.named('test') {
    useJUnitPlatform()
}
```

- build.gradle에 `apply from: 'gradle/openApi.gradle'` 라는 내용이 있다.
   - openApi 서버 Stub 생성 전용 gradle 파일을 만들어서 import 해준 것이다.

```java
**file: openApi.gradle**

        openApiGenerate {
        generatorName = 'spring'
        inputSpec = "$rootDir/src/main/resources/api.yaml".toString()
        outputDir = "$buildDir/openapi".toString()
        apiPackage = 'minjun.openapigenerator.api'
        modelPackage = 'minjun.openapigenerator.api.model'
        modelNameSuffix = "Dto"
        apiFilesConstrainedTo = [""]
        modelFilesConstrainedTo = [""]
        supportingFilesConstrainedTo = ["ApiUtil.java"]
        configOptions = [
        delegatePattern: "true",
        title: "openapigenerator",
        useTags: "true",
        dateLibrary: "java8",
        java8: "true",
        hideGenerationTimestamp: "true"
        ]
        validateSpec = true
        }

        sourceSets {
        main {
        java {
        srcDir file("${project.buildDir.path}/openapi/src/main/java")
        }
        }
        }

        compileJava.dependsOn("openApiGenerate")
```

- 참고: [openApi generator 태스크 Key 설명](https://github.com/OpenAPITools/openapi-generator/tree/master/modules/openapi-generator-gradle-plugin)