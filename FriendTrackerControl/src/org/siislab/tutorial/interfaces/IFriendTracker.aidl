package org.siislab.tutorial.interfaces;

interface IFriendTracker {

	boolean isTracking();
	boolean addNickname(in String nick, int contactId);
	
}