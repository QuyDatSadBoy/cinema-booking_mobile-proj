package com.example.cinema_booking_mobile.service.perplexity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PerplexityRequest {
    @SerializedName("model")
    private String model;

    @SerializedName("messages")
    private List<Message> messages;

    @SerializedName("max_tokens")
    private int maxTokens;

    @SerializedName("temperature")
    private double temperature;

    @SerializedName("top_p")
    private double topP;

    @SerializedName("top_k")
    private int topK;

    public PerplexityRequest(String prompt) {
        this.model = "sonar-pro";

        // Convert prompt to chat format
        this.messages = new ArrayList<>();

        // Split system instruction from user query if prompt contains "User question:"
        if (prompt.contains("User question:")) {
            String[] parts = prompt.split("User question:", 2);
            String systemPrompt = parts[0].trim();
            String userQuery = parts[1].trim();

            this.messages.add(new Message("system", systemPrompt));
            this.messages.add(new Message("user", userQuery));
        } else {
            // Fallback to simple user message
            this.messages.add(new Message("user", prompt));
        }

        this.maxTokens = 1000;
        this.temperature = 0.7;
        this.topP = 0.9;
        this.topK = 0;
    }

    // Inner class to represent a message
    public static class Message {
        @SerializedName("role")
        private String role;

        @SerializedName("content")
        private String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    // Getters and setters

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public int getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(int maxTokens) {
        this.maxTokens = maxTokens;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getTopP() {
        return topP;
    }

    public void setTopP(double topP) {
        this.topP = topP;
    }

    public int getTopK() {
        return topK;
    }

    public void setTopK(int topK) {
        this.topK = topK;
    }
}
