package block;

import java.io.Serializable;
import java.util.ArrayList;

import transaction.Transaction;

public class Block implements Serializable{

	public String blockHash;
	public BlockHeader header;
	public ArrayList<Transaction> transcations;

	public String getBlockHash() {
		return blockHash;
	}

	public BlockHeader getHeader() {
		return header;
	}

	public ArrayList<Transaction> getTranscations() {
		return transcations;
	}

	public Block(String blockHash, BlockHeader header, ArrayList<Transaction> transactions) {
		this.header = header;
		this.transcations = transactions;
		this.blockHash = blockHash;
	}
}
