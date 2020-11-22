package per.sc.tool.entity.vo;
public class ResultVo {

	private int errorNo = 0; //默认返回值0
	private String errorMsg;
	private Object data;
	
	public ResultVo(){ super(); }
	
	public ResultVo(int errorNo,String errorMsg){
		super();
		this.errorNo = errorNo;
		this.errorMsg = errorMsg;
	}
	public ResultVo(int errorNo,String errorMsg,Object data){
		super();
		this.errorNo = errorNo;
		this.errorMsg = errorMsg;
		this.data = data;
	}
	public void setResultVo(int errorNo,String errorMsg,Object data){
		this.errorNo = errorNo;
		this.errorMsg = errorMsg;
		this.data = data;
	}
	public void setResultVo(int errorNo,String errorMsg){
		setResultVo(errorNo, errorMsg, null);
	}
	public Object getData() {
		return data ;
	}
	public void setData(Object data) {
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
}
