(ns es-cqrs-clj.eventsource-protocol)

(defprotocol EventSourceProtocol
  "Actions that handle subscribing to topics, and fetching events"
  (emit [this topic event] "Emit a new event")
  (get-events [this topic] "Get events emitted to topic")
  (subscribe [this topic handler] "subscribe a handler to a topic")
  (unsubscribe [this topic handler] "unsubscribe a handler from a topic"))
