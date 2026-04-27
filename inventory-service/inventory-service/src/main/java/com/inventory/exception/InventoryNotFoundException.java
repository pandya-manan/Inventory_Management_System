package com.inventory.exception;

@SuppressWarnings("serial")
public class InventoryNotFoundException extends RuntimeException{
	
	public InventoryNotFoundException(String message)
	{
		super(message);
	}

}
