# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table comment (
  id                        bigint auto_increment not null,
  title                     varchar(255),
  content                   varchar(255),
  constraint uq_comment_title unique (title),
  constraint uq_comment_content unique (content),
  constraint pk_comment primary key (id))
;

create table complaints (
  id                        bigint auto_increment not null,
  date                      date,
  complaint_data            varchar(255),
  status                    varchar(255),
  constraint pk_complaints primary key (id))
;

create table customer (
  id                        bigint auto_increment not null,
  first_name                varchar(255),
  last_name                 varchar(255),
  contact                   varchar(255),
  email                     varchar(255),
  password                  varchar(255),
  constraint uq_customer_email unique (email),
  constraint pk_customer primary key (id))
;

create table feedback (
  id                        bigint auto_increment not null,
  date                      datetime,
  feedback_data             varchar(255),
  status                    varchar(255),
  constraint pk_feedback primary key (id))
;

create table merchant (
  id                        bigint auto_increment not null,
  full_name                 varchar(255),
  business_name             varchar(255),
  contact                   varchar(255),
  email                     varchar(255),
  website                   varchar(255),
  address                   varchar(255),
  faxno                     varchar(255),
  rank                      varchar(255),
  password                  varchar(255),
  constraint uq_merchant_email unique (email),
  constraint pk_merchant primary key (id))
;

create table product (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  type                      varchar(255),
  amount                    double,
  constraint uq_product_name unique (name),
  constraint pk_product primary key (id))
;

create table transaction (
  transation_id             bigint auto_increment not null,
  date                      date,
  amount                    double,
  status                    varchar(255),
  customer_id               bigint,
  product_id                bigint,
  merchant_id               bigint,
  constraint pk_transaction primary key (transation_id))
;

create table user (
  id                        bigint auto_increment not null,
  email                     varchar(255),
  first_name                varchar(255),
  last_name                 varchar(255),
  password                  varchar(255),
  constraint pk_user primary key (id))
;

alter table transaction add constraint fk_transaction_customer_1 foreign key (customer_id) references customer (id) on delete restrict on update restrict;
create index ix_transaction_customer_1 on transaction (customer_id);
alter table transaction add constraint fk_transaction_product_2 foreign key (product_id) references product (id) on delete restrict on update restrict;
create index ix_transaction_product_2 on transaction (product_id);
alter table transaction add constraint fk_transaction_merchant_3 foreign key (merchant_id) references merchant (id) on delete restrict on update restrict;
create index ix_transaction_merchant_3 on transaction (merchant_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table comment;

drop table complaints;

drop table customer;

drop table feedback;

drop table merchant;

drop table product;

drop table transaction;

drop table user;

SET FOREIGN_KEY_CHECKS=1;

