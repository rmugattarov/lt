Financial Instrument Demo Application by Robert Mugattarov
==========================================================

Overview
--------

This application is an implementation of the Producer/Consumer design 
pattern with one producer and four consumer threads.
The producer uses Scanner to parse the source file and a poison pill 
to notify the consumers of exhaustion.
Each of the instruments INSTRUMENT1, INSTRUMENT2 and INSTRUMENT3 have 
a corresponding consumer thread.
A fourth consumer thread is used to process other instruments.
This application uses a Derby in-memory database to produce instrument 
value multipliers.
The multipliers are cached in a Guava LoadingCache.
The application prints the current instrument statistics in runtime and 
a message of completion once the source file is parsed and the consumer 
threads are done.

Software Requirements
---------------------

To build and run this application you will need:
- Maven3
- JDK8

Running
-------
1. cd to the source root and build the project:

        mvn clean package

2. Run the application with the path to the source file in exec.args value:

        mvn exec:java -Dexec.args="D:\\example_input.txt"

