# Updated 

In this assignment,we had to implement a concurrency control algorithm for an airline database.
The system consists of transaction threads,concurrency control manager and an airline database.
The database has a set of transactions and data structures containing flight information(passenger list for all flights).

The goal was to study the performance improvements based on the granularity of locking.
The first version of program implements only serial schedule(lock the entire database).
The second version does two-phase locking(2 PL) at a more fine-grained level.

Read the pdf for more details.
