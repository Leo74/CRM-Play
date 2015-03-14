# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table bank (
  id                        bigint auto_increment not null,
  full_name                 varchar(255),
  name                      varchar(255),
  contact                   varchar(255),
  address                   varchar(255),
  constraint pk_bank primary key (id))
;

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
  customer_id               bigint,
  merchant_id               bigint,
  date                      date,
  complaint_data            varchar(255),
  status                    varchar(255),
  constraint pk_complaints primary key (id))
;

create table country (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  constraint pk_country primary key (id))
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
  customer_id               bigint,
  merchant_id               bigint,
  date                      datetime,
  feedback_data             varchar(255),
  admin_read                tinyint(1) default 0,
  merchant_read             tinyint(1) default 0,
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
  id                        bigint auto_increment not null,
  date                      datetime,
  amount                    double,
  status                    varchar(255),
  description               varchar(255),
  customer_id               bigint,
  product_id                bigint,
  merchant_id               bigint,
  buying_location_id        bigint,
  bank_id                   bigint,
  admin_read                tinyint(1) default 0,
  merchant_read             tinyint(1) default 0,
  otp                       varchar(255),
  opt_send_date             datetime,
  constraint pk_transaction primary key (id))
;

create table user (
  id                        bigint auto_increment not null,
  email                     varchar(255),
  first_name                varchar(255),
  last_name                 varchar(255),
  password                  varchar(255),
  constraint pk_user primary key (id))
;

alter table complaints add constraint fk_complaints_customer_1 foreign key (customer_id) references customer (id) on delete restrict on update restrict;
create index ix_complaints_customer_1 on complaints (customer_id);
alter table complaints add constraint fk_complaints_merchant_2 foreign key (merchant_id) references merchant (id) on delete restrict on update restrict;
create index ix_complaints_merchant_2 on complaints (merchant_id);
alter table feedback add constraint fk_feedback_customer_3 foreign key (customer_id) references customer (id) on delete restrict on update restrict;
create index ix_feedback_customer_3 on feedback (customer_id);
alter table feedback add constraint fk_feedback_merchant_4 foreign key (merchant_id) references merchant (id) on delete restrict on update restrict;
create index ix_feedback_merchant_4 on feedback (merchant_id);
alter table transaction add constraint fk_transaction_customer_5 foreign key (customer_id) references customer (id) on delete restrict on update restrict;
create index ix_transaction_customer_5 on transaction (customer_id);
alter table transaction add constraint fk_transaction_product_6 foreign key (product_id) references product (id) on delete restrict on update restrict;
create index ix_transaction_product_6 on transaction (product_id);
alter table transaction add constraint fk_transaction_merchant_7 foreign key (merchant_id) references merchant (id) on delete restrict on update restrict;
create index ix_transaction_merchant_7 on transaction (merchant_id);
alter table transaction add constraint fk_transaction_buyingLocation_8 foreign key (buying_location_id) references country (id) on delete restrict on update restrict;
create index ix_transaction_buyingLocation_8 on transaction (buying_location_id);
alter table transaction add constraint fk_transaction_bank_9 foreign key (bank_id) references bank (id) on delete restrict on update restrict;
create index ix_transaction_bank_9 on transaction (bank_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table bank;

drop table comment;

drop table complaints;

drop table country;

drop table customer;

drop table feedback;

drop table merchant;

drop table product;

drop table transaction;

drop table user;

SET FOREIGN_KEY_CHECKS=1;

