# stripe-java-api-poc

The idea of this POC is:
- connect a user account with their stripe account
  - becoming a seller
- add products for sellers (name, price)
- buy product
  - user a buyer account (regular marketplace account)
  - make a payment using stripe api
    - send a part of the payment to the seller stripe account
    - send the fee part to marketplace stripe account