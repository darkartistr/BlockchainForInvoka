import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;

class Block {
    public String hash;
    public String previousHash;
    private String data;
    private long timeStamp;
    private int nonce;

    public Block(String data, String previousHash) {
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    public String calculateHash() {
        String input = previousHash + Long.toString(timeStamp) + Integer.toString(nonce) + data;
        return applySha256(input);
    }

    public void mineBlock(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Block mined: " + hash);
    }

    public static String applySha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

class Blockchain {
    public static ArrayList<Block> blockchain = new ArrayList<>();
    public static int difficulty = 4;

    public static boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;

        for (int i = 1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i - 1);

            if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
                System.out.println("Current Hashes not equal");
                return false;
            }

            if (!currentBlock.previousHash.equals(previousBlock.hash)) {
                System.out.println("Previous Hashes not equal");
                return false;
            }
        }
        return true;
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to INVOKA Token Blockchain!");

        Block firstBlock = new Block("Genesis Block - 1000 INVOKA to Alice", "0");
        System.out.println("Mining first block...");
        firstBlock.mineBlock(4);

        Block secondBlock = new Block("Transfer 50 INVOKA from Alice to Bob", firstBlock.hash);
        System.out.println("Mining second block...");
        secondBlock.mineBlock(4);

        Block thirdBlock = new Block("Transfer 20 INVOKA from Bob to Charlie", secondBlock.hash);
        System.out.println("Mining third block...");
        thirdBlock.mineBlock(4);

        Blockchain.blockchain.add(firstBlock);
        Blockchain.blockchain.add(secondBlock);
        Blockchain.blockchain.add(thirdBlock);

        boolean isValid = Blockchain.isChainValid();
        System.out.println("Is the INVOKA blockchain valid? " + isValid);
    }
}
