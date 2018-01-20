package com.stockWathcer.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.stockWathcer.client.stockWathcerService;

public class stockWathcerServiceImpl extends RemoteServiceServlet implements stockWathcerService {
    // Implementation of sample interface method
    public String getMessage(String msg) {
        return "Client said: \"" + msg + "\"<br>Server answered: \"Hi!\"";
    }
}