package kr.co.itcen.mysite.dto;

public class JSONResult {
	private String result; /* success of fail */
	private Object data; /* if success, set */
	private String message; /* if fail, set */

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
