package io.github.scifi9902.friendship.redis;

import io.github.scifi9902.friendship.FriendshipPlugin;
import io.github.scifi9902.friendship.redis.action.ActionType;
import lombok.Getter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.util.function.Consumer;
import java.util.regex.Pattern;

@Getter
public class RedisHandler {

    private final String password;

    private final String channel;

    private final JedisPool jedisPool;

    public RedisHandler(FriendshipPlugin instance, String host, int port, String password, String channel) {
        this.password = password;
        this.channel = channel;

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

}
