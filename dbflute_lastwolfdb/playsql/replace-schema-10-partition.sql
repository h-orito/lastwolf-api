-- partition table fk delete

alter table MESSAGE drop foreign key FK_message_village;
alter table MESSAGE drop foreign key FK_message_village_day;
alter table MESSAGE drop foreign key FK_message_village_player;
alter table MESSAGE drop foreign key FK_message_message_type;

-- partition

-- message
alter table MESSAGE partition by hash (village_id) partitions 100;
