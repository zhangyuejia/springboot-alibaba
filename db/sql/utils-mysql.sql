
CREATE TABLE IF NOT EXISTS `INSERT_MSG` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(128) NOT NULL DEFAULT '',
  `TYPE` int(11) NOT NULL DEFAULT 0,
  `EXDATA` varchar(2000) NOT NULL DEFAULT '',
  `CREATE_TIME` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UPDATE_TIME` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `INSERT_MSG` ADD INDEX IDX_INSERT_MSG_TYPE ( `TYPE` );