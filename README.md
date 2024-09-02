# stripe-java-api-poc

The idea of this POC is:
- Connect a vendor account to the marketplace stripe account
- Allow single payment flow (marketplace gets a fee)
- Allow subscription flow (marketplace gets a fee)

This is a spring boot application. To run it:

mvn spring-boot:run

For stripe integration to work, we need:
- set stripe.secret.key property in application.properties
- set  stripe.integration.connected-account-id property in application.properties
  - this is used as the main seller for the payments. The connected account needs a name for the checkout to work properly

Examples for the above:
- link vendor account: http://localhost:8080/link
- single payment: http://localhost:8080/checkout
- subscription: http://localhost:8080/subscription