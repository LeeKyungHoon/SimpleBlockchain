import java.io.IOException;
import java.util.Base64;
import java.util.LinkedList;
import java.util.Scanner;

import block.Block;
import chain.Chain;
import p2p.Client;
import p2p.Server;
import peer.Peer;
import peer.PeerLists;
import security.StringUtil;
import transaction.Transaction;
import wallet.Wallet;

public class Main {

	static LinkedList<Peer> peers;

	public static void main(String[] args) throws Exception {

		peers = PeerLists.getInstance();
		Chain blockchain = Chain.getInstance();
		Wallet wallet = new Wallet();

		Scanner scanner = new Scanner(System.in);

		Client client = new Client();
		client.getPeerLists();

		for (Peer peer : peers) {
			System.out.println("peer address : " + peer.getPeerAddress() + "/ port : " + peer.getPort());
		}

		Server server = new Server();
		client.client(peers.getLast());

		new Thread() {
			public void run() {
				try {
					server.connect();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();

		System.out.println("Wallet A public Key"+StringUtil.getStringFromKey(wallet.publicKey));


		Thread.sleep(3000);

		System.out.println(blockchain.getBlockSize());
		if (blockchain.getBlockSize() == 0) {
			System.out.println("create genesis Block");
			blockchain.createGenesisHeader();
			blockchain.createGenesisBlock();
			System.out.println("createad genesis Block..." + blockchain.getAllBlocks().get(0));
		} else {
			System.out.println("exist genesis block...");
		}

		blockchain.createNewTransactionList();

		new Thread() {
			public void run() {
				while (true) {
					System.out.print("who send money? : ");
					String to = scanner.next();
					
					if(to.equals("chain")) {
						for (Block block : blockchain.getAllBlocks()) {
							System.out.println(block.getBlockHash());
						}
						continue;
					}else if(to.equals("money")) {
						System.out.println(wallet.getBalance());
						continue;
					}
					
					
					System.out.print("How much send money? : ");
					float value = scanner.nextFloat();

					Transaction Tx = new Transaction(StringUtil.getKeyFromString(to), wallet.publicKey, value);
					Tx.generateSignature(wallet.privateKey);

					blockchain.insertTransaction(Tx);
					System.out.println("to occur transaction...");

					System.out.println("send new Transaction...");
					client.client(Tx);

					System.out.println("block mining...");
					blockchain.mining();
					
					blockchain.createNewTransactionList();

				}
			}
		}.start();

	}

}
