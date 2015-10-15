package com.java.pojo;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the blocks database table.
 * 
 */
@Entity
@Table(name="blocks")
@NamedQuery(name="Block.findAll", query="SELECT b FROM Block b")
public class Block implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="block_id")
	private String blockId;

	@Column(name="executed_qty")
	private int executedQty;

	@Column(name="limit_price")
	private float limitPrice;
	
	@Column(name="symbol")
	private String symbol;
	@Column(name="side")
	private String side;
	
	@Column(name="open_qty")
	private int openQty;

	private String status;

	@Column(name="stop_price")
	private float stopPrice;

	private Timestamp timestamp;

	@Column(name="total_qty")
	private int totalQty;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="traderid")
	private User user;

	//bi-directional many-to-one association to Executeblock
	@OneToMany(mappedBy="block")
	private List<ExecuteBlock> executeblocks;

	//bi-directional many-to-one association to Order
	@OneToMany(mappedBy="block")
	private List<Order> orders;

	
	
	
	public Block() {
	}
	
	

	public Block(String symbol, String side, String status, User user, List<Order> orders) {
		super();
		this.executedQty = 0;
		this.symbol = symbol;
		this.side = side;
		this.status = status;
		this.totalQty = 0;
		this.user = user;
		this.orders = orders;
	}



	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getSide() {
		return side;
	}

	public void setSide(String side) {
		this.side = side;
	}

	public String getBlockId() {
		return this.blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

	public int getExecutedQty() {
		return this.executedQty;
	}

	public void setExecutedQty(int executedQty) {
		this.executedQty = executedQty;
	}

	public float getLimitPrice() {
		return this.limitPrice;
	}

	public void setLimitPrice(float limitPrice) {
		this.limitPrice = limitPrice;
	}

	public int getOpenQty() {
		return this.openQty;
	}

	public void setOpenQty(int openQty) {
		this.openQty = openQty;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public float getStopPrice() {
		return this.stopPrice;
	}

	public void setStopPrice(float stopPrice) {
		this.stopPrice = stopPrice;
	}

	public Timestamp getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public int getTotalQty() {
		return this.totalQty;
	}

	public void setTotalQty(int totalQty) {
		this.totalQty = totalQty;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<ExecuteBlock> getExecuteblocks() {
		return this.executeblocks;
	}

	public void setExecuteblocks(List<ExecuteBlock> executeblocks) {
		this.executeblocks = executeblocks;
	}

	public ExecuteBlock addExecuteblock(ExecuteBlock executeblock) {
		getExecuteblocks().add(executeblock);
		executeblock.setBlock(this);

		return executeblock;
	}

	public ExecuteBlock removeExecuteblock(ExecuteBlock executeblock) {
		getExecuteblocks().remove(executeblock);
		executeblock.setBlock(null);

		return executeblock;
	}

	public List<Order> getOrders() {
		return this.orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public Order addOrder(Order order) {
		getOrders().add(order);
		order.setBlock(this);

		return order;
	}

	public Order removeOrder(Order order) {
		getOrders().remove(order);
		order.setBlock(null);

		return order;
	}
	
}
