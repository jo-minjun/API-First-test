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

## yaml 또는 json으로 OpenApi Spec 문서 기술

- OpenApi의 공식 예제인 [petstore.yaml](https://github.com/OAI/OpenAPI-Specification/blob/main/examples/v3.0/petstore.yaml)을 사용했습니다.
- 문법은 2. API-First 도구 설명 (OpenApi)에 있습니다.
- 이 문서에서는 yaml 파일 위치를 src/main/resources/api.yaml 로 했습니다.

## 서버 stub 생성 및 클라이언트 라이브러리 생성

### 서버 stub 생성

- openApi.gradle 생성
    - 참고: [openApi generator 태스크 Key 설명](https://github.com/OpenAPITools/openapi-generator/tree/master/modules/openapi-generator-gradle-plugin)
    - openApiGenerate → configOptions → delegatePattern을 true로 하면 delegate pattern이 적용되어 소스코드가 생성됩니다.
    - sourceSets을 지정해야 프로젝트에서 openAPI Genterator로 생성된 소스코드가 인식됩니다.

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

- build.gradle 설정
    - 아래 내용을 build.gradle에 추가합니다.

```bash
// OpenApi 설정을 gradle/openApi.gradle에 두었음 (import)
apply from: 'gradle/openApi.gradle'
```

- OpenApi 태스크 실행

```bash
./gradlew openApiGenerate
```

- 서버 stub 확인
    - 상술된 gradle 설정을 사용하면 위와 같은 디렉토리에 소스코드가 생성됩니다.

![2](./img/2.png)

- 이제 생성된 소스코드를 이용해서 서버 개발을 하면 된다.

### 클라이언트 라이브러리 생성

**클라이언트 소스코드 생성**

- 이 문서에서는 java-webclient 클라이언트를 생성한다.
    - java-resttemplate, typescript 등도 지원한다.
- 프로젝트에 clients/webclient를 만든다. (이름과 디렉토리는 원하는 것으로 하면 된다.)
- settings.gradle에 clients/webclient를 추가한다.

```bash
include 'clients:webclient'
```

- clients/webclient에 build.gradle을 추가한다.

```bash
plugins {
    id 'java'
    id 'org.springframework.boot'
    id 'io.spring.dependency-management'
    id 'org.openapi.generator'
}

def libraryName = 'webclient'
def specPath = "${parent.rootDir}/src/main/resources/api.yaml"
def outputPath = "${buildDir}/generated/java-${libraryName}-client"

group = 'minjun.client'
version = '0.0.1-SNAPSHOT'
archivesBaseName = "${libraryName}-client"

sourceCompatibility = '1.8'
targetCompatibility = '1.8'

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-webflux'
    implementation 'io.swagger:swagger-annotations:1.6.2'
    implementation 'org.openapitools:jackson-databind-nullable:0.2.3'
    implementation 'com.google.code.findbugs:jsr305:3.0.2'
}

openApiGenerate {
    generatorName = "java"
    inputSpec = specPath
    outputDir = outputPath
    apiPackage = "minjun.openapi.api"
    modelPackage = "minjun.openapi.api.model"
    modelNameSuffix = "Dto"
    apiFilesConstrainedTo = [""]
    modelFilesConstrainedTo = [""]
    supportingFilesConstrainedTo = [""]
    configOptions = [
            title: "webclient",
            useTags: "true",
            dateLibrary: "java8",
            java8: "true",
            hideGenerationTimestamp: "true",
            library: "${libraryName}",
            serializableModel: "true",
            serializationLibrary: "jackson",
            bigDecimalAsString: "true",
            useRuntimeException: "true"
    ]

    validateSpec = true
}

sourceSets {
    main {
        java {
            srcDir file("${outputPath}/src/main/java")
        }
    }
}

jar {
    enabled = true
    from sourceSets.main.output
}

bootJar {
    enabled = false
}

java {
    withJavadocJar()
    withSourcesJar()
}

compileJava.dependsOn("openApiGenerate")
```

- gradle 테스크를 실행한다.

```bash
./gradlew :clients:webclient:openApiGenerate
```

- 위 태스크를 실행하면 소스코드를 확인할 수 있다.

**클라이언트 라이브러리 생성**

- publish를 하려면 아래 내용을 clients/webclient/build.gradle에 추가해야 한다.

```bash
plugins {
    id 'java-library'
    id 'maven-publish'
}

publishing {
    publications {
        maven(MavenPublication) {
            artifactId = archivesBaseName

            from components.java
        }
    }
}
```

- 이 문서에서는 local에 publishing 한다. 아래 태스크를 실행한다.

```bash
./gradlew :clients:webclient:publishToMavenLocal
```

- 아래 경로로 이동하면 클라이언트 라이브러리가 publish 되었는 것을 볼 수 있다.

```bash
cd ~/.m2/repository/{webclient의 build.gralde 경로}/0.0.1-SNAPSHOT
```

```bash
$ ls
maven-metadata-local.xml
webclient-client-0.0.1-SNAPSHOT.module
webclient-client-0.0.1-SNAPSHOT.pom
webclient-client-0.0.1-SNAPSHOT-javadoc.jar
webclient-client-0.0.1-SNAPSHOT-plain.jar
webclient-client-0.0.1-SNAPSHOT-sources.jar
```

- publishing을 사내 nexus로 하면, 클라이언트를 개발하는 다른 개발자가 publish된 클라이언트 라이브러리를 사용하면 된다.

## (추가) 클라이언트 라이브러리 의존성 추가해보기!

- 새 프로젝트를 만들고 publishing한 라이브러리가 의존성에 추가 되는지 확인해보자
- 새 프로젝트 build.gradle에 의존성 추가

```bash
repositories {
		// local 저장소 추가
    mavenLocal()
}

dependencies {
    // openapi client library
    implementation 'minjun.client:webclient-client:0.0.1-SNAPSHOT'
}
```

![3.PNG](./img/3.png)

- 위와 같이 의존성이 추가된다.

```java
package openapi.client;

import minjun.openapi.ApiClient;
import minjun.openapi.api.StoreApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Webclient {

    @Bean
    public ApiClient apiClient() {
				// baseUrl 설정 필요시 추가
				// new ApiClient().setBasePath("url");
        return new ApiClient();
    }

    @Bean
    public StoreApi storeApi(ApiClient apiClient) {
        return new StoreApi(apiClient);
    }
}
```

- 위와 같이 빈으로 등록한 후 사용하면 된다. 끝.