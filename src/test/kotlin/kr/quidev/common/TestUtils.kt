package kr.quidev.common

import java.util.UUID.randomUUID

class TestUtils {
    companion object {
        fun randomString(): String {
            return randomUUID().toString()
        }
    }
}
