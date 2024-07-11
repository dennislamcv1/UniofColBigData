package io.collective;

import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SimpleAgedCache {

    private final Clock clock;
    private final List<ExpirableEntry> entries;

    public SimpleAgedCache(Clock clock) {
        this.clock = clock;
        this.entries = new ArrayList<>();
    }

    public SimpleAgedCache() {
        this(Clock.systemUTC());
    }

    public void put(Object key, Object value, int retentionInMillis) {
        Instant expirationTime = clock.instant().plusMillis(retentionInMillis);
        ExpirableEntry newEntry = new ExpirableEntry(key, value, expirationTime);
        entries.add(newEntry);
    }

    public boolean isEmpty() {
        removeExpiredEntries();
        return entries.isEmpty();
    }

    public int size() {
        removeExpiredEntries();
        return entries.size();
    }

    public Object get(Object key) {
        removeExpiredEntries();
        for (ExpirableEntry entry : entries) {
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }
        return null;
    }

    private void removeExpiredEntries() {
        Instant now = clock.instant();
        Iterator<ExpirableEntry> iterator = entries.iterator();
        while (iterator.hasNext()) {
            ExpirableEntry entry = iterator.next();
            if (entry.getExpirationTime().isBefore(now)) {
                iterator.remove();
            }
        }
    }

    private class ExpirableEntry {
        private final Object key;
        private final Object value;
        private final Instant expirationTime;

        public ExpirableEntry(Object key, Object value, Instant expirationTime) {
            this.key = key;
            this.value = value;
            this.expirationTime = expirationTime;
        }

        public Object getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }

        public Instant getExpirationTime() {
            return expirationTime;
        }
    }
}
