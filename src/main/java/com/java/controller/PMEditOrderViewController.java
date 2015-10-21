package com.java.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.java.pojo.Order;
import com.java.pojo.Portfolio;
import com.java.pojo.Security;
import com.java.pojo.User;
import com.java.service.OrderManager;
import com.java.service.PortfolioManager;
import com.java.service.SecurityManager;
import com.java.service.UserManager;

@Controller
@SessionAttributes(value = { "passedOrder" })
public class PmEditOrderViewController {

	@Autowired
	private OrderManager orderManager;

	@Autowired
	private UserManager userManager;

	@Autowired
	private SecurityManager securityManager;

	@Autowired
	private PortfolioManager portfolioManager;
	
	@RequestMapping(value = "/edit-order", method = RequestMethod.POST, consumes = "application/json")
	public ModelAndView editorder(@RequestBody String json,
			HttpSession session, Model model) {
		System.out.println("START EDIT-ORDER MAPPING");
		Order editOrder = orderManager.getOrderWithId(json);
		model.addAttribute("passedOrder", editOrder);
		
		System.out.println(editOrder.toString());
		return new ModelAndView("edit-order", "order", new Order());
	}
	
	@RequestMapping(value = "/amend-order", method = RequestMethod.POST, consumes = "application/json")
	public ModelAndView amendtorder(@RequestBody String json,
			HttpSession session, Model model) {
		System.out.println("amend-order");
		Order editOrder = orderManager.getOrderWithId(json);
		model.addAttribute("passedOrder", editOrder);

		return new ModelAndView("amend-order", "order", new Order());
	}

	@RequestMapping(value = "/editdetails", method = RequestMethod.POST)
	public String addOrder(@ModelAttribute("order") Order order, Model model, HttpSession session) {
		User user = (User) session.getAttribute("authenticatedUser");
		model.addAttribute("symbol", order.getSymbol2());
		model.addAttribute("side", order.getSide());
		model.addAttribute("quantity", order.getTotalQty());

		User trader = userManager.getUserWithId(order.getTraderId2());
		Portfolio port = portfolioManager.getUserDetails(order.getPortId2());

		User pm = userManager.getUserWithId(user.getUserId());

		Security security = securityManager.getSecurityDetails(order
				.getSymbol2());
		System.out.println(security);
		order.setSecurity(security);

		order.setPortfolio(port);
		order.setUser2(trader);
		order.setUser1(pm);

		int sellQty = order.getTotalQty() * -1;

		long totalqtyowned = orderManager.getTotalEquityOwned(order);

		// update when flag is true
		boolean flag;
		String redirect = "";
		String errorMessage = "";

		if (order.getSide().equals("Sell")
				&& order.getTotalQty() < totalqtyowned
				&& getEquitiesInPortfolio(order).contains(
						order.getSecurity().getSymbol())) {
			System.out.println("first check");
			flag = true;
		} else if (order.getSide().equals("Buy")) {
			System.out.println("Second check");
			flag = true;
		} else {
			System.out.println("requirements not met");
			flag = false;
		}
		if (flag == true) {
			redirect = "pmOrderForm_results";
			if (order.getSide().equals("Sell")) {
				order.setTotalQty(sellQty);
			}
			orderManager.updateOrder(order);
		} else if (flag == false) {
			if (order.getTotalQty() > totalqtyowned) {
				errorMessage = "You don't own enough equity to sell the quantity entered";
			}
			if (order.getTotalQty() > totalqtyowned) {
				errorMessage = "You don't own enough equity to sell the quantity entered";
			}
			model.addAttribute("ErrorMessage", errorMessage);
			model.addAttribute("totalowned", totalqtyowned);
			redirect = "ErrorOrder";
		}
		return redirect;
	}

	public List<String> getEquitiesInPortfolio(Order order) {
		List<Order> equitiesInPort = orderManager.getEquitiesInPortfolio(order);
		List<String> equitiesOwned = new ArrayList<String>();
		for (Order symbol : equitiesInPort) {
			equitiesOwned.add(symbol.getSecurity().getSymbol());
		}
		return equitiesOwned;
	}

	@ModelAttribute("sideList")
	public List<String> provideEquitySide() {
		List<String> sideList = new ArrayList<String>();
		sideList.add("Buy");
		sideList.add("Sell");
		return sideList;
	}

	@ModelAttribute("orderTypeList")
	public List<String> provideEquityOrderType() {
		List<String> orderTypeList = new ArrayList<String>();
		orderTypeList.add("Market");
		orderTypeList.add("Limit");
		orderTypeList.add("Stop");
		return orderTypeList;
	}

	@ModelAttribute("qualifierTypeList")
	public List<String> provideEquityQualifierType() {
		List<String> qualifierTypeList = new ArrayList<String>();
		qualifierTypeList.add("Day Order");
		qualifierTypeList.add("GTC");
		return qualifierTypeList;
	}

	@ModelAttribute("traderId")
	public Map<String, String> provideEquityTrader() {
		List<User> TraderList = userManager.getTraders();
		Map<String, String> traderIds = new HashMap<String, String>();
		for (User trader : TraderList) {
			traderIds.put(trader.getUserId(),
					trader.getFName() + " " + trader.getLName());
		}
		return traderIds;
	}

	@ModelAttribute("accountTypeList")
	public List<String> provideEquityAccountType() {
		List<String> accountTypeList = new ArrayList<String>();
		accountTypeList.add("Cash");
		accountTypeList.add("Margin");
		return accountTypeList;
	}

	@ModelAttribute("portfolioList")
	public Map<String, String> provideEquityPortfolio(HttpSession session) {
		User user = (User) session.getAttribute("authenticatedUser");
		System.out.println(user.getUserId());
		List<Portfolio> portfolioList = portfolioManager.getPortfolios(user.getUserId());
		Map<String, String> portfolios = new HashMap<String, String>();
		for (Portfolio port : portfolioList) {
			portfolios.put(port.getPortId(), port.getName());
		}
		return portfolios;
	}
	
}
