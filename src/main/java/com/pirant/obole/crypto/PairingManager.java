package com.pirant.obole.crypto;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.util.Arrays;

public class PairingManager {

    public static String generateConfirmationCode(PublicKey remoteKey) throws Exception{
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(remoteKey.getEncoded());
        return String.format("%06d", Math.abs(Arrays.hashCode(hash)) % 1_000_000);
    }

    public static boolean confirmCode(String localCode, String remoteCode){
        return localCode.equals(remoteCode);
    }
}
