package com.ech.votoelectronicoseguridadinformatica;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Block {
    private String PO;
    private ArrayList<String> Transaction;
    private String preHash;
    private String Hash;

    public Block(String PO) {
        this.PO = PO;
        this.preHash="";
        this.Hash="";
        this.Transaction = new ArrayList<>();

    }

    public String hash_transact(String transaction){
            return Sha256.sha256(transaction);
    }

    public String hash_block(int n){
            return Sha256.sha256(this.preHash+this.PO+randomNumber(n));
    }

    public String final_hash_block(int n){
        if (preHash=="")return Sha256.sha256(this.Hash+this.Transaction.get(0)+randomNumber(n));
        return Sha256.sha256(this.preHash+this.Hash+this.Transaction.get(0)+this.Transaction.get(0)+randomNumber(n));

    }

    public String randomNumber(int n){
        System.out.println("rand "+ (int)( Math.random() * ((Math.pow(10,n)-1) - (Math.pow(10,n-1))) +(Math.pow(10,n-1))));

       return n+"";

    }


    public void seal(int n){
        this.Hash=this.hash_block(n);
    }

    public void final_seal(int n){
        ArrayList<String> newTransaction=new ArrayList<>();
        for (String transact: Transaction){
            newTransaction.add(hash_transact(transact));
        }
        this.Transaction=newTransaction;
        this.Hash=this.final_hash_block(n);
    }

    @Override
    public String toString() {
        return "Block{" +
                "PO='" + PO + '\'' +
                ", Transaction=" + Transaction +
                ", preHash='" + preHash + '\'' +
                ", Hash='" + Hash + '\'' +
                '}';
    }

    public String getPO() {
        return PO;
    }

    public void setPO(String PO) {
        this.PO = PO;
    }

    public ArrayList<String> getTransaction() {
        return Transaction;
    }

    public void addTransaction(String transaction) {
        Transaction.add( hash_transact( transaction));
    }

    public String getPreHash() {
        return preHash;
    }

    public void setPreHash(String preHash) {
        this.preHash = preHash;
    }

    public String getHash() {
        return Hash;
    }

    public void setHash(String hash) {
        Hash = hash;
    }
}
