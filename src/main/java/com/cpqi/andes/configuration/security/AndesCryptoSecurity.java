package com.cpqi.andes.configuration.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author tfacundo
 *
 */
public class AndesCryptoSecurity {

	private static final byte[] SALT = { (byte) 0x95, (byte) 0xDF, (byte) 0xAD, (byte) 0x65, (byte) 0xE5, (byte) 0x7F,
			(byte) 0xEF, (byte) 0x61, (byte) 0xA7, (byte) 0x97, (byte) 0x133, (byte) 0x14B, (byte) 0x191, (byte) 0x233,
			(byte) 0x1C1, (byte) 0x277 };

	private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
	private static final String AES = "AES";

	private Cipher cipher = null;
	private Key key = null;
	private IvParameterSpec ivParamSpec = null;

	public AndesCryptoSecurity(String password)
			throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException {

		this.ivParamSpec = new IvParameterSpec(SALT);
		this.key = new SecretKeySpec(password.getBytes(), AES);
		this.cipher = Cipher.getInstance(ALGORITHM);
	}

	public byte[] encrypt(byte[] valueToEncrypt) throws InvalidKeyException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException {

		this.cipher.init(Cipher.ENCRYPT_MODE, this.key, this.ivParamSpec);

		return this.cipher.doFinal(valueToEncrypt);
	}

	public byte[] decrypt(byte[] valueToDecrypt) throws InvalidKeyException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {

		this.cipher.init(Cipher.DECRYPT_MODE, this.key, this.ivParamSpec);

		return this.cipher.doFinal(valueToDecrypt);
	}

	public String buildPipeArray(byte[] encryptedValue) {
		final StringBuilder acc = new StringBuilder();
		for (int i = 0; i < encryptedValue.length; i++) {

			acc.append(encryptedValue[i]);

			if (i < encryptedValue.length - 1) {
				acc.append("|");
			}
		}

		return acc.toString();
	}

	public byte[] splitPipeArray(String pipeArray) {

		if (pipeArray == null) {
			throw new IllegalArgumentException();
		}

		final String[] values = pipeArray.split("\\|");
		final byte[] byteValues = new byte[values.length];

		for (int i = 0; i < values.length; i++) {
			byteValues[i] = Byte.valueOf(values[i]);
		}

		return byteValues;
	}
}
