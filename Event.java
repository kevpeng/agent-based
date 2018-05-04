import java.util.*;

enum EventType
{
	move, die;
}

public class Event
{
	private Agent a;
	private EventType e;
	private double t;
	
	Event(Agent a, EventType e, double t)
	{
		this.a = a;
		this.e = e;
		this.t = t;
	}
	
	/* Getter Methods */
	public Agent getAgent()
	{
		return this.a;
	}
	
	public EventType getEvent()
	{
		return this.e;
	}
	
	public double getTime()
	{
		return this.t;
	}
	
	public void print()
	{
//		System.out.print("Agent: ");
//		this.a.print();
		System.out.println("Event Type: " + this.e);
		System.out.println("Event Time: " + this.t + "\n");
	}
	
	/* public static void main(String args[])
	{
		Random rng = new Random();
		PriorityQueue<Event> calendar = new PriorityQueue<Event>(new EventComparator());
		for(int i = 0; i < 5; i++)
		{
			calendar.add(new Event(new Agent("agent " + i), EventType.move, rng.nextDouble()));
		}
		for(int i = 0; i < 5; i ++)
		{
			calendar.poll().getAgent().print();
		}
	} */
}

class EventComparator implements Comparator<Event>
{
	public int compare(Event one, Event two)
	{
		if(one.getTime() < two.getTime())
		{
			return -1;
		}
		if(one.getTime() > two.getTime())
		{
			return 1;
		}
		return 0;
	}
}