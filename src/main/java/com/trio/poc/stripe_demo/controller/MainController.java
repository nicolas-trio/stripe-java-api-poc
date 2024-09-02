package com.trio.poc.stripe_demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @Value("${spring.application.name}")
    String appName;

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        return "home";
    }

    @GetMapping("/link")
    public String linkPage(Model model) {
        return "linkAccount";
    }

    @GetMapping("/success")
    public String successPage(Model model){
        return "success";
    }

    @GetMapping("/payment")
    public String paymentPage(Model model){
        return "payment";
    }

    @GetMapping("/checkout")
    public String checkoutPage(Model model){
        return "checkout";
    }

    @GetMapping("/payment-success")
    public String paymentSuccessPage(Model model){
        return "payment-success";
    }

    @GetMapping("/checkout-success")
    public String checkoutSuccessPage(Model model){
        return "checkout-success";
    }

    @GetMapping("/checkout-cancel")
    public String checkoutCancelPage(Model model){
        return "checkout-cancel";
    }

    @GetMapping("/subscription")
    public String subscriptionPage(Model model){
        return "subscription";
    }

    @GetMapping("/subscription-success")
    public String subscriptionSuccessPage(Model model){
        return "subscription-success";
    }

    @GetMapping("/subscription-cancel")
    public String subscriptionCancelPage(Model model){
        return "subscription-cancel";
    }
}
