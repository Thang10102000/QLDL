use `testing_01`;

-- question 1----
select  DISTINCT A.*,D.departmentName
from  department D 
join  `account` A on D.DepartmentID=A.DepartmentID; 

-- question 2
 select * from `account` where createDate >'2010-12-20';
 
 -- question 3 Viết lệnh để lấy ra tất cả các developer
 select  P.positionName 
 from `position` P
 join `account` A
 on P.PositionID=A.PositionID;
 
 
 -- question 4 Viết lệnh để lấy ra danh sách các phòng ban có >3 nhân viên 
 select A.DepartmentID,D.DepartmentNAME,count(A.DepartmentID) as 'số lượng ' 
 from `account` A
 join `department` D 
 on A.DepartmentID=D.DepartmentID
 group by A.DepartmentID
 having count(A.DepartmentID) >1;
 

-- question 5 Viết lệnh để lấy ra danh sách câu hỏi được sử dụng trong đề thi nhiều nhất
select  Q.QuestionID, count(ExamID) as 'số lượng '
from question Q
join examquestion E
on Q.QuestionID=E.QuestionID 
group by QuestionID
order by count(ExamID) desc limit 1;


-- question 6 Thông kê mỗi category Question được sử dụng trong bao nhiêu Question
select C.categoryID,categoryName, count(questionID) as 'số lần được sử dụng '
from categoryquestion C
join question Q
on C.CategoryID=Q.CategoryID
group by Q.CategoryID ;


-- question 7 Thông kê mỗi Question được sử dụng trong bao nhiêu Exam
select Q.questionID, Content, count(examID) as 'số exam  sử dụng '
from question Q 
left join examquestion EQ
on Q.QuestionID= EQ.QuestionID 
group by Q.QuestionID ;


-- question 8 Lấy ra Question có nhiều câu trả lời nhất
-- cách 1 : chỉ in ra được 1 giá trị lớn nhất 
select Q.QuestioniD, count(AnswerID) as 'nhiều câu trả lời nhất '
from question Q 
join answer A
on Q.QuestionID=A.QuestionID 
group by QuestionID 
order  by count(AnswerID) desc limit 1;

-- cách 2 : in ra đầy đủ các giá trị lớn nhất 
drop temporary table if exists Table8;
create temporary table Table8
select Q.QuestionID, count(AnswerID) as num
from Question Q
join Answer A 
on Q.QuestionID = A.QuestionID
group by Q.QuestionID;
set @maxNum = (select max(num)
                    from QuestionUsed);
select *
from Table8
where num = @maxNum;


-- question 9 Thống kê số lượng account trong mỗi group
select G.groupID, GroupName, count(AccountID) as 'số lượng'
from `group` G
left join groupaccount GA
on G.GroupID = GA.GroupID 
group by G.GroupID;


-- question 10 Tìm chức vụ có ít người nhất 
drop temporary table if exists table10 ;
create temporary table table10
select P.PositionID, PositionName, count(AccountID) as num10
from Position P
left join Account A
on P.PositionID = A.PositionID 
group by P.PositionID;
set @minNum =(select min(num10)
                             from table10 );
select * from table10
where  num10=@minNum ;

-- question 11 Thống kê mỗi phòng ban có bao nhiêu dev, test, scrum master, PM 
select D.departmentID, departmentName, count(AccountID)
from department D 
join account A on D.DepartmentId = A.DepartmentId
join position P on P.PositionID = a.PositionID 
where PositionName in ('dev','test','scrum master','PM') 
group by A.DepartmentId,PositionName;


-- question 12 Lấy thông tin chi tiết của câu hỏi bao gồm: thông tin cơ bản của question, loại câu hỏi, ai là người tạo ra câu hỏi, câu trả lời là gì, ...
select Q.questionID, Q.Content, Categoryname, TypeName, fullname as'người tạo ', isCorrect as' câu trả lời '
from question Q 
join Account A on Q.CreatorID = A.AccountID 
join categoryquestion CQ on Q.CategoryID = CQ.CategoryID
join typequestion TQ on Q.TypeID=TQ.TypeID
join answer An on Q.QuestionID = An.QuestionID 
 ;  

-- question 13 Lấy ra số lượng câu hỏi của mỗi loại tự luận hay trắc nghiệm
select TypeName, count(questionID) as ' số lượng'
from typequestion TP 
join question Q 
on TP.TypeID = Q.TypeID
where TypeName in('tự luận','trắc nghiệm')
group by TP.TypeID;

-- question 14 15 Lấy ra group không có account nào
select G.GroupID, GroupName
from `group` G 
left join groupaccount GA on G.GroupID = GA.GroupID 
where AccountID is null
group by G.GroupID ;  


-- question 16: Lấy ra question không có answer nào
select Q.questionID, Q.Content 
from question Q 
left join answer A on Q.QuestionID = A.QuestionID 
where AnswerID is null 
group by Q.QuestionID;

-- question 17 
select A.accountID, email, fullname 
from account A 
inner join groupaccount GA on A.AccountId = GA.AccountId 
where GroupID =1
union
 select A.accountID, email, fullname 
from account A 
inner join groupaccount GA on A.AccountId = GA.AccountId 
where GroupID =2 ;

-- question 18 
select G.GroupID, GroupName
from `group` G 
inner join `groupaccount` GA on G.GroupID = GA.GroupID
 group by G.GroupID
 having count(AccountID)>5 
 union
select G.GroupID, GroupName
from `group` G 
inner join `groupaccount` GA on G.GroupID = GA.GroupID
 group by G.GroupID
 having count(AccountID)<7 ; 




