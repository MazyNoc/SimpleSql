# SimpleSql

In the shadow of all noSql and ORM libraries we might still use Androids build in stuctured storage - Sqlite -.
This library adds named paramters to sql queries (no need to count all the question marks anymore and provides a very thin layer around jdbc cursor and replaces that with a callback method per row.

It also provides functions to allow you to write Mappers between the stored data and your own data objects.

In its current state its more aimed to retrieve data rather than storing it so it has a lot of different ways to iterate over cursors but few for transaction handling and inserts even if it's still supported.

Look at the example module to get some ideas on how to use it.

