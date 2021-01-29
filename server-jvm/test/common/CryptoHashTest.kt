package doreshnikov.common

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertEquals

class CryptoHashTest {

    @Test
    fun validatesCorrectly() {
        listOf("", "data", "long".repeat(10)).forEach { s ->
            assertTrue { CryptoHash.validate(s, CryptoHash.encode(s)) }
        }
    }

    @Test
    fun noCollisions() {
        assertEquals(
            listOf("", "data", "long".repeat(10))
                .map(CryptoHash::encode).toSet().size,
            3
        )
    }

}