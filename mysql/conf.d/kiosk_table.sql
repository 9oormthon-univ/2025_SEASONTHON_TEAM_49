use your_database_name;

-- ====================================================
-- 0. 기존 테이블 삭제 (의존성 역순)
-- ====================================================

DROP TABLE IF EXISTS question_option;
DROP TABLE IF EXISTS question_item;
DROP TABLE IF EXISTS question;
DROP TABLE IF EXISTS option_value;
DROP TABLE IF EXISTS option_group;
DROP TABLE IF EXISTS item;
DROP TABLE IF EXISTS category;

-- ====================================================
-- 1. 테이블 생성
-- ====================================================
CREATE TABLE category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    kiosk_type VARCHAR(20) NOT NULL,
    name        VARCHAR(100) NOT NULL
);

CREATE TABLE item (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_id BIGINT NOT NULL,
    name        VARCHAR(100) NOT NULL,
    base_price  INT         NOT NULL,
    FOREIGN KEY (category_id) REFERENCES category(id)
);

CREATE TABLE option_group (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    min_select  INT         NOT NULL,
    max_select  INT         NOT NULL,
    category_id BIGINT,
    item_id     BIGINT,
    FOREIGN KEY (category_id) REFERENCES category(id),
    FOREIGN KEY (item_id)       REFERENCES item(id)
);

CREATE TABLE option_value (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    group_id         BIGINT NOT NULL,
    label            VARCHAR(100) NOT NULL,
    price_delta      INT         NOT NULL,
    default_selected BOOLEAN     NOT NULL,
    target_item_id   BIGINT,
    FOREIGN KEY (group_id)       REFERENCES option_group(id),
    FOREIGN KEY (target_item_id) REFERENCES item(id)
);

CREATE TABLE question (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    description     VARCHAR(255) NOT NULL,
    kiosk_type      VARCHAR(20)  NOT NULL,
    packaging       VARCHAR(20)  NOT NULL,
    payment_method  VARCHAR(20)  NOT NULL
);

CREATE TABLE question_item (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    question_id  BIGINT NOT NULL,
    item_id      BIGINT NOT NULL,
    quantity     INT     NOT NULL,
    FOREIGN KEY (question_id) REFERENCES question(id),
    FOREIGN KEY (item_id)       REFERENCES item(id)
);

CREATE TABLE question_option (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    question_item_id    BIGINT NOT NULL,
    group_id            BIGINT NOT NULL,
    value_id            BIGINT NOT NULL,
    FOREIGN KEY (question_item_id) REFERENCES question_item(id),
    FOREIGN KEY (group_id)            REFERENCES option_group(id),
    FOREIGN KEY (value_id)            REFERENCES option_value(id)
);












