package block;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import merkleTree.MerkleTree;
import transaction.Transaction;

public class BlockHeader implements Serializable {

	private String previousHash, merckleTreeHash;
	private long timestamp;
	private int difficulty, nonce;
	
	public BlockHeader(String previousHash, ArrayList<Transaction> transactions) {
		this.previousHash = previousHash;
		this.merckleTreeHash = new MerkleTree().getMerkleRoot(transactions);
		this.timestamp = new Date().getTime();
		this.difficulty = 5;
		this.nonce = 0;
	}

	public String getHeaderData() {
		return previousHash+merckleTreeHash+String.valueOf(timestamp)+String.valueOf(difficulty)+String.valueOf(nonce);
	}

	public String getPreviousHash() {
		return previousHash;
	}

	public String getMerckleTreeHash() {
		return merckleTreeHash;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public int getNonce() {
		return nonce;
	}
	
	public void setNonce(int nonce) {
		this.nonce = nonce;
	}
}
