package transaction;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;

import security.StringUtil;

public class Transaction implements Serializable {

	public String transactionId;
	public PublicKey sender;
	public PublicKey reciepient;
	public float value;
	public byte[] signature;

	private static int sequence = 0;

	public Transaction(PublicKey from, PublicKey to, float value) {
		this.sender = from;
		this.reciepient = to;
		this.value = value;
		this.transactionId = calculateHash();
	}

	private String calculateHash() {
		sequence++;
		return StringUtil.applySha256(StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient)
				+ Float.toString(value) + sequence);
	}

	public void generateSignature(PrivateKey privateKey) {
		String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient)
				+ Float.toString(value);
		signature = StringUtil.applyECDSASig(privateKey, data);
	}

	public boolean verifySignature() {
		String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient)
				+ Float.toString(value);
		return StringUtil.verifyECDSASig(sender, data, signature);
	}

}
