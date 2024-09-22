# storeManagement

This repository contains a store management tool. Right now only the backend was implemented.
It was made using Spring Boot.

The store management tool API offers the following requests:
+ addProduct
+ getAllProducts
+ updateProductQuantity/id/quantity
+ updateProductPrice/id/price
+ deleteProduct/id

The input from the requests will be validated and converted to an entity object before the relevant actions will take place.
