package com.veeva.petstore.context;

import java.util.HashMap;
import java.util.Map;

/**
 * ScenarioContext acts as a shared storage (like a blackboard)
 * between different step definition classes within a single scenario.
 * Example: Step 1 creates a pet and stores its ID here.
 *          Step 2 reads the ID from here to do a GET request.
 */
public class ScenarioContext {

    private final Map<String, Object> context = new HashMap<>();

    public void set(String key, Object value) {
        context.put(key, value);
    }

    public Object get(String key) {
        return context.get(key);
    }

    public String getString(String key) {
        return (String) context.get(key);
    }

    public Long getLong(String key) {
        Object val = context.get(key);
        if (val instanceof Integer) return ((Integer) val).longValue();
        return (Long) val;
    }

    public Integer getInt(String key) {
        return (Integer) context.get(key);
    }

    public void clear() {
        context.clear();
    }
}
