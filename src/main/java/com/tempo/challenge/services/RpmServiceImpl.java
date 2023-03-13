package com.tempo.challenge.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RpmServiceImpl implements RpmService {
    private static final String KEY = "tempo_challenge_quote_key";
    private static final Integer ONE_MINUTE = 1;
    private static final Long FIRST = 1l;

    private final RedisTemplate<String, Integer> redis;
    private final Long limit;

    @Autowired
    public RpmServiceImpl(final RedisTemplate<String, Integer> redis, @Value("${app.quote}") final Long limit) {
        this.redis = redis;
        this.limit = limit;
    }

    @Override
    public boolean canProcess() {
        Long value = redis.opsForValue().increment(KEY);
        if (FIRST.equals(value)) redis.expire(KEY, ONE_MINUTE, TimeUnit.MINUTES);
        boolean gtLimit = limit < value;
        if (gtLimit) rollback();
        return !gtLimit;
    }

    @Override
    public void rollback() {
        redis.opsForValue().decrement(KEY);
    }
}
