public class Event 
{
	private double time;
	private String type;
	private Agent agent;

	//Constructor
	Event(double time, String type, Agent agent)
	{
		this.time  = time;
		this.type  = type;
		this.agent = agent;
	}

	public double getTime() { return this.time;  }
	public String getType() { return this.type;  }
	public Agent getAgent() { return this.agent; }

	//use these to alter the time and type of event once
	//completed
	public void setTime(double time)
	{
		this.time = time;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public void setAgent(Agent agent)
	{
		this.agent = agent;
	}
}	
