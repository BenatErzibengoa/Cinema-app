# Project about the management of a Cinema.

---

### Members of DirtyCode team:

- Viktoria Andrushkiv
- Beñat Ercibengoa
- Laura Rodríguez
- Théo Rolland
- Ekhi Ugarte

---

### Description

First project in teams for the Software Engineering course at the Faculty of Computer Science in Donostia.
The project consists of creating a system for managing a cinema, from having different kind of
users, to being able to buy some tickets.

---

### First sprint

We firstly decided what the project was going to be about, and then we wrote altogether the
wording of it. Each member provided new ideas that where discussed and added.

Then, we divided the project into tasks and assigned them to each member; as some of the tasks
were more demanding than others, we tried to balance the workload by assigning two members to those tasks.

All of us worked concurrently on the requirements' analysis and gave feedback to each other via GitHub's
project issues. After being on the same page, we started coding the first prototype of the main menu with a
BorderLayout, and gave it a style, as well as putting the attributes of the classes that we had defined.

### Second sprint

Before progressing with new functionalities, we dedicated time to reviewing and correcting issues identified during the first sprint to ensure a solid foundation.

We then began integrating a database into our project. This included designing the schema, creating the necessary SQL queries, and conducting initial tests to ensure everything functioned correctly.

With the updated requirements in mind, we reallocated tasks among team members, continuing to be mindful of workload balance.

Our main focus for this sprint was the development of the application's controllers and user interfaces. Each team member was responsible for specific windows or components, working in parallel to build out the system's interactive elements and connect them between eachother. For this connection we created a class for storing data from one view to another.

The controllers and UIs we have developed during this second sprint are MovieList, ShowTimeView, OrderFood, SeatSelection, Receipt and PurchaseReceiptHistory.

We also created a sequence diagram, and we stylized our views following the style used in the previous sprint.

### Third sprint

As previously done in the second sprint, we firstly started correcting some errors that we got in the last sprint and ensuring that everything that we missed was functioning well and was all finished.

Then we started hashing our passwords, we created some tests and we implemented an API that would let us easily get the information of the films for adding them to the cinema. 

After that, we implemented the admin and the worker roles, each with their own UIs and functionalities.
We also updated the customer role.

The customer will now have a feature to post reviews of the film that they watched, and those reviews will count to the rating of each film.
Also the customer will be able to request a ticket cancellation, at least 3h before the screening of the film.

The admin will be able to add films just by searching the title, then the api will return the information of that film and add it to the system. Along with that, the admin will be able to hire and fire workers, by adding them to the database with the worker role, or in the other case removing them from the database. Finally, it will be able to add films to concrete showtimes; The admin will select the room and get the available time frames for that room, then it will create a showtime in the selected time frame with the previously selected film.

The worker will be able to accept cancellation requests before the screening of the film. Also the worker can buy tickets for a customer given the user's email (this is for simulating a customer calling the cinema or buying the ticket in person)

We have implemented internationalization in all of our project, supporting English and Spanish.

We also styled our project to be as clean and visually appealing as possible.

### Test Users

The system comes with several pre-configured test users for different roles:

#### Admin User

- Email: juanan.pereira@ehu.eus
- Password: admin1234
- Name: Juanan Pereira
- Salary: 2500

#### Worker Users

1. Beñat Ercibengoa

   - Email: bercibengoa001@ikasle.ehu.eus
   - Password: 12345678
   - Salary: 2000

2. Viktoria Andrushkiv

   - Email: vandrushkiv001@ikasle.ehu.eus
   - Password: 87654321
   - Salary: 2000

3. Théo Rolland

   - Email: trolland001@ikasle.ehu.eus
   - Password: abcdefghi
   - Salary: 2000

4. Laura Rodríguez

   - Email: lrodriguez154@ikasle.ehu.eus
   - Password: 12345678
   - Salary: 2000

5. Ekhi Ugarte
   - Email: eugarte001@ikasle.ehu.eus
   - Password: 111222333g
   - Salary: 2000

#### Customer Users

1. Aitor Elizondo

   - Email: aitor@gmail.com
   - Password: 12345

2. Amaia Susperregi

   - Email: amaia@gmail.com
   - Password: 12345

3. Uxue Etxebeste
   - Email: uxue@gmail.com
   - Password: 12345

These test users are automatically created when the database is initialized with the "initialize" mode set in the configuration.
