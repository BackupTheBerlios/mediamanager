package ch.fha.pluginstruct;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author ia02vond
 * @version $Id: EventHandler.java,v 1.4 2004/06/28 11:25:33 ia02vond Exp $
 */
public final class EventHandler {
	
	private String[] events;
	
	private LinkedList eventHandlerListeners;
	
	protected int getEventNumber(String event) {
		event = event.toLowerCase();
		for (int i=0; i<events.length; i++) {
			if (event.equals(events[i])) {
				return i;
			}
		}
		return -1;
	}
	
	private Node[] eventListener;
	
	protected EventHandler(String[] eventList) {
		events = eventList;
		eventListener = new Node[events.length];
		eventHandlerListeners = new LinkedList();
	}
	
	protected void addEventListener(
			Plugin plugin,
			String identifier,
			String event,
			String condition) throws PluginLogicException {
		
		// validate parameter
		int e = getEventNumber(event);
		if (identifier == null || identifier.equals("")) {
			throw new PluginLogicException(plugin, "empty identifier");
		}
		if (e < 0 || e >= events.length) {
			throw new PluginLogicException(plugin, "event does not exist");
		}
		if (condition == null || condition.equals("") || condition.equals("*")) {
			condition = null;
		}
		
		// add event listener to list
		if (eventListener[e] == null) {
			eventListener[e] = new Node(identifier, condition);
		} else {
			Node node;
			for (node=eventListener[e]; node.next!=null; node=node.next) {}
			node.next = new Node(identifier, condition);
			fireAddedEventListener(identifier, event, condition);
		}
	}
	
	protected void addEventListener(
			Plugin plugin,
			String event,
			String condition) throws PluginLogicException {
		
		try {
			addEventListener(plugin, plugin.getIdentifier(), event, condition);
		} catch (RuntimeException e) {
			throw new PluginLogicException(plugin, "unknown plugin runtime exception", e);
		}
	}
	
	protected void addEventListener(
			Plugin plugin,
			String event) throws PluginLogicException {
		
		try {
			addEventListener(plugin, plugin.getIdentifier(), event, "");
		} catch (RuntimeException e) {
			throw new PluginLogicException(plugin, "unknown plugin runtime exception", e);
		}	
	}
		
	protected void removeEventListener(String identifier) {
		for (int i=0; i<events.length; i++) {
			if (eventListener[i] != null) {
				Node node, last = null;
				for (node=eventListener[i]; node!=null; node=node.next) {
					if (node.pluginId.equals(identifier)) {
						if (last == null) {
							eventListener[i] = node.next;
						} else {
							last.next = node.next;
						}
					} else {
						last = node;
					}					
				}
			}
		}
	}
					
	protected void removeEventListener(
			Plugin plugin,
			String identifier,
			String event,
			String condition) throws PluginLogicException {
		
		// validate parameter
		int e = getEventNumber(event);
		if (e < 0 || e >= events.length) {
			throw new PluginLogicException(plugin, "event does not exist");
		}
		if (condition == null || condition.equals("") || condition.equals("*")) {
			condition = null;
		}
		
		// remove event listener
		if (eventListener[e] == null) return;
		Node node, last = null;
		for (node=eventListener[e]; node!=null; node=node.next) {
			if (node.pluginId.equals(identifier) &&
				( (condition == null && node.condition == null) ||
				   condition != null && node.condition != null && condition.equals(node.condition)) ){
				if (last == null) {
					eventListener[e] = node.next;
				} else {
					last.next = node.next;
				}
				fireRemovedEventListener(identifier, event, condition);
			} else {
				last = node;
			}
		}
	}
	
	protected void removeEventListener(
			Plugin plugin,
			String event,
			String condition) throws PluginLogicException {
		
		try {
			removeEventListener(plugin, plugin.getIdentifier(), event, condition);
		} catch (RuntimeException e) {
			throw new PluginLogicException(plugin, "unknown plugin runtime exception", e);
		}
	}

	protected void fireEvent(
			Returnable returnable,
			PluginEvent pluginEvent,
			String event,
			String condition) {

		// validate parameter
		int e = getEventNumber(event);
		if (e < 0 || e >= events.length) {
			throw new IllegalArgumentException("event does not exist");
		}
		if (condition == null || condition.equals("") || condition.equals("*")) {
			condition = null;
		}
		if (pluginEvent == null) pluginEvent = new PluginEvent();
		pluginEvent.setEventName(event);
		
		// fire event
		fireNode = new Node(null, null);
		fireNode.next = eventListener[e];
		firePluginEvent = pluginEvent;
		fireCondition = condition;
		fireState = RUNNING;
		fireReturnable = returnable;

		fireNext();
	}
	
	private Node fireNode;
	private PluginEvent firePluginEvent;
	private String fireCondition;
	private int fireState;
	private Returnable fireReturnable;
	
	private final static int RUNNING  = 1;
	private final static int CANCELED = 2;
	private final static int WAITING  = 3;
	private final static int DONE     = 0;

	public void fireNext() {
		if (fireNode != null) {
			fireNode = fireNode.next;
			if (fireNode != null) {
				Plugin plugin = PluginManager.getContainer().getPlugin(fireNode.pluginId);
				if (PluginManager.getContainer().isPluginActivated(fireNode.pluginId) &&
						(fireCondition == null || fireNode.condition == null || fireCondition.equals(fireNode.condition)) ) {
					
					boolean continuee;
					
					try {
						
						continuee = plugin.run(firePluginEvent, this);
						
					} catch (RuntimeException e) {
						new PluginLogicException(plugin, "unknown plugin runtime exception", e).show();
						continuee = true;
					}
					
					if (continuee) {
						fireNext();
					} else {
						fireState = WAITING;
					}
				
				} else {
					fireNext();
				}
			} else {
				System.out.println("set firestate = done (z. 219)");
				fireState = DONE;
				fireReturnable.fireReturn(false);
			}
		} else {
			System.out.println("set firestate = done (z. 223)");
			fireState = DONE;
			fireReturnable.fireReturn(false);
		}
	}
	
	public void finish() {
		fireState = RUNNING;
		fireNext();
	}
					
	public void cancelOperation() {
		fireState = CANCELED;
		fireReturnable.fireReturn(true);
	}
		
	
	protected void addEventHandlerListener(EventHandlerListener listener) {
		eventHandlerListeners.add(listener);
	}
	
	protected void removeEventHandlerListener(EventHandlerListener listener) {
		eventHandlerListeners.remove(listener);
	}
	
	private void fireAddedEventListener(
			String identifier, String event, String condition) {
	
		Iterator it = eventHandlerListeners.iterator();
		while (it.hasNext()) {
			((EventHandlerListener)it.next()).addedEventListener(identifier, event, condition);
		}
	}
	
	private void fireRemovedEventListener(
			String identifier, String event, String condition) {
		
		Iterator it = eventHandlerListeners.iterator();
		while (it.hasNext()) {
			((EventHandlerListener)it.next()).removedEventListener(identifier, event, condition);
		}
	}
		
	
	protected Iterator iterator(String event) {
		return new PluginEventListenerIterator(
				this, event, eventListener[getEventNumber(event)]);
	}
	
	private class Node {
		protected String pluginId;
		protected String condition;
		protected Node next;
		protected Node(String pluginId, String condition) {
			this.pluginId = pluginId;
			this.condition = condition;
		}
	}

	private class PluginEventListenerIterator implements Iterator {
		private EventHandler eventHandler;
		private String eventStr;
		private int event;
		private Node node;
		
		protected PluginEventListenerIterator(EventHandler eventHandler, String event, Node node) {
			this.eventHandler = eventHandler;
			this.eventStr = event;
			this.event = getEventNumber(event);
			this.node = node;
		}
		
		public void remove() {
			throw new UnsupportedOperationException();	
		}

		public boolean hasNext() {
			return node != null;
		}

		public Object next() {
			PluginEventObserver peo = new PluginEventObserver();
			peo.plugin = PluginManager.getContainer().getPlugin(node.pluginId);
			peo.condition = node.condition;
			peo.event = eventStr;
			node = node.next;			
			return peo;
		}
	}
}