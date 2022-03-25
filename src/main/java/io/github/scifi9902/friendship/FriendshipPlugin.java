package io.github.scifi9902.friendship;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;
import io.github.scifi9902.command.CommandHandler;
import io.github.scifi9902.friendship.api.FriendshipAPI;
import io.github.scifi9902.friendship.mongo.MongoHandler;
import io.github.scifi9902.friendship.profile.ProfileHandler;
import io.github.scifi9902.friendship.redis.RedisHandler;
import io.github.scifi9902.friendship.utilities.Config;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class FriendshipPlugin extends JavaPlugin {

    private Config config;

    private Config messages;

    private FriendshipAPI friendshipAPI;

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .setLongSerializationPolicy(LongSerializationPolicy.STRING)
            .disableHtmlEscaping()
            .create();

    private MongoHandler mongoHandler;

    private RedisHandler redisHandler;


    public void onEnable() {
        this.config = new Config(this, "config");
        this.messages = new Config(this, "messages");

        CommandHandler commandHandler = new CommandHandler(this, "friendship");

        this.mongoHandler = new MongoHandler(this.config.getString("mongo.host"), this.config.getInt("mongo.port"), this.config.getBoolean("mongo.auth"), this.config.getString("mongo.username"), this.config.getString("mongo.password"), this.config.getString("mongo.database"));
        this.redisHandler = new RedisHandler(this, this.config.getString("redis.host"), this.config.getInt("redis.port"), this.config.getString("redis.password"), this.config.getString("redis.channel"));

        this.friendshipAPI = new FriendshipAPI();
        this.friendshipAPI.setProfileHandler(new ProfileHandler(this));
    }

    public void onDisable() {
        this.friendshipAPI.getProfileHandler().unload();
    }

}
