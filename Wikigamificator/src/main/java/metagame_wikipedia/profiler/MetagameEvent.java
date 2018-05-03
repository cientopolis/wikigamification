package metagame_wikipedia.profiler;

import java.util.Date;

import de.tudarmstadt.ukp.wikipedia.revisionmachine.common.util.Time;
import de.tudarmstadt.ukp.wikipedia.revisionmachine.difftool.data.tasks.content.DiffAction;

public class MetagameEvent implements Comparable<MetagameEvent> {
	// Email to identify the player
	private String playerEmail;
	// Contribution,Reinsformet, Disemination
	private String eventType;
	// what the player do
	private DiffAction action;
	private String eventName;
	private Date timestamp;
	private String page;
	private String proyect;



	public MetagameEvent(String playerEmail, String eventType, DiffAction action, String eventName, Date timestamp,
			String page, String proyect) {
		super();
		this.playerEmail = playerEmail;
		this.eventType = eventType;
		this.action = action;
		this.eventName = eventName;
		this.timestamp = timestamp;
		this.page = page;
		this.proyect = proyect;
	}

	public String getProyect() {
		return proyect;
	}

	public void setProyect(String proyect) {
		this.proyect = proyect;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getPage() {
		return page;
	}

	public String getPlayerEmail() {
		return playerEmail;
	}

	public void setPlayerEmail(String playerEmail) {
		this.playerEmail = playerEmail;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public DiffAction getAction() {
		return action;
	}

	public void setAction(DiffAction action) {
		this.action = action;
	}

	public int compareTo(MetagameEvent o) {

		return this.getTimestamp().compareTo(o.getTimestamp());
	}

}
