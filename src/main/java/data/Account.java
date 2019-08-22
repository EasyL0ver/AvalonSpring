package data;

import javax.persistence.*;

@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
