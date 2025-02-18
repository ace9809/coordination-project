## 상품 코디 프로젝트

> 카테고리 및 브랜드의 상품 가격 최저가/최고가 정보 코디 서비스

## 개발환경

- Kotlin
- JDK 21
- Spring Boot 3.4.2
- H2 Database
- Caffeine Cacheg
- Kotest, Mockk, Fixture

## 실행

### 프로젝트 빌드

```shell
./gradlew build
```

### 프로젝트 실행

```shell
./gradlew bootRun
```

### 테스트 코드 실행

```shell
./gradlew :test
```

## API 문서

- 스웨거를 통해 API 스펙 확인 및 실행
- http://localhost:8080/swagger-ui/index.html

## h2 DB 접속

- http://localhost:8080/h2-console
- `jdbc url: jdbc:h2:mem:test, username: sa`

## 프로젝트 구현 설명

### 통계 테이블 생성
- 상품별 최저가, 최고가 및 집계된 데이터를 매번 API 요청시 계산하지 않기 위해 통계 테이블 생성
- 상품, 생성, 삭제시 `@TransactionalEventListner`를 적용하여 이벤트 발생시에 통계 테이블 업데이트
- 현재는 데이터가 많이 없지만 추후 집계 자체가 오래걸릴 수 있다고 판단하여 `@Async`를 사용하여 비동기 처리

#### 추후 개선 포인트

- `OpenSearch` 및 `ElasticSearch` 같은 검색 엔진 도입
  - 역인덱스를 활용하여 상품 조회 성능 개선
  - 집계 함수를 활용하거나 Spring 배치를 이용하여 주기적으로 상품 통계 인덱스를 갱신

### 로컬 캐시 적용
- Caffeine Cache를 적용하여 조회 성능 최적화
- 데이터가 없데이트 될 경우에는 캐시 최신화를 위해 `@CacheEvict` 적용

#### 추후 개선 포인트
- 추후 서비스가 확장하게 될 경우 Redis 글로벌 캐시 도입 고려

### 테이블 스키마

- 최저가/최고가를 보여주는 서비스다 보니 상품 같은 경우 (카테고리, 가격), (카테고리, 브랜드) 조회가 많이 일어날 거라고 생각해 디비 옵티마이저 계산을 거치지 않기 위해 복합 인덱스로 구현하였습니다.

```sql
DROP TABLE IF EXISTS product_category_statistics;
DROP TABLE IF EXISTS product_brand_statistics;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS brands;

CREATE TABLE brands
(
  id         BIGINT AUTO_INCREMENT PRIMARY KEY,
  name       VARCHAR(25) NOT NULL,
  created_at DATETIME(6) NULL COMMENT '생성일자',
  updated_at DATETIME(6) NULL COMMENT '변경일자'
);

CREATE TABLE products
(
  id         BIGINT AUTO_INCREMENT PRIMARY KEY,
  category   VARCHAR(25) NOT NULL,
  brand_id   BIGINT       NOT NULL,
  price      BIGINT       NOT NULL,
  created_at DATETIME(6) NULL COMMENT '생성일자',
  updated_at DATETIME(6) NULL COMMENT '변경일자'
);

CREATE TABLE product_brand_statistics
(
  id          BIGINT AUTO_INCREMENT PRIMARY KEY,
  brand_id    BIGINT NOT NULL,
  total_price BIGINT NOT NULL,
  created_at  DATETIME(6) NULL COMMENT '생성일자',
  updated_at  DATETIME(6) NULL COMMENT '변경일자'
);

CREATE TABLE product_category_statistics
(
  id             BIGINT AUTO_INCREMENT PRIMARY KEY,
  category       VARCHAR(25) NOT NULL,
  min_brand_id   BIGINT       NOT NULL,
  min_product_id BIGINT       NOT NULL,
  min_price      BIGINT       NOT NULL,
  max_brand_id   BIGINT       NOT NULL,
  max_product_id BIGINT       NOT NULL,
  max_price      BIGINT       NOT NULL,
  created_at     DATETIME(6) NULL COMMENT '생성일자',
  updated_at     DATETIME(6) NULL COMMENT '변경일자'
);


CREATE INDEX `index_product_category_statistics_on_category` ON product_category_statistics (`category`);
CREATE INDEX `index_product_brand_statistics_on_total_price` ON product_brand_statistics (`total_price`);
CREATE INDEX `index_product_brand_statistics_on_brand_id` ON product_brand_statistics (`brand_id`);
CREATE INDEX `index_products_on_brand_id` ON products (brand_id);
CREATE INDEX `index_products_on_category_brand_id` ON products (category, brand_id);
CREATE INDEX `index_products_on_category_price` ON products (category, price);
```
