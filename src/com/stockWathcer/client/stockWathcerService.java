package com.stockWathcer.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("stockWathcerService")
public interface stockWathcerService extends RemoteService {
    // Sample interface method of remote interface
    String getMessage(String msg);

    /**
     * Utility/Convenience class.
     * Use stockWathcerService.App.getInstance() to access static instance of stockWathcerServiceAsync
     */
    public static class App {
        private static stockWathcerServiceAsync ourInstance = GWT.create(stockWathcerService.class);

        public static synchronized stockWathcerServiceAsync getInstance() {
            return ourInstance;
        }
    }
}
