drop table if exists books;

create table books(id int not null auto_increment, 
			bname varchar(50), 
			author varchar(50),
			price decimal(8,2), 
			primary key(id));

insert into books(bname , author, price) values ("Frankenstein", "Mary Shelley", 70);

insert into books(bname , author, price) values ("Pride and Prejudice", "Jane Austen", 420);

insert into books(bname , author, price) values ("Romeo and Juliet", "William Shakespeare", 21);
		