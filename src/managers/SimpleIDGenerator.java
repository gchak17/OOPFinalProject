package managers;

public abstract class SimpleIDGenerator {
	
	protected long nextID;
	
	protected SimpleIDGenerator() {
		nextID = 0;
	}
	
	public long generateID() {
		return ++nextID;
	}
	
	
	
}
