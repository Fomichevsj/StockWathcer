package com.stockWathcer.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface stockWathcerServiceAsync {
    void getMessage(String msg, AsyncCallback<String> async);
}
