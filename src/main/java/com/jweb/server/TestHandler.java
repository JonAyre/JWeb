package com.jweb.server;

public class TestHandler extends ServiceHandler
{
    public TestHandler() {
        addMethod("", this::alive);
    }

    private ServiceResponse alive(ServiceRequest request)
    {
        return new ServiceResponse(200,
                "I'm alive!!\n\nTest service successfully pinged\n");
    }
}
