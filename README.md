# Using of Oracle QRN
>Given library provides Oracle query notification functionality to an existing system. It can be use as a standalone application with
>leader election functionality through Zookeeper and able to send notification with RowID to any messaging queue such as Apollo or Kafka. Consumer of the
>queue can consume the message and query to the data base to get the changed row and feed Elastic search or execute any operation.

### Version
1.0-snapshot
### Prerequisites
* Oracle JDBC 11g driver needs to compile the project.
* apache zookeeper
* apache Apollo
* elastic search

### Core functionalities
* [qrcn] - collect notification from Oracle and send the notifications to any existing queue [apollo].
* [es] - consumer, collects the message from the queue and index in Elastic search
* [es-dto] - common dto

