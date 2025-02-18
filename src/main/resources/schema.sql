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