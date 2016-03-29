# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table configs (
  id_config                 bigint auto_increment not null,
  config_key                varchar(255),
  value                     varchar(255),
  description               varchar(255),
  constraint pk_configs primary key (id_config))
;

create table location (
  id_location               integer auto_increment not null,
  start_lat                 float(10,6),
  start_long                float(10,6),
  end_lat                   float(10,6),
  end_long                  float(10,6),
  current_lat               float(10,6),
  current_long              float(10,6),
  status_location           smallint,
  follow                    varchar(255),
  constraint pk_location primary key (id_location))
;




# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table configs;

drop table location;

SET FOREIGN_KEY_CHECKS=1;

