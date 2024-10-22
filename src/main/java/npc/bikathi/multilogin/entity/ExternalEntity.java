package npc.bikathi.multilogin.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "external_entity")
public class ExternalEntity {
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    public Long getId() {
        return id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ExternalEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public ExternalEntity() {
    }
}
