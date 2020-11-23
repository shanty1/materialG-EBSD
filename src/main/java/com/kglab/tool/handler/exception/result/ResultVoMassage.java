package com.kglab.tool.handler.exception.result;


public class ResultVoMassage extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	private int errorNo = 0; //默认返回值0
	private String errorMsg;
	private Object data;
	public ResultVoMassage(int errorNo,String errorMsg){
		super();
		this.errorNo = errorNo;
		this.errorMsg = errorMsg;
	}
	public ResultVoMassage(int errorNo,String errorMsg,Object data){
		super();
		this.errorNo = errorNo;
		this.errorMsg = errorMsg;
		this.data = data;
	}
	public int getErrorNo() {
		return errorNo;
	}
	public void setErrorNo(int errorNo) {
		this.errorNo = errorNo;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	

}
