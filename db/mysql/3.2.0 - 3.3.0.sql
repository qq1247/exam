ALTER TABLE `EXM_BULLETIN` ADD COLUMN `START_TIME` datetime DEFAULT NULL COMMENT '开始时间' AFTER `ID`;

ALTER TABLE `EXM_BULLETIN` ADD COLUMN `END_TIME` datetime DEFAULT NULL COMMENT '结束时间' AFTER `START_TIME`;

ALTER TABLE `EXM_BULLETIN` ADD COLUMN `SHOW_TYPE` int(11) DEFAULT NULL COMMENT '展示类型（1：正常；2：置顶；3：轮播；）' AFTER `URL`;

ALTER TABLE `EXM_BULLETIN` DROP COLUMN `TOP_STATE`;

ALTER TABLE `EXM_EXAM_TYPE` DROP COLUMN `RW_STATE`;

ALTER TABLE `EXM_EXAM_TYPE` DROP COLUMN `READ_USER_IDS`;

ALTER TABLE `EXM_EXAM_TYPE` DROP COLUMN `WRITE_USER_IDS`;


ALTER TABLE `EXM_PAPER_TYPE` DROP COLUMN `RW_STATE`;

ALTER TABLE `EXM_PAPER_TYPE` DROP COLUMN `WRITE_USER_IDS`;
ALTER TABLE `EXM_QUESTION` DROP COLUMN `VER`;

ALTER TABLE `EXM_QUESTION` DROP COLUMN `SRC_ID`;

ALTER TABLE `EXM_QUESTION` DROP COLUMN `NO`;

ALTER TABLE `EXM_QUESTION_TYPE` DROP COLUMN `RW_STATE`;

ALTER TABLE `EXM_QUESTION_TYPE` DROP COLUMN `READ_USER_IDS`;

ALTER TABLE `SYS_PARM` ADD COLUMN `FILE_UPLOAD_DIR` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '上传目录' AFTER `UPDATE_TIME`;

ALTER TABLE `SYS_PARM` ADD COLUMN `DB_BAK_DIR` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '数据库备份目录' AFTER `FILE_UPLOAD_DIR`;

ALTER TABLE `SYS_PARM` ADD COLUMN `PWD_TYPE` int(11) DEFAULT NULL COMMENT '密码类型（1：随机；2：固定）' AFTER `DB_BAK_DIR`;

ALTER TABLE `SYS_PARM` ADD COLUMN `PWD_VALUE` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '密码初始化默认值' AFTER `PWD_TYPE`;

ALTER TABLE `SYS_USER` ADD COLUMN `TYPE` int(11) DEFAULT NULL COMMENT '类型（1：普通用户；2：管理员）' AFTER `ROLES`;

INSERT INTO `SYS_VER` VALUES (12, '3.2.1', '2021-11-25 16:26:01', 'zhanghc', '');
INSERT INTO `SYS_VER` VALUES (13, '3.3.0', '2020-11-30 11:11:11', 'zhanghc', '');

INSERT INTO `SYS_DICT` VALUES (42, 'PAPER_SHOW_TYPE', '1', '整卷展示', 1);
INSERT INTO `SYS_DICT` VALUES (43, 'PAPER_SHOW_TYPE', '2', '章节显示', 2);
INSERT INTO `SYS_DICT` VALUES (44, 'PAPER_SHOW_TYPE', '3', '单题展示', 3);
INSERT INTO `SYS_DICT` VALUES (45, 'PAPER_GEN_TYPE', '1', '人工组卷', 1);
INSERT INTO `SYS_DICT` VALUES (46, 'PAPER_GEN_TYPE', '2', '随机组卷', 2);
INSERT INTO `SYS_DICT` VALUES (47, 'EXAM_STATE', '3', '归档', 4);
INSERT INTO `SYS_DICT` VALUES (48, 'BULLETIN_SHOW_TYPE', '1', '正常', 1);
INSERT INTO `SYS_DICT` VALUES (49, 'BULLETIN_SHOW_TYPE', '2', '置顶', 2);

INSERT INTO `SYS_PARM` VALUES (1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, '111111');

DELETE FROM `EXM_QUESTION_TYPE` WHERE ID = 1;
DELETE FROM `EXM_PAPER_TYPE` WHERE ID = 1;
DELETE FROM `EXM_EXAM_TYPE` WHERE ID = 1;