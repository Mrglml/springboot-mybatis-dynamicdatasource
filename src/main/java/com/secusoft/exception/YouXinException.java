package com.secusoft.exception;

public class YouXinException extends RuntimeException
{
	private static final long serialVersionUID = 1L;
	
	private String code = "500";

	public YouXinException()
	{
		super();
	}

	public YouXinException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public YouXinException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public YouXinException(String message)
	{
		super(message);
	}
	
	public YouXinException(String message,String code)
	{
		super(message);
		this.code = code;
	}

	public YouXinException(Throwable cause)
	{
		super(cause);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
