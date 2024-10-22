package npc.bikathi.multilogin.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "internal_entity")
public class InternalEntity {
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    @JsonIgnore
    private String password;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

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

    public InternalEntity() {
    }

    public InternalEntity(String username, String password, Long id) {
        this.username = username;
        this.password = password;
        this.id = id;
    }

    public InternalEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
