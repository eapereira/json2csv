package com.xlib.json2csv.exceptions;

/**
 * 
 * @author <a href="eap.eapereira@gmail.com">Edson Alves Pereira</a>
 *
 */
public class JSON2CSVException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5268850367418657878L;

	public JSON2CSVException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public JSON2CSVException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public JSON2CSVException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public JSON2CSVException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public JSON2CSVException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public JSON2CSVException(String message, Object... args) {
		super(String.format(message, args));
	}
}
