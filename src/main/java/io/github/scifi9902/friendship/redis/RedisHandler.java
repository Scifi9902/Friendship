package io.github.scifi9902.friendship.redis;

import com.google.gson.Gson;
import io.github.scifi9902.friendship.FriendshipPlugin;
import io.github.scifi9902.friendship.profile.Profile;
import io.github.scifi9902.friendship.redis.action.ActionType;
import lombok.Getter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.regex.Pattern;

@Getter
public class RedisHandler {

    private final FriendshipPlugin instance;

    private final String password;

    private final String channel;

    private final JedisPool jedisPool;

    private final Gson gson;

    public RedisHandler(FriendshipPlugin instance, String host, int port, String password, String channel) {
        this.instance = instance;
        this.password = password;
        this.channel = channel;
        this.gson = this.instance.getGson();
        this.jedisPool = new JedisPool(host,port);
        this.connect();
    }

    private void connect() {
        new Thread(() -> {
            this.runCommand(jedis -> {
                jedis.subscribe(new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String message) {
                        String[] arguments = message.split(Pattern.quote("||"));
                        ActionType actionType = ActionType.valueOf(arguments[0]);

                        switch (actionType) {
                            case PROFILE_ADD: {
                                Profile profile = gson.fromJson(arguments[1], Profile.class);

                                if (profile == null) {
                                    break;
                                }
                                Optional<Profile> optional = instance.getFriendshipAPI().getProfileHandler().getProfileFromCache(profile.getUniqueId());

                                if (optional.isPresent()) {
                                    break;
                                }

                                instance.getFriendshipAPI().getProfileHandler().addToCache(profile.getUniqueId(), profile);
                                break;
                            }

                            case PROFILE_UPDATE: {
                                Profile updateWith = instance.getGson().fromJson(arguments[1], Profile.class);

                                Optional<Profile> optional = instance.getFriendshipAPI().getProfileHandler().getProfileFromCache(updateWith.getUniqueId());

                                if (!optional.isPresent()) {
                                    break;
                                }

                                Profile toUpdate = optional.get();

                                toUpdate.setFriends(updateWith.getFriends());
                                toUpdate.setFriendRequests(updateWith.getFriendRequests());
                                break;
                            }

                            case PROFILE_UNLOAD: {
                                Profile profile = gson.fromJson(arguments[1], Profile.class);

                                if (profile == null) {
                                    break;
                                }
                                Optional<Profile> optional = instance.getFriendshipAPI().getProfileHandler().getProfileFromCache(profile.getUniqueId());

                                if (!optional.isPresent()) {
                                    break;
                                }

                                instance.getFriendshipAPI().getProfileHandler().removeFromCache(profile.getUniqueId());
                                break;
                            }
                        }

                    }
                }, this.channel);
            });
        }).start();
    }

    public void runCommand(Consumer<Jedis> consumer) {
        try (Jedis jedis = this.jedisPool.getResource()){
            if (jedis != null) {
                consumer.accept(jedis);
            }
        }
    }

    public void publish(ActionType actionType, Object object) {
        this.runCommand(jedis -> jedis.publish(this.channel, actionType + "||" + this.gson.toJson(object)));
    }

}
