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
(5,  N'kỹ Thuật')
;

drop database if exists `Account`;
create table if not exists `Account`
(  `AccountID` tinyint primary key auto_increment,
`Email` varchar(50),
`UserName` varchar(20),
`FullName` varchar(20),
`DepartmentID` int,
`PositionID` int,
`createDate` date
);
insert into Account(AccountID,Email,UserName,FullName,DepartmentID,PositionID,createDate)
value
(1,'phunthang1@gmail.com','thang','phungminhthang1',1,1,1/1/2000),
(2,'phungthang2@gmail.com','thang2','phungminhthang2',2,2,2/2/2000),
(3,'phunthang3@gmail.com','thang3','phungminhthang3',3,3,3/3/2000),
(4,'phunthang4@gmail.com','thang4','phungminhthang4',4,4,4/4/2000),
(5,'phunthang5@gmail.com','thang5','phungminhthang5',5,5,5/5/2000);


drop database if exists `Group`;
create table if not exists `Group`
(  `GroupID` tinyint primary key auto_increment,
`GroupName` varchar(20),
`CreatorID` int,
`CreateDate` date
);
insert into `Group` (GroupID,GroupName,CreatorID,CreateDate)
value
(1,' Toan ',1,1/1/2000),
(2,' Ly ',2,2/2/2000),
(3,' Hoa',3,3/3/2000),
(4,'Anh',4,4/4/2000),
(5,'Dia',5,5/5/2000);


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
 `TypeID` tinyint,
`CreatorID` int,
`CreateDate` date ,
foreign key (`TypeID`) references  TypeQuestion (`TypeID`)
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
`isCorrect` bit
);
insert into Answer(Content,QuestionID,isCorrect)
value
(N'lý thuyết',1,1),
(N' ý nghĩa',2,1),
(N'vận dụng ',3,0),
(N'các sử dụng',4,0),
(N'lí do ',5,0);


drop database if exists `Exam`;
create table if not exists `Exam`(
 `ExamID` tinyint primary key auto_increment,
 `Code` char(20),
 `Title` varchar(20),
 `CategoryID` tinyint,
 `Duration` time,
 `CreatorID` int,
 `CreateDate` date,
  foreign key (CategoryID) references CategoryQuestion (CategoryID)
 );
 insert into Exam(`Code`,Title,CategoryID,Duration,CreatorID,CreateDate)
 value
 ('vt01',N'kiểm tra cuối kì 1',1,80,1,'2000-1-1'),
 ('vti01',N'kiểm tra giữa kì 1',2,60,'2000-2-2'),
 ('vti03',N'kiểm tra cuối kì 2',3,75,'2000-3-3'),
 ('vti04',N'kiểm tra giữa kì 2',4,85,4,'2000-4-4'),
 ('vti05',N'thi ',5,5,5,'2000-4-4');
 
 create table ExamQuestion(
 ExamID  int,
 QuestionID int
 
 );
 insert into ExamQuestion(ExamID,QuestionID)
 value
 (1,1),
(2,12),
(3,3),
(4,4),
(5,5);
 
 


     





