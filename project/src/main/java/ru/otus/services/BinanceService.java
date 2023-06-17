package ru.otus.services;


import com.binance.connector.futures.client.exceptions.BinanceClientException;
import com.binance.connector.futures.client.exceptions.BinanceConnectorException;
import com.binance.connector.futures.client.impl.UMFuturesClientImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.crm.model.Symbol;
import ru.otus.crm.model.Trade;
import ru.otus.crm.model.User;
import ru.otus.crm.model.UserData;

import java.util.*;

@Service
public class BinanceService {
    private static final Logger log = LoggerFactory.getLogger(BinanceService.class);
    public static final String UM_BASE_URL = "https://fapi.binance.com";
    private final Gson gson;
    public BinanceService(){
        this.gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
    }
    public List<Trade> getTrades(User user, Symbol symbol, UserData userData){
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();

        UMFuturesClientImpl client = new UMFuturesClientImpl(user.getApiKey(), user.getSecretKey(), UM_BASE_URL);

        parameters.put("symbol", symbol.getName().toUpperCase());
        parameters.put("limit", 1000);
        parameters.put("fromId", userData.getLastTrade() + 1);

        try {
            var stringResult = client.account().accountTradeList(parameters);
            stringResult = stringResult.replace("BUY", "1").replace("SELL", "-1");
            return gson.fromJson(stringResult, new TypeToken<ArrayList<Trade>>(){}.getType());
        } catch (BinanceConnectorException e) {
            log.error("fullErrMessage: {}", e.getMessage(), e);
            return null;
        } catch (BinanceClientException e) {
            log.error("fullErrMessage: {} \nerrMessage: {} \nerrCode: {} \nHTTPStatusCode: {}",
                    e.getMessage(), e.getErrMsg(), e.getErrorCode(), e.getHttpStatusCode(), e);
            return null;
        }
    }
    public Map<Long, Float> getKLines(User user, String symbol, String interval, Long startFrom){
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();

        UMFuturesClientImpl client = new UMFuturesClientImpl(user.getApiKey(), user.getSecretKey(), UM_BASE_URL);

        parameters.put("symbol", symbol);
        parameters.put("limit", 1000);
        parameters.put("interval", interval);
        parameters.put("startTime", startFrom);

        try {
            var stringResult = client.market().klines(parameters).replace("],", ";")
                    .replace("[","").replace(" ", "").replace("]", "").replace("\"", "");
            var splits = stringResult.split(";");
            var result = new HashMap<Long, Float>();
            for(var s : splits){
                var ss = s.split(",");
                result.put(Long.parseLong(ss[0]), Float.parseFloat(ss[1]));
            }
            return result;
        } catch (BinanceConnectorException e) {
            log.error("fullErrMessage: {}", e.getMessage(), e);
            return null;
        } catch (BinanceClientException e) {
            log.error("fullErrMessage: {} \nerrMessage: {} \nerrCode: {} \nHTTPStatusCode: {}",
                    e.getMessage(), e.getErrMsg(), e.getErrorCode(), e.getHttpStatusCode(), e);
            return null;
        }
    }
}