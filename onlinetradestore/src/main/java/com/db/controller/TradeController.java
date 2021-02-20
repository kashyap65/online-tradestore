package com.db.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.db.exception.TradeException;
import com.db.model.TradeModel;
import com.db.service.TradeService;

@RestController
@RequestMapping(path="/onlinetradestore")
public class TradeController {

	@Autowired
	private TradeService tradeService;
	
	@GetMapping(path = "/getAllAvailableTrades", consumes = "application/json",produces = "application/json")
	public List<TradeModel> getAllAvailableTrades() throws TradeException {
		return tradeService.getAllAvailableTrades();
	}
	
	@PostMapping(path = "/receiveTrade", consumes = "application/json")
	public ResponseEntity<Object> receiveTrade(@RequestBody TradeModel tradeModel) throws TradeException {
		boolean status = tradeService.receiveTrade(tradeModel);
		if (status) {
			return new ResponseEntity<>("Trade has been added successfully.", HttpStatus.ACCEPTED);
		}
		return new ResponseEntity<>("Trade has been matured/expired.", HttpStatus.BAD_REQUEST);
	}
	
	/*
	 * This is the API which is scheduled to call after every 24 hours
	 * to check whether the trade stored in data store is expired or not.
	 * If expired, it'll change the flag.
	 * */
	@Scheduled(fixedRateString = "${fixedRate.in.milliseconds}")
	public void checkAndUpdateMaturedTrades() throws TradeException {
		tradeService.checkAndUpdateMaturedTrades();
	}
	
}
