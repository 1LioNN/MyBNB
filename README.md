# MyBNB
C43 Project - MyBnB\
Frontend - Javascript (React)\
Backend - Java (Maven)\
Database - MySQL (embedded in Java)
## Installation
1. Install [Node.js v18.9.1](https://nodejs.org/en/download/current/)
2. Download a ZIP of the repository and unzip it or checkout the repository.
3. Install dependencies for the backend by running the following in the mybnb/ directory:
   ```
   cd backend
   mvn install
   ```

4. Install dependencies for the frontend by running the follwing in a new terminal in the mybnb/ directory:
   ```
   cd frontend
   npm install
   ```
3. Create a new file in the backend foldler called .env and set the contents of the file to the following (make sure there are no spaces between the =):
   ```
   DB_NAME=mybnb
   DB_PASS= //password of your local database
   ```


4. In the terminal where you installed the backend dependencies, run the following to start the server:
   ```
   mvn exec:java
   ```
5. In the terminal where you installed the frontend dependencies, run the following to start the frontend:

   ```
   npm run start
   ```
