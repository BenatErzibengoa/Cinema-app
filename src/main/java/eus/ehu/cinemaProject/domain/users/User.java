package eus.ehu.cinemaProject.domain.users;
import jakarta.persistence.*;


@Entity
@Table(name = "USERS")
//@DiscriminatorColumn(name = "USER_TYPE", discriminatorType = DiscriminatorType.STRING)
public class User {


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;


        @Column(unique = true, nullable = false)
        private String userName;


        @Column(nullable = false)
        private String password;



        // Default constructor
        public User() {
        }


        // Constructor with fields
        public User(String userName, String password) {
                this.userName = userName;
                this.password = password;
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



        @Override
        public String toString() {
                return "User{" +
                        "id=" + id +
                        ", userName='" + userName + '\'' +
                        '}';
        }
}
