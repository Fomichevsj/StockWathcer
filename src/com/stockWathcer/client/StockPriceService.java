package com.stockWathcer.client;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("stockPrice")
public interface StockPriceService {
    StockPrice[] getPrices(String[] symbols);
}
