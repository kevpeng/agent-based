import java.util.*;

public class EventList
{
	PriorityQueue<Event> eventList = new PriorityQueue<Event>();

	EventList(PriorityQueue<Event> eventList)
	{
		this.eventList = eventList;
	}

	public Event getEvent() { return eventList.poll(); }
	public void setEvent(Event e) 
	{
		eventList.add(e);
	}


}

/*
while(t<T)
{
	e <- getNextEvent()
	t <- e.time
	if(e.type == Move)
		e.agent.move(t)
		//schedule nxt event for agent
	elseif(e.type == Die)
		e.agent.die(t)
}
*/