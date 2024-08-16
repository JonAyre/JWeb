package com.jweb.ai;

import com.avatarai.Avatar;
import com.avatarai.TextImporter;
import com.google.gson.JsonObject;
import com.jweb.server.ServiceHandler;
import com.jweb.server.ServiceRequest;
import com.jweb.server.ServiceResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MicrobotHandler extends ServiceHandler
{
    Avatar microBot;

    public MicrobotHandler() throws IOException {
        addMethod("test-sentiment", this::testSentiment, "", "text");
        String avatarSettings = Files.readString(Path.of("web/sentiment-bot.json"));
        microBot = new Avatar(avatarSettings);
    }

    private ServiceResponse testSentiment(ServiceRequest request)
    {
        String text = request.params().get("text").get(0);

        try
        {
            double[] inputs = TextImporter.getEmbeddings(text);
            double[] outputs = microBot.present(inputs);
            JsonObject answer = new JsonObject();
            if (outputs[0] > outputs[1])
            {
                answer.addProperty("sentiment", "positive");
            }
            else
            {
                answer.addProperty("sentiment", "negative");
            }
            return new ServiceResponse(200, answer.toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ServiceResponse(504,"Error calling microbot service: " + e.getMessage());
        }
    }
}
