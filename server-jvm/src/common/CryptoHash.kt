package doreshnikov.common

import org.apache.commons.codec.binary.Base64
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

object CryptoHash {

    private val hasher = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
    private val salt = SecureRandom().generateSeed(16)

    fun encode(value: String): String {
        val keySpec = PBEKeySpec(value.toCharArray(), salt, 64, 128)
        val passwordHash = hasher.generateSecret(keySpec).encoded
        return Base64.encodeBase64String(passwordHash)
    }

    fun validate(value: String, hash: String): Boolean {
        return encode(value) == hash
    }

}