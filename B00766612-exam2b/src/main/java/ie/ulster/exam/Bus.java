package ie.ulster.exam;
class Bus
{
    private String destination;
    private Integer people;
    private Boolean accessible;
    private String feature;

    Bus(String destination, Integer people, String feature, boolean accessible)
    {
        this.destination = destination;
        this.people = people;
        this.feature = feature;
        this.accessible = accessible;
    }
	//destination
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	//people
	public Integer getPeople() {
		return people;
		
	}
	public void setPeople(Integer people) {
		this.people = people;
	}
	//accessible
	public boolean isAccessible() {
		return accessible;
	}
	
	public void setAccessible(Boolean accessible) {
		this.accessible = accessible;
	}
	
	
	public String getFeature() {
		return feature;
	}
	
	public void setAmount(String feature) {
		this.feature = feature;
	}
}