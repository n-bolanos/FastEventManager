from kafka import KafkaProducer
import json

producer = KafkaProducer(bootstrap_servers='localhost:1234',
                         value_serializer=lambda v: json.dumps(v).encode('utf-8'))

class Message:
    '''
    Base structure for messages
    '''
    def __init__(self, to, subject, etype):
          self.to = to
          self.subject = subject
          self.type = etype
    
    def to_dict(self):
        return self.__dict__

class Capacity(Message):
    '''
    Structure for max capacity reached messages
    '''
    def __init__(self, to, name, event_name, capacity):
        super().__init__(to, '''Max Capacity Reached''', "CAPACITY_REACHED")
        self.params = {"name":name, "eventName":event_name, "capacity":capacity}

class Confirmation(Message):
    '''
    Structure for max capacity reached messages
    '''
    def __init__(self, to, name, event_name, date, location):
        super().__init__(to, '''Attendance Confirmation''', "EVENT_CONFIRMATION")
        self.params = {"name":name, "eventName":event_name, "eventDate":date, "location":location}

class WaitList(Message):
    '''
    Structure for waitlist assignment messages.
    '''
    def __init__(self, to, name, event_name):
        super().__init__(to, '''You are on the Waitlist''', "WAITLIST_NOTIFICATION")
        self.params = {"name":name, "eventName":event_name}

class WaitListPromotion(Message):
    '''
    Structure for waitlist state switch mesages.
    '''
    def __init__(self, to, name, event_name, date, location):
        super().__init__(to, '''Waitlist Promotion''', "WAITLIST_PROMOTION")
        self.params = {"name":name, "eventName":event_name, "eventDate":date, "location":location}
  
def send_notification(topic: str, message: dict):
    '''
    Send a mail to notify about a certain topic.
    '''
    producer.send(topic, message)
