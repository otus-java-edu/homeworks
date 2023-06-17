package ru.otus.services;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.core.repository.*;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.crm.model.PnL;
import ru.otus.crm.model.User;
import ru.otus.crm.model.UserData;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class ScheduledTasks {

    private final BinanceService binanceService;
    private final TradeRepository tradeRepository;
    private final UserRepository userRepository;
    private final UserDataRepository userDataRepository;
    private final SymbolRepository symbolRepository;
    private final PnLRepository pnlRepository;
    private final TransactionManager transactionManager;

    private final Map<Long, Float> bnbPrices = new HashMap<>();

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    @Scheduled(fixedDelay = 60000)
    public void updateTrades() {
        var users = userRepository.findAll();
        var symbols = symbolRepository.findAll();
        for (var user : users) {
            for(var symbol : symbols) {
                var defaultUserData = new UserData(new UserData.Pk(user.getId(), symbol.getId()), 0L, 0L, 0L);
                defaultUserData.setNew(true);
                log.info("process trades for {} symbol {}", user.getName(), symbol.getName());
                while(true){
                    var trades = binanceService.getTrades(user, symbol, userDataRepository.findById(new UserData.Pk(user.getId(), symbol.getId()))
                            .orElse(defaultUserData));
                    if (trades == null)
                        break;
                    if (trades.size() > 0){
                        boolean status = transactionManager.doInTransaction(() ->{
//                            tradeRepository.saveAll(trades);
                            trades.forEach(t-> {
                                t.setPk(user.getId(), symbol.getId());
                                t.setNew(true);
                                tradeRepository.saveEntry(t);
                            });
                            var lastTrade = trades.get(trades.size() - 1);
                            var userData = userDataRepository.findById(new UserData.Pk(user.getId(), symbol.getId()))
                                    .orElse(defaultUserData);
                            userData.setLastTrade(lastTrade.getId().id());
                            userData.setLastSyncTime(lastTrade.getTime());
                            if (userData.isNew())
                            {
                                userDataRepository.saveEntry(userData);
                                userData.setNew(false);
                            }
                            else
                                userDataRepository.update(userData);
                            return true;
                        });
                    }else {
                        var userData = userDataRepository.findById(new UserData.Pk(user.getId(), symbol.getId()))
                                .orElse(defaultUserData);
                        userData.setLastSyncTime(Instant.now().toEpochMilli());
                        if (userData.isNew())
                        {
                            userDataRepository.saveEntry(userData);
                            userData.setNew(false);
                        }
                        else
                            userDataRepository.update(userData);
                        break;
                    }
                }
            }
            if (!user.isInitialized()){
                user.setInitialized(true);
                userRepository.save(user);
            }
        }
    }
    @Scheduled(fixedDelay = 60000, initialDelay = 0)
    public void updatePnL() {
        var users = userRepository.findAll();
        var symbols = symbolRepository.findAll();
        var calendar = Calendar.getInstance();
        for (var user : users) {
            if (!user.isInitialized())
                continue;
            for(var symbol : symbols) {
                log.info("process pnl for {} account symbol {}", user.getName(), symbol.getName());
                var userData = userDataRepository.findById(new UserData.Pk(user.getId(), symbol.getId()));
                if (userData.isEmpty())
                    continue;
                var lastPnlTime = userData.get().getLastPnLTime();
                if (lastPnlTime == null)
                    lastPnlTime = 0L;
                var trades = tradeRepository.findByStartTime(user.getId(), symbol.getId(), lastPnlTime);
                if (trades.size() == 0)
                    continue;
                var newPnlTime = lastPnlTime;
                var profit = 0f;
                var volume = 0f;
                var commissions = 0f;
                var entryPrice = 0f;
                if (lastPnlTime == 0L){
                    calendar.setTime(getDate(trades.get(0).getTime()));
                    calendar.add(Calendar.DATE, 1);
                    newPnlTime = calendar.getTime().getTime();
                }else{
                    calendar.setTime(new Date(lastPnlTime));
                    calendar.add(Calendar.DATE, 1);
                    newPnlTime = calendar.getTime().getTime();
                    if (newPnlTime > userData.get().getLastSyncTime())
                        continue;
                    var pnl = pnlRepository.findById(new PnL.Pk(user.getId(), symbol.getId(), lastPnlTime)).get();
                    profit = pnl.getProfit();
                    volume = pnl.getVolume();
                    commissions = pnl.getCommissions();
                    entryPrice = pnl.getEntryPrice();
                }
                for (int i = 0; i < trades.size(); i++){
                    var trade = trades.get(i);
                    var shouldExit = false;
                    while (trade.getTime() > newPnlTime){
                        var pnl = new PnL(new PnL.Pk(user.getId(), symbol.getId(), newPnlTime), profit, volume, entryPrice, commissions);
                        pnl.setNew(true);
                        Long finalNewPnlTime = newPnlTime;
                        boolean status = transactionManager.doInTransaction(() ->{
                            var ud = userDataRepository.findById(new UserData.Pk(user.getId(), symbol.getId())).get();
                            ud.setLastPnLTime(finalNewPnlTime);
                            userDataRepository.update(ud);
                            pnlRepository.saveEntry(pnl);
                            return true;
                        });
                        calendar.setTime(new Date(newPnlTime));
                        calendar.add(Calendar.DATE, 1);
                        newPnlTime = calendar.getTime().getTime();
                        if (newPnlTime > userData.get().getLastSyncTime()){
                            shouldExit = true;
                            break;
                        }
                    }
                    if (shouldExit)
                        break;
                    if (trade.getCommissionAsset().contains("USD")){
                        profit -= trade.getCommission();
                        commissions += trade.getCommission();
                    }
                    if (trade.getCommissionAsset().contains("BNB")){
                        var bnbPrice = getBNBPrice(user, trade.getTime());
                        profit -= trade.getCommission() * bnbPrice;
                        commissions += trade.getCommission() * bnbPrice;
                    }

                    if (volume >= 0f){
                        if (trade.getDirection() > 0){
                            entryPrice = (entryPrice * volume + trade.getVolume() * trade.getPrice()) / (volume + trade.getVolume());
                            volume += trade.getVolume();
                        }else {
                            if (volume > trade.getVolume()){
                                volume -= trade.getVolume();
                                profit += (trade.getPrice() - entryPrice) * trade.getVolume();
                            }else{
                                profit += (trade.getPrice() - entryPrice) * volume;
                                entryPrice = trade.getPrice();
                                volume -= trade.getVolume();
                            }
                        }
                    }else{
                        if (trade.getDirection() < 0){
                            entryPrice = (trade.getVolume() * trade.getPrice() - entryPrice * volume) / (trade.getVolume() - volume);
                            volume -= trade.getVolume();
                        }else{
                            if (volume * -1 > trade.getVolume()){
                                volume += trade.getVolume();
                                profit += (entryPrice - trade.getPrice()) * trade.getVolume();
                            }else{
                                profit += (entryPrice - trade.getPrice()) * volume;
                                entryPrice = trade.getPrice();
                                volume += trade.getVolume();
                            }
                        }
                    }
                }
                while (newPnlTime < userData.get().getLastSyncTime()){
                    var pnl = new PnL(new PnL.Pk(user.getId(), symbol.getId(), newPnlTime), profit, volume, entryPrice, commissions);
                    pnl.setNew(true);
                    Long finalNewPnlTime = newPnlTime;
                    boolean status = transactionManager.doInTransaction(() ->{
                        var ud = userDataRepository.findById(new UserData.Pk(user.getId(), symbol.getId())).get();
                        ud.setLastPnLTime(finalNewPnlTime);
                        userDataRepository.update(ud);
                        pnlRepository.saveEntry(pnl);
                        return true;
                    });
                    calendar.setTime(new Date(newPnlTime));
                    calendar.add(Calendar.DATE, 1);
                    newPnlTime = calendar.getTime().getTime();
                }
            }
        }
    }

    private Float getBNBPrice(User user, Long timestamp){
        var maxAvailable = bnbPrices.keySet().stream().filter(t->t<=timestamp).max(Long::compareTo);
        if (maxAvailable.isEmpty()){
            var result = binanceService.getKLines(user, "BNBBUSD", "5m", timestamp - 20 * 60 * 1000);
            bnbPrices.putAll(result);
            maxAvailable = bnbPrices.keySet().stream().filter(t->t<=timestamp).max(Long::compareTo);
        }
        while(timestamp - maxAvailable.get() > 1000*60*20){
            var result = binanceService.getKLines(user, "BNBBUSD", "5m", maxAvailable.get());
            if (result == null)
                continue;
            bnbPrices.putAll(result);
            maxAvailable = bnbPrices.keySet().stream().filter(t->t<=timestamp).max(Long::compareTo);
        }
        return bnbPrices.get(maxAvailable.get());
    }
    private Date getDate(Long timestamp){
        var ms = timestamp % 1000;
        var seconds = (timestamp / 1000) % 60 ;
        var minutes = ((timestamp / (1000*60)) % 60);
        var hours   = ((timestamp / (1000*60*60)) % 24);
        return new Date(timestamp - ms - seconds * 1000 - minutes * 1000 * 60 - hours * 1000 * 60 * 60);
    }
}
