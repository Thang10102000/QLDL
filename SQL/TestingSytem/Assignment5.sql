-- question 1 ----
drop view if exists PMT ;
create view PMT as
(
select accountID,fullname,departmentName
      from `account` A
       join department D 
        on A.DepartmentId = D.DepartmentID
        where DepartmentName ='phòng sale')
;    
 select *from PMT; 
 -- question 2 Tạo view có chứa thông tin các account tham gia vào nhiều group nhất
 with CTE_PMT2 as ( select A.accountID, fullName, count(GroupID) as num
                from `account` A
                 join groupaccount GA on A.AccountId = GA.AccountId
                  group by A.AccountId  ) ,
	maxVl as (select max(CTE_PMT2.num) from CTE_PMT2)
    select * from CTE_PMT2 where CTE_PMT2.num = ( select * from  maxVL )
    ;
    
    -- question 3  Tạo view có chứa câu hỏi có những content >300 và xóa nó đi
       with CTE_PMT3 as(  select * from question where character_length(Content) >10 )
       select * from CTE_PMT3 ;          
 
 -- question 4 Tạo view có chứa danh sách các phòng ban có nhiều nhân viên nhất
 with CTE_PMT4 as( select D.DepartmentID, DepartmentName, count(AccountID) as num4
					from department D
                     join `account` A
                      on D.DepartmentID = A.DepartmentId 
                      group by D.DepartmentId),
			 max4 as(select max(CTE_PMT4.num4) from CTE_PMT4)
 select * from CTE_PMT4 where CTE_PMT4.num4 = (select * from max4) ;
 
 
-- question 5 Tạo view có chứa tất các các câu hỏi do user họ Nguyễn tạo
 create or replace view CTE_PMT5 as
select A.FullName ,Q.QuestionID, Q.Content, Q.CategoryID, Q.TypeID, Q.CreatorID, Q.CreateDate
from Question Q
         join Account A on Q.CreatorID = A.AccountId
where A.FullName like 'Nguyễn%';
select * from CTE_PMT5;
 
 
 
 
 