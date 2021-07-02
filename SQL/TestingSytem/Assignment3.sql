use `testing_01`;
-- ------------------------------------------------1.thêm các record ---------------------------------------------------------
-- Account-----------
insert  into account(Email,UserName,FullName,DepartmentID,PositionID,createDate)
values('long@gamil.com','long','đặng hoàng long',6,6,'2019-12-20'),
('duc@gamil.com','duc','đặng hoàng đức',7,7,'2018-10-20'),
('hoang@gamil.com','hoang','đặng hoàng hoàng',8,8,'2018-10-5'),
('dao@gamil.com','hao','đặng hoàng Đào',9,9,'2017-10-20'),
('thao@gamil.com','duc','đặng hoàng thạo',10,10,'2001-10-20') ; 

-- Answer-------------
insert into answer(Content,questionID,isCorrect)
values  ('câu hỏi',6,1),
        ('câu hỏi',7,0),
        ('câu trả lời ',8,1),
        ('câu nghi vấn ',9,1),
        ('câu sai',10,0);
        
-- CategoryQuestion-------
insert into categoryquestion(categoryName)
value ('C++'),
	('C'),
    ('C#'),
    ('Python'),
    ('Ruby');

-- Department-----
insert into department(DepartmentNAME)
values ('tài chính'),
        ('phó giám đốc '),
        ('giám đốc '),
        ('thư kí '),
        ('bán hàng ');
        
-- Exam-----------
 insert into Exam(`Code`,Title,CategoryID,Duration,CreatorID,CreateDate)
 value
 ('vt06',N'kiểm tra cuối kì 1',1,85,1,'2020-1-1'),
 ('vti07',N'kiểm tra giữa kì 1',2,60,2,'1998-2-2'),
 ('vti08',N'kiểm tra cuối kì 2',3,70,3,'2018-3-3'),
 ('vti9',N'kiểm tra giữa kì 2',4 , 80 , 4 , '2017-4-4'),
 ('vti10',N'thi ',5,50,5,'2021-4-4');
 
 -- Group--------------
 insert into `Group` (GroupName,CreatorID,CreateDate)
values
(' sử ',6,'2021-1-1'),
(' công dân ',7,'2020-2-2'),
(' sinh học ',8,'2019-3-3'),
('thể dục ',9,'2017-4-4'),
('quốc phòng ',10,'2016-5-5');
 
 -- GroupAccount-----
 insert into GroupAccount(groupID,AccountID,JoinDate)
value
(6,6,'2021-1-1'),
(7,7,'2019-2-2'),
(8,8,'2017-3-3'),
(9,9,'2018-4-4'),
(10,10,'2020-5-5');
 
 -- question--------
 insert into Question(questionID,Content,CategoryID,TypeID,CreatorID,CreateDate)
value
(6,N'lý thuyết',6,6,6,'2021-1-1'),
(7,N' ý nghĩa',7,7,7,'2011-2-2'),
(8,N'vận dụng ',8,8,8,'2019-3-3'),
(9,N'các sử dụng',9,9,9,'2020-4-4'),
(10,N'lí do ',10,10,10,'2017-5-5');

-- typequestion----
insert into TypeQuestion(Typename)
value
(N'Trắc nghiệm 2'),
(N'Tự luận 2'),
(N'ôn tập 2'),
(N'Bài tập 2'),
(N'lời giải 2');
 
 
-- 2.Lấy ra tất cả các phòng ban -------------------------------------------------------------------------------------
select distinct * from department ;


 -- 3.lấy ra id của phòng ban "Sale"-----------------------------------------------------------------------------------
select departmentID from department where departmentName='sale';


 -- 4.lấy ra thông tin account có full name dài nhất-------------------------------------------------------------------
select *from `account` where character_length(fullname)=(select max(character_length(fullname)) from `account`);


-- 5.Lấy ra thông tin account có full name dài nhất và thuộc phòng ban có id= 3------------------------------------------
select *from `account` where (character_length(fullname)=(select max(character_length(fullname)) from `account`)) and( departmentID =3);


--  6.Lấy ra tên group đã tham gia trước ngày 20/12/2019-------------------------------------------------------------------


-- 7.Lấy ra ID của question có >= 4 câu trả lời---------------------------------------------------------------------------
select questionID from question where TypeID >=4;


-- -8.Lấy ra các mã đề thi có thời gian thi >= 60 phút và được tạo trước ngày 20/12/2019---------------------------------------
select ExamID from Exam where ((ExamID>=60) and(createDate >='2019-12-20') );


-- 9.Lấy ra 5 group được tạo gần đây nhất---------------------------------------------------------------------------------------
select  * from `Group` order by `GroupID` desc limit 5;


-- 10.Đếm số nhân viên thuộc department có id = 2---------------------------------------------------------------------------------
select char_length(departmentID) as 'số lượng nhân viên ' from department where departmentID=2 ;


-- 11. Lấy ra nhân viên có tên bắt đầu bằng chữ "D" và kết thúc bằng chữ "o"-------------------------------------------------------
select * from testing_01.account where (fullname like 'D%') and (fullname like '%O');


-- 12.Xóa tất cả các exam được tạo trước ngày 20/12/2019----------------------------------------------------------------------------
delete  from `exam` where CreateDate <='2019-12-20';


-- 13.Xóa tất cả các question có nội dung bắt đầu bằng từ "câu hỏi"------------------------------------------------------------------
delete from  `question` where Content like 'câu hỏi %';


-- 14.Update thông tin của account có id = 5 thành tên "Nguyễn Bá Lộc" và email thành loc.nguyenba@vti.com.vn--------------------------
update `account` set fullname=' Nguyễn Bá Lộc  ',email='loc.nguyenba@vti.com.vn' where accountID=5;


-- 15.update account có id = 5 sẽ thuộc group có id = 4------------------------------------------------------------------------------
update groupaccount set groupID=4 where AccountID =5;