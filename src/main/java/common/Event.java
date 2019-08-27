package common;

import java.util.LinkedList;
import java.util.List;

public class Event<T> {

    private final List<EventHandler<T>> attachedEventHandlers = new LinkedList<>();

    public void AttachHandler(EventHandler<T> eventHandler){
        attachedEventHandlers.add(eventHandler);
    }

    public void DetachHandler(EventHandler<T> eventHandler) {attachedEventHandlers.remove(eventHandler);}

    public void Invoke(T params){
        for(EventHandler<T> eventHandler : attachedEventHandlers){
            eventHandler.Handle(params);
        }
    }
}
