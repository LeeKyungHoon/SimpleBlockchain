package server;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

import peer.Peer;
import peer.PeerLists;

public class Server {

	private ServerSocket serverSocket;
	private LinkedList<Peer> peerLists;

	public Server() throws IOException {
		peerLists = PeerLists.getInstance();
		serverSocket = new ServerSocket(15963);
	}

	public void chkPeer(String address, int port) {

		if (peerLists.isEmpty()) {
			System.out.println("nothing have any peer...");
			peerLists.add(new Peer(address, port));
		} else {
			boolean flag = true;
			for (Peer peer : peerLists) {
				if (!peer.getPeerAddress().equals(address) || peer.getPort() != port) {
					flag = false;
				}
			}
			if (!flag) {
				System.out.println("add peer...");
				peerLists.addLast(new Peer(address, port));
			}
		}
	}

	// peer가 들어올 때 마다 받고 다시 대기
	// public static void server(Blockchain coin) throws IOException {
	public void server() throws IOException {

		while (true) {
			System.out.println("\nWating for peer...");
			Socket socket = serverSocket.accept();
			System.out.println("peer connected");
			System.out.println("peerAddress : " + socket.getInetAddress() + "/ peerPort : " + socket.getPort());
			chkPeer(socket.getInetAddress().toString().replace("/", ""), socket.getPort());

			System.out.println("peerList send");
			try (OutputStream outputStream = socket.getOutputStream();
					BufferedOutputStream bos = new BufferedOutputStream(outputStream);
					ObjectOutputStream oos = new ObjectOutputStream(bos);) {

				oos.writeObject(peerLists);
				System.out.println("complete sending peerList");

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	public static void main(String[] args) {

		try {
			new Server().server();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
