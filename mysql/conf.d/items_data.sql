-- =================================================================
-- 최종 데이터 INSERT 스크립트 (모든 요구사항 반영)
-- =================================================================

-- 데이터 초기화 (필요시에만 주석 해제하여 사용)
-- DELETE FROM option_value;
-- DELETE FROM option_group;
-- DELETE FROM item;
-- DELETE FROM category;
-- ALTER TABLE category AUTO_INCREMENT = 1;
-- ALTER TABLE item AUTO_INCREMENT = 1;
-- ALTER TABLE option_group AUTO_INCREMENT = 1;
-- ALTER TABLE option_value AUTO_INCREMENT = 1;

-- 1. 카테고리 (Category) 데이터
INSERT INTO category (kiosk_type, name) VALUES
('BURGER','버거'),      -- 1
('BURGER','사이드'),     -- 2
('BURGER','음료'),      -- 3
('BURGER','디저트'),     -- 4
('BURGER','세트'),      -- 5
('CAFE','커피'),       -- 6
('CAFE','음료'),       -- 7
('CAFE','디저트'),     -- 8
('CAFE','베이커리'),   -- 9
('MOVIE','영화 예매'),  -- 10
('MOVIE','팝콘/스낵'), -- 11
('MOVIE','음료');      -- 12

-- 2. 아이템 (Item) 데이터
-- 햄버거 아이템
INSERT INTO item (category_id, name, base_price) VALUES
(1, '불고기버거', 5400), (1, '치즈버거', 5200), (1, '새우버거', 6200), (1, '더블치즈버거', 7000), (1, '스파이시 치킨버거', 6500),
(2, '감자튀김', 2500), (2, '치즈스틱 (2조각)', 2800), (2, '어니언링', 2800), (2, '양념감자', 3000),
(3, '코카콜라', 2000), (3, '사이다', 2000), (3, '코카콜라 제로', 2000), (3, '오렌지 주스', 2500),
(4, '소프트 아이스크림', 1500), (4, '애플파이', 2000),
(5, '불고기버거세트', 8500), (5, '치즈버거세트', 8300), (5, '새우버거세트', 9300), (5, '스파이시 치킨버거세트', 9600);


-- 카페 아이템
INSERT INTO item (category_id, name, base_price) VALUES
(6, '아메리카노', 4500), (6, '카페라떼', 5000), (6, '에스프레소', 4000), (6, '콜드브루', 5500), (6, '아인슈페너', 6000),
(7, '캐모마일 티', 4800), (7, '복숭아 아이스티', 4500), (7, '자몽에이드', 6000), (7, '딸기 스무디', 6500),
(8, '뉴욕 치즈케이크', 6500), (8, '가나슈 초코케이크', 6800), (8, '티라미수', 6800), (8, '생크림 카스텔라', 5500),
(9, '소금빵', 3800), (9, '플레인 스콘', 4200), (9, '크루아상', 4000);


-- 영화관 아이템
INSERT INTO item (category_id, name, base_price) VALUES
(10, '범죄도시4 (성인)', 15000), (10, '인사이드 아웃 2 (성인)', 15000), (10, '서울의 봄 (성인)', 14000), (10, '파묘 (성인)', 14000),
(11, '오리지널 팝콘(L)', 6000), (11, '카라멜 팝콘(L)', 7000), (11, '반반 팝콘(L)', 7000), (11, '나초', 5500), (11, '칠리치즈 핫도그', 6000),
(12, '콜라(L)', 3500), (12, '제로콜라(L)', 3500), (12, '스프라이트(L)', 3500);


-- 3. 옵션 그룹 (Option Group) 데이터
INSERT INTO option_group (name, min_select, max_select, category_id, item_id) VALUES
('토핑 추가', 0, 3, 1, NULL),     -- 그룹ID 1: '버거' 카테고리(1)에 적용
('토핑 제거', 0, 2, 1, NULL),     -- 그룹ID 2: '버거' 카테고리(1)에 적용
('소스 추가', 0, 2, 1, NULL),     -- 그룹ID 3: '버거' 카테고리(1)에 적용
('토핑 추가', 0, 3, 5, NULL),     -- 그룹ID 4: '세트' 카테고리(5)에도 '토핑 추가'를 적용 (데이터 중복)
('사이드 변경', 1, 1, 5, NULL),     -- 그룹ID 5: [수정] '세트' 카테고리(5) 전체에 적용
('음료 변경', 1, 1, 5, NULL),     -- 그룹ID 6: [수정] '세트' 카테고리(5) 전체에 적용
('샷 추가', 0, 2, 6, NULL),       -- 그룹ID 7: '커피' 카테고리(6)에 적용
('시럽 추가', 0, 2, 6, NULL),     -- 그룹ID 8: '커피' 카테고리(6)에 적용
('맛 선택', 2, 2, NULL, 38);      -- 그룹ID 9: '반반 팝콘' 아이템(38)에만 적용


-- 4. 옵션 값 (Option Value) 데이터 

-- 그룹 1: 토핑 추가 (버거 카테고리용)
-- [수정] default_selected 컬럼 추가 및 값으로 FALSE 지정
INSERT INTO option_value (group_id, label, price_delta, default_selected) VALUES
(1, '체다치즈', 500, FALSE),
(1, '베이컨', 1000, FALSE),
(1, '계란후라이', 700, FALSE),
(1, '토마토', 500, FALSE);

-- 그룹 2: 토핑 제거 (버거 카테고리용)
-- [수정] default_selected 컬럼 추가 및 값으로 FALSE 지정
INSERT INTO option_value (group_id, label, price_delta, default_selected) VALUES
(2, '피클 제외', 0, FALSE),
(2, '양상추 제외', 0, FALSE);

-- 그룹 3: 소스 추가 (버거 카테고리용)
-- [수정] default_selected 컬럼 추가 및 값으로 FALSE 지정
INSERT INTO option_value (group_id, label, price_delta, default_selected) VALUES
(3, '케첩', 0, FALSE),
(3, '마요네즈', 0, FALSE),
(3, '머스타드', 0, FALSE);

-- 그룹 4: 토핑 추가 (세트 카테고리용)
-- [수정] default_selected 컬럼 추가 및 값으로 FALSE 지정
INSERT INTO option_value (group_id, label, price_delta, default_selected) VALUES
(4, '체다치즈', 500, FALSE),
(4, '베이컨', 1000, FALSE),
(4, '계란후라이', 700, FALSE),
(4, '토마토', 500, FALSE);

-- 그룹 5: 사이드 변경 (세트 카테고리용) -> 이 부분은 원래 형식이 올바릅니다.
INSERT INTO option_value (group_id, label, price_delta, default_selected, target_item_id) VALUES
(5, '감자튀김', 0, TRUE, 6),
(5, '치즈스틱', 300, FALSE, 7),
(5, '어니언링', 300, FALSE, 8);

-- 그룹 6: 음료 변경 (세트 카테고리용) -> 이 부분은 원래 형식이 올바릅니다.
INSERT INTO option_value (group_id, label, price_delta, default_selected, target_item_id) VALUES
(6, '코카콜라', 0, TRUE, 10),
(6, '코카콜라 제로', 0, FALSE, 12),
(6, '사이다', 0, FALSE, 11),
(6, '오렌지 주스', 500, FALSE, 13);

-- 그룹 7: 샷 추가 (커피 카테고리용)
-- [수정] default_selected 컬럼 추가 및 값으로 FALSE 지정
INSERT INTO option_value (group_id, label, price_delta, default_selected) VALUES
(7, '에스프레소 샷 추가', 500, FALSE);

-- 그룹 8: 시럽 추가 (커피 카테고리용)
-- [수정] default_selected 컬럼 추가 및 값으로 FALSE 지정
INSERT INTO option_value (group_id, label, price_delta, default_selected) VALUES
(8, '바닐라 시럽', 500, FALSE),
(8, '헤이즐넛 시럽', 500, FALSE);

-- 그룹 9: 맛 선택 (반반 팝콘용) -> 이 부분은 원래 형식이 올바릅니다.
INSERT INTO option_value (group_id, label, price_delta, default_selected, target_item_id) VALUES
(9, '오리지널', 0, TRUE, 36),
(9, '카라멜', 0, TRUE, 37),
(9, '어니언', 0, FALSE, NULL),
(9, '치즈', 0, FALSE, NULL);

