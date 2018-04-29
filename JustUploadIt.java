import java.util.PriorityQueue;

public class MainTest 
{
	public static void main(String[] args)
	{
		PriorityQueue<Event> calendar = new PriorityQueue<Event>(new EventComparator());
		calendar.add(new Event("4", 1, 5));
		calendar.add(new Event("1", 1, 0.01));
		calendar.add(new Event("2", 0, 0.03));
		calendar.add(new Event("3", 0, 0.001));
		while(!calendar.isEmpty())
		{
			System.out.println(calendar.poll().getT());
		}
	}
}
