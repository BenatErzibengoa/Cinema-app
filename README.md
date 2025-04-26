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

Our main focus for this sprint was the development of the application's controllers and user interfaces. Each team member was responsible for specific windows or components, working in parallel to build out the system's interactive elements and later connect them with backend logic.

The controllers and UIs we have developed during this second sprint are MovieList, ShowTimeView, OrderFood, SeatSelection, Receipt and PurchaseReceiptHistory.

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
