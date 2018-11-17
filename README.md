# Instructions

maven build:

`mvn install`

maven creates war, which is than deployed on web application platforms such as apache tomcat.

after war is deployed REST api is exposed.

REST service exposed for word counts:

`http://<host>:<port>/words`

REST service exposed for event types counts:

`http://<host>:<port>/types
`


# Improvements

1. NoSQL handling for scaling up, memory usage and performance.

2. better understanding of RXJava and its objects, handling decoupling better and initialization of streaming and handling.

3. error handling and testing