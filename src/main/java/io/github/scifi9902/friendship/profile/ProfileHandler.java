package io.github.scifi9902.friendship.profile;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import io.github.scifi9902.friendship.FriendshipPlugin;
import org.bson.Document;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;

public class ProfileHandler {

    private final Map<UUID, Profile> profileMap = new ConcurrentHashMap<>();

    private final FriendshipPlugin instance;

    /**
     * Constructs a new {@link ProfileHandler} instance
     *
     * @param instance instance of the plugin
     */
    public ProfileHandler(FriendshipPlugin instance) {
        this.instance = instance;
    }

    /**
     * Add a players profile to the cache
     *
     * @param uniqueId the player to add to the cache
     * @param profile  the players profile to add
     */
    public void addToCache(UUID uniqueId, Profile profile) {
        if (uniqueId == null || profile == null) {
            return;
        }

        this.profileMap.put(uniqueId, profile);
    }

    /**
     * Remove a players profile from the cache
     *
     * @param uniqueId the player to remove from the cache
     */
    public void removeFromCache(UUID uniqueId) {
        if (uniqueId == null) {
            return;
        }

        this.profileMap.remove(uniqueId);
    }

    /**
     * Fetch a players profile from the database
     *
     * @param uniqueId the player to search for
     * @return a CompletableFuture with the players profile
     */
    public CompletableFuture<Profile> getProfileFromDatabase(UUID uniqueId) {
        return CompletableFuture.supplyAsync(() -> {
            Document document = this.instance.getMongoHandler().getProfiles().find(Filters.eq("_id", uniqueId.toString())).first();

            if (document == null) {
                return null;
            }

            return this.instance.getGson().fromJson(document.toJson(), Profile.class);
        });
    }

    /**
     * Get a players profile from the cache
     *
     * @param uniqueId player to check the cache for
     * @return an optional containing the results
     */
    public Optional<Profile> getProfileFromCache(UUID uniqueId) {
        if (profileMap.containsKey(uniqueId)) {
            return Optional.of(profileMap.get(uniqueId));
        }
        return Optional.empty();
    }

    /**
     * Save a specific profile to the database
     *
     * @param profile the profile to save
     */
    public void saveToDatabase(Profile profile) {
        ForkJoinPool.commonPool().execute(() -> {
            this.instance.getMongoHandler().getProfiles().replaceOne(Filters.eq("_id", profile.getUniqueId().toString()),
                    Document.parse(this.instance.getGson().toJson(profile)), new UpdateOptions().upsert(true));
        });
    }

    /**
     * Save all the loaded profiles to the database
     */
    public void unload() {
        for (Profile profile : this.profileMap.values()) {
            this.saveToDatabase(profile);
        }
    }


}
