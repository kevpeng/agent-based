import java.util.Comparator;

public class Event
{
	private String id;
	private int event;
	private double time;
	
	public Event(String id, int e, double t)
	{
		this.id = id;
		event = e;
		time = t;
	}
	
	public double getT()
	{
		return this.time;
	}
	
	public int getEvent()
	{
		return this.event;
	}
	
	public String getID()
	{
		return this.id;
	}
}

class EventComparator implements Comparator<Event>
{
	public int compare(Event one, Event two)
	{
		if(one.getT() < two.getT())
		{
			return -1;
		}
		if(one.getT() > two.getT())
		{
			return 1;
		}
		return 0;
	}
}
