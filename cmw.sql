CREATE TABLE `movies` (
	`id` INTEGER NOT NULL AUTO_INCREMENT UNIQUE,
	`m_ten` VARCHAR,
	`m_daodien` VARCHAR,
	`m_dienvien` VARCHAR,
	`m_theloai` VARCHAR,
	`m_phanloai` VARCHAR,
	`m_dinhdang` VARCHAR,
	`m_ngaykhoichieu` DATE,
	`m_thoiluong` INTEGER,
	`m_phude` VARCHAR,
	`m_mota` VARCHAR,
	`m_trangthai` TINYINT,
	`m_poster` MEDIUMBLOB,
	PRIMARY KEY(`id`)
);


CREATE TABLE `users` (
	`id` INTEGER NOT NULL AUTO_INCREMENT UNIQUE,
	`u_hoten` VARCHAR,
	`u_gioitinh` TINYINT,
	`u_ngaysinh` DATE,
	`u_diachi` VARCHAR,
	`u_email` VARCHAR,
	`u_sdt` INTEGER,
	`u_matkhau` VARCHAR,
	`u_trangthai` TINYINT,
	`u_avatar` MEDIUMBLOB,
	PRIMARY KEY(`id`)
);


CREATE TABLE `concessions` (
	`id` INTEGER NOT NULL AUTO_INCREMENT UNIQUE,
	`c_ten` VARCHAR,
	`c_dongia` DOUBLE,
	`c_mota` VARCHAR,
	`c_trangthai` TINYINT,
	`c_image` MEDIUMBLOB,
	PRIMARY KEY(`id`)
);


CREATE TABLE `seats` (
	`id` INTEGER NOT NULL AUTO_INCREMENT UNIQUE,
	`s_ten` VARCHAR,
	`s_loai` VARCHAR,
	`s_dongia` DOUBLE,
	`s_trangthai` TINYINT,
	`s_room_id` INTEGER,
	PRIMARY KEY(`id`)
);


CREATE TABLE `rooms` (
	`id` INTEGER NOT NULL AUTO_INCREMENT UNIQUE,
	`r_ten` VARCHAR,
	`r_dientich` INTEGER,
	`r_tranthai` TINYINT,
	`r_image` MEDIUMBLOB,
	`r_theater_id` INTEGER,
	PRIMARY KEY(`id`)
);


CREATE TABLE `schedules` (
	`id` INTEGER NOT NULL AUTO_INCREMENT UNIQUE,
	`sc_ngaychieu` DATE,
	`sc_movie_id` INTEGER,
	`sc_theater_id` INTEGER,
	PRIMARY KEY(`id`)
);


CREATE TABLE `theaters` (
	`id` INTEGER NOT NULL AUTO_INCREMENT UNIQUE,
	`th_ten` VARCHAR,
	`th_diachi` VARCHAR,
	`th_mota` VARCHAR,
	`th_trangthai` TINYINT,
	PRIMARY KEY(`id`)
);


CREATE TABLE `invoices` (
	`id` INTEGER NOT NULL AUTO_INCREMENT UNIQUE,
	`i_ngaygiotao` DATETIME,
	`i_trangthai` TINYINT,
	`i_tongtien` DOUBLE,
	`i_movie_id` INTEGER,
	`i_schedule_time_id` INTEGER,
	`i_schedule_id` INTEGER,
	`i_room_id` INTEGER,
	`i_theater_id` INTEGER,
	`i_user_id` INTEGER,
	PRIMARY KEY(`id`)
);


CREATE TABLE `invoice_details` (
	`id` INTEGER NOT NULL AUTO_INCREMENT UNIQUE,
	`i_seat_id` INTEGER,
	`i_concesstion_id` INTEGER,
	`i_promotion_id` INTEGER,
	`i_invoice_id` INTEGER,
	PRIMARY KEY(`id`)
);


CREATE TABLE `roles` (
	`id` INTEGER NOT NULL AUTO_INCREMENT UNIQUE,
	`r_ten` VARCHAR,
	PRIMARY KEY(`id`)
);


CREATE TABLE `user_roles` (
	`id` INTEGER NOT NULL AUTO_INCREMENT UNIQUE,
	`user_id` INTEGER,
	`role_id` INTEGER,
	PRIMARY KEY(`id`)
);


CREATE TABLE `promotions` (
	`id` INTEGER NOT NULL AUTO_INCREMENT UNIQUE,
	`p_ten` VARCHAR,
	`p_giatri` FLOAT,
	`p_mota` VARCHAR,
	`p_trangthai` TINYINT,
	`p_seat_id` INTEGER,
	`p_concession_id` INTEGER,
	PRIMARY KEY(`id`)
);


CREATE TABLE `schedule_time` (
	`id` INTEGER NOT NULL AUTO_INCREMENT UNIQUE,
	`sc_giochieu` TIME,
	`sc_room_id` INTEGER,
	`sc_schedule_id` INTEGER,
	PRIMARY KEY(`id`)
);


ALTER TABLE `user_roles`
ADD FOREIGN KEY(`role_id`) REFERENCES `roles`(`id`)
ON UPDATE NO ACTION ON DELETE CASCADE;
ALTER TABLE `invoice_details`
ADD FOREIGN KEY(`i_invoice_id`) REFERENCES `invoices`(`id`)
ON UPDATE NO ACTION ON DELETE CASCADE;
ALTER TABLE `schedule_time`
ADD FOREIGN KEY(`sc_schedule_id`) REFERENCES `schedules`(`id`)
ON UPDATE NO ACTION ON DELETE CASCADE;
ALTER TABLE `promotions`
ADD FOREIGN KEY(`id`) REFERENCES `invoice_details`(`i_promotion_id`)
ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE `concessions`
ADD FOREIGN KEY(`id`) REFERENCES `invoice_details`(`i_concesstion_id`)
ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE `seats`
ADD FOREIGN KEY(`id`) REFERENCES `invoice_details`(`i_seat_id`)
ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE `invoices`
ADD FOREIGN KEY(`i_user_id`) REFERENCES `users`(`id`)
ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE `invoices`
ADD FOREIGN KEY(`i_theater_id`) REFERENCES `theaters`(`id`)
ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE `invoices`
ADD FOREIGN KEY(`i_room_id`) REFERENCES `rooms`(`id`)
ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE `invoices`
ADD FOREIGN KEY(`i_schedule_id`) REFERENCES `schedules`(`id`)
ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE `invoices`
ADD FOREIGN KEY(`i_schedule_time_id`) REFERENCES `schedule_time`(`id`)
ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE `invoices`
ADD FOREIGN KEY(`i_movie_id`) REFERENCES `movies`(`id`)
ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE `schedules`
ADD FOREIGN KEY(`sc_movie_id`) REFERENCES `movies`(`id`)
ON UPDATE NO ACTION ON DELETE CASCADE;
ALTER TABLE `seats`
ADD FOREIGN KEY(`s_room_id`) REFERENCES `rooms`(`id`)
ON UPDATE NO ACTION ON DELETE CASCADE;
ALTER TABLE `promotions`
ADD FOREIGN KEY(`p_seat_id`) REFERENCES `seats`(`id`)
ON UPDATE NO ACTION ON DELETE CASCADE;
ALTER TABLE `promotions`
ADD FOREIGN KEY(`p_concession_id`) REFERENCES `concessions`(`id`)
ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE `user_roles`
ADD FOREIGN KEY(`user_id`) REFERENCES `users`(`id`)
ON UPDATE NO ACTION ON DELETE CASCADE;
ALTER TABLE `schedule_time`
ADD FOREIGN KEY(`sc_room_id`) REFERENCES `rooms`(`id`)
ON UPDATE NO ACTION ON DELETE CASCADE;
ALTER TABLE `schedules`
ADD FOREIGN KEY(`sc_theater_id`) REFERENCES `theaters`(`id`)
ON UPDATE NO ACTION ON DELETE CASCADE;