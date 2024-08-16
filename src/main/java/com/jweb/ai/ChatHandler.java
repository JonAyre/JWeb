package com.jweb.ai;

import com.google.gson.Gson;
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
    private record Message(String role, String content){}
    private record Conversation(ArrayList<Message> messages){}

    HashMap<String, Conversation> conversations = new HashMap<>();

    private static final String OLLAMA_API_URL = "http://192.168.1.246:11434/api/chat";

    public ChatHandler()
    {
        addMethod("prompt", this::prompt, "", "prompt");
    }

    private ServiceResponse prompt(ServiceRequest request)
    {
        Conversation conversation;
        String prompt = request.params().get("prompt").get(0);

        if (conversations.containsKey(request.user()))
        {
            conversation = conversations.get(request.user());
        }
        else
        {
            conversation = new Conversation(new ArrayList<>());
            prompt = "Keeping your answer to one or two short sentences, " + prompt;
            conversations.put(request.user(), conversation);
        }
        conversation.messages.add(new Message("user", prompt));

        try
        {
            HttpClient client = HttpClient.newHttpClient();
            JsonObject requestBody = new JsonObject();
            requestBody.add("messages", gson.toJsonTree(conversation.messages()));
            HttpRequest chatRequest;

            JsonObject options = new JsonObject();
            requestBody.add("options", options);

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
                JsonObject message = responseBody.getAsJsonObject("message");
                conversation.messages.add(new Message(message.get("role").getAsString(), message.get("content").getAsString()));
                return new ServiceResponse(200, message.toString());
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
