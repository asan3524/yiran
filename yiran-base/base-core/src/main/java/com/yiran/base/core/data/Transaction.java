package com.yiran.base.core.data;

import com.yiran.base.core.util.IdUtil;

/**
 * @author lishibang
 * 
 */
public class Transaction {

	public static final String YIRAN_BASE_HEADER_TRANSACTION = "transaction";
	public static final String YIRAN_BASE_HEADER_VALIDITY = "validity";
	/**
	 * 默认初始化
	 */
	public Transaction() {
		this(IdUtil.generateId().toString());
	}

	/**
	 * @param transaction
	 *            事务ID 默认有效时间为30秒
	 */
	public Transaction(String transaction) {
		this(transaction, 30);
	}

	/**
	 * @param transaction
	 *            事务ID
	 * @param validity
	 *            单位秒
	 */
	public Transaction(String transaction, Integer validity) {
		this.transaction = null == transaction ? IdUtil.generateId().toString() : transaction;
		this.validity = null == validity ? 30 : validity;

	}

	private String transaction;
	private Integer validity;

	public String getTransaction() {
		return transaction;
	}

	public void setTransaction(String transaction) {
		this.transaction = transaction;
	}

	public Integer getValidity() {
		return validity;
	}

	public void setValidity(Integer validity) {
		this.validity = validity;
	}
}
