package com.epam.jmp.rest.domain.builder;

import com.epam.jmp.rest.domain.User;

import javax.annotation.Nonnull;

public class UserBuilder implements DomainBuilder<User> {

    private String login;
    private String email;
    private String img;


    public UserBuilder(@Nonnull final String email) {
        this.email = email;
    }

    public UserBuilder login(final String login) {
        this.login = login;
        return this;
    }
    public UserBuilder img(final String img) {
        this.img = img;
        return this;
    }

    @Override
    @Nonnull
    public User build() {
        User user = new User();

        user.setEmail(email);
        user.setLogin(login);
        user.setImg(img);

        return user;
    }

    @Override
    @Nonnull
    public User build(@Nonnull final User user) {
        User copy = new User();
        copy.setEmail(this.email);
        copy.setLogin(user.getLogin());
        copy.setImg(user.getImg());
        return copy;
    }
}
