create table TEST_121_A (
  id1 int not null,
  id2 int not null,

  constraint PK_TEST_121_A primary key (id1, id2)
);



create table TEST_121_B (
  id1 int not null,
  id2 int not null,

  a_id1 int not null,
  a_id2 int not null,

  constraint PK_TEST_121_B primary key (id1, id2),

  constraint UK_TEST_121_B unique (a_id1, a_id2),

  constraint FK_TEST_121_B_A foreign key (a_id1, a_id2) references TEST_121_A (id1, id2) on update cascade on delete restrict
);



create table TEST_M21_A (
  id1 int not null,
  id2 int not null,

  constraint PK_TEST_M21_A primary key (id1, id2)
);



create table TEST_M21_B (
  id1 int not null,
  id2 int not null,

  a_id1 int not null,
  a_id2 int not null,

  constraint PK_TEST_M21_B primary key (id1, id2),

  constraint FK_TEST_M21_B_A foreign key (a_id1, a_id2) references TEST_M21_A (id1, id2) on update cascade on delete restrict
);

create index ID_TEST_M21_B_A on TEST_M21_B (a_id1, a_id2);



create table TEST_M2M_A (
  id1 int not null,
  id2 int not null,

  constraint PK_TEST_M2M_A primary key (id1, id2)
);



create table TEST_M2M_B (
  id1 int not null,
  id2 int not null,

  constraint PK_TEST_M2M_B primary key (id1, id2)
);



create table TEST_M2M_REL (
  ab_id1 int not null,
  a_id2 int not null,
  b_id2 int not null,

  constraint PK_TEST_M2M_REL primary key (ab_id1, a_id2, b_id2),

  constraint FK_TEST_M2M_REL_A foreign key (ab_id1, a_id2) references TEST_M2M_A (id1, id2) on update cascade on delete restrict,
  constraint FK_TEST_M2M_REL_B foreign key (ab_id1, b_id2) references TEST_M2M_B (id1, id2) on update cascade on delete restrict
);

create index ID_TEST_M2M_REL_A on TEST_M2M_REL (ab_id1, a_id2);
create index ID_TEST_M2M_REL_B on TEST_M2M_REL (ab_id1, b_id2);
