package com.edu.hutech.major.controller.api;

import com.edu.hutech.major.dto.OrderDTO;
import com.edu.hutech.major.global.GlobalData;
import com.edu.hutech.major.model.Order;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PaypalController {

	@Autowired
	PaypalService service;

	public static final String SUCCESS_URL = "pay/success";
	public static final String CANCEL_URL = "pay/cancel";

//	@GetMapping("/")
//	public String home() {
//		return "home";
//	}

	@PostMapping("/pay")
	public String payment(@ModelAttribute("order") OrderDTO orderDTO) {
		try {
			//only need total
			Payment payment = service.createPayment(orderDTO.getTotal(), "USD", "paypal",
					"sale", orderDTO.getNote(), "http://localhost:8080/" + CANCEL_URL,
					"http://localhost:8080/" + SUCCESS_URL);
			for(Links link:payment.getLinks()) {
				if(link.getRel().equals("approval_url")) {
					return "redirect:"+link.getHref();
				}
			}

		} catch (PayPalRESTException e) {
			e.printStackTrace();
		}
		return "redirect:/";
	}

	 @GetMapping(value = CANCEL_URL)
	    public String cancelPay() {
	        return "cancel";
	    }

	    @GetMapping(value = SUCCESS_URL)
	    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
	        try {
	        	OrderDTO orderDTO = new OrderDTO();
	            Payment payment = service.executePayment(paymentId, payerId);
	            System.out.println(payment.toJSON()); //check json data
				PayerInfo payerInfo = payment.getPayer().getPayerInfo();
				orderDTO.setEmail(payerInfo.getEmail());
				orderDTO.setRecipientName(payerInfo.getShippingAddress().getRecipientName());
				orderDTO.setPhone(payerInfo.getShippingAddress().getPhone());
				orderDTO.setLine1(payerInfo.getShippingAddress().getLine1());
				orderDTO.setLine2(payerInfo.getShippingAddress().getLine2());
				orderDTO.setCity(payerInfo.getShippingAddress().getCity());
				orderDTO.setCountryCode(payerInfo.getShippingAddress().getCountryCode());
				orderDTO.setPostalCode(payerInfo.getShippingAddress().getPostalCode());
				orderDTO.setState(payerInfo.getShippingAddress().getState());

				orderDTO.setNote(payment.getTransactions().get(0).getDescription());

				orderDTO.setCart(GlobalData.cart);
				orderDTO.setTotal(Double.parseDouble(payment.getTransactions().get(0).getAmount().getTotal()));
	            if (payment.getState().equals("approved")) {
	            	//do insert in db
	                return "success";
	            }
	        } catch (PayPalRESTException e) {
	         System.out.println(e.getMessage());
	        }
	        return "redirect:/";
	    }

}
