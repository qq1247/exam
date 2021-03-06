INSERT INTO SYS_VER VALUES (20, '3.7.0', '2021-03-30 13:06:00', 'zhanghc', '');

UPDATE EXM_EXAM SET SCORE_STATE = 2, RANK_STATE = 2;

ALTER TABLE SYS_USER ADD COLUMN HEAD_FILE_ID int(11) DEFAULT NULL COMMENT '头像ID' AFTER UPDATE_TIME;

ALTER TABLE EXM_EXAM ADD COLUMN ANON_STATE int(255) DEFAULT NULL COMMENT '匿名阅卷状态（1：公开；2：不公开）' AFTER EXAM_TYPE_ID;

ALTER TABLE EXM_QUESTION CHANGE SCORE_OPTIONS AI_OPTIONS varchar(8) comment '1：漏选得分；2：答案无顺序；3：大小写不敏感；';

ALTER TABLE EXM_PAPER_QUESTION CHANGE SCORE_OPTIONS AI_OPTIONS varchar(8) comment '1：漏选得分；2：答案无顺序；3：大小写不敏感；';

ALTER TABLE EXM_PAPER_QUESTION_RULE CHANGE SCORE_OPTIONS AI_OPTIONS varchar(8) comment '1：漏选得分；2：答案无顺序；3：大小写不敏感；';


UPDATE SYS_DICT SET DICT_INDEX = 'QUESTION_AI_OPTIONS' WHERE ID = 50;
UPDATE SYS_DICT SET DICT_INDEX = 'QUESTION_AI_OPTIONS' WHERE ID = 51;
UPDATE SYS_DICT SET DICT_INDEX = 'QUESTION_AI_OPTIONS' WHERE ID = 52;