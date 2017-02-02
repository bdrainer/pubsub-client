# Pubsub

This application demonstrates using the Google gRPC PubSub Java Client Library.  

See com.gearlaunch.google.PubsubClientTest

##Overview
https://cloud.google.com/pubsub/docs/emulator

https://cloud.google.com/pubsub/grpc-overview

Hub is using an older client library.  The Google API PubSub Java Client Library.

https://developers.google.com/api-client-library/java/apis/pubsub/v1

Google recommends using the gRPC library however, it is not supported on Google App Engine.
  
## Important

Until GAE supports the gRPC library, there doesn't seem to be a way to use the emulator when 
Hub runs locally.

**Stackoverflow** - 
http://stackoverflow.com/questions/41989624/does-google-pubsub-emulator-work-with-the-google-json-pubsub-api
  
## Emulator

[Emulators Overview](https://cloud.google.com/sdk/gcloud/reference/beta/emulators)

Executing either of the commands below will trigger installation of the pubsub
emulator.  You will be asked if you want to install it.

`gcloud beta emulators pubsub start` - Starts the pubsub emulator

`gcloud beta emulators pubsub env-init` - Shows you what environment varialbes to set.

First start the emulator.  The output shows the port it is running on.

The "env-init" command gives you the command to set the environment variables.  It will be
 something like `export PUBSUB_EMULATOR_HOST=localhost:8152`
 
Setting this environment variable in the environment an application runs means the application
will use the gRPC pubsub emulator.  The gRPC library utilizes the PUBSUB_EMULATOR_HOST environment
variable.
 
### Use Case
Running the emulator when developing locally one could connect both Hub and the logistics-service
to it.  They will interact just as expected where logistics publishes messages and Hub consumes them.

### Important

The emulator does not persist messages when it is shutdown.  All unread, unacknowledge messages
will be lost.

## PubsubClientTest

The test is a good example of how easy it is to interface with PubSub.
  
com.gearlaunch.google.PubsubClientTest->testPubsub()

The test...

* usses the emulator via LocalPubSubHelper
* creates/deletes topics and subscriptions.
* publishes a message
* pull a message
* gets the payload off the message
* acknowledges the message

PubsubClientTest uses Google's LocalPubSubHelper.  The gRPC client lib has test helpers where
  the API client lib does not have any.


