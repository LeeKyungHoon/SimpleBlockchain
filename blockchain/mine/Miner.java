package mine;

import block.BlockHeader;
import security.StringUtil;

public class Miner {

	private String previousHash, merckleTreeHash;
	private long timestamp;
	private int nonce, difficulty;
	private String blockHash;
	private boolean mineState;

	public Miner(BlockHeader header) {
		this.previousHash = header.getPreviousHash();
		this.merckleTreeHash = header.getMerckleTreeHash();
		this.timestamp = header.getTimestamp();
		this.nonce = 0;
		this.difficulty = header.getDifficulty();
		this.mineState = true;
		this.blockHash = calculateHash();
	}
	
	public void chkMinedBlock(int nonce) {
		this.nonce = nonce;
		this.blockHash = calculateHash();
	}
	
	public void mineBlock() {
		String target = new String(new char[difficulty]).replace('\0', '0');
		while (!this.blockHash.substring(0, difficulty).equals(target)) {
			if(!mineState)break;
			this.nonce++;
			this.blockHash = calculateHash();
		}
		System.out.println("Block Mined : " + this.blockHash);
	}
	
	public boolean getMineState() {
		return mineState;
	}
	
	public void stopMining() {
		this.mineState = false;
	}

	public String calculateHash() {
		return StringUtil.applySha256(this.previousHash + Long.toString(this.timestamp) + Integer.toString(this.nonce) + this.merckleTreeHash);
	}
	
	public String getBlockHash() {
		return blockHash;
	}
	
	public int getNonce() {
		return this.nonce;
	}

}
