ALTER TABLE `exam_`.`exm_my_exam_detail` ADD COLUMN `ANSWER_FILE_ID` int(11) DEFAULT NULL COMMENT '�𰸸���ID' AFTER `QUESTION_SCORE`;

ALTER TABLE `exam_`.`exm_my_mark` MODIFY COLUMN `AUTO_STATE` int(11) DEFAULT NULL COMMENT '1�����Զ��ľ�2��δ�Զ��ľ�' AFTER `EXAM_ID`;

ALTER TABLE `exam_`.`exm_paper` MODIFY COLUMN `MINIMIZE_NUM` int(11) DEFAULT NULL COMMENT '��С���������' AFTER `OPTIONS`;

ALTER TABLE `exam_`.`exm_paper` MODIFY COLUMN `CREATE_USER_ID` int(11) DEFAULT NULL COMMENT '������' AFTER `MINIMIZE_NUM`;

ALTER TABLE `exam_`.`exm_question` ADD COLUMN `ANSWER` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '��' AFTER `TITLE`;

ALTER TABLE `exam_`.`exm_question` MODIFY COLUMN `CREATE_USER_ID` int(11) DEFAULT NULL COMMENT '������' AFTER `STATE`;

ALTER TABLE `exam_`.`exm_question` MODIFY COLUMN `SCORE_OPTIONS` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '1����԰�֣�2������˳��3����Сд�����У�4�������𰸵÷�' AFTER `SCORE`;

ALTER TABLE `exam_`.`exm_question` MODIFY COLUMN `AI` int(2) DEFAULT NULL COMMENT '0����1����' AFTER `SCORE_OPTIONS`;

drop table if exists SYS_SENSITIVE;

/*==============================================================*/
/* Table: SYS_SENSITIVE                                         */
/*==============================================================*/
create table SYS_SENSITIVE
(
   ID                   int not null auto_increment,
   WHITE_LIST           text comment '������',
   BLACK_LIST           text comment '������',
   UPDATE_USER_ID       int comment '�޸���',
   UPDATE_TIME          datetime comment '�޸�ʱ��',
   primary key (ID)
);

alter table SYS_SENSITIVE comment '���д�';

drop table if exists EXM_QUESTION_COMMENT;

/*==============================================================*/
/* Table: EXM_QUESTION_COMMENT                                  */
/*==============================================================*/
create table EXM_QUESTION_COMMENT
(
   ID                   int not null auto_increment comment '����',
   QUESTION_ID          int comment '����id',
   CONTENT              text comment '��������',
   PARENT_ID            int comment '��ID',
   PARENT_SUB           varchar(512) comment '���ӹ�ϵ����ʽ��_��ID_��ID_����ID_... ...��',
   LEVEL                int comment '����',
   STATE                int comment '״̬(0��ɾ����1������,)',
   CREATE_USER_ID       int comment '������',
   CREATE_TIME          datetime comment '����ʱ��',
   UPDATE_USER_ID       int comment '�޸���',
   UPDATE_TIME          datetime comment '�޸�ʱ��',
   primary key (ID)
);

alter table EXM_QUESTION_COMMENT comment '��������';

drop table if exists SYS_PARM;

/*==============================================================*/
/* Table: SYS_PARM                                              */
/*==============================================================*/
create table SYS_PARM
(
   ID                   int not null auto_increment,
   EMAIL_HOST           varchar(64) comment '�ʼ�����',
   EMAIL_USER_NAME      varchar(64) comment '�ʼ��û���',
   EMAIL_PWD            varchar(64) comment '�ʼ�����',
   EMAIL_PROTOCOL       varchar(16) comment '�ʼ�Э��',
   EMAIL_ENCODE         varchar(16) comment '�ʼ�����',
   ORG_LOGO             int comment '��λ�̱�',
   ORG_NAME             varchar(32) comment '��λ����',
   UPDATE_USER_ID       int comment '�޸���',
   UPDATE_TIME          datetime comment '�޸�ʱ��',
   primary key (ID)
);

alter table SYS_PARM comment '����';



INSERT INTO `SYS_DICT` VALUES (37, 'STATE_OPEN', '1', '����', 1);
INSERT INTO `SYS_DICT` VALUES (38, 'STATE_OPEN', '2', '����', 2);
INSERT INTO `SYS_DICT` VALUES (39, 'STATE_OPEN', '0', 'ɾ��', 3);

INSERT INTO `SYS_VER` VALUES (10, '3.1.0', '2020-09-30 13:58:00', 'zhanghc', '');