# Prasunet_AD_02
It is a to-do list app that allows users to add, edit, and delete tasks
<br>
Author- Srijita Dasgupta

explanation:

1. User.java:
   - Added password hashing using MessageDigest.
   - Added authentication method to verify password.

2. DataManager.java:
   - Manages the loading and saving of users with their tasks.

3. ToDoListApp.java:
   - Added a registration and login mechanism.
   - Added action listeners for sorting tasks by date and priority.
   - Modified the user interface to include additional buttons for sorting.
   - Modified AddButtonListener, EditButtonListener, and DeleteButtonListener to interact with the current user's tasks and update the data manager accordingly.

Running the Application

1. Compile and run the ToDoListApp class.
2. Register a new user or log in with existing credentials.
3. Use the application to add, edit, delete, and sort tasks.
4. The tasks are saved and loaded per user, and the data is stored securely with hashed passwords.
