# Why 
- you want to publish events related to changes in data in your service
- 
 - You want transactional guarantees* around internal stage changes & event publication
- You want a runtime guarantee that your messages can be read by the consumer
 - you want read your own writes semantics


# Why can't i just ... 

- publish to kafka in a @Transactional method?
  - we have to use 2 phase commit
  - we introduce complexity into use of multiple transaction managers
- write to kafka only & do DB updates later
  - can't read you own writes
  - introduces complexity around consistency 
- use Kafka connect directly on tables ? 
  - lose the schema guarantees



# Sounds great - when should I not use this pattern? 

- In an application without a strong business layer, where lots of database writes happen in external processes
  - The idea here is we are providing events based on changes ! if we have untracked changes, we can't guarentee the publishing of changes
- If you are using a database that doesn't support transactions across tables like GCP datastore or pre-accord Apache Cassandra



# Ok - so how does it work ?

Essentially an implementation of https://microservices.io/patterns/data/polling-publisher.html
Using Kafka connect & Debezium Event router
https://debezium.io/documentation/reference/stable/transformations/outbox-event-router.html

You could argue this is technically a log-tailing producer instead of a polling producer given the use of the Postgres connector instead of a JDBC connector - while this is semantically correct, as a "black box"

## Transactional Guarantee
  

  
  
## Schema Evolution Guarantee





