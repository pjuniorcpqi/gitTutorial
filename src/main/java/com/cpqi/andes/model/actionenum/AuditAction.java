package com.cpqi.andes.model.actionenum;

/**
 *
 * @author tfacundo
 *
 */
public enum AuditAction {

	READ("READ"), DELETE("DELETE"), CREATE("CREATE"), SAVE_UPDATE("SAVE/UPDATE"), UPDATE("UPDATE");

	private AuditAction(String action) {
		this.action = action;
	}

	private String action = "";

	/**
	 * @return the action
	 */
	public String value() {
		return action;
	}

}
