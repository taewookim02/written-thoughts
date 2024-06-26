-----------------------------
---------- DROP -------------
-----------------------------
DROP TABLE MEMBER CASCADE;
DROP TABLE LABEL CASCADE;
DROP TABLE ENTRY CASCADE;
drop table LIST cascade;
drop table LIST_ITEM cascade;

-----------------------------
--------- CREATE ------------
-----------------------------
CREATE TABLE MEMBER (
	MEMBER_NO SERIAL PRIMARY KEY,
	ID VARCHAR(100) NOT NULL,
	PWD VARCHAR(255) NOT NULL,
	NICK VARCHAR(30),
	DEL_YN CHAR(1) NOT NULL DEFAULT 'N',
	CREATED_DATE TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE LABEL (
	LABEL_NO SERIAL PRIMARY KEY,
	MEMBER_NO INTEGER not null,
	NAME VARCHAR(100) not null
);


create table ENTRY (
	ENTRY_NO SERIAL PRIMARY KEY,
	MEMBER_NO INTEGER NOT NULL,
	LABEL_NO INTEGER,
	TITLE VARCHAR(100) NOT NULL,
	CONTENT TEXT NOT NULL,
	PUBLIC_YN CHAR(1) NOT NULL DEFAULT 'N',
	CREATED_DATE TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

create table LIST (
	LIST_NO SERIAL primary key,
	MEMBER_NO INTEGER not null,
	TITLE VARCHAR(45)
);

create table LIST_ITEM (
	LIST_ITEM_NO SERIAL primary key,
	LIST_NO INTEGER not null,
	content VARCHAR(250) 
);



-----------------------------
--------- ALTER -------------
-----------------------------

alter table label
add constraint FK_LABEL_MEMBER
foreign key (MEMBER_NO) REFERENCES member (MEMBER_NO);

alter table entry 
add constraint FK_ENTRY_MEMBER
foreign key (MEMBER_NO) references member (MEMBER_NO);

alter table entry 
add constraint FK_ENTRY_LABEL
foreign key (LABEL_NO) references label (LABEL_NO);

alter table list 
add constraint FK_LIST_MEMBER
foreign key (MEMBER_NO) references member (MEMBER_NO);

alter table LIST_ITEM
add constraint FK_LIST_ITEM_LIST
foreign key (LIST_NO) references LIST (LIST_NO);