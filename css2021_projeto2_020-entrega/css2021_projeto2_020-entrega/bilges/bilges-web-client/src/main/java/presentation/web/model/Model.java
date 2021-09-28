package presentation.web.model;

import java.util.LinkedList;
import java.util.List;

public class Model {

	private List<String> messages;

	public Model() {
		messages = new LinkedList<>();
	}

	public List<String> getMessages () {
		return messages;
	}
	
	public void addMessage(String message) {
		messages.add(message);
	}
	
	public boolean isHasMessages() {
		return !messages.isEmpty();
	}
	
	public void clearMessages() {
		messages = new LinkedList<>();
	}

}
