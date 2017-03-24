package com.cpqi.andes.viewmodel;

/**
 * <p>
 * ResponseViewModel
 * </p>
 *
 * @author Yury Jefse <ysilva@cpqi.com>
 * @since 0.1
 * @version 0.1
 */
public class ResponseViewModel {
	private String erroMsg;

	public String getMsg() {
		return erroMsg;
	}

	public void setMsg(String msg) {
		this.erroMsg = msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ResponseViewModel [erroMsg=" + erroMsg + "]";
	}

}
