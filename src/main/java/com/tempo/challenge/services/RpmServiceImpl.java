package com.tempo.challenge.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RpmServiceImpl implements RpmService {
    private static final Long FIRST = 1l;

    private final RedisTemplate<String, Integer> redis;

    @Autowired
    public RpmServiceImpl(final RedisTemplate<String, Integer> redis) {
        this.redis = redis;
    }

    @Override
    public boolean canProcess() {
        //TODO
        Long v = redis.opsForValue().increment("key"); //Obtengo e incremento atomicamente
        if (FIRST.equals(v)) redis.expire("key", 1, TimeUnit.MINUTES); //Si es la primera le pongo expiracion
        if (4l == v) rollback(); //Si supera el limite tengo que volver atras (por si alguna falla) y retorno false
        return false;
    }

    @Override
    public void rollback() {

    }
}
