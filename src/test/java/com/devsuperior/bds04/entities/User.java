package com.devsuperior.bds04.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A serialização é quando um objeto é transformado, em umac adeia de bytes e desta forma pode ser manipulado de maneira mais fácil, seja
 * através de transporte pela rede ou salvo no disco. Após a transmissão ou o armazenamento esta cadeia de bytes pode ser transformada
 * novamente no objeto Java que o originou. Em suma, a ideia por trás da serialização é a de "congelar" o objeto, guardando-o por um tempo
 * indeterminado, movê-lo e depois "descongelar" esseobjeto tornando novamente utilizável.Cenários comuns para o uso da serialização de
 * objetos dentro do Java, são as invocações remotas de métodos através de objetos distribuídos, aplicações que fazem uso do mapeamento
 * objeto/relacional e servidores de aplicações quando um cliente está desatualizado em relação a versão dos jars necessários.
 *
 * A serialização trabalha apenas com atributos de instância de uma classe, não incluindo os atributos estáticos. Outro detalhe importante
 * da serialização é que se o objeto a ser serializado for proveniente de uma subclasse, todos os atributos de instancia, mesmos aqueles
 * origináriosde superclasses, serão serializados. Também se um objeto contiver referências de outros objetos, todas as referências serão
 * serializadas.
 */

@Entity
@Table(name = "tb_user")
public class User implements UserDetails, Serializable {
    /**
     * SerialVersionUID é um número queidentificaa versão da classe que foi usada durante o processo de serialização.
     * Esse valor é utilizado para rastrear a compatibilidade de versões serializadas das classes e saber se o objeto
     * que se está recuperando é de uma versão “compatível” com a versão da classe que foi utilizada na origem paraserializar o objeto:
     * em outras palavras, os arquivos .class gerados a partir da compilação da classe não precisam ser necessariamente os mesmos para
     * que o processo de serialização ocorra com sucesso. O objetivo da presença desse atributo é identificar a versão da classe que foi
     * criada durante o processo de serializaçãodo objeto.
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tb_user_role",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User(){
    }

    public User(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
