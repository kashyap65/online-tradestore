package com.db;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.db.controller.TradeController;
import com.db.exception.TradeException;
import com.db.mapper.TradeMapper;
import com.db.model.TradeModel;
import com.db.service.TradeService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { TradeController.class, TradeService.class,TradeMapper.class})
@EnableConfigurationProperties
public class TradeControllerTest {
	
	@Autowired
	private TradeController tradeController;
	@Autowired
	private TradeService tradeService;
	
	@Test
	public void receiveTradeTestForMaturedTrade() throws Exception {
		TradeModel model = new TradeModel("T1", 1, "CP-1", "B1", parseDate("20/05/2020"), parseDate("20/02/2021"), "N");
		assertEquals(new ResponseEntity<>("Trade has been matured/expired.", HttpStatus.BAD_REQUEST), tradeController.receiveTrade(model));
	}
	
	@Test
	public void receiveTradeTestHappyPath() throws Exception {
		TradeModel model = new TradeModel("T2", 2, "CP-2", "B1", parseDate("20/05/2021"), parseDate("20/02/2021"), "N");
		assertEquals(new ResponseEntity<>("Trade has been added successfully.", HttpStatus.ACCEPTED), tradeController.receiveTrade(model));
	}
	
	@Test(expected=TradeException.class)
	public void receiveTradeTestForLowerVersionTrade() throws Exception {
		TradeModel latestmodel = new TradeModel("T2", 2, "CP-2", "B1", parseDate("20/05/2021"), parseDate("20/02/2021"), "N");
		TradeModel oldermodel = new TradeModel("T2", 1, "CP-1", "B1", parseDate("20/05/2021"), parseDate("14/03/2015"), "N");
		tradeService.receiveTrade(latestmodel);
		tradeController.receiveTrade(oldermodel);
	}

	public static Date parseDate(String date) {
		try {
			return new SimpleDateFormat("dd/MM/yyyy").parse(date);
		} catch (ParseException e) {
			return null;
		}
	}
}
