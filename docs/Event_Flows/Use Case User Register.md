Use Case: User Register

Main Flow:

1- The user enters the required details for registration (name, email, username, password)

2- The system checks if the username or email already exists.

3- If all validations pass, the system saves the user information in the database and sends a verification email.

4- The system displays a success message.

5- The user will be redirected to the login page


Alternative flows:

2.A- If the credentials are incorrect, the system displays an error message indicating invalid username or password.
2.B- If a user is already registered with those credentials (email), the system will display an error message indicating that that mail is already registered.
