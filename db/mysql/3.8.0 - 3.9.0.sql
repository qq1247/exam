INSERT INTO SYS_VER VALUES (21, '3.8.0', '2021-05-13 16:34:00', 'zhanghc', '');

ALTER TABLE EXM_PAPER_QUESTION_ANSWER ADD COLUMN `EXAM_ID` int(11) DEFAULT NULL COMMENT '考试ID' AFTER `PAPER_QUESTION_ID`;
ALTER TABLE EXM_PAPER_QUESTION_ANSWER ADD COLUMN `USER_ID` int(11) DEFAULT NULL COMMENT '用户ID' AFTER `EXAM_ID`;
ALTER TABLE EXM_QUESTION_ANSWER ADD COLUMN `QUESTION_TYPE` int(11) DEFAULT NULL COMMENT '试题类型' AFTER `QUESTION_ID`;
ALTER TABLE EXM_QUESTION_ANSWER ADD COLUMN `QUESTION_AI` int(11) DEFAULT NULL COMMENT '试题智能类型' AFTER `QUESTION_TYPE`;
ALTER TABLE EXM_PAPER_QUESTION_ANSWER ADD COLUMN `QUESTION_TYPE` int(11) DEFAULT NULL COMMENT '试题类型' AFTER `QUESTION_ID`;
ALTER TABLE EXM_PAPER_QUESTION_ANSWER ADD COLUMN `QUESTION_AI` int(11) DEFAULT NULL COMMENT '试题智能类型' AFTER `QUESTION_TYPE`;

UPDATE EXM_PAPER_QUESTION_ANSWER PAPER_QUESTION_ANSWER 
	INNER JOIN EXM_PAPER_QUESTION PAPER_QUESTION ON  PAPER_QUESTION.ID = PAPER_QUESTION_ANSWER.PAPER_QUESTION_ID
	SET PAPER_QUESTION_ANSWER.EXAM_ID = PAPER_QUESTION.EXAM_ID, PAPER_QUESTION_ANSWER.USER_ID = PAPER_QUESTION.USER_ID;

UPDATE EXM_QUESTION_ANSWER QUESTION_ANSWER 
	INNER JOIN EXM_QUESTION QUESTION ON  QUESTION_ANSWER.QUESTION_ID = QUESTION.ID
	SET QUESTION_ANSWER.QUESTION_TYPE = QUESTION.TYPE, QUESTION_ANSWER.QUESTION_AI = QUESTION.AI;
	
UPDATE EXM_PAPER_QUESTION_ANSWER PAPER_QUESTION_ANSWER 
	INNER JOIN EXM_QUESTION QUESTION ON  PAPER_QUESTION_ANSWER.QUESTION_ID = QUESTION.ID
	SET PAPER_QUESTION_ANSWER.QUESTION_TYPE = QUESTION.TYPE, PAPER_QUESTION_ANSWER.QUESTION_AI = QUESTION.AI;

ALTER TABLE EXM_PAPER_QUESTION DROP COLUMN PAPER_QUESTION_RULE_ID;