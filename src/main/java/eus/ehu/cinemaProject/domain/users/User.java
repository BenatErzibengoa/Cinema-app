package eus.ehu.cinemaProject.domain.users;
import jakarta.persistence.*;


@Entity
@Table(name = "users")
public class User {


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;


        @Column(unique = true, nullable = false)
        private String userName;


        @Column(nullable = false)
        private String password;


        @Column(nullable = false)
        private String role;


        // Default constructor
        public User() {
        }


        // Constructor with fields
        public User(String userName, String password, String role) {
                this.userName = userName;
                this.password = password;
                this.role = role;
        }


        // Getters and Setters
        public Long getId() {
                return id;
        }


        public void setId(Long id) {
                this.id = id;
        }


        public String getUserName() {
                return userName;
        }


        public void setUserName(String userName) {
                this.userName = userName;
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


        @Override
        public String toString() {
                return "User{" +
                        "id=" + id +
                        ", userName='" + userName + '\'' +
                        ", role='" + role + '\'' +
                        '}';
        }
}
