# Message-Oriented-Middleware-and-Kafka-Streams


Grade of 72% in this Assignment

Project Description:
Objectives
• Learn how to create simple asynchronous and message-oriented applications.

• Learn to use Kafka Streams.

Make sure you understand the following concepts:
• Producer

• Consumer

• Topic

• Partition and partition offset

• Broker

• Zookeper

The credit card company has the following Kafka topics:
• DBInfo, where one or more source connectors write a list of clients and managers
present in the database.

• Credits topic, where a process simulating client actions with the credit card keeps
writing new outstanding debts from a client. Each purchase includes a price and a
currency. Students should randomly select the values and the currency.

• Payments topic, where a process simulating client actions (possibly the same)
keeps writing payments, just as in the previous case. Students should need not
concern if payments exceed the credits.

• One or more applications using Kafka streams will listen to these topics and
compute a few metrics, as enumerated in the requirements, including credits,
Credits Topic
Payments Topic
Administrator CLI
Database
REST
Insert persons
Kafka
Streams
Get responses
Kafka
Connect
Results Topics
Kafka
Connect
Clients
DB Info Topics
payments, balances, etc. This or these applications will then write their
computations to another topic, the Results topics, from where a Kafka connector
will write the results back to the database.
A server-side application will read data from the database and expose the data via REST.
The final piece of the application is a command line application that allows the
administrator to enter items, countries and read statistics from the REST services. No
authentication is necessary.

Components to Develop:
Students need to develop the following applications of Figure 1: Clients, Kafka Streams,
the administrator CLI and the REST implementation. From this list, the former are standalone
applications, while the latter is a server-side application to deploy on a server. They
must also configure Kafka connectors to automatically extract and write data from/to the
database.
Except for the data that was mentioned before, the precise definition of the communication
format is left for the students to determine. Clean solutions that serialize complex data
structures as JSON strings are encouraged.

Requirements
As an administrator I can perform the following actions (results should be compute in
euros):
1. Add managers to the database. To simplify managers cannot be deleted and optionally
not changed.
2. Add clients to the database. Again, these cannot be deleted and optionally not changed.
Each client has a manager.
3. Add a currency and respective exchange rate for the euro to the database.
5. List managers from the database.
6. List clients from the database.
7. List currencies.
8. Get the credit per client (students should compute this and the following values in
euros).
8. Get the payments (i.e., credit reimbursements) per client.
9. Get the current balance of a client.
10. Get the total (i.e., sum of all persons) credits.
11. Get the total payments.
12. Get the total balance.
13. Compute the bill for each client for the last month1 (use a tumbling time window).
14. Get the list of clients without payments for the last two months.
15. Get the data of the person with the highest outstanding debt (i.e., the most negative
current balance).
16. Get the data of the manager who has made the highest revenue in payments from his
or her clients.

Students should include means in their application to enable a fast verification of the
results they are displaying. Solutions should maximize the computations they do with
Kafka streams and reduce the database queries to their simplest possible form (for
example, students should use Kafka Stream to compute profits, instead of computing them
on the database).
