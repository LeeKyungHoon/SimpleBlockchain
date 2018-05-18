package peer;

import java.io.Serializable;

public class Peer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String peerID;
	private int peerNo;
	private String peerAddress;
	private int port;

	public Peer(String address, int port) {
		setPeerAddress(address);
		setPort(port);
		setPeerNo(PeerLists.getInstance().size() + 1);
		setPeerID(address + String.valueOf(port));
	}

	public Peer() {

	}

	public String getPeerID() {
		return peerID;
	}

	public void setPeerID(String peerID) {
		this.peerID = peerID;
	}

	public int getPeerNo() {
		return peerNo;
	}

	public void setPeerNo(int peerNo) {
		this.peerNo = peerNo;
	}

	public String getPeerAddress() {
		return peerAddress;
	}

	public void setPeerAddress(String peerAddress) {
		this.peerAddress = peerAddress;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
