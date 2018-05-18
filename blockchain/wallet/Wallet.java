package wallet;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import transaction.Transaction;

public class Wallet {

	public PrivateKey privateKey;
	public PublicKey publicKey;
	private float balance;
	private ArrayList<Transaction> transactions;

	private static class SingletonWallet {
		private static final Wallet WALLET = new Wallet();
	}

	public static Wallet getInstance() {
		return SingletonWallet.WALLET;
	}

	public Wallet() {
		generateKeyPair();
		this.balance = 100;
		this.transactions = new ArrayList<Transaction>();
	}

	public void generateKeyPair() {

		Security.addProvider(new BouncyCastleProvider());
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");

			keyGen.initialize(ecSpec, random);
			KeyPair keyPair = keyGen.generateKeyPair();

			privateKey = keyPair.getPrivate();
			publicKey = keyPair.getPublic();
		} catch (Exception e) {
			throw new RuntimeException();
		}

	}

	public Transaction createTransaction(PublicKey to, PublicKey from, float value) {
		Transaction Tx = new Transaction(from, to, value);
		Tx.generateSignature(privateKey);
		this.balance -= value;
		return Tx;
	}

	public ArrayList<Transaction> getTransactionList() {
		return transactions;
	}

	public void removeTransaction(Transaction Tx) {
		transactions.remove(Tx);
	}

	public float getBalance() {
		return this.balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}

	// public void getBalance(TransactionList list) {
	// this.balance = list.pooledValue();
	// }

}
