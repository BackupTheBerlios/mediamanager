package ch.fha.pluginstruct;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author ia02vond
 * @version $Id: EventHandler.java,v 1.3 2004/06/23 22:55:39 ia02vond Exp $
 */
public final class EventHandler {
	
	private String[] events;
	
	private LinkedList eventHandlerListeners;
	
	private OperationCancelException oce;
	
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

	protected synchronized void fireEvent(
			PluginEvent pluginEvent,
			String event,
			String condition) throws OperationCancelException {

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
		Node node;
		Plugin plugin = null;
		for (node=eventListener[e]; node!=null; node=node.next) {
			plugin = PluginManager.getContainer().getPlugin(node.pluginId);
			if (PluginManager.getContainer().isPluginActivated(node.pluginId) &&
				(condition == null || node.condition == null || condition.equals(node.condition)) ) {
				
				oce = null;
				
				PluginThread pThread = new PluginThread(
						this,
						Thread.currentThread(),
						plugin,
						pluginEvent);
				pThread.start();
				
				try {
					wait();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				 if (oce != null) {
					throw oce;
				}		
			}
		}
	}
	
	public synchronized void finish(
			Thread applThread,
			OperationCancelException oce,
			PluginLogicException ple) {
		
		if (ple != null) {
			ple.show();
		}
		this.oce = oce;
		notify();
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