package com.jweb.chat;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jweb.server.ServiceHandler;
import com.jweb.server.ServiceRequest;
import com.jweb.server.ServiceResponse;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;

public class ChatHandler extends ServiceHandler
{
    private static final String OLLAMA_API_URL = "http://192.168.1.246:11434/api/chat";

    public ChatHandler()
    {
        addMethod("prompt", this::prompt, "", "prompt");
    }

    private ServiceResponse prompt(ServiceRequest request)
    {
        try
        {
            HttpClient client = HttpClient.newHttpClient();
            JsonObject requestBody = new JsonObject();
            String prompt = "Keeping your answer to one or two very short sentences, " + request.params().get("prompt").get(0);
            JsonArray prompts = new JsonArray(1);
            JsonObject message = new JsonObject();
            message.addProperty("role", "user");
            message.addProperty("content", prompt);
            prompts.add(message);
            requestBody.add("messages", prompts);
            HttpRequest chatRequest;

            requestBody.addProperty("model", "gemma2:2b");
            requestBody.addProperty("stream", false);
            chatRequest = HttpRequest.newBuilder()
                    .uri(new URI(OLLAMA_API_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(requestBody)))
                    .build();

            // Send the request
            HttpResponse<String> response = client.send(chatRequest, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200)
            {
                JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
                //String answer = responseBody.getAsJsonObject("message").get("content").getAsString();
                return new ServiceResponse(200, responseBody.getAsJsonObject("message").toString());
            } else {
                return new ServiceResponse(504,"Failed to get chat response: " + response.body());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ServiceResponse(504,"Error calling chat service: " + e.getMessage());
        }

    }
}
