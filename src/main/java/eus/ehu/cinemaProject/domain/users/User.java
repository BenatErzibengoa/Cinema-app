package eus.ehu.cinemaProject.domain.users;
import jakarta.persistence.*;
import org.h2.engine.UserBuilder;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "users")
@DiscriminatorColumn(name = "ROLE", discriminatorType = DiscriminatorType.STRING)
public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(unique = true, nullable = false)
        private String email;

        @Column(nullable = false)
        private String password;

        @Column(nullable = false)
        private String name;

        @Column(nullable = false)
        private String surname;


        // Default constructor required by JPA
        protected User() {}

        // Private constructor used by the builder
        protected User(UserBuilder<?> builder) {
                this.email = builder.email;
                this.password = builder.password;
                this.name = builder.name;
                this.surname = builder.surname;
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
                        ", email='" + email + '\'' +
                        ", password='" + password + '\'' +
                        ", userName='" + name + '\'' +
                        ", surname='" + surname + '\'' +
                        '}';
        }


        // Abstract builder class with generic type parameter for method chaining
        public static abstract class UserBuilder<T extends UserBuilder<T>> {
                // Required fields
                private String email;
                private String password;

                // Optional fields with defaults
                private String name = "";
                private String surname = "";

                // Constructor with required fields
                protected UserBuilder(String email, String password) {
                        this.email = email;
                        this.password = password;
                }

                // Self-return method to allow method chaining in subclasses
                protected abstract T self();

                // Builder methods for optional fields
                public T name(String name) {
                        this.name = name;
                        return self();
                }

                public T surname(String surname) {
                        this.surname = surname;
                        return self();
                }

                // Build method
                public User build() {
                        return new User(this);
                }
        }

        // Concrete builder for User
        public static class Builder extends UserBuilder<Builder> {
                public Builder(String email, String password) {
                        super(email, password);
                }

                @Override
                protected Builder self() {
                        return this;
                }
        }
}
