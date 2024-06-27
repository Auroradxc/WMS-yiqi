CREATE TABLE `Supplement` (
	`id` INT NOT NULL AUTO_INCREMENT UNIQUE,
	`sup_id` VARCHAR(255) NOT NULL,
	`sup_name` VARCHAR(255),
	`supplier` VARCHAR(255) NOT NULL,
	`creator` VARCHAR(255),
	`updator` VARCHAR(255),
	`create_time` DATETIME,
	`update_time` DATETIME,
	PRIMARY KEY(`id`, `sup_id`, `supplier`)
);

/* 器具表 */
CREATE TABLE `Appliance` (
	`id` INT NOT NULL AUTO_INCREMENT UNIQUE,
	`sup_id` VARCHAR(255) NOT NULL,
	`supplier` VARCHAR(255),
	`wrap_id` VARCHAR(255),
	`wrap_size` VARCHAR(255),
	`creator` VARCHAR(255),
	`updator` VARCHAR(255),
	`create_time` DATETIME,
	`update_time` DATETIME,
	PRIMARY KEY(`id`, `sup_id`, `supplier`, `wrap_size`)
) COMMENT='器具表';

CREATE TABLE `Stock` (
	`id` INT NOT NULL AUTO_INCREMENT UNIQUE,
	`sup_id` VARCHAR(255) NOT NULL,
	`stock_num` INT,
	`lowest_num` INT,
	`highest_num` INT,
	`creator` VARCHAR(255),
	`updator` VARCHAR(255),
	`create_time` DATETIME,
	`update_time` DATETIME,
	PRIMARY KEY(`id`, `sup_id`)
);

CREATE TABLE `Enstore_List` (
	`id` INT NOT NULL AUTO_INCREMENT UNIQUE,
	`List_num` VARCHAR(255) NOT NULL,
	`List_Status` VARCHAR(255) NOT NULL,
	`creator` VARCHAR(255),
	`updator` VARCHAR(255),
	`create_time` DATETIME,
	`update_time` DATETIME,
	PRIMARY KEY(`id`, `List_num`)
);

CREATE TABLE `Enstore_List_Detail` (
	`id` INT NOT NULL AUTO_INCREMENT UNIQUE,
	`List_id` VARCHAR(255) NOT NULL,
	-- 单号， 不是数量
	`List_num` VARCHAR(255) COMMENT '单号， 不是数量',
	`sup_id` VARCHAR(255),
	`wrap_size` VARCHAR(255),
	`exp_in_num` INT,
	`act_in_num` INT,
	`creator` VARCHAR(255),
	`updator` VARCHAR(255),
	`create_time` DATETIME,
	`update_time` DATETIME,
	PRIMARY KEY(`id`, `List_id`, `List_num`)
);

CREATE TABLE `Supplier` (
	`id` INT NOT NULL AUTO_INCREMENT UNIQUE,
	`supplier_id` VARCHAR(255) NOT NULL UNIQUE,
	`supplier_name` VARCHAR(255),
	`creator` VARCHAR(255),
	`updator` VARCHAR(255),
	`create_time` DATETIME,
	`update_time` DATETIME,
	PRIMARY KEY(`id`, `supplier_id`)
);

CREATE TABLE `Exstore_List` (
	`id` INT NOT NULL AUTO_INCREMENT UNIQUE,
	`List_num` VARCHAR(255),
	`List_Status` VARCHAR(255),
	`creator` VARCHAR(255),
	`updator` VARCHAR(255),
	`create_time` DATETIME,
	`update_time` DATETIME,
	PRIMARY KEY(`id`, `List_num`)
);

CREATE TABLE `Exstore_List_Detail` (
	`id` INT NOT NULL AUTO_INCREMENT UNIQUE,
	`List_id` VARCHAR(255),
	`List_num` VARCHAR(255),
	`sup_id` VARCHAR(255),
	`wrap_size` VARCHAR(255),
	`exp_in_num` INT,
	`act_in_num` INT,
	`creator` VARCHAR(255),
	`updator` VARCHAR(255),
	`create_time` DATETIME,
	`update_time` DATETIME,
	PRIMARY KEY(`id`, `List_id`, `List_num`)
);

/* 不知道这个in_out_state是来自于那里的，所以目前不用这个表
CREATE TABLE `Bar_Code` (
	`id` INT NOT NULL AUTO_INCREMENT UNIQUE,
	`sup_id` VARCHAR(255),
	`supplier` VARCHAR(255),
	`bar_num` VARCHAR(255) NOT NULL,
	`in_out_state(?)` VARCHAR(255),
	`creator` VARCHAR(255),
	`updator` VARCHAR(255),
	`create_time` VARCHAR(255),
	`update_time` VARCHAR(255),
	PRIMARY KEY(`id`, `bar_num`)
) COMMENT='不知道这个in_out_state是来自于那里的，所以目前不用这个表';*/

ALTER TABLE `Appliance`
ADD FOREIGN KEY(`sup_id`) REFERENCES `Supplement`(`sup_id`)
ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE `Appliance`
ADD FOREIGN KEY(`supplier`) REFERENCES `Supplement`(`supplier`)
ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE `Stock`
ADD FOREIGN KEY(`sup_id`) REFERENCES `Supplement`(`sup_id`)
ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE `Supplement`
ADD FOREIGN KEY(`supplier`) REFERENCES `Supplier`(`supplier_id`)
ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE `Enstore_List`
ADD FOREIGN KEY(`List_num`) REFERENCES `Enstore_List_Detail`(`List_num`)
ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE `Enstore_List_Detail`
ADD FOREIGN KEY(`wrap_size`) REFERENCES `Appliance`(`wrap_size`)
ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE `Enstore_List_Detail`
ADD FOREIGN KEY(`sup_id`) REFERENCES `Appliance`(`sup_id`)
ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE `Exstore_List`
ADD FOREIGN KEY(`List_num`) REFERENCES `Exstore_List_Detail`(`List_num`)
ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE `Exstore_List_Detail`
ADD FOREIGN KEY(`sup_id`) REFERENCES `Appliance`(`sup_id`)
ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE `Exstore_List_Detail`
ADD FOREIGN KEY(`wrap_size`) REFERENCES `Appliance`(`wrap_size`)
ON UPDATE CASCADE ON DELETE CASCADE;