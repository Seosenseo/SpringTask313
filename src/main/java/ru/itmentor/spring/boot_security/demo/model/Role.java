package ru.itmentor.spring.boot_security.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Data
@NoArgsConstructor
@Entity
@Table(name = "role_table")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EnumRole name;


    public Role(EnumRole name) {
        this.name = name;
    }

    public String getName() {
        return name.toString();
    }

    @Override
    public String getAuthority() {
        return name.name();
    }
}
