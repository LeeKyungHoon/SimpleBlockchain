package chain;

import java.io.Serializable;
import java.util.ArrayList;

import block.Block;
import block.BlockHeader;
import mine.Miner;
import p2p.Client;
import transaction.Transaction;
import transaction.TransactionList;

public class Chain implements Serializable {

	public ArrayList<Block> blockList;
	public TransactionList TxList;
	public Miner miner;
	public BlockHeader header;

	private static class SingletonChain {
		private static final Chain chain = new Chain();
	}

	public static Chain getInstance() {
		return SingletonChain.chain;
	}

	public Chain() {
		blockList = new ArrayList<Block>();
		TxList = new TransactionList();
	}

	public void createNewTransactionList() {
		TxList = new TransactionList();
		System.out.println("created new TransactionList...");
	}

	public void insertTransaction(Transaction Tx) {
		TxList.addTransaction(Tx);
		System.out.println("inserted new Transaction...");
	}

	public void addAllBlocks(ArrayList<Block> blockList) {
		this.blockList.addAll(blockList);
	}

	public ArrayList<Block> getAllBlocks() {
		return this.blockList;
	}

	public ArrayList<Transaction> getTransactionList() {
		return TxList.getTransactionList();
	}
	
	public void createGenesisHeader() {
		header = new BlockHeader("", TxList.getTransactionList());
		System.out.println("genesis Header created...");
	}

	public void createNewHeader() {
		header = new BlockHeader(blockList.get(blockList.size() - 1).getBlockHash(), TxList.getTransactionList());
		System.out.println("new header created...");
	}

	public void mining() {
		System.out.println("mining start...");
		createNewHeader();
		miner = new Miner(header);
		miner.mineBlock();
		if (miner.getMineState()) {
			blockList.add(new Block(miner.getBlockHash(), header, TxList.getTransactionList()));
			System.out.println("add new block in blockList...");
			new Client().client(blockList.get(blockList.size()-1));
//			TxList.poolingTransaction();
		}
	}

	public void addBlock(Block block) {
		System.out.println("add Block...");
		
		blockList.add(block);
		
	}
	
	public void chkMinedBlock(Block block) {
		miner.stopMining();
		System.out.println("mining stoped...");
		miner = new Miner(new BlockHeader(block.getHeader().getPreviousHash(), block.getTranscations()));
//		miner.chkMinedBlock(block.getHeader().getNonce());
//		if (!block.getBlockHash().equals(miner.getBlockHash())) {
//			System.out.println("wrong mining");
//		} else {
//			System.out.println("add recieved block..");
//		}
		blockList.add(block);
//		TxList.poolingTransaction();
	}

	public void createGenesisBlock() {
		blockList.add(new Block(new Miner(header).getBlockHash(), header, TxList.getTransactionList()));
		System.out.println("created genesisBlock...");
	}

	public void chkAndPoolingTx() {
		TxList.poolingTransaction();
	}

	public int getBlockSize() {
		return blockList.size();
	}

	public int getSizeTransactionList() {
		return this.TxList.getTransactionList().size();
	}
}
