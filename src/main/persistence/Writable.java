package persistence;

import org.json.JSONObject;

// Code based on code found in demo project: JsonSerializationDemo
public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
