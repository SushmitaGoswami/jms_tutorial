# What is messaging?
Messaging service allows multiple heterogenous application to communicate with the following added advantages over shared data(database, file) mechanism.

- Heterogenous applications can communicate irrespective of the implementation language.
- Makes the applications loosley coupled. In this context, the difference between rest service and messaging service is that rest services doesn't ensure the durability of the messages. It may be lost. 
- It reduces system bottlenecks. If the receiver is down or overwhelmed, then it adjust the processing time. 
- Sender doesn't need to know about the receiver. It provides flexibility as it is possible to replace a receiver completely.
- Scablity can be achived by having mutliple receivers.


## Messaging Models 
- **Point-to-Point** -  Only one receiver can receive the message and it can be consumed only once. It can be synchronous request/reply messaging or asynchronous messaging. Example - Greeting card delivery system.
- **Publisher/Subscriber modelling** - There can be multiple consumer subcribed to a single topic. JMS provider ensures the message is not lost when a Durable consumer is subscribed to a topic. A shared consumers consume the message in parallel to avaoid the duplicate consumption of messages. This way also helps in balancing the loads between multiple consumers. Example - Hospital management app(When a new patient is checked-in/checked out from the hospital, hospital managment app will create a notification for other applications (e.g. claim management, bed management, clinical management).


## JMS Provider
JMS provider is the Message oriented middleware (MOM). This implements the JMS specification and ensures all the above five features of messaging service specification. Examples include ActiveMQ, RabbitMQ, TIBCOMQ. Provider creates various administor objects like ConnectioNFactory, Queue, Topic in the Java naming registry and makes those available via JNDI.

## Anatomy of messages
Each messsage consists of following parts
 ### A. Message Header
  It can be either set by JMS Provider or the Developers.
   #### Headers set by JMS Provider are as follows 
   - **JMSDestination** - Queue/Topic
   - **JMSDeliveryMode** - Based on type of Acknowledgement of messages.
   - **JMSMessageID** - unique message id.
   - **JMSTimestamp** - Time at which message is being sent.
   - **JMSExpiration** - expiry time (TimeToLive) - When a message is expired, it is available in the expiry queue already registered in the JMS provider.
   - **JMSPriority** - Priority of the message (0-9) - default is 4 (avg).
   - **JMSRedelivered** - When the message is not consumed by the consumer for some reason, JMS provider resend it based on the delivery mode being set.
	
   #### Headers set by Developers are as follows
   - **JMSReplyTo** - Producer set the reply to destination in the message header for synchronous p2p application.
   - **JMSCorrelaionID** - Set the consumer while replying to synchronize the message.
   - **JMSType** - Type of JMS Message

 ### B. Message Property 
 It can be either set by JMS Provider or the Developers.
  - **Properties set by JMS Provider** - e.g. - JMSGroupID - used for messaging grouping.
  - **Properties set by developer** - mostly used.

 ### C. Message Payload/Body 
  - Message itself


## Message Filtering - 
Consumer might want to process a message based on some filter. This can be achived using Message filter. It only works on certain JMS headers and JMS properties
 #### Example of such headers are
  - **JMSDeliveryMode**
  - **JMSPriority**
  - **JMSCorrelaionID**
  - **JMSMessageID**
  - **JMSType**
   
SQL Like syntax can be used to perform the filtering. Following are some the operators being used.
 - Arithmatic Operator (e.g. +/-)
 - IS NULL
 - BETWEEN
 - LIKE
 - NOT
 - Algebric
 - IN
 - OR

## Message Grouping 
Message grouping ensures that messages are consumed by only a particular consumers even in case of parallel processing. Producer must set JMSGroupID header for this purpose.

## Gurranted Messaging - 
This is a protocol being used between client runtime (producer and consumer) and JMS provider. It can be of following types
 - **AUTO_ACKNOWLEDGE** - JMS Provider sends the acknowledgement to the Producer as soon as it recieves the message from the Producer. In this mode, consumer will automatically sends the acknowledgement to the JMS Provider as soon as it consumes the message. In this mode, there is an additional overhead on the JMS Provider as it needs to ensure the message is sent only once to the consumer. For the durable consumer, it needs to pesist the message in a persistence storage when the consumer is down.
 - **DUPS_OK_ACKNOWLEDGE** - Same as AUTO_ACKNOWLEDGE except the process the JMS Provider follows to handle the acknowledgement. JMS Provider uses JMSRedelivered header for this purpose.
 - **CLIENT_ACKNOWLEDGE** - The JMS Provider doesn't handle the acknowledgement automatically. Client has to manually acknowledge for each message.
 - **TRANSANCTION** - It is similar to database transanction. In this mode, Producer sends a message in a transanction. JMS Provider writes the messages in a local cache. When the Producer send commit/rollback command, the JMS Provider the saves the message in the queue or discard the message respectively. Similarly, in this mode, when a Consumer commits, then only the JMS Provider will remove the data from queue.
 

## JMS Security
JMS provides user/role based authorization.Following are the key files being used.
 - **artemis-users.properties** - contains the user details.
 - **artemis-roles.properties** - contains the role details.
 - **broker.xml** - contains the configurations for a queue.
 


## JMS Provider used - 
- ActiveMQ


## Maven dependency being used - 
- artemis-jms-client-all:2.17.0

## Examples
 This project includes example of the following features of messaging model.
 ### p2p - Point to Point messaging 
   - **jms_1.0** - Uses JMS 1.0 specification
   - **jms_2.0** - Uses JMS 2.0 specification
   - **querybrowser** - Look through the messages in the messaging server, but doesn't consume it.
   - **request_reply** - In this example, producer mentions the reply queue while sending the message. Consumer receive the message and send the acknowledgement/new message to the reply queue. During that time the producer waits and consumes the message from the reply queue. Consumer uses MessageListener object of the JMS provider to asynchronously process the message.    
   
 ### expiry_queue
  - In this example, Producer sets a specific timeout for a message. Consumer first tries to get it from registered queue, if it fails, it fetches it from expiry queue.

 ### custom_property 
  - In this example, Producer sends some custom properties along with the message.

 ### message_type
  - This example plays with different types of messages (e.g. Text Message, Stream Message, Object Message, Map Message). It uses JMS 2.0 specification APIs.

 ### pub_sub - Publisher/Subscriber messaging
  - **jms_1.0** - Uses JMS 1.0 specification
  - **jms_2.0** - Uses JMS 2.0 specification
  - **durable_consumer** - This example demonstrates the power of durable subscription which says that even if the consumer goes down, JMS provider will ensure that the consumer can still receive the message when it comes alive. JMS provider identifies a durable consumer by the consumer id and subscription name.
  - **parallel_processing** - This is a small project for a demo application where There is a CardApp which produces cards and other two application named Alert app (which is a duarble consumer) and a Security App (which is a shared consumer). JMS provider ensures the message is reached at any cost to a durable_consumer. A shared consumers consume the message in parallel to avaoid the duplicate consumption of messages. This way also helps in balancing the loads between multiple consumers.
   
 ### message_filtering
  - **filtering_by_property** - Filter the message by the properties set on the message by the producer.
  - **filtering_by_header** - Filter the message by the header set on the message by the producer (e.g. JMSPriority).
  
 ### acknowledgement 
  - **auto** - Uses AUTO_ACKNOWLEDGE.
  - **client** - Uses CLIENT_ACKNOWLEDGE.
  - **dups_ok** - Uses DUPS_OK_ACKNOWLEDGE.
  - **transanction** - Uses Transanction for communication.
  
 ## message_grouping
 This example demontrates the JMS message_grouping functionality.
 
