package com.db.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.domain.Trade;
import com.db.exception.TradeException;
import com.db.mapper.TradeMapper;
import com.db.model.TradeModel;
import com.db.repository.TradeRepository;

@Service
public class TradeService {
	
	@Autowired
	private TradeMapper tradeMapper;
	
	private TradeRepository tradeRepository = new TradeRepository();
	
	public List<TradeModel> getAllAvailableTrades() throws TradeException {
		List<TradeModel> tradeModels = new ArrayList<TradeModel>();
		Collection<Trade> trades = tradeRepository.getTradeMap().values();
		trades.stream().forEach(t->{
			TradeModel model = new TradeModel();
			BeanUtils.copyProperties(t, model);
			tradeModels.add(model);
		});
		return tradeModels;
	}
	
	public boolean receiveTrade(TradeModel tradeModel) throws TradeException {
		HashMap<String, Trade> tradeMap = tradeRepository.getTradeMap();
		if (tradeModel != null && checkMaturityDate(tradeModel)) {
			if (tradeMap.containsKey(tradeModel.getTradeId())) {
				Trade tradeData = tradeMap.get(tradeModel.getTradeId());
				if (tradeModel.getVersion() < tradeData.getVersion()) {
					throw new TradeException();
				}
			} else {
				tradeMap.put(tradeModel.getTradeId(), tradeMapper.convertToEntity(tradeModel));
				tradeRepository.setTradeMap(tradeMap);
			}
			return true;
		}
		return false;
	}
	
	private boolean checkMaturityDate(TradeModel model){
		if(model!=null && model.getMaturityDate().compareTo(new Date())>0){
			return true;
		}
		return false;
	}
	
	public void checkAndUpdateMaturedTrades() throws TradeException {
		HashMap<String, Trade> tradeMap = tradeRepository.getTradeMap();
		List<Trade> trades = (List<Trade>) tradeMap.values();
		for(Trade trade:trades){
			if(trade.getMaturityDate().compareTo(new Date())<=0){
				trade.setExpiredFlag("Y");
			}
		}
	}

}
