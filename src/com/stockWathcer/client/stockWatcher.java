package com.stockWathcer.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;
import java.util.Date;


/**
 * Entry point classes define <code>onModuleLoad()</code>
 * <p>Входная точка приложения. Вся основная логика релизована здесь.</p>
 */
public class stockWatcher implements EntryPoint {

    /**
     * Время в милисикундах спустя которое нужно обновлять данные акций
     */
    private static final int REFRESH_INTERVAL = 5000; // ms
    /**
     * Основная вертикальная панель на которой расположена таблица и кнопка добавить
     */
    private VerticalPanel mainPanel = new VerticalPanel();
    /**
     * Таблица в которой будут храниться записи - акции
     */
    private FlexTable stocksFlexTable = new FlexTable();
    /**
     * Горизонтальная панель, на которую добавят кнопку "добавтиь" и поле с вводом новой акции
     */
    private HorizontalPanel addPanel = new HorizontalPanel();
    /**
     * Поле для ввода названия новой акции
     */
    private TextBox newSymbolTextBox = new TextBox();
    /**
     * Кнопка добавить. При нажатии на нее появляется возможность ввести новую название новой акции
     */
    private Button addStockButton = new Button("Add");
    /**
     * Пометка( надпись) которая хранит дату последнего обновления
     */
    private Label lastUpdatedLabel = new Label();
    /**
     * Список строк который хранит названия акций
     */
    private ArrayList<String> stocks = new ArrayList<String>();

    /**
     * This is the entry point method.
     * <p>Метод - точка входа в приложение.</p>
     */
    public void onModuleLoad() {
        // Создаем заголвок таблицы
        stocksFlexTable.setText(0, 0, "Symbol");
        stocksFlexTable.setText(0, 1, "Price");
        stocksFlexTable.setText(0, 2, "Change");
        stocksFlexTable.setText(0, 3, "Remove");

        // Add styles to elements in the stock list table.
        //Определяем css стили для заголовка, и всей таблицы
        stocksFlexTable.getRowFormatter().addStyleName(0, "watchListHeader");
        stocksFlexTable.addStyleName("watchList");
        stocksFlexTable.getCellFormatter().addStyleName(0, 1, "watchListNumericColumn");
        stocksFlexTable.getCellFormatter().addStyleName(0, 2, "watchListNumericColumn");
        stocksFlexTable.getCellFormatter().addStyleName(0, 3, "watchListRemoveColumn");

        // Assemble the Add Stock panel
        // Добавить на нижнию панель элементы: поля ввода имени акции и кнопку добавить
        addPanel.add(newSymbolTextBox);
        addPanel.add(addStockButton);
        addPanel.addStyleName("addPanel");// Добавить стиль на нижнюю панель
        // Assemble Add Stock panel.
        //TODO две нижние строки нужны? 0_0 я же уже добавил их
        addPanel.add(newSymbolTextBox);
        addPanel.add(addStockButton);

        // Assemble Main panel.
        // Добавим на основую панель таблицу и нижнюю часть
        mainPanel.add(stocksFlexTable);
        mainPanel.add(addPanel);// Добавить нижнюю часть
        mainPanel.add(lastUpdatedLabel);
        // Associate the Main panel with the HTML host page.
        // Прикрпепить к приложениюю? нашу основную панель
        RootPanel.get("stockList").add(mainPanel);
        // Move cursor focus to the input box.
        // Перевести курсор в поле ввода новой акции
        newSymbolTextBox.setFocus(true);

        Timer timer = new Timer() {
            @Override
            public void run() {
                refreshWatchList();
            }
        };

        // Обновлять таблицу через каждые REFRESH_INTERVAL милисекнуд
        timer.scheduleRepeating(REFRESH_INTERVAL);

        // Обработчик нажатия кнопки "добавить"
        addStockButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                addStock();
            }
        });

        // Обработчик нажатия клавиши Enter
        newSymbolTextBox.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    addStock();
                }
            }
        });
    }

    /**
     * <p>Метод которые обновляет данные в таблице цен</p>
     */
    private void refreshWatchList() {
        final double MAX_PRICE = 100.0; // $100.00
        final double MAX_PRICE_CHANGE = 0.02; // +/- 2%

        StockPrice[] prices = new StockPrice[stocks.size()];
        for (int i = 0; i < stocks.size(); i++) {
            double price = Random.nextDouble() * MAX_PRICE;
            double change = price * MAX_PRICE_CHANGE
                    * (Random.nextDouble() * 2.0 - 1.0);

            prices[i] = new StockPrice(stocks.get(i), price, change);
        }

        updateTable(prices);
    }

    /**
     * <p>Фкнция которая обновляет список ЦенНаАкции (class StockPrice)</p>
     * @param prices
     */
    private void updateTable(StockPrice[] prices) {
        for (int i = 0; i < prices.length; i++) {
            updateTable(prices[i]);
        }

        // Display timestamp showing last refresh.
        DateTimeFormat dateFormat = DateTimeFormat.getFormat(
                DateTimeFormat.PredefinedFormat.DATE_TIME_MEDIUM);
        lastUpdatedLabel.setText("Last update : "
                + dateFormat.format(new Date()));
    }

    /**
     * Update a single row in the stock table.
     * <p>Обновляет одну запись ЦенуНаАкцию (StockPrice)</p>
     * @param price Цена на акцию для одной строки
     */
    private void updateTable(StockPrice price) {
        // Make sure the stock is still in the stock table.
        // Убедиться, что акция все еще есть в списке акций
        if (!stocks.contains(price.getSymbol())) {
            return;
        }

        int row = stocks.indexOf(price.getSymbol()) + 1;

        // Format the data in the Price and Change fields.
        String priceText = NumberFormat.getFormat("#,##0.00").format(
                price.getPrice());
        NumberFormat changeFormat = NumberFormat.getFormat("+#,##0.00;-#,##0.00");
        String changeText = changeFormat.format(price.getChange());
        String changePercentText = changeFormat.format(price.getChangePercent());

        // Populate the Price and Change fields with new data.
        stocksFlexTable.setText(row, 1, priceText);
        Label changeWidget = (Label)stocksFlexTable.getWidget(row, 2);
        changeWidget.setText(changeText + " (" + changePercentText + "%)");

        // Change the color of text in the Change field based on its value.
        String changeStyleName = "noChange";
        if (price.getChangePercent() < -0.1f) {
            changeStyleName = "negativeChange";
        }
        else if (price.getChangePercent() > 0.1f) {
            changeStyleName = "positiveChange";
        }

        changeWidget.setStyleName(changeStyleName);
    }

    private void addStock() {
        final String symbol = newSymbolTextBox.getText().toUpperCase().trim();
        newSymbolTextBox.setFocus(true);

        // Stock code must be between 1 and 10 chars that are numbers, letters, or dots.
        if (!symbol.matches("^[0-9A-Z\\.]{1,10}$")) {
            Window.alert("'" + symbol + "' is not a valid symbol.");
            newSymbolTextBox.selectAll();
            return;
        }

        if (stocks.contains(symbol)) {
            newSymbolTextBox.setText("");
            return;
        }

        // Add the stock to the table.
        int row = stocksFlexTable.getRowCount();
        stocks.add(symbol);
        stocksFlexTable.setText(row, 0, symbol);
        stocksFlexTable.setWidget(row, 2, new Label());

        stocksFlexTable.getCellFormatter().addStyleName(row, 1, "watchListNumericColumn");
        stocksFlexTable.getCellFormatter().addStyleName(row, 2, "watchListNumericColumn");
        stocksFlexTable.getCellFormatter().addStyleName(row, 3, "watchListRemoveColumn");


        // Add a button to remove this stock from the table.
        Button removeStockButton = new Button("x");
        removeStockButton.addStyleDependentName("remove");
        removeStockButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                int removedIndex = stocks.indexOf(symbol);
                stocks.remove(removedIndex);
                stocksFlexTable.removeRow(removedIndex + 1);
            }
        });
        stocksFlexTable.setWidget(row, 3, removeStockButton);

        // Get the stock price.
        refreshWatchList();

        newSymbolTextBox.setText("");
    }

}
