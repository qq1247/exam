INSERT INTO `SYS_VER` VALUES (5, '1.1.5', '2019-12-16 23:16:00', 'ZHC', '');

drop table if exists SYS_CRON;

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

INSERT INTO `SYS_CRON` VALUES ('1', '�����ʱ����', 'com.wcpdoc.exam.file.job.ClearFileJob', '0 0 1 * * ?', '1', '1', '2019-09-13 19:31:07');
INSERT INTO `SYS_CRON` VALUES ('2', 'ǿ�ƽ���', 'com.wcpdoc.exam.core.job.ForcePaperJob', '0 0 1 * * ?', '1', '1', '2019-09-13 19:31:07');
