package com.syberpunk.content;

import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class LoginAttemptService {
    private record Attempt(int failures, long expires) {}
    private final ConcurrentHashMap<String, Attempt> attempts = new ConcurrentHashMap<>();
    public boolean blocked(String address) {
        Attempt attempt = attempts.get(address);
        return attempt != null && attempt.expires() > System.currentTimeMillis() && attempt.failures() >= 5;
    }
    public void failed(String address) {
        long now = System.currentTimeMillis();
        attempts.entrySet().removeIf(entry -> entry.getValue().expires() <= now);
        if (attempts.size() >= 10000 && !attempts.containsKey(address)) return;
        attempts.compute(address, (key, previous) -> new Attempt(previous == null ? 1 : previous.failures() + 1, now + 300000));
    }
    public void succeeded(String address) { attempts.remove(address); }
}
