package com.db.repository;

import java.util.HashMap;

import com.db.domain.Trade;

public class TradeRepository {

	private HashMap<String,Trade> tradeMap = new HashMap<String,Trade>();
	
	public TradeRepository() {
	}

	
	public HashMap<String,Trade> getTradeMap() {
		return tradeMap;
	}


	public void setTradeMap(HashMap<String, Trade> tradeMap) {
		this.tradeMap = tradeMap;
	}

}
