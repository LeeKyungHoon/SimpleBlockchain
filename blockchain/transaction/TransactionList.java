package transaction;

import java.io.Serializable;
import java.security.PublicKey;
import java.util.ArrayList;

import wallet.Wallet;

public class TransactionList implements Serializable {

	public ArrayList<Transaction> transactions;
	public PublicKey key;

	public TransactionList() {
		this.transactions = new ArrayList<Transaction>();
		this.key = Wallet.getInstance().publicKey;
	}
	
	public void addTransaction(Transaction Tx) {
		this.transactions.add(Tx);
	}
	
	public void addTransactionList(ArrayList<Transaction> TxList) {
		this.transactions.addAll(TxList);
	}

	public ArrayList<Transaction> getTransactionList() {
		return transactions;
	}

	public void poolingTransaction() {
	
		ArrayList<Transaction> Txs = new ArrayList<Transaction>();
		float inputValue = 0;
		float outputValue = 0;
		ArrayList<Transaction> removeTx = new ArrayList<Transaction>();
		//자신에게 해당하는 트랜잭션들 확인 및 새로운 리스트에 넣기
		for (Transaction transaction : transactions) {
			if(transaction.verifySignature()) {
				Txs.add(transaction);
			}
		}
		
		//출금값 추산
		for (Transaction transaction : Txs) {
			if(transaction.sender.equals(key)) {
				if(outputValue>Wallet.getInstance().getBalance()) {
					System.out.println("cancle Transaction" + transaction.transactionId);
					Wallet.getInstance().removeTransaction(transaction);
					removeTx.add(transaction);
				}else {
					outputValue+=transaction.value;
				}
			}
		}
		
		Wallet.getInstance().setBalance(Wallet.getInstance().getBalance()-outputValue);
		
		if(!removeTx.isEmpty()) {
			Txs.removeAll(removeTx);
		}
		
		//입금되는 값들 추산
		for (Transaction transaction : Txs) {
			if(transaction.reciepient.equals(key)) {
				inputValue+=transaction.value;
			}
		}
		
		Wallet.getInstance().setBalance(Wallet.getInstance().getBalance() + inputValue);
		
	}
	
//	public float pooledValue() {
//		return this.inputValue;
//	}
	

}
