package eus.ehu.cinemaProject.domain.users;
import jakarta.persistence.*;


@Entity
@Table(name = "users")
public class User {


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(unique = true, nullable = false)
        private String email;

        @Column(nullable = false)
        private String password;

        @Column(nullable = false)
        private String role;
        private String name;
        private String surname;



        // Default constructor
        public User() {
        }


        // Constructor with fields
        public User(String email, String password, String role, String name, String surname) {
                this.email = email;
                this.password = password;
                this.role = role;
                this.name = name;
                this.surname = surname;
        }


        // Getters and Setters
        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public String getPassword() {
                return password;
        }


        public void setPassword(String password) {
                this.password = password;
        }


        public String getRole() {
                return role;
        }

        public void setRole(String role) {
                this.role = role;
        }


        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getSurname() {
                return surname;
        }

        public void setSurname(String surname) {
                this.surname = surname;
        }




        @Override
        public String toString() {
                return "User{" +
                        "id=" + id +
                        ", role='" + role + '\'' +
                        ", email='" + email + '\'' +
                        ", password='" + password + '\'' +
                        ", userName='" + name + '\'' +
                        ", surname='" + surname + '\'' +
                        '}';
        }
}
