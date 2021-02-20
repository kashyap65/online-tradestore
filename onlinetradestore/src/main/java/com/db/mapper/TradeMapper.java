package com.db.mapper;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.db.domain.Trade;
import com.db.model.TradeModel;

@Component
public class TradeMapper {

	public TradeModel convertToModel(Trade trade){
		TradeModel tradeModel = new TradeModel();
		BeanUtils.copyProperties(trade, tradeModel);
		return tradeModel;
	}
	
	public Trade convertToEntity(TradeModel model){
		Trade trade = new Trade();
		BeanUtils.copyProperties(model, trade);
		return trade;
	}
}
