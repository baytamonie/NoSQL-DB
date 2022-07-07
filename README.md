# Methodology

The entire DB project consists of 3 main parts 

- Controller
- Nodes
- Client driver for the client to use

## Controller ( Connection Manager)

### Connecting nodes and client drivers

- Two thread types to handle clients and nodes.
- Whenever a **node** is initialized, it sends the port to the **Controller.** The controller then receives the node and it’s port.
- Whenever a client makes a connection request through the **Client Driver** to the **Controller** it sends one of the ports available through the **load balancer** to the client driver.
- The **load balancer** in the controller uses **Round Robin** with weights, weights here are the loads of the available nodes.
- If a node loses a client, the load of the node is updated in the controller
- The load balancer is sorted every time before a client driver asks for a node
- The **Client Driver** receives the port from the controller and connects to the node at the given port.

### Main source of data

- One of the requirements was that writes are done only through a connection to the database **controller.**
- Whenever the controller receives a request from the client to do a write operation (update, delete, write). The **controller** then updates the it’s main source of data and refreshes every node with the latest data. To make sure all nodes have the same data, I’m using version numbers where each database has a small txt file containing the version number.
- Databases are JSON formats. Under src/main/java/resources directory for the **Controller** and the **Nodes** we have the databases.
- Each database is a folder containing multiple **Collections**. We also have a **usernames** database for the clients to use to login.
    - usernames contains a list of usernames, passwords and authorities for each user, authorities are:
        - read, Write, Create index (rwi)
- Each **Collection** has multiple JSON files:
    - data.json —> Has an array of JSON objects.
    - ids.json —> Has the place for each JSON object in the file (starting byte and ending byte) for the **Random Access File** to directly find the object the user is looking for.
    
    ![Figure 1, shows how databases are structed.](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/4ae1a0b6-d4db-42d6-8509-d867b11074df/Untitled.png)
    
    Figure 1, shows how databases are structed.
    
    - version.txt —> has a number containing the version of the data.
    - schema.json —> has the schema for the collection.
    
    ![Figure 2, shows how schemas are stored.](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/370ccdd0-dcfa-471a-b6f0-a6cf4d976142/Untitled.png)
    
    Figure 2, shows how schemas are stored.
    

### Random access files

Choosing how to format the files of the database was an important decision to make while making this project. 
I ended up choosing using one file to hold the data for each collection. And use the random access files to go through the file by having another file to save the location for each object. Whenever an object is written the controller saves where the object was written in ids.json.

**ids.json** saves the id of the file with the corresponding start of the object and where it ends.  

![Figure 3, shows how location of documents are stored.](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/b9fd1870-ba68-4108-a8ac-b731e71354a6/Untitled.png)

Figure 3, shows how location of documents are stored.

### Writes

Writes are done through the controller, for each database collection the **controller** has a pointer, this pointer is at the last place the client has written to. 

For writing to a database the client must have the Write authority (’w’).

- creating a database
    - A directory is made with the name of the database and puts the version number of the database
- creating a collection ( the database has to be there, if it’s not there it will cause an error to the client)
    - client has to provide a schema
    - data.json, ids.json, schema.json, version.txt are initialized under the collection directory
- adding a document to a collection (the collection has to exist, otherwise an error is generated to the user)
    - the document has to match the schema ( nulls are generated if the client didn’t supply all values for the schema. Errors are generated for the client if he supplied anything that does not exist in the schema)
    - indexing the document will be discussed later in the report.
- deleting a database
    - deletes the entire directory in the controller node, then refreshes all nodes
- deleting a collection
    - deletes the collection directory
- delete a document
    - deletes everything in the corresponding bytes  to that document
        - this wastes a lot of disk space if we have many delete operations
    - another idea is to have all document objects have a specific size, for example 256 bytes. any object that exceeds that limit can take double the space and so on. This makes it easier to put an object in place of the deleted object
    - we can also shift the data, however this takes a lot of processing power if the collection is big
- updating a document
    - takes the new object value and replaces it with the older one

For write operations we can also implement Update schema.

### Indexing documents

Before this project, I had little knowledge of NoSQL databases. So I had to research the best way to index documents. 

The implementation I ended up with is that each document is indexed based on the following rules:

- Natural keys
- Human readable
- Deterministic
- Semantic

For each document the indexing is as follows:

database name, collection name, number. For example —>

database1.collection1.10101 

This also helps with caching where I don’t have to store to which database or collection an object belongs. Also makes it easier for debugging and for clients to read.

The number is the number of the document in the file.

### Create indexes on a single json property

For the client to be able to create an index he has to have the authority ‘i’ —> create index

***Inverted indexing***

An inverted index is **an index data structure storing a mapping from content, such as words or numbers, to its locations in a document or a set of documents**. In simple words, it is a hashmap like data structure that directs you from a word to a document.

![Figure 4, shows an example of inverted indexing taken from [https://www.youtube.com/watch?v=bnP6TsqyF30&ab_channel=AdityaAmbasth](https://www.youtube.com/watch?v=bnP6TsqyF30&ab_channel=AdityaAmbasth).](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/992b2f52-feba-4566-99c0-0a3c40a7f135/Untitled.png)

Figure 4, shows an example of inverted indexing taken from [https://www.youtube.com/watch?v=bnP6TsqyF30&ab_channel=AdityaAmbasth](https://www.youtube.com/watch?v=bnP6TsqyF30&ab_channel=AdityaAmbasth).

In our document DB, we can have a hashmap that stores the name of the property we want to index as key, and the value as a linked list that has the ids of documents that have that property. Since we have the id of the document we can quickly get it from the file since we know its location in the data file.

After creating an index, a new file is created in the collection named after the property name.

It stores the data in a similar manner to the ids.json. 

If a node requests to create an index, the controller returns the file that has the indexing if it exists or creates a new one and returns it.

<aside>
👉 Multithreading is supported in the controller, with locks on writes

</aside>

## Nodes

Whenever a node is started, It sends its port to the controller and asks for a refresh for the data if it’s not in sync with the version number.

A client connects to the node by the controller as described earlier.

Whenever a client disconnected from a node, the node sends the new load of the node to the controller. 

### Communication

**`public class** Packet **implements** Serializable`

Main source of communication between clients and nodes are **Packets**. Packet is a class that has a String message. 
A packet is sent from the Client handler to the Node describing the operation. A new operation is then created using Functions **factory pattern**. then the function is executed. (If a function requires more information from the client, they are sent as packets)

Packet has a const serialVersionUID to make sure packets are understood between the different packages.

**`private static final long** *serialVersionUID* = 1;`

### Reads

Reads operations follow the chain of responsibility pattern. 

- checks if data requested is valid; correct database name, and correct collection name
- checks the cache to check if data is available, if it’s in cache returns it from cache
- if the user requested a list of document by property name, we check if there’s an indexed property created for that property. If there is, we find the start and the end of that document in the data.json file.

after the client selects the database and collection, the node loads the ids.json 

### caching

***LRU Caching***

The LRU caching scheme is to remove the least recently used frame when the cache is full and a new page is referenced which is not there in the cache.

The cache is updated after each read using the Observer pattern.

**Data structures used in caching**

**Queue**
 is implemented using a doubly-linked list. The size of the linked list is the same as the cache size. **Hash**
 with page number as key and address of the corresponding queue node as value

![Figure 5, explains how the cache works. Taken from [https://www.geeksforgeeks.org/lru-cache-implementation/](https://www.geeksforgeeks.org/lru-cache-implementation/)](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/a2fac1e0-b29f-43ee-9590-6579e4e5d5b9/Untitled.png)

Figure 5, explains how the cache works. Taken from [https://www.geeksforgeeks.org/lru-cache-implementation/](https://www.geeksforgeeks.org/lru-cache-implementation/)

![Figure 6, explains how the cache works. Taken from [https://www.geeksforgeeks.org/lru-cache-implementation/](https://www.geeksforgeeks.org/lru-cache-implementation/)](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/50fc9297-ebfb-4410-b47a-70dabfa769d1/Untitled.png)

Figure 6, explains how the cache works. Taken from [https://www.geeksforgeeks.org/lru-cache-implementation/](https://www.geeksforgeeks.org/lru-cache-implementation/)

### Horizontal scaling

A node can be created at any time, using a docker image or by running the jar file more than once.

<aside>
👉 Multithreading is supported for each node to handle multiple clients at once, every time the load is updated its sent to the controller

</aside>

## Client handler documentation

The client can expect these functions:

**`void** createConnection();` —> creates a connection with a node running by sending a packet to the controller. throws a run time exception if no connection is made.

**`void** login(String username, String password);` —> the user must login and have access to read, write or create property indexes.

`JSONArray getCollection(String databaseName, String collectionName);`  —> returns a collection of json objects or throws an illegal argument exception if no collection/database is found.

`JSONObject getDocument(String databaseName, String collectionName, String qdocumentId);` —> returns a document if found, if not throws an illegal argument exception.

`Object getProperty(String databaseName,String collectionName, String documentId, String propertyName);` —> returns the value for a single property inside a json document. Throws illegal argument exception if the property is not found.

`List<JSONObject> getProperties(String databaseName,String collectionName, String property,String value);` —> returns a list of json objects that has a specific value for a property. returns null if nothing is found.

**`void** createDatabase(String name);` —>creates a database with the name provided. If the database exists it throw an illegal argument exception.

**`void** createCollection(String databaseName, String collectionName, Schema schema);` creates a collection inside a database. Throws a runtime exception if no database with the name is found or a collection with the same name exists.

**`void** deleteDatabase(String databaseName);` deletes a database. Throws an illegal argument exception if no database with that name is found.

**`void** deleteCollection(String databaseName, String collectionName);` —>deletes a collection. Throws an illegal argument exception if no database with that name is found or if no collection with the same name is found.

**`void** writeDocument(String databaseName,String collectionName,JSONObject document);` —> writes a new document. Throws an illegal argument if the database name or the collection name is not found and if the document is null.

**`void** updateDocument(String databaseName,String collectionName, String documentId,JSONObject newValue);` —>replaces an existing document with a new one. Throws an illegal argument exception if no database or collection name or document id with the same values are found.

**`void** deleteDocument(String databaseName,String collectionName, String documentId);` —> deletes a document. Throws an illegal argument exception if no database or collection or document with the same values are found.

**`void** createIndex(String databaseName, String collectionName, String indexName);` —>

creates a new index on the property. Throws an illegal argument exception if the index or the database or the collection were not found.

Please note the following—>

- you must create a connection before doing any other operation
- for read operations you must login and have the ‘r’ authority
- for write operations you must login and have the ‘w’ authority
- for creating index operation you must have the ‘i’ authority
- you can have multiple authorities
