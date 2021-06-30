drop database if exists `testing_01`;
Create database if not exists `testing_01`;

use `testing_01`;

drop table if exists `Department`;
create table if not exists `Department`
(  `DepartmentID` tinyint primary key auto_increment,
`DepartmentNAME` varchar(50)
);

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
(TK01,phunthang1,thang1,phungminhthang1,1,1,10-10-2000),
(TK02,phungthang2,thang2,phungminhthang2,2,11-11-2000),
(TK03,phunthang3,thang3,phungminhthang3,3,12-12-2000),
(Tk04,phunthang4,thang4,phungminhthang4,4,1-1-2000),
(TK05,phunthang5,thang5,phungminhthang5,5,2-2-2000);


drop database if exists `Group`;
create table if not exists `Group`
(  `GroupID` tinyint primary key auto_increment,
`GroupName` varchar(20),
`CreatorID` int,
`CreateDate` date
);
create table GroupAccount(
GroupID int,
AccountID int,
JoinDate date
);

drop database if exists `TypeQuestion`;
create table if not exists `TypeQuestion`(
`TypeID` tinyint primary key auto_increment,
`TypeName` varchar(20)
);

drop database if exists `CategoryQuestion`;
create table if not exists`CategoryQuestion`(
`CategoryID` tinyint primary key auto_increment,
`CategoryName` varchar(20)
);

drop database if exists `Question`;
create table if not exists `Question`(
`QuestionID` tinyint primary key auto_increment,
`Content` varchar(20),
`CategoryID` int,
`TypeID` int,
`CreatorID` int,
`CreateDate` date
);

drop database if exists`Answer`;
create table if not exists`Answer`(
`AnswerID` tinyint primary key auto_increment,
`Content` varchar(20),
`QuestionID` int,
`isCorrect` boolean
);

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
 
 create table ExamQuestion(
 ExamID  int,
 QuestionID int
 );
 
 insert into Department(DepartmentID,DepartmentName)
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


     





