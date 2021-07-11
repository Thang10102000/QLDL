-- question 1 Tạo store để người dùng nhập vào tên phòng ban và in ra tất cả các account thuộc phòng ban đó
delimiter $$ 
drop procedure if exists question1;
create procedure question1 (in dName varchar(50) )
begin 
 declare Did int;
 select departmentID into Did from department where departmentName like dName;
 select * from account where departmentID = Did ;
  end $$
  delimiter ;
  call question1( 'phòng giám đốc');
  
  
  -- question 2  Tạo store để in ra số lượng account trong mỗi group
  delimiter $$
drop procedure if exists question2;
create procedure question2()
begin
    select count(AccountID) as 'số lượng', G.GroupID, G.GroupName
    from `Group` G
             join GroupAccount GA on G.GroupID = GA.GroupID
    group by G.GroupID;
end $$
delimiter ;
call question2();


-- question 3 Tạo store để thống kê mỗi type question có bao nhiêu question được tạo trong tháng hiện tại
delimiter &&
drop procedure if exists question3 ;
create procedure question3()
begin 
select count(questionID) as 'số lượng trong tháng',  TQ.typeID, TypeName 
from typequestion TQ
join question Q
on Q.typeID = TQ.typeID 
where month(createDate) = month(now())
and year(createDate) = year(now()) 
group by TQ.typeID ;
end &&
delimiter ;
call question3() ;


-- question 4 Tạo store để trả ra id của type question có nhiều câu hỏi nhất
Create or replace view v_question as (
select Q.typeID,typeName, count(*) as `question_number` from question Q
 join typequestion TQ
 on Q.TypeID=TQ.TypeID
 group by TypeID
 order by `question_number` desc );
 
 
delimiter $$
drop procedure if exists question4;
create procedure question4(out a int)
begin 
select typeID into a from v_question where question_number = (Select max(question_number) from v_question);
end $$
delimiter ;
call question4(@a);
select @a; 

-- question 5 Sử dụng store ở question 4 để tìm ra tên của type question
set @a5=0;
call question4(@a5)  ;
select @a5;
select typeName 
from Typequestion 
where TypeID = @a5 ;


-- question 6: Viết 1 store cho phép người dùng nhập vào 1 chuỗi và trả về group có tên
-- chứa chuỗi của người dùng nhập vào hoặc trả về user có username chứa chuỗi của người dùng nhập vào
delimiter $$
drop procedure if exists question6;
create procedure question6(in chuoi char(50))
begin
select GroupID, GroupName 
from `group` 
where groupName like concat('%',chuoi,'%')
union 
select AccountID, Fullname 
from `account`
where UserName like concat('%',chuoi,'%');
end $$ 
delimiter ;
call question6('a');


-- question 7
delimiter $$
drop procedure if exists question7;
create procedure question7(in full_name char(50), in email2 char(50) )
begin 
declare user_name char(50);
declare position_ID int;
 select A.positionID into position_ID from `account` A
 join position P on A.positionID=P.positionID where positionName like '%dev%' limit 1;
set user_name = substring_index(email2, '@', 1);
insert into `Account`(email, username, fullname,  PositionID, createdate)
    values (email2, user_name, full_name,  position_ID, createdate);
    select 'ban da them thanh cong ';
end $$
delimiter ;
 call question7('phung minh triu','triii@gamil.com');


-- question 8 Viết 1 store cho phép người dùng nhập vào Essay,Multiple-Choice để thống kê câu hỏi nào có content dài nhất
delimiter $$
drop procedure if exists question8;
create procedure question8(in nhap enum ('essay','multiple-choice'))
begin
    declare max_length int;
    set max_length = (select max(character_length(Content))
                      from Question
                               join TypeQuestion TQ on Question.TypeID = TQ.TypeID
                      where TypeName like concat('%', nhap, '%'));
    select character_length(Content) as DoDaiContent,
           TypeName,QuestionID,Content 
    from Question Q
             join TypeQuestion T on Q.TypeID = Q.TypeID
    where TypeName like concat('%',nhap, '%')
      and character_length(Content) = max_length;
end $$
delimiter ;
call question8('multiple-choice');
call question8('essay');


-- question 9 Viết 1 store cho phép người dùng xóa exam dựa vào ID
drop procedure if exists question9;
delimiter $$
create procedure question9( in ID int)
begin
delete from exam where ExamID=ID ;
end $$
delimiter ;
call question9(1);


-- question 10 
select count(examID) as 'số lượng exam xóa ' from exam where timestampdiff(year,createDate,now())>3; 
set @a= (select examID from exam where timestampdiff(year,createDate,now())>3); 
select timestampdiff(year,createDate,now()) from exam  ;
call question9(@a);


-- question 11 
drop procedure if exists question11;
delimiter $$
create procedure question11( in dName char(50))
begin
declare dID int ;
select departmentID into dID from department where DepartmentName=dName limit 1;
if(dName is not null) then 
    update account set DepartmentID=null where DepartmentID= dID ;
    delete from department where DepartmentID = dID;
    end if;
end $$
delimiter ;
call question11('phòng sale');


-- question 12 Viết store để in ra mỗi tháng có bao nhiêu câu hỏi được tạo trong năm nay
create or replace view question12 as
select month(createDate) as 'questionMonth' from question where year(createDate)=year(now());
 select questionMonth as thang,year(now()) as 'năm', count(*) as 'so luong cau hoi' from question12 group by questionMonth order by questionMonth ;


-- question 13 
with CTE_question6 as (
 select month(date_sub(now(),interval 5 month )) as month,
        year (date_sub(now(),interval 5 month )) as `year`
union 
select month(date_sub(now(),interval 4 month )) as month,
        year (date_sub(now(),interval 4 month )) as `year`
union
select month(date_sub(now(),interval 3 month )) as month,
        year (date_sub(now(),interval 3 month )) as `year`
union 
select month(date_sub(now(),interval 2 month )) as month,
        year (date_sub(now(),interval 2 month )) as `year`
union
select month(date_sub(now(),interval 1 month )) as month,
        year (date_sub(now(),interval 1 month )) as `year`
union
select month(now()) as month , year(now()) as `year`
)








