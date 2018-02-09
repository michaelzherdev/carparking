# --- Sample dataset

# --- !Ups
INSERT INTO car (id,number,arrival,departure) values (  1,'a123bc','2018-02-07 09:59:00',null);
INSERT INTO car (id,number,arrival,departure) values (  2,'d456ef','2018-02-07 10:00:00','2018-02-07 10:30:00');
INSERT INTO car (id,number,arrival,departure) values (  3,'a980ee','2018-02-07 10:05:00',null);
INSERT INTO car (id,number,arrival,departure) values (  4,'e987er','2018-02-07 10:05:10',null);
INSERT INTO car (id,number,arrival,departure) values (  5,'8974vf','2018-02-07 10:05:15',null);
INSERT INTO car (id,number,arrival,departure) values (  6,'fdv3454','2018-02-07 10:05:20',null);
INSERT INTO car (id,number,arrival,departure) values (  7,'e333jj','2018-02-07 10:05:26',null);
INSERT INTO car (id,number,arrival,departure) values (  8,'x889xx','2018-02-07 10:08:31',null);
INSERT INTO car (id,number,arrival,departure) values (  9,'a777aa','2018-02-07 10:11:00','2018-02-07 10:13:00');
INSERT INTO car (id,number,arrival,departure) values (  10,'k444kk','2018-02-07 10:12:10',null);
INSERT INTO car (id,number,arrival,departure) values (  11,'c333cc','2018-02-07 10:13:14',null);
INSERT INTO car (id,number,arrival,departure) values (  12,'c323cc','2018-02-07 10:15:20',null);
INSERT INTO car (id,number,arrival,departure) values (  13,'c313cc','2018-02-07 10:15:38',null);
INSERT INTO car (id,number,arrival,departure) values (  14,'c303cc','2018-02-07 10:16:01','2018-02-07 10:17:08');

# --- !Downs
DELETE FROM car;
