package p2p;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;

import peer.Peer;
import peer.PeerLists;

public class Client {

	private static LinkedList<Peer> peerLists;

	public static void main(String argsp[]) {
		System.out.println("start client");
		new Client().getPeerLists();
		for (Peer peer : peerLists) {
			System.out.println(peer);
			System.out.println("peerId : " + peer.getPeerID() + "/ peerAddress : " + peer.getPeerAddress()
					+ "/ peerPort : " + peer.getPort());
		}
	}

	public Client() {
		peerLists = PeerLists.getInstance();
	}

	@SuppressWarnings("unchecked")
	public void getPeerLists() {
		System.out.println("try server connect...");
		try (Socket client = new Socket("127.0.0.1", 15963);
				InputStream inputStream = client.getInputStream();
				BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
				ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream);) {
			System.out.println("try get peerlists");

			peerLists.addAll((LinkedList<Peer>) objectInputStream.readObject());
			// peerLists = (LinkedList<Peer>) objectInputStream.readObject();
			PeerLists.setLocalPeer(new Peer(peerLists.getLast().getPeerAddress(), peerLists.getLast().getPort()));

			System.out.println("local peer info : " + PeerLists.getLocalPeer().getPeerAddress() + " / " + PeerLists.getLocalPeer().getPort()
					+ " / " + PeerLists.getLocalPeer().getPeerID());

			System.out.println("get all peerList");
		} catch (UnknownHostException e) {
			System.out.println("can not find server");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error...");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("can not find LinkedList Class");
			e.printStackTrace();
		}
	}

	public void client(Object object) {
		System.out.println("try connect others peer...");
//		System.out.println(peerLists);
		for (Peer peer : peerLists) {
			if (peer.getPeerID().equals(PeerLists.getLocalPeer().getPeerID())) {
				continue;
			}

			new Thread() {
				public void run() {
					try (Socket client = new Socket(peer.getPeerAddress(), peer.getPort());
							OutputStream os = client.getOutputStream();
							BufferedOutputStream bos = new BufferedOutputStream(os);
							ObjectOutputStream oos = new ObjectOutputStream(bos);) {

						System.out.println("conneted peer");
						System.out.println("peer address : " + peer.getPeerAddress() + "/ port : " + peer.getPort());
						System.out.println("send data");

						oos.writeObject(object);
						System.out.println("complete send data");
						// ObjectInputStream ois = new ObjectInputStream(bis);
						// String str = new String((String)ois.readObject());
						// System.out.println(str);

						oos.close();

					} catch (UnknownHostException e) {
						e.printStackTrace();
					} catch (ConnectException e) {
						System.out.println(peer.getPeerID() + " client is closed");
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}.start();
		}
	}

}
