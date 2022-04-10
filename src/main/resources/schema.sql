CREATE TABLE `RESPONSE_DATA`
(
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `url` VARCHAR2 NOT NULL,
    `status` INTEGER NOT NULL,
    `response_time` BIGINT NOT NULL,
    `created_at` BIGINT NOT NULL    
);

CREATE TABLE `AVERAGE_VIEW_MODEL`
(
    `id` BIGINT PRIMARY KEY,    
    `moving_average_response_time` BIGINT NOT NULL,    
    `window_size` INTEGER NOT NULL
);