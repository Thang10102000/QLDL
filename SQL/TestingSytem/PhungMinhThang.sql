drop database if exists `exam`;
Create database if not exists `exam`;
use `exam`;


drop database if exists `Student`;
create table if not exists `Student`
(  `ID` tinyint primary key auto_increment,
`Name` varchar(50),
`Age` int ,
`Gender`  varchar(50)
);
insert into Student(`Name`,Age,Gender)
values('thắng','20','male'),
	   ('long',21,'male'),
       ('ngoc',18,'female');




drop database if exists `Subject`;
create table if not exists `Subject`
(  `ID` tinyint primary key auto_increment,
`Name` varchar(50)
);
insert into Subject(`Name`)
values ('toán'),('lý'),('hóa') ;




drop database if exists `StudentSubject`;
create table if not exists `StudentSubject`
(  `StudentID` tinyint primary key auto_increment,
`SubjectID` tinyint primary key auto_increment ,
`Mark` int,
`Date`  datetime,
constraint  primary key (StudentID,SubjectID) 
) ; 

insert into StudentSubject(Mark,`Date`)
values (9,'200-1-1'), 
        (8,'200-5-1'),
      (4,'200-7-12');



-- question 2  a,Lấy tất cả các môn học không có bất kì điểm nào
select ID, `Name`,Mark
from subject  SJ
join studentsubject STJ on SJ.ID=STJ.SubjectID group by ID having Mark is null;

-- b,Lấy danh sách các môn học có ít nhất 2 điểm
select ID, `Name`,Mark
from subject  SJ
join studentsubject STJ on SJ.ID=STJ.SubjectID group by ID having Mark>=2 ;





-- question 3 

drop view if exists SinhVienInfo;
create  view SinhVienInfo as
(select S.ID, SJ.ID as SubjectID, S.`Name` as NameStudent, Age, Gender, SJ.`Name`, Mark, `Date` 
from Student S 
         left join`subject` SJ on S.ID = SJ.ID 
         left join `studentsubject` STB on S.ID = STB.StudentID ) ; 


update student set `Gender`='male' where `gender`= 0 ; 
update student set `Gender`='female' where`gender`= 1 ;
update student set Gender='unknwon' where gender is null;

select * from SinhVienInfo ;



-- question 4 
drop trigger if exists  SubjectUpdateID;
delimiter $$
create trigger SubjectUpdateID
before update on `subject` 
for each row
begin
update studentsubject set ID =new.ID ;
end $$
 delimiter ;
 UPDATE `subject`  SET `ID` = 8 WHERE (`ID` = 7);


drop trigger if exists  StudentDeleteID;
delimiter $$
create trigger StudentDeleteID
before delete on `student` 
for each row
begin
delete from studentsubject where StudentID= old.ID ;
end $$
 delimiter ;
delete from `student` where ID= 2 ;


-- question 5 
delimiter $$
drop procedure if exists DeleteStudent ;
create procedure  DeleteStudent(in dName varchar(50)) 
begin 
	if (dName like '*')
    then delete from `student` ;
    else delete from `student` where `name` like dname ;
    end if;
    
    if (dName like '*')
    then delete from `studentsubject` ;
    else delete from `studentsubject` where StudentID = (select ID from `student` where `Name` like dname);
    end if;
end $$
delimiter ;
call DeleteStudent('thang'); 
begin work ;
call DeleteStudent('*');
rollback ;
select * from student;
select * from studentsubject; 




































