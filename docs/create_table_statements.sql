

CREATE TABLE app_user(
    user_id     VARCHAR2(32),
    first_name  VARCHAR2(25),
    last_name   VARCHAR2(25),
    username    VARCHAR2(25)    NOT NULL,
    password    VARCHAR2(100)   NOT NULL,
    role        VARCHAR2(50)    CHECK(role IN ('farmer', 'customer')),
    CONSTRAINT pk_user PRIMARY KEY(user_id)
);


CREATE TABLE farm(
    farm_id     VARCHAR2(6),
    user_id     VARCHAR2(32),
    farm_name   VARCHAR2(20)    NOT NULL,
    address     VARCHAR2(256)   NOT NULL,
    email       VARCHAR2(50)    NOT NULL,
    phone_no    VARCHAR2(10)    NOT NULL,
    account_balance     NUMBER(10,2),
    CONSTRAINT pk_farm PRIMARY KEY(farm_id),
    CONSTRAINT fk_farm_user FOREIGN KEY(user_id) REFERENCES app_user(user_id) ON DELETE CASCADE
);


CREATE TABLE crop(
    crop_id         VARCHAR2(6),
    scientific_name VARCHAR2(100)   NOT NULL,
    cultivar        VARCHAR2(50)    NOT NULL,
    common_name     VARCHAR2(50)    NOT NULL,
    life_cycle      VARCHAR2(10)    CHECK(life_cycle IN ('annual', 'biennial', 'perennial')),
    CONSTRAINT pk_crop PRIMARY KEY(crop_id)
);
SELECT * FROM crop
;

CREATE TABLE seeds_lot(
    seeds_lot_id                VARCHAR2(10),
    crop_id                     VARCHAR2(6),
    planting_period             VARCHAR2(50),
    harvesting_period           VARCHAR2(50),
    price_per_unit              NUMBER(10,2),
    sale_unit                   VARCHAR2(10)    CHECK(sale_unit IN ('pcs', 'kg')),
    plants_per_sq_meter         NUMBER,
    expected_yield_per_sq_meter NUMBER,
    available_quantity          NUMBER(10,2),
    CONSTRAINT pk_seeds_lot PRIMARY KEY(seeds_lot_id),
    CONSTRAINT crop_id FOREIGN KEY(crop_id) REFERENCES crop(crop_id) ON DELETE SET NULL
);


CREATE TABLE farm_seeds(
    seeds_id           VARCHAR2(10),
    seeds_lot_id       VARCHAR2(10),
    buying_date        DATE,
    quantity_bought    NUMBER       CHECK(quantity_bought > 0),
    quantity_available NUMBER       CHECK(quantity_available >= 0),
    CONSTRAINT pk_farm_seeds PRIMARY KEY(seeds_id),
    CONSTRAINT seeds_lot_id FOREIGN KEY (seeds_lot_id) REFERENCES seeds_lot(seeds_lot_id) ON DELETE SET NULL                         
);


CREATE TABLE land_lot(
    land_lot_id         VARCHAR2(10),
    lot_name            VARCHAR2(10),
    farm_id             VARCHAR2(6),
    area                NUMBER(10,2)    NOT NULL,
    land_usage               VARCHAR2(20)    CHECK(land_usage IN ('arable', 'pasture', 'orchard')),
    CONSTRAINT pk_land_lot PRIMARY KEY(land_lot_id),
    CONSTRAINT farm_id  FOREIGN KEY(farm_id) REFERENCES farm(farm_id) ON DELETE CASCADE
);

CREATE TABLE arable_land(
    land_lot_id         VARCHAR2(10),
    seeds_id            VARCHAR2(10),
    planting_date       DATE,
    is_cultivated       VARCHAR2(3)      CHECK(is_cultivated IN ('yes', 'no')),
    CONSTRAINT pk_arable_land PRIMARY KEY(land_lot_id),
    CONSTRAINT fk_arable_lot FOREIGN KEY(land_lot_id) REFERENCES land_lot(land_lot_id) ON DELETE CASCADE,
    CONSTRAINT fk_arable_seeds FOREIGN KEY(seeds_id) REFERENCES farm_seeds(seeds_id) ON DELETE CASCADE
);

CREATE TABLE orchard(
    land_lot_id         VARCHAR2(10),
    crop_id             VARCHAR2(6),
    age                 NUMBER(3),
    CONSTRAINT pk_orchard PRIMARY KEY(land_lot_id),
    CONSTRAINT fk_orachrd_lot FOREIGN KEY(land_lot_id) REFERENCES land_lot(land_lot_id) ON DELETE CASCADE,
    CONSTRAINT fk_orchard_crop FOREIGN KEY(crop_id) REFERENCES crop(crop_id) ON DELETE CASCADE
);


CREATE TABLE harvest(
    harvest_id          VARCHAR2(10),
    farm_id             VARCHAR2(6),
    seeds_id            VARCHAR2(10),
    land_lot_id         VARCHAR2(10),
    harvest_date        DATE,
    yielded_quantity    NUMBER(10,2),
    on_sale             VARCHAR2(3)     CHECK(on_sale IN('yes', 'no')),
    CONSTRAINT pk_harvest PRIMARY KEY(harvest_id),
    CONSTRAINT fk_harvest_farm FOREIGN KEY(farm_id) REFERENCES farm(farm_id) ON DELETE CASCADE,
    CONSTRAINT fk_harvest_seeds FOREIGN KEY(seeds_id) REFERENCES  farm_seeds(seeds_id) ON DELETE CASCADE,
    CONSTRAINT fk_harvest_land_lot FOREIGN KEY(land_lot_id) REFERENCES land_lot(land_lot_id) ON DELETE SET NULL
);


CREATE TABLE harvest_on_sale(
    harvest_id          VARCHAR2(10) PRIMARY KEY,
    quantity_on_sale    NUMBER,
    quantity_sold       NUMBER,
    sale_price          NUMBER(10, 2),
    CONSTRAINT fk_harv FOREIGN KEY(harvest_id) REFERENCES harvest(harvest_id) ON DELETE CASCADE
);

INSERT INTO crop(crop_id, scientific_name, cultivar, common_name, life_cycle) 
    VALUES ('a1b2c3', 'Raphanus sativus', 'Johanna', 'Ridichi de luna', 'annual');
INSERT INTO crop(crop_id, scientific_name, cultivar, common_name, life_cycle) 
    VALUES ('d2e3f4', 'Phaseolus vulgaris', 'Sondela', 'Fasole', 'annual');
INSERT INTO crop(crop_id, scientific_name, cultivar, common_name, life_cycle) 
    VALUES ('a00001', 'Solanum lycopersicum', 'Inima de bou', 'Rosii', 'annual');
INSERT INTO crop(crop_id, scientific_name, cultivar, common_name, life_cycle) 
    VALUES ('b00002', 'Solanum lycopersicum', 'Yellow pearshaped', 'Rosii cherry', 'annual');
INSERT INTO crop(crop_id, scientific_name, cultivar, common_name, life_cycle) 
    VALUES ('c00003', 'Capsicum annuum', 'Jalapeno', 'Ardei iute', 'annual');
    
   

INSERT INTO seeds_lot VALUES('aaa', 'a1b2c3', 'Februarie', 'Aprilie', 30, 'kg', 35, 20, 1000);
INSERT INTO seeds_lot VALUES('bbb', 'd2e3f4', 'Aprilie', 'Iulie', 20, 'kg', 40, 10, 500);
INSERT INTO seeds_lot VALUES('ccc', 'a00001', 'Mai', 'August', 50, 'kg', 5, 100, 2000);
INSERT INTO seeds_lot VALUES('ddd', 'b00002', 'Mai', 'Septembrie', 60, 'kg', 5, 30, 400);
INSERT INTO seeds_lot VALUES('eee', 'c00003', 'Mai', 'Septembrie', 40, 'kg', 8, 20, 200);


ALTER TABLE farm DROP CONSTRAINT fk_farm_user;
ALTER TABLE farm MODIFY phone_no VARCHAR2(15);
DROP TABLE app_user;
ALTER TABLE farm ADD CONSTRAINT fk_farm_user FOREIGN KEY(user_id) REFERENCES app_user(user_id) ON DELETE CASCADE;

ALTER TABLE harvest DROP CONSTRAINT check_qnt;
ALTER TABLE harvest DROP COLUMN quantity_on_sale;
--DESCRIBE app_user;
--DESCRIBE farm;

DELETE FROM app_user WHERE 1=1;
SELECT * FROM app_user;
commit;
SELECT * FROM farm; where user_id = '7f7dbcc4-b2d5-47e1-b6c7-001a7c9d8a28' OR farm_name = '7f7dbcc4-b2d5-47e1-b6c7-001a7c9d8a28';

SELECT * FROM farm_seeds;

ALTER TABLE farm_seeds ADD farm_id VARCHAR2(6);
ALTER TABLE farm_seeds ADD CONSTRAINT fk_farm_seeds FOREIGN KEY (farm_id) REFERENCES farm(farm_id) ON DELETE CASCADE;
commit;


SELECT * FROM USER_CONSTRAINTS WHERE UPPER(TABLE_NAME) LIKE 'CROP';
ALTER TABLE CROP DROP CONSTRAINT SYS_C008237;
SELECT * FROM arable_land;

DELETE FROM land_lot where 1=1;

commit;

ALTER TABLE land_lot MODIFY lot_name VARCHAR(50);

