package testing;

import java.util.HashMap;

public class DataPacket extends HashMap<String, String> {
    private static final String DELIMITER = "~";

    // Serialize the HashMap to a String
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Entry<String, String> entry : this.entrySet()) {
            sb.append(entry.getKey()).append(DELIMITER).append(entry.getValue()).append(";");
        }
        return sb.toString();
    }

    // Deserialize a String to a HashMap
    public void fromString(String data) {
        this.clear();
        String[] keyValuePairs = data.split(";");
        for (String kv : keyValuePairs) {
            String[] parts = kv.split(DELIMITER);
            if (parts.length == 2) {
                this.put(parts[0], parts[1]);
            }
        }
    }

    public static void main(String[] args) {
        DataPacket data = new DataPacket();

        // Add key-value pairs
        data.put("name", "Alice");
        data.put("age", "30");
        data.put("city", "New York");

        // Serialize to a String
        String serializedData = data.toString();
        System.out.println("Serialized: " + serializedData);

        // Deserialize from a String
        DataPacket deserializedData = new DataPacket();
        deserializedData.fromString(serializedData);

        // Access the deserialized data
        System.out.println("Name: " + deserializedData.get("name"));
        System.out.println("Age: " + deserializedData.get("age"));
        System.out.println("City: " + deserializedData.get("city"));
    }
}

