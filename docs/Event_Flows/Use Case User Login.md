Use Case: User Login

Preconditions:
The user must be registered in the system with valid credentials (username and password).

Main Flow:

1- The user enters their username and password.

2- The system validates the credentials against the stored user database.

3- If the credentials are correct, the system retrieves the user role (Worker, Admin, or Client) and grants access based on the user's role.

4- The user is redirected to their respective dashboard.


Alternative flows:

2.A- If the credentials are incorrect, the system displays an error message indicating invalid username or password.
