package p2p;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

import block.Block;
import chain.Chain;
import peer.Peer;
import peer.PeerLists;
import transaction.Transaction;

public class Server {

	private ServerSocket serverSocket;
	private LinkedList<Peer> peerLists;

	public Server() throws IOException {
		peerLists = PeerLists.getInstance();
		System.out.println("last Peer port" + peerLists.getLast().getPort());
		serverSocket = new ServerSocket(peerLists.getLast().getPort());
	}

	public void connect() throws IOException {

		while (true) {
			// 새로운 소켓이 들어올 때마다 서버에 접속 할 수 있게 함
			System.out.println("\nWating for peer...");
			Socket socket = serverSocket.accept();
			System.out.println("connected...");
			try (InputStream is = socket.getInputStream();
					BufferedInputStream bis = new BufferedInputStream(is);
					ObjectInputStream ois = new ObjectInputStream(bis);) {
				System.out.println("object received");
				chkType(ois.readObject());
				// ObjectOutputStream oos = new ObjectOutputStream(bos);
				// oos.writeObject("object received");

				ois.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	@SuppressWarnings("unchecked")
	public void chkType(Object reciever) {
		if (reciever instanceof Peer) {
			chkPeerList((Peer) reciever);

			if (Chain.getInstance().getBlockSize() != 0) {
				System.out.println(Chain.getInstance().getBlockSize());
				// for (int i = 0; i < Chain.getInstance().getBlockSize(); i++) {
				// new Client().client(Chain.getInstance().getAllBlocks().get(i));
				// System.out.println("send block...");
				// }
				new Client().client(Chain.getInstance().getAllBlocks());
			}

		} else if (reciever instanceof Block) {
			System.out.println("recieved mined Block...");

			Block block = (Block) reciever;

			new Thread() {
				public void run() {
					Chain.getInstance().chkMinedBlock(block);
				}
			}.start();
			
			

			// Chain.getInstance().addBlock((Block) reciever);
		} else if (reciever instanceof Transaction) {
			Chain.getInstance().insertTransaction((Transaction) reciever);
			System.out.println("recieved and added Transaction...");
			System.out.println("start mining...");
			Chain.getInstance().mining();
			Chain.getInstance().createNewTransactionList();
		} else if (reciever instanceof ArrayList<?>) {

			ArrayList<Block> list = (ArrayList<Block>) reciever;

			System.out.println(list);
			for (int i = 0; i < (list).size(); i++) {
				System.out.println(list.get(i));
			}
			Chain.getInstance().addAllBlocks((list));
			System.out.println("added all Blocks");
			System.out.println(Chain.getInstance().getBlockSize());
			for (int i = 0; i < Chain.getInstance().getBlockSize(); i++) {
				System.out.println(Chain.getInstance().getAllBlocks().get(i));
			}
		}
	}

	public void chkPeerList(Peer peer) {
		boolean exist = false;
		for (int index = 0; index < peerLists.size(); index++) {
			if (peerLists.get(index).getPeerAddress().equals(peer.getPeerAddress())
					&& peerLists.get(index).getPort() == peer.getPort()) {
				exist = true;
			}
		}
		if (!exist) {
			peerLists.addLast(peer);
			System.out.println("add peer");
		}
	}

}
