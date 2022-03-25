package io.github.scifi9902.friendship.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import lombok.Getter;
import org.bson.Document;

import java.util.Collections;

@Getter
public class MongoHandler {

    private final MongoCollection<Document> profiles;

    /**
     * Constructs a new {@link MongoHandler} instance
     *
     * @param host host of the database
     * @param port port of the database
     * @param auth if auth is enabled
     * @param username the username if applicable
     * @param password the password if applicable
     * @param database name of the database
     */
    public MongoHandler(String host, int port, boolean auth, String username, String password, String database) {
        final MongoClient mongoClient;

        if (auth) {
            mongoClient = new MongoClient(new ServerAddress(host, port), Collections.singletonList(MongoCredential.createCredential(username,database,password.toCharArray())));
        } else {
            mongoClient = new MongoClient(new ServerAddress(host,port));
        }

        this.profiles = mongoClient.getDatabase(database).getCollection("profiles");
    }


}
