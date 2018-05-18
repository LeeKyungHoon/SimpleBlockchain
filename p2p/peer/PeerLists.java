package peer;

import java.io.Serializable;
import java.util.LinkedList;

public class PeerLists implements Serializable {

	private static Peer localPeer;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static class SingletonPeerLists {
		private static final LinkedList<Peer> peerLists = new LinkedList<Peer>();
	}

	public static LinkedList<Peer> getInstance() {
		return SingletonPeerLists.peerLists;
	}

	public static void setLocalPeer(Peer peer) {
		localPeer = peer;
	}
	
	public static Peer getLocalPeer() {
		return localPeer;
	}
	
}
