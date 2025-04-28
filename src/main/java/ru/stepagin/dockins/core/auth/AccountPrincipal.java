package ru.stepagin.dockins.core.auth;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.stepagin.dockins.core.user.entity.AccountEntity;

import java.util.Collection;
import java.util.Collections;

@Getter
public class AccountPrincipal implements UserDetails {
    private final AccountEntity account;

    public AccountPrincipal(AccountEntity account) {
        this.account = account;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
//                 account.getRoles().stream()
//                .map(r -> new SimpleGrantedAuthority(r.getName()))
//                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        return account.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !account.isDeleted();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return account.isDeleted();
    }

}
