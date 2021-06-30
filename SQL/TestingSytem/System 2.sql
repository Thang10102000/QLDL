drop database if exists `testing_01`;
Create database if not exists `testing_01`;

use `testing_01`;

drop database if exists `Department`;
create table if not exists `Department`
(  `DepartmentID` tinyint primary key auto_increment,
`DepartmentNAME` varchar(50)
);
insert into Department (DepartmentID,DepartmentName)
 value
 (1,  N'Marketing'),
 (2,  N'Sale'),
(3,  N'Bảo vệ '),
(4,  N'Nhân Sự '),
(5,  N'kỹ Thuật'),
(6,  N'Tài Chính '),
(7,  N'Phó Giám Đốc'),
 (8,  N'Giám Đốc'),
(9,  N'Thư kí '),
(10,  N'Bán hàng ');

drop database if exists `Account`;
create table if not exists `Account`
(  `AccountID` tinyint primary key auto_increment,
`Email` varchar(20),
`UserName` varchar(20),
`FullName` varchar(20),
`DepartmentID` int,
`PositionID` int,
`createDate` date
);
insert into Account(AccountID,Email,UserName,FullName,DepartmentID,PositionID,createDate)
value
(1,'phunthang1','thang','phungminhthang1',1,1,1/1/2000),
(2,'phungthang2','thang2','phungminhthang2',2,2,2/2/2000),
(3,'phunthang3','thang3','phungminhthang3',3,3,3/3/2000),
(4,'phunthang4','thang4','phungminhthang4',4,4,4/4/2000),
(5,'phunthang5','thang5','phungminhthang5',5,5,5/5/2000);


drop database if exists `Group`;
create table if not exists `Group`
(  `GroupID` tinyint primary key auto_increment,
`GroupName` varchar(20),
`CreatorID` int,
`CreateDate` date
);
insert into Group  (GroupID,GroupName,CreatorID,CreateDate)
value
(1,' Toan ',1,1/1/2000),
(2,' Ly ',2,2/2/2000),
(3,' hoa',3,3/3/2000),
(4,'Anh',4,4/4/2000),
(5,'Dia',5,5/5/2000)
;


create table GroupAccount(
GroupID int,
AccountID int,
JoinDate date
);
insert into GroupAccount(GroupID,AccountID,JoinDate)
value
(1,1,1/1/2000),
(2,2,2/2/2000),
(3,3,3/3/2000),
(4,4,4/4/2000),
(5,5,5/5/2000);


drop database if exists `TypeQuestion`;
create table if not exists `TypeQuestion`(
`TypeID` tinyint primary key auto_increment,
`TypeName` varchar(20)
);
insert into TypeQuestion(TypeID,Typename)
value
(1,N'Trắc nghiệm '),
(2,N'Tự luận'),
(3,N'ôn tập'),
(4,N'Bài tập'),
(5,N'lời giải');


drop database if exists `CategoryQuestion`;
create table if not exists`CategoryQuestion`(
`CategoryID` tinyint primary key auto_increment,
`CategoryName` varchar(20)
);
insert into CategoryQuestion(CategoryID,CategoryName)
value
(1, 'JAVA'),
(2,'SQL'),
(3,'.NET'),
(4,'PHP'),
(5,'CSS');


drop database if exists `Question`;
create table if not exists `Question`(
`QuestionID` tinyint primary key auto_increment,
`Content` varchar(20),
`CategoryID` int,
`TypeID` int,
`CreatorID` int,
`CreateDate` date
);
insert into Question(QuestionID,Content,CategoryID,TypeID,CreatorID,CreateDate)
value
(1,N'lý thuyết',1,1,1,1/1/2000),
(2,N' ý nghĩa',2,2,2,2/2/2000),
(3,N'vận dụng ',3,3,3,3/3/2000),
(4,N'các sử dụng',4,4,4,4/4/2000),
(5,N'lí do ',5,5,5,5/5/2000);


drop database if exists`Answer`;
create table if not exists`Answer`(
`AnswerID` tinyint primary key auto_increment,
`Content` varchar(20),
`QuestionID` int,
`isCorrect` boolean
);
insert into Answer(AnswerID,Content,QuestionID,isCorrect)
value
(1,N'lý thuyết',1,'yes'),
(2,N' ý nghĩa',2,'yes'),
(3,N'vận dụng ',3,'no'),
(4,N'các sử dụng',4,'no'),
(5,N'lí do ',5,'no');


drop database if exists `Exam`;
create table if not exists `Exam`(
 `ExamID` tinyint primary key auto_increment,
 `Code` int,
 `Title` varchar(20),
 `CategoryID` int,
 `Duration` time,
 `CreatorID` int,
 `CreateDate` date
 );
 insert into Exam(ExamID,Code,Title,CategoryID,Duration,CreatorID,CreateDate)
 value
 (1,1,N'kiểm tra cuối kì 1',1,1,1/1/2000),
 (2,2,N'kiểm tra giữa kì 1',2,2,2/2/2000),
 (3,3,N'kiểm tra cuối kì 2',3,3,3/3/2000),
 (4,4,N'kiểm tra giữa kì 2',4,4,4/4/2000),
 (5,5,N'thi ',5,5,5/5/2000);
 
 create table ExamQuestion(
 ExamID  int,
 QuestionID int
 );
 insert into ExamQuestion(ExamID,Question)
 value
 (1,N'lý thuyết'),
(2,N' ý nghĩa'),
(3,N'vận dụng '),
(4,N'các sử dụng'),
(5,N'lí do ');
 
 


     





