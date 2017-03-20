package com.epam.jmp.rest.rest;

import com.epam.jmp.rest.dao.UserDao;
import com.epam.jmp.rest.domain.User;
import com.epam.jmp.rest.domain.builder.UserBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;

@Controller
@Path("/users")
public class UserService {

    public static final String USER_NOT_FOUND_MSG = "User with email[%s] does not exist";
    public static final String USER_EXISTS_MSG = "User with email[%s] already exists";

    private UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) { this.userDao = userDao; }

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response create(User user) {

        if (userDao.create(user).isPresent()) {
            return Response.status(NO_CONTENT).build();
        }
        return Response.status(BAD_REQUEST)
                .entity(format(USER_EXISTS_MSG, user.getEmail()))
                .type(MediaType.TEXT_PLAIN).build();
    }


    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response readAll() {

        List<User> users = new ArrayList<>(userDao.readAll());
        GenericEntity<List<User>> usersEntity = new GenericEntity<List<User>>(users) {
        };
        return Response.ok(usersEntity).build();
    }


    @GET
    @Path("{email}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response readByOwner(@PathParam("email") String email) throws NotFoundException {

        Optional<User> optional = userDao.readByEmail(email);
        if (optional.isPresent()) {
            return Response.ok(optional.get()).build();
        }
        return Response.status(NOT_FOUND).entity(format(USER_NOT_FOUND_MSG, email)).build();
    }

    @DELETE
    @Path("{email}")
    public Response delete(@PathParam("email") String email) throws NotFoundException {

        if (userDao.delete(new UserBuilder(email).build())) {
            return Response.status(NO_CONTENT).build();
        }
        return Response.status(NOT_FOUND).entity(format(USER_NOT_FOUND_MSG, email)).build();
    }

    @PUT
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response update(User user) {

        if (userDao.update(user).isPresent()) {
            return Response.status(NO_CONTENT).build();
        }
        return Response.status(BAD_REQUEST)
                .entity(format(USER_EXISTS_MSG, user.getEmail()))
                .type(MediaType.TEXT_PLAIN).build();
    }

}
