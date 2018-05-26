(ns eventsourcing.eventlog-protocol)

(defprotocol EventLogProtocol
  "Actions that handle writing to, and reading from the event log."
  (add-event [this topic event] "Add a new event to the log")
  (get-events [this topic] "Get all events for a topic"))
