package com.trio.poc.stripe_demo.stripe;

import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.net.RequestOptions;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.AccountLinkCreateParams;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import com.trio.poc.stripe_demo.service.PaymentsService;
import com.trio.poc.stripe_demo.service.SubscriptionsService;
import com.trio.poc.stripe_demo.service.VendorsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class StripeService {

    Logger logger = LoggerFactory.getLogger(StripeService.class);

    @Autowired
    private PaymentsService paymentsService;

    @Autowired
    private SubscriptionsService subscriptionsService;

    @Autowired
    private VendorsService vendorsService;

    public String createStripeConnectedAccount(String vendorEmail) throws StripeException {
        //create the stripe account using Account api
        AccountCreateParams params = AccountCreateParams.builder()
                .setType(AccountCreateParams.Type.STANDARD)
                .setCountry("US")
                .setEmail(vendorEmail)
                .build();

        var account =  Account.create(params);

        return account.getId();
    }

    public String getAccountLinkUrl(String stripeAccountId) throws StripeException {
        //create the accountLink so the vendor can do the onboarding on Stripe
        AccountLinkCreateParams linkParams = AccountLinkCreateParams.builder()
                .setAccount(stripeAccountId) // The ID of the connected account
                .setRefreshUrl("http://localhost:8080/link") // URL to redirect if the link is expired
                .setReturnUrl("http://localhost:8080/success") // URL to redirect to after the onboarding is complete
                .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
                .build();

        AccountLink accountLink = AccountLink.create(linkParams);

        // Return the URL for the vendor to complete onboarding
        return accountLink.getUrl();
    }

    private RequestOptions getLinkedAccountHeader(String connectedStripeAccountId){
        return RequestOptions.builder()
            .setStripeAccount(connectedStripeAccountId)
            .build();
    }

    public String createProduct(String vendorAccountId, String productName) throws StripeException {
        ProductCreateParams params = ProductCreateParams.builder()
            .setName(productName)
            .build();
        Product product = Product.create(params, getLinkedAccountHeader(vendorAccountId));
        return product.getId();
    }

    public String createPrice(String vendorAccountId, String productId, Long priceInCents) throws StripeException {
        PriceCreateParams params = PriceCreateParams.builder()
            .setCurrency("usd")
            .setUnitAmount(priceInCents)
            .setProduct(productId)
            .build();
        Price price = Price.create(params, getLinkedAccountHeader(vendorAccountId));
        return price.getId();
    }

    public String createSubscriptionPrice(String vendorAccountId, String productId, Long priceInCents, PriceCreateParams.Recurring.Interval interval) throws StripeException {
        PriceCreateParams params = PriceCreateParams.builder()
            .setCurrency("usd")
            .setUnitAmount(priceInCents)
            .setRecurring(
                PriceCreateParams.Recurring.builder()
                    .setInterval(interval)
                    .build()
            )
            .setProduct(productId)
            .build();
        Price price = Price.create(params, getLinkedAccountHeader(vendorAccountId));
        return price.getId();
    }


    public void handleWebHookEvent(Event event) throws Exception {
        // Deserialize the nested object inside the event
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;
        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        } else {
            throw new Exception("Unable to deserialize stripe event data");
        }

        // Handle the event
        switch (event.getType()) {
            case "checkout.session.completed":
                Session  checkoutSession = (Session) stripeObject;
                paymentsService.handleCheckoutCompleted(checkoutSession);
                break;

            case "customer.subscription.created":
                Subscription stripeSubscription = (Subscription) stripeObject;
                subscriptionsService.handleSubscriptionCreated(stripeSubscription);
                break;

            case "account.updated":
                Account stripeAccount = (Account) stripeObject;
                vendorsService.handleAccountUpdated(stripeAccount);

            default:
                logger.warn("Unhandled event type: " + event.getType());
        }
    }

    public String getSinglePaymentCheckoutLink(Long applicationFee, String priceStripeID, String vendorStripeAccountId) throws StripeException {
        SessionCreateParams params = SessionCreateParams.builder()
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setSuccessUrl("http://localhost:8080/checkout-success")
            .setCancelUrl("http://localhost:8080/checkout-cancel")
            .addLineItem(
                SessionCreateParams.LineItem.builder()
                    .setPrice(priceStripeID)
                    .setQuantity(1L)
                    .build()
            )
            .setPaymentIntentData(
                    SessionCreateParams.PaymentIntentData.builder()
                            .setApplicationFeeAmount(applicationFee)
                            .build()
            )
            .build();

        Session session = Session.create(params, getLinkedAccountHeader(vendorStripeAccountId));

        return session.getUrl();
    }

    public String getSubscriptionCheckoutLink(BigDecimal applicationFeePercent, String priceStripeId, String vendorStripeAccountId) throws StripeException {
        SessionCreateParams params = SessionCreateParams.builder()
            .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
            .setSuccessUrl("http://localhost:8080/subscription-success")
            .setCancelUrl("http://localhost:8080/subscription-cancel")
            .addLineItem(
                SessionCreateParams.LineItem.builder()
                    .setPrice(priceStripeId)
                    .setQuantity(1L)
                    .build()
            )
            .setSubscriptionData(
                SessionCreateParams.SubscriptionData.builder()
                    .setApplicationFeePercent(applicationFeePercent)
                    .build()
            )
            .build();

        Session session = Session.create(params, getLinkedAccountHeader(vendorStripeAccountId));

        return session.getUrl();
    }
}
