# Java-Cassandra-CompositeKey-Example

#Create keyspace inventory by using following syntax

create keyspace inventory with replication={'class':'SimpleStrategy','replication_factor':1};

use inventory;


#create stock table 


CREATE TABLE inventory.stock (
 dpci varchar,
location varchar,
stocktype varchar,
qty varchar,
PRIMARY KEY (dpci, location, stocktype)
);

#Run App.java
you will see row inserted in cassandra table
