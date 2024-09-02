package com.trio.poc.stripe_demo.service;

import com.stripe.model.checkout.Session;
import com.trio.poc.stripe_demo.model.Payment;
import com.trio.poc.stripe_demo.repository.PaymentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentsService {

    @Autowired
    private PaymentsRepository paymentsRepository;

    public void handleCheckoutCompleted(Session checkoutSession) {
        var newPayment = new Payment();
        newPayment.setClientEmail(checkoutSession.getCustomerEmail());
        newPayment.setStripePaymentId(checkoutSession.getPaymentIntentObject().getId());
        newPayment.setAmount(checkoutSession.getAmountTotal());
        newPayment.setMarketplaceFee(checkoutSession.getPaymentIntentObject().getApplicationFeeAmount());

        paymentsRepository.save(newPayment);
    }
}
