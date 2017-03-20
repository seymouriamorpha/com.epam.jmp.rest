package com.epam.jmp.rest.domain;

import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;

import javax.annotation.Nullable;
import javax.xml.bind.annotation.*;
import java.io.Serializable;

import static com.google.common.base.Strings.emptyToNull;

@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {"login", "email", "img"})
public class User implements Serializable, Comparable<User>{

    private String _id;
    private String login;
    private String email;
    private String img;

    @XmlTransient
    public String get_id() {
        return _id;
    }

    public void set_id(final String _id) {
        this._id = _id;
    }

    @XmlElement(name = "login")
    public String getLogin() {
        return login;
    }

    public void setLogin(final String login) {
        this.login = login;
    }

    @Nullable
    @XmlElement(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(@Nullable final String email) {
        this.email = emptyToNull(email);
    }

    @Nullable
    @XmlElement(name = "img")
    public String getImg() {
        return img;
    }

    public void setImg(final String img) {
        this.img = img;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        User that = (User) o;
        return Objects.equal(this.email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(email);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("login", login)
                .add("email", email)
                .add("img", img)
                .toString();
    }

    @Override
    public int compareTo(final User that) {
        return ComparisonChain.start()
                .compare(this.email, that.email)
                .result();
    }

}
