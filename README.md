# Using of Oracle QRN
>Given library provides Oracle query notification functionality to an existing system. It can be use as a standalone application with
>leader election functionality through Zookeeper and able to send notification with RowID to any messaging queue such as Apollo or Kafka. Consumer of the
>queue can consume the message and query to the data base to get the changed row and feed Elastic search or execute any operation.

### Version
1.0-snapshot
### Prerequisites
Oracle JDBC 11g driver needs to compile the project.

### Core functionalities
* [oracledbnotification] - collect and send the notification of QRN to any existing queue.
* [event-processor] - consumer, collects the message from the queue and start processing

