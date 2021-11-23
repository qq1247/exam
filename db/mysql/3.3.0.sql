/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2021/11/11 11:25:00                          */
/*==============================================================*/


drop table if exists EXM_BULLETIN;

drop table if exists EXM_EXAM;

drop table if exists EXM_EXAM_TYPE;

drop table if exists EXM_MY_EXAM;

drop table if exists EXM_MY_EXAM_DETAIL;

drop table if exists EXM_MY_MARK;

drop table if exists EXM_PAPER;

drop table if exists EXM_PAPER_QUESTION;

drop table if exists EXM_PAPER_QUESTION_ANSWER;

drop table if exists EXM_PAPER_REMARK;

drop table if exists EXM_PAPER_TYPE;

drop table if exists EXM_QUESTION;

drop table if exists EXM_QUESTION_ANSWER;

drop table if exists EXM_QUESTION_COMMENT;

drop table if exists EXM_QUESTION_OPTION;

drop table if exists EXM_QUESTION_TYPE;

drop table if exists EXM_QUESTION_TYPE_OPEN;

drop table if exists SYS_CRON;

drop table if exists SYS_DICT;

drop table if exists SYS_FILE;

drop table if exists SYS_ORG;

drop table if exists SYS_PARM;

drop table if exists SYS_SENSITIVE;

drop table if exists SYS_USER;

drop table if exists SYS_VER;

/*==============================================================*/
/* Table: EXM_BULLETIN                                          */
/*==============================================================*/
create table EXM_BULLETIN
(
   ID                   int not null auto_increment comment '����',
   TITLE                varchar(32) comment '����',
   IMG_FILE_ID          varchar(256) comment 'ͼƬ',
   VIDEO_FILE_ID        varchar(256) comment '��Ƶ',
   CONTENT              text comment '����',
   IMGS_HEIGHT          int comment 'ͼƬ��',
   IMGS_WIDTH           int comment 'ͼƬ��',
   URL                  varchar(128) comment '��ת����',
   SHOW_TYPE            int comment 'չʾ���ͣ�1��������2���ö���3���ֲ�����',
   NO                   int comment '����',
   STATE                int comment '0��ɾ����1��������2���ݸ�',
   UPDATE_TIME          datetime comment '�޸�ʱ��',
   UPDATE_USER_ID       int comment '�޸���',
   READ_USER_IDS        varchar(256) comment '�û���Ȩ��',
   READ_ORG_IDS         varchar(64) comment '������Ȩ��',
   primary key (ID)
);

alter table EXM_BULLETIN comment '����';

/*==============================================================*/
/* Table: EXM_EXAM                                              */
/*==============================================================*/
create table EXM_EXAM
(
   ID                   int not null auto_increment comment '����',
   NAME                 varchar(32) comment '����',
   START_TIME           datetime comment '���Կ�ʼ',
   END_TIME             datetime comment '���Խ���',
   MARK_START_TIME      datetime comment '�ľ�ʼ',
   MARK_END_TIME        datetime comment '�ľ����',
   MARK_STATE           datetime comment '�ľ�״̬��1��δ�ľ�2���ľ��У�3�����ľ���',
   SCORE_STATE          int comment '�ɼ�״̬��1��������2����������',
   RANK_STATE           int comment '����״̬��1��������2����������',
   LOGIN_TYPE           int comment '��½��ʽ��1�����ſ��ԣ�2�����½���ԣ�',
   DESCRIPTION          varchar(512) comment '����',
   CREATE_USER_ID       int comment '������',
   CREATE_TIME          datetime comment '����ʱ��',
   UPDATE_USER_ID       int comment '�޸���',
   UPDATE_TIME          datetime comment '�޸�ʱ��',
   STATE                int comment '״̬��0��ɾ����1��������2���ݸ壻3���鵵��',
   PAPER_ID             int comment '�Ծ�ID',
   EXAM_TYPE_ID         int comment '���Է���ID',
   primary key (ID)
);

alter table EXM_EXAM comment '����';

/*==============================================================*/
/* Table: EXM_EXAM_TYPE                                         */
/*==============================================================*/
create table EXM_EXAM_TYPE
(
   ID                   int not null auto_increment comment '����',
   NAME                 varchar(32) comment '����',
   IMG_ID               int comment '����ID',
   CREATE_USER_ID       int comment '������',
   CREATE_TIME          datetime comment '����ʱ��',
   UPDATE_USER_ID       int comment '�޸���',
   UPDATE_TIME          datetime comment '�޸�ʱ��',
   STATE                int comment '0��ɾ����1������',
   primary key (ID)
);

alter table EXM_EXAM_TYPE comment '���Է���';

/*==============================================================*/
/* Table: EXM_MY_EXAM                                           */
/*==============================================================*/
create table EXM_MY_EXAM
(
   ID                   int not null auto_increment comment 'id',
   EXAM_ID              int comment '����ID',
   USER_ID              int comment '�û�ID',
   ANSWER_START_TIME    datetime comment '����ʱ��',
   ANSWER_END_TIME      datetime comment '�������ʱ��',
   MARK_USER_ID         int comment '�ľ��û�',
   MARK_START_TIME      datetime comment '�ľ�ʱ��',
   MARK_END_TIME        datetime comment '�ľ����ʱ��',
   TOTAL_SCORE          decimal(5,2) comment '�ܷ���',
   STATE                int comment '1��δ���ԣ�2�������У�3���ѽ���4��ǿ�ƽ���',
   MARK_STATE           int comment '1��δ�ľ�2���ľ��У�3�����ľ�',
   ANSWER_STATE         int comment '1������2��������',
   UPDATE_USER_ID       int comment '�޸���',
   UPDATE_TIME          datetime comment '�޸�ʱ��',
   primary key (ID)
);

alter table EXM_MY_EXAM comment '�ҵĿ���';

/*==============================================================*/
/* Table: EXM_MY_EXAM_DETAIL                                    */
/*==============================================================*/
create table EXM_MY_EXAM_DETAIL
(
   ID                   int not null auto_increment comment 'id',
   MY_EXAM_ID           int comment '�����û�ID',
   EXAM_ID              int comment '����ID',
   USER_ID              int comment '�û�ID',
   QUESTION_ID          int comment '����ID',
   ANSWER_TIME          datetime comment '����ʱ��',
   MARK_USER_ID         int comment '�ľ��û�ID',
   MARK_TIME            datetime comment '�ľ�ʱ��',
   ANSWER               text comment '��',
   SCORE                decimal(5,2) comment '�÷�',
   QUESTION_SCORE       decimal(5,2) comment '�����ֵ',
   ANSWER_FILE_ID       int comment '�𰸸���ID',
   primary key (ID)
);

alter table EXM_MY_EXAM_DETAIL comment '�ҵĿ�����ϸ';

/*==============================================================*/
/* Table: EXM_MY_MARK                                           */
/*==============================================================*/
create table EXM_MY_MARK
(
   ID                   int not null auto_increment comment 'id',
   MARK_USER_ID         int comment '�ľ��û�ID',
   EXAM_USER_IDS        varchar(1024) comment '�����û�IDS',
   QUESTION_IDS         varchar(1024) comment '����IDS',
   UPDATE_USER_ID       int comment '�޸���',
   UPDATE_TIME          datetime comment '�޸�ʱ��',
   EXAM_ID              int comment '����ID',
   primary key (ID)
);

alter table EXM_MY_MARK comment '�ҵ��ľ�';

/*==============================================================*/
/* Table: EXM_PAPER                                             */
/*==============================================================*/
create table EXM_PAPER
(
   ID                   int not null auto_increment comment '����',
   NAME                 varchar(32) comment '����',
   PASS_SCORE           decimal(5,2) comment '���������%��',
   TOTAL_SCORE          decimal(5,2) comment '�ܷ���',
   MARK_TYPE            int comment '�ľ�ʽ��1�������ľ�2���˹��ľ��������ֵ䣺PAPER_MARK_TYPE',
   SHOW_TYPE            int comment 'չʾ��ʽ��1������չʾ��2���½���ʾ��3������չʾ���������ֵ䣺PAPER_SHOW_TYPE',
   READ_REMARK          text comment '��ǰ�Ķ�',
   READ_NUM             int comment '�Ķ�ʱ�����֣�',
   STATE                int comment '0��ɾ����1��������2���ݸ壻3���鵵',
   PAPER_TYPE_ID        int not null comment '�Ծ����ID',
   GEN_TYPE             int comment '���ʽ��1���˹����2��������',
   OPTIONS              varchar(32) comment '1����������2��ѡ������3�������Ҽ���4����ֹ���ƣ�5����С������',
   MINIMIZE_NUM         int comment '��С�����������OPTIONS=5��Ч��',
   CREATE_USER_ID       int comment '������',
   CREATE_TIME          datetime comment '����ʱ��',
   UPDATE_USER_ID       int comment '�޸���',
   UPDATE_TIME          datetime comment '�޸�ʱ��',
   primary key (ID)
);

alter table EXM_PAPER comment '�Ծ�';

/*==============================================================*/
/* Table: EXM_PAPER_QUESTION                                    */
/*==============================================================*/
create table EXM_PAPER_QUESTION
(
   ID                   int not null auto_increment comment 'id',
   NAME                 varchar(32) comment '�½�����',
   DESCRIPTION          varchar(512) comment '�½�����',
   PARENT_ID            int comment '��ID',
   PARENT_SUB           varchar(512) comment '���ӹ�ϵ����ʽ��_��ID_��ID_����ID_... ...��',
   UPDATE_USER_ID       int comment '�޸���',
   UPDATE_TIME          datetime comment '�޸�ʱ��',
   PAPER_ID             int comment '�Ծ�ID',
   QUESTION_ID          int comment '����ID',
   TYPE                 int comment '1���½ڣ�2���̶����⣻3���������',
   SCORE                decimal(5,2) comment '����',
   SCORE_OPTIONS        varchar(8) comment '1��©ѡ�÷֣�2������˳��3����Сд�����У�',
   NO                   int comment '����',
   primary key (ID)
);

alter table EXM_PAPER_QUESTION comment '�Ծ�����';

/*==============================================================*/
/* Table: EXM_PAPER_QUESTION_ANSWER                             */
/*==============================================================*/
create table EXM_PAPER_QUESTION_ANSWER
(
   ID                   int not null auto_increment,
   ANSWER               text comment 'һ�����ж��ͬ�����\n�ָ�',
   SCORE                decimal(5,2) comment '��ֵ',
   NO                   int comment '����',
   PAPER_ID             int comment '�Ծ�ID',
   QUESTION_ID          int not null comment '����ID',
   PAPER_QUESTION_ID    int comment '�Ծ�����ID',
   primary key (ID)
);

alter table EXM_PAPER_QUESTION_ANSWER comment '�Ծ������';

/*==============================================================*/
/* Table: EXM_PAPER_REMARK                                      */
/*==============================================================*/
create table EXM_PAPER_REMARK
(
   ID                   int not null auto_increment comment '����',
   SCORE                decimal(5,2) comment '�������ٷֱȣ�',
   REMARK               varchar(32) comment '����',
   NO                   int comment '����',
   PAPER_ID             int not null comment '�Ծ�ID',
   primary key (ID)
);

alter table EXM_PAPER_REMARK comment '�Ծ�����';

/*==============================================================*/
/* Table: EXM_PAPER_TYPE                                        */
/*==============================================================*/
create table EXM_PAPER_TYPE
(
   ID                   int not null auto_increment comment 'id',
   NAME                 varchar(32) comment '����',
   IMG_ID               int comment '����ID',
   CREATE_USER_ID       int comment '������',
   CREATE_TIME          datetime comment '����ʱ��',
   UPDATE_USER_ID       int comment '�޸���',
   UPDATE_TIME          datetime comment '�޸�ʱ��',
   STATE                int comment '0��ɾ����1������',
   READ_USER_IDS        varchar(1024) comment '��Ȩ��',
   primary key (ID)
);

alter table EXM_PAPER_TYPE comment '�Ծ����';

/*==============================================================*/
/* Table: EXM_QUESTION                                          */
/*==============================================================*/
create table EXM_QUESTION
(
   ID                   int not null auto_increment,
   TYPE                 int comment '1����ѡ��2����ѡ��3����գ�4���жϣ�5���ʴ�',
   DIFFICULTY           int comment '1�����ף�2���򵥣�3�����У�4�����ѣ�5������',
   TITLE                text comment '���',
   ANALYSIS             text comment '����',
   STATE                int comment '״̬��0��ɾ����1��������2���ݸ壩',
   CREATE_USER_ID       int comment '������',
   CREATE_TIME          datetime comment '����ʱ��',
   UPDATE_USER_ID       int comment '�޸���',
   UPDATE_TIME          datetime comment '�޸�ʱ��',
   QUESTION_TYPE_ID     int comment '�������',
   SCORE                decimal(5,2) comment 'Ĭ�Ϸ�ֵ',
   AI                   int comment '�����ľ�1���ǣ�2���񣻣�',
   SCORE_OPTIONS        varchar(8) comment '��ֵѡ�1��©ѡ�÷֣�2������˳��3����Сд�����У�)',
   primary key (ID)
);

alter table EXM_QUESTION comment '����';

/*==============================================================*/
/* Table: EXM_QUESTION_ANSWER                                   */
/*==============================================================*/
create table EXM_QUESTION_ANSWER
(
   ID                   int not null auto_increment,
   ANSWER               text comment 'һ�����ж��ͬ�����\n�ָ�',
   SCORE                decimal(5,2) comment '��ֵ',
   NO                   int comment '����',
   QUESTION_ID          int not null comment '����ID',
   primary key (ID)
);

alter table EXM_QUESTION_ANSWER comment '�����';

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

/*==============================================================*/
/* Table: EXM_QUESTION_OPTION                                   */
/*==============================================================*/
create table EXM_QUESTION_OPTION
(
   ID                   int not null auto_increment,
   OPTIONS              text comment 'ѡ�option�ǹؼ��֣�',
   NO                   int comment '����',
   QUESTION_ID          int not null comment '����ID',
   primary key (ID)
);

alter table EXM_QUESTION_OPTION comment '����ѡ��';

/*==============================================================*/
/* Table: EXM_QUESTION_TYPE                                     */
/*==============================================================*/
create table EXM_QUESTION_TYPE
(
   ID                   int not null auto_increment comment 'id',
   NAME                 varchar(32) comment '����',
   IMG_ID               int comment '����ID',
   CREATE_USER_ID       int comment '������',
   CREATE_TIME          datetime comment '����ʱ��',
   UPDATE_USER_ID       int comment '�޸���',
   UPDATE_TIME          datetime comment '�޸�ʱ��',
   STATE                int comment '0��ɾ����1������',
   WRITE_USER_IDS       varchar(1024) comment 'дȨ��',
   primary key (ID)
);

alter table EXM_QUESTION_TYPE comment '�������';

/*==============================================================*/
/* Table: EXM_QUESTION_TYPE_OPEN                                */
/*==============================================================*/
create table EXM_QUESTION_TYPE_OPEN
(
   ID                   int not null auto_increment comment 'id',
   START_TIME           datetime comment '��ʼʱ��',
   END_TIME             datetime comment '����ʱ��',
   USER_IDS             varchar(1024) comment '�����û�',
   ORG_IDS              varchar(256) comment '���Ż���',
   UPDATE_USER_ID       int comment '�޸���',
   UPDATE_TIME          datetime comment '�޸�ʱ��',
   STATE                int comment '0��ɾ����1������',
   QUESTION_TYPE_ID     int comment '�������ID',
   COMMENT_STATE        int comment '����״̬(0���رգ�1��ֻ����2���ɱ༭,)',
   primary key (ID)
);

alter table EXM_QUESTION_TYPE_OPEN comment '������࿪��';

/*==============================================================*/
/* Table: SYS_CRON                                              */
/*==============================================================*/
create table SYS_CRON
(
   ID                   int not null auto_increment comment '����',
   NAME                 varchar(32) comment '����',
   JOB_CLASS            varchar(64) comment 'ʵ����',
   CRON                 varchar(64) comment 'cron���ʽ',
   STATE                int comment '1��������2��ֹͣ��',
   UPDATE_USER_ID       int comment '�޸���',
   UPDATE_TIME          datetime comment '�޸�ʱ��',
   primary key (ID)
);

alter table SYS_CRON comment '��ʱ����';

/*==============================================================*/
/* Table: SYS_DICT                                              */
/*==============================================================*/
create table SYS_DICT
(
   ID                   int not null auto_increment,
   DICT_INDEX           varchar(32) comment '����',
   DICT_KEY             varchar(32) comment '��',
   DICT_VALUE           varchar(32) comment 'ֵ',
   NO                   int comment '����',
   primary key (ID)
);

alter table SYS_DICT comment '�����ֵ�';

/*==============================================================*/
/* Table: SYS_FILE                                              */
/*==============================================================*/
create table SYS_FILE
(
   ID                   int not null auto_increment,
   NAME                 varchar(64) comment 'ǰ׺',
   EXT_NAME             varchar(32) comment '��׺',
   FILE_TYPE            varchar(128) comment '����',
   PATH                 varchar(64) comment '·��',
   IP                   varchar(16) comment '�ϴ�IP',
   STATE                int comment '0��ɾ����1������',
   UPDATE_USER_ID       int comment '������',
   UPDATE_TIME          datetime comment '����ʱ��',
   primary key (ID)
);

alter table SYS_FILE comment '����';

/*==============================================================*/
/* Table: SYS_ORG                                               */
/*==============================================================*/
create table SYS_ORG
(
   ID                   int not null auto_increment comment '����',
   NAME                 varchar(32) comment '����',
   CODE                 varchar(32) comment '����Ψһ',
   PARENT_ID            int comment '��ID',
   PARENT_IDS           varchar(128) comment '����IDS',
   LEVEL                int comment '����',
   UPDATE_USER_ID       int comment '�޸���',
   UPDATE_TIME          datetime comment '�޸�ʱ��',
   STATE                int comment '0��ɾ����1��������',
   NO                   int comment '����',
   primary key (ID)
);

alter table SYS_ORG comment '��֯����';

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
   FILE_UPLOAD_DIR      varchar(64) comment '�ϴ�Ŀ¼',
   DB_BAK_DIR           varchar(64) comment '���ݿⱸ��Ŀ¼',
   PWD_TYPE             int comment '�������ͣ�1�������2���̶���',
   PWD_VALUE            varchar(32) comment '�����ʼ��Ĭ��ֵ',
   primary key (ID)
);

alter table SYS_PARM comment '����';

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

/*==============================================================*/
/* Table: SYS_USER                                              */
/*==============================================================*/
create table SYS_USER
(
   ID                   int not null auto_increment comment '����',
   NAME                 varchar(16) comment '����',
   LOGIN_NAME           varchar(16) comment '��½�˺�',
   EMAIL                varchar(64) comment '����',
   PHONE                varchar(11) comment '�ֻ���',
   PWD                  varchar(32) comment '����',
   REGIST_TIME          datetime comment 'ע��ʱ��',
   LAST_LOGIN_TIME      datetime comment '����½ʱ��',
   ORG_ID               int comment '��֯����ID',
   ROLES                varchar(64) comment '��ɫ',
   TYPE                 int comment '���ͣ�1����ͨ�û���2������Ա��',
   STATE                int comment '״̬��0��ɾ����1��������2�����᣻��',
   UPDATE_USER_ID       int comment '�޸���',
   UPDATE_TIME          datetime comment '�޸�ʱ��',
   primary key (ID)
);

alter table SYS_USER comment '�û�';

/*==============================================================*/
/* Table: SYS_VER                                               */
/*==============================================================*/
create table SYS_VER
(
   ID                   int not null auto_increment comment 'id',
   VER                  varchar(16) comment '�汾',
   UPDATE_TIME          datetime comment '�޸�ʱ��',
   AUTHOR               varchar(16) comment '����',
   REMARK               text comment '��ע',
   primary key (ID)
);

alter table SYS_VER comment '�汾';


/*==============================================================*/
/* ����								*/
/*==============================================================*/

INSERT INTO `SYS_ORG` VALUES ('1', '��֯����', 'code', '0', '_1_', '1', '1', '2017-08-01 22:31:43', '1', '1');

INSERT INTO `SYS_USER` VALUES ('1', '����Ա', 'admin', null, null,'79nRuL+wDo42R5kPfXTR2A==', '2017-08-01 22:31:43', '2017-08-01 22:31:43', null, 'admin', '1', '2017-08-01 22:31:43', '1');

INSERT INTO `SYS_DICT` VALUES (1, 'STATE', '0', 'ɾ��', 1);
INSERT INTO `SYS_DICT` VALUES (2, 'STATE', '1', '����', 2);
INSERT INTO `SYS_DICT` VALUES (3, 'STATE', '2', '����', 3);
INSERT INTO `SYS_DICT` VALUES (4, 'STATE_YN', '1', '��', 1);
INSERT INTO `SYS_DICT` VALUES (5, 'STATE_YN', '2', '��', 2);
INSERT INTO `SYS_DICT` VALUES (6, 'CRON_TYPE', '1', '����', 1);
INSERT INTO `SYS_DICT` VALUES (7, 'CRON_TYPE', '2', 'ֹͣ', 2);
INSERT INTO `SYS_DICT` VALUES (8, 'QUESTION_TYPE', '1', '��ѡ', 1);
INSERT INTO `SYS_DICT` VALUES (9, 'QUESTION_TYPE', '2', '��ѡ', 2);
INSERT INTO `SYS_DICT` VALUES (10, 'QUESTION_TYPE', '3', '���', 3);
INSERT INTO `SYS_DICT` VALUES (11, 'QUESTION_TYPE', '4', '�ж�', 4);
INSERT INTO `SYS_DICT` VALUES (12, 'QUESTION_TYPE', '5', '�ʴ�', 5);
INSERT INTO `SYS_DICT` VALUES (13, 'QUESTION_DIFFICULTY', '1', '����', 1);
INSERT INTO `SYS_DICT` VALUES (14, 'QUESTION_DIFFICULTY', '2', '��', 2);
INSERT INTO `SYS_DICT` VALUES (15, 'QUESTION_DIFFICULTY', '3', '����', 3);
INSERT INTO `SYS_DICT` VALUES (16, 'QUESTION_DIFFICULTY', '4', '����', 4);
INSERT INTO `SYS_DICT` VALUES (17, 'QUESTION_DIFFICULTY', '5', '����', 5);
INSERT INTO `SYS_DICT` VALUES (18, 'QUESTION_OPTIONS', '1', 'A', 1);
INSERT INTO `SYS_DICT` VALUES (19, 'QUESTION_OPTIONS', '2', 'B', 2);
INSERT INTO `SYS_DICT` VALUES (20, 'QUESTION_OPTIONS', '3', 'C', 3);
INSERT INTO `SYS_DICT` VALUES (21, 'QUESTION_OPTIONS', '4', 'D', 4);
INSERT INTO `SYS_DICT` VALUES (22, 'QUESTION_OPTIONS', '5', 'E', 5);
INSERT INTO `SYS_DICT` VALUES (23, 'QUESTION_OPTIONS', '6', 'F', 6);
INSERT INTO `SYS_DICT` VALUES (24, 'QUESTION_OPTIONS', '7', 'G', 7);
INSERT INTO `SYS_DICT` VALUES (25, 'MY_EXAM_STATE', '1', 'δ����', 1);
INSERT INTO `SYS_DICT` VALUES (26, 'MY_EXAM_STATE', '2', '������', 2);
INSERT INTO `SYS_DICT` VALUES (27, 'MY_EXAM_STATE', '3', '�ѽ���', 3);
INSERT INTO `SYS_DICT` VALUES (28, 'MY_EXAM_STATE', '4', 'ǿ�ƽ���', 4);
INSERT INTO `SYS_DICT` VALUES (29, 'MY_EXAM_MARK_STATE', '1', 'δ�ľ�', 1);
INSERT INTO `SYS_DICT` VALUES (30, 'MY_EXAM_MARK_STATE', '2', '�ľ���', 2);
INSERT INTO `SYS_DICT` VALUES (31, 'MY_EXAM_MARK_STATE', '3', '���ľ�', 3);
INSERT INTO `SYS_DICT` VALUES (32, 'MY_EXAM_ANSWER_STATE', '1', '����', 1);
INSERT INTO `SYS_DICT` VALUES (33, 'MY_EXAM_ANSWER_STATE', '2', '������', 2);
INSERT INTO `SYS_DICT` VALUES (34, 'EXAM_STATE', '0', 'ɾ��', 1);
INSERT INTO `SYS_DICT` VALUES (35, 'EXAM_STATE', '1', '����', 2);
INSERT INTO `SYS_DICT` VALUES (36, 'EXAM_STATE', '2', '�ݸ�', 3);
INSERT INTO `SYS_DICT` VALUES (37, 'STATE_OPEN', '1', '����', 1);
INSERT INTO `SYS_DICT` VALUES (38, 'STATE_OPEN', '2', '����', 2);
INSERT INTO `SYS_DICT` VALUES (39, 'STATE_OPEN', '0', 'ɾ��', 3);
INSERT INTO `SYS_DICT` VALUES (40, 'PAPER_MARK_TYPE', '1', '�����ľ�', 1);
INSERT INTO `SYS_DICT` VALUES (41, 'PAPER_MARK_TYPE', '2', '�˹��ľ�', 2);

INSERT INTO `SYS_CRON` VALUES ('1', '������ʱ����', 'com.wcpdoc.file.job.ClearFileJob', '0 0 0 1/1 * ? ', '1', '1', '2020-08-26 18:42:08');
INSERT INTO `SYS_CRON` VALUES (2, '���ݿⱸ��', 'com.wcpdoc.quartz.job.DbBackJob', '0 0 0 1/1 * ? ', 1, 1, '2020-08-26 18:42:08');
INSERT INTO `SYS_CRON` VALUES (3, '�Զ��ľ�', 'com.wcpdoc.exam.core.job.AutoMarkJob', '0/1 * * * * ? *', 1, 1, '2020-08-26 18:42:08');

INSERT INTO `SYS_VER` VALUES (1, '1.0.0', '2017-09-07 15:06:00', 'zhanghc', '��ʼ�汾');
INSERT INTO `SYS_VER` VALUES (2, '1.1.0', '2018-11-27 22:47:00', 'zhanghc', '');
INSERT INTO `SYS_VER` VALUES (3, '1.1.1', '2019-02-23 15:35:21', 'zhanghc', '');
INSERT INTO `SYS_VER` VALUES (4, '1.1.2', '2019-03-03 13:20:00', 'zhanghc', '');
INSERT INTO `SYS_VER` VALUES (5, '1.1.3', '2019-08-14 18:49:00', 'zhanghc', '');
INSERT INTO `SYS_VER` VALUES (6, '1.1.4', '2019-09-05 09:58:00', 'zhanghc', '');
INSERT INTO `SYS_VER` VALUES (7, '1.1.5', '2019-12-16 23:16:00', 'zhanghc', '');
INSERT INTO `SYS_VER` VALUES (8, '2.0.0', '2020-10-15 00:00:00', 'zhanghc', '');
INSERT INTO `SYS_VER` VALUES (9, '3.0.0', '2020-08-06 13:58:00', 'zhanghc', '');
INSERT INTO `SYS_VER` VALUES (10, '3.1.0', '2020-09-30 13:58:00', 'zhanghc', '');
INSERT INTO `SYS_VER` VALUES (11, '3.2.0', '2020-10-31 16:43:00', 'zhanghc', '');

INSERT INTO `SYS_CRON` VALUES (2, '���ݿⱸ��', 'com.wcpdoc.quartz.job.DbBackJob', '0 0 0 1/1 * ? ', 1, 1, '2020-08-26 18:42:08');
INSERT INTO `SYS_CRON` VALUES (3, '�Զ��ľ�', 'com.wcpdoc.exam.core.job.AutoMarkJob', '0/1 * * * * ? *', 1, 1, '2020-08-26 18:42:08');


INSERT INTO `EXM_EXAM_TYPE` VALUES ('1', '���Է���', NULL, '1', '2021-11-11 09:20:44', '1', '2021-11-11 09:20:40', '1');
INSERT INTO `EXM_PAPER_TYPE` VALUES ('1', '�Ծ����', NULL, '1', '2021-11-11 09:21:07', '1', '2021-11-11 09:21:11', '1', '1');
INSERT INTO `EXM_QUESTION_TYPE` VALUES ('1', '�������', NULL, '1', '2021-11-11 09:21:51', '1', '2021-11-11 09:21:55', '1', NULL);