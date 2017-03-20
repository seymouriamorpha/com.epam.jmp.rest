package com.epam.jmp.rest.dao;

import com.epam.jmp.rest.domain.User;
import com.epam.jmp.rest.domain.builder.UserBuilder;
import com.epam.jmp.rest.util.AppUtils;
import com.mongodb.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

public class UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    private MongoClient mongoClient;
    private String dbName;
    private String collectionName;
    private DBCollection usersCollection;

    public UserDao() {}

    public void init() throws UnknownHostException {
        DB usersDatabase = mongoClient.getDB(dbName);
        usersCollection = usersDatabase.getCollection(collectionName);
    }

    public Optional<User> create(@Nonnull final User userToCreate) {
        checkNotNull(userToCreate, "Argument[userToCreate] must not be null");

        User user = new UserBuilder(userToCreate.getEmail()).build(userToCreate);
        user.set_id(user.getEmail());

        try {
            usersCollection.insert(AppUtils.toDBObject(user));
            logger.info("Added new user{}", userToCreate);
            return Optional.of(userToCreate);
        } catch (MongoException.DuplicateKey e) {
            logger.info("User with email[{}] already exists", userToCreate.getEmail());
            return Optional.empty();
        }
    }

    public Optional<User> update(@Nonnull User userToUpdate){
        checkNotNull(userToUpdate, "Argument[userToUpdate] must not be null");

        User user = new UserBuilder(userToUpdate.getEmail()).build(userToUpdate);

        DBObject query1 = new BasicDBObject("email", user.getEmail());
        DBObject dbObject1 = usersCollection.findOne(query1);

        if (usersCollection.findOne(dbObject1) == null){
            logger.info("User with email[{}] not present", userToUpdate.getEmail());
            return Optional.empty();
        } else {
            DBObject query = new BasicDBObject("email", user.getEmail());
            DBObject dbObject = usersCollection.findOne(query);
            usersCollection.update(dbObject, AppUtils.toDBObject(user), false, false);
            logger.info("User with email[{}] updated", userToUpdate.getEmail());
            return Optional.of(userToUpdate);
        }
    }

    public List<User> readAll() {
        final List<User> users = new ArrayList<>();

        try (DBCursor cursor = usersCollection.find()) {
            while (cursor.hasNext()) {
                DBObject dbObject = cursor.next();
                users.add((User) AppUtils.fromDBObject(dbObject, User.class));
            }
        }
        logger.info("Retrieved [{}] users", users.size());
        return users;
    }

    public Optional<User> readByEmail(@Nonnull final String email) {
        checkNotNull(email, "Argument[email] must not be null");

        DBObject query = new BasicDBObject("email", email);
        DBObject dbObject = usersCollection.findOne(query);

        if (dbObject != null) {
            User user = (User) AppUtils.fromDBObject(dbObject, User.class);
            logger.info("Retrieved user for email[{}]:{}", email, user);
            return Optional.of(user);
        }
        logger.info("User with email[{}] does not exist", email);
        return Optional.empty();
    }

    public boolean delete(@Nonnull final User userToDelete) {
        checkNotNull(userToDelete, "Argument[userToDelete] must not be null");

        DBObject query = new BasicDBObject("_id", userToDelete.getEmail());
        WriteResult result = usersCollection.remove(query);

        if (result.getN() == 1) {
            logger.info("Deleted user with email[{}]", userToDelete.getEmail());
            return true;
        }
        logger.info("User with email[{}] does not exist", userToDelete.getEmail());
        return false;
    }

    public void setMongoClient(final MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public void setDbName(final String dbName) {
        this.dbName = dbName;
    }

    public void setCollectionName(final String collectionName) {
        this.collectionName = collectionName;
    }

}
