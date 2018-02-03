package com.stockWathcer.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.stockWathcer.client.StockPrice;
import com.stockWathcer.client.StockPriceService;

import java.util.Random;

public class StockPriceServiceImpl extends RemoteServiceServlet implements StockPriceService {
    /**
     * Максимальная цена акции
     */
    private static final double MAX_PRICE = 100.0;
    /**
     * Максимальное изменение цены акции в процентах
     */
    private static final double MAX_CHANGE = 0.02;
    @Override
    public StockPrice[] getPrices(String[] symbols) {
        Random rnd = new Random();

        StockPrice[] prices = new StockPrice[symbols.length];
        for (int i=0; i<symbols.length; i++) {
            double price = rnd.nextDouble() * MAX_PRICE;
            double change = price * MAX_CHANGE * (rnd.nextDouble() * 2f - 1f);

            prices[i] = new StockPrice(symbols[i], price, change);
        }

        return prices;
    }
}
