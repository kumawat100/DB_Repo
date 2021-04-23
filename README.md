# DB_Repo

DB Trade Store
--------------

Trades flow to TradeStore with a structure as of Trade.java.
Trade gets added to a HashMap after some validations and only if the trade is a valid one.
TradeMain is the main class, to run and test the program.
TradeStoreUnitTests contain all the unit tests.
Dummy data is being inserted every time the main class gets executed OR unit tests are executed.


To run this project, execute mvnw.cmd in Command Prompt 
OR
Simply run the below in cmd (after cd to the project directory)
"%JAVA_HOME%"\bin\java.exe -cp target\DBTradeStore-0.0.1-SNAPSHOT.jar com.db.trade.TradeMain
