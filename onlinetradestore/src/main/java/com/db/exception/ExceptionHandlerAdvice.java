package com.db.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice {

	@ExceptionHandler(value = TradeException.class)
	   public ResponseEntity<Object> exception(TradeException exception) {
	      return new ResponseEntity<>("Received trade has lower version compare to the trade available in the store for given Trade id.", HttpStatus.CONFLICT);
	   }
}


