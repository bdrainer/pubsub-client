# Pubsub

This application demonstrates using the Google Java gRPC PubSub Client Library.  

https://cloud.google.com/pubsub/docs/emulator
https://cloud.google.com/pubsub/grpc-overview

Hub is using an older client library.  The Google Java API PubSub Client Library

https://developers.google.com/api-client-library/java/apis/pubsub/v1

Google recommends using the gRPC library however, it is not supported on Google App Engine.
  
## Disclaimer

Until GAE support the gRPC library, there is no way to use the emulator when running Hub locally.

There is a stackoverflow question to confirm that is true:
 http://stackoverflow.com/questions/41989624/does-google-pubsub-emulator-work-with-the-google-json-pubsub-api
  
Below is described how to install and use the emulator.  

## Emulator

[Emulators Overview](https://cloud.google.com/sdk/gcloud/reference/beta/emulators)

The PubsubClientTest uses Google's LocalPubSubHelper.  

LocalPubSubHelper downloads a version of the PubSub emulator if your environment does not already have one.  This is just a copy used strictly by the helper.

It makes sense to install the emulator separately from the test so you can run it 
in its own command window (i.e. standalone)a
   
`gcloud beta emulators pubsub env-init`

`gcloud beta emulators pubsub start`

If you don't have the emulator installed running the above two command will trigger an install.

When the emulator is running standalone you can configure the logistics-service and Hub to run against.  This would allow them to communicate locally through the emulator.  Logistics publishes and Hub consumes.

## PubsubClientTest

The test is a good example of how easy it is to interface with PubSub.  

**Features**

* It usses the emulator via LocalPubSubHelper
* It creates/deletes topics and subscriptions.
* It publishes a message
* It pulls messages
* It pulls the payload off the message
* It acknowledges messages
