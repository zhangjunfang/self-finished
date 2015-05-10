--prompt PL/SQL Developer import file
--prompt Created on 2013年7月30日 by renhui
--set feedback off
--set define off
--First　delete　the　tables　if　they　exist.　
--Ignore　the　table　does　not　exist　error　if　present　
drop table ANIMAL;
drop table incrementer;
--prompt Creating ANIMAL...
create table ANIMAL
(
  id     VARCHAR(32) primary key  not null,
  name   VARCHAR(32),
  length int
);
--prompt Creating incrementer...
create table incrementer
(
  value int generated always as identity, 
  dummy char(1)
);
--prompt Disabling triggers for ANIMAL...
--alter table ANIMAL disable all triggers;
--prompt Disabling triggers for A_BRANCH...
--alter table A_BRANCH disable all triggers;
--prompt Disabling triggers for A_PEOPLE...
--alter table A_PEOPLE disable all triggers;
--prompt Loading ANIMAL...
--prompt Table is empty
--prompt Loading A_BRANCH...
--prompt Table is empty
--prompt Loading A_PEOPLE...
--prompt Table is empty
--prompt Enabling triggers for ANIMAL...
--alter table ANIMAL enable all triggers;
--prompt Enabling triggers for A_BRANCH...
--alter table A_BRANCH enable all triggers;
--prompt Enabling triggers for A_PEOPLE...
--alter table A_PEOPLE enable all triggers;
--set feedback on
--set define on
--prompt Done.
