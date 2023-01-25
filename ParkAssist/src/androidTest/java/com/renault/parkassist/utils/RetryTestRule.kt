package com.renault.parkassist.utils

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class RetryTestRule(val retryCount: Int = 3) : TestRule {

    override fun apply(base: Statement, description: Description): Statement {
        return statement(base, description)
    }

    private fun statement(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                (1..retryCount).asSequence().forEach { tryIndex ->
                    try {
                        base.evaluate()
                        return
                    } catch (t: Throwable) {
                        if (tryIndex == retryCount) {
                            throw RetryException(description.displayName, retryCount, t)
                        }
                    }
                }
            }
        }
    }
}

class RetryException(displayName: String, retryCount: Int, cause: Throwable) : Exception(cause) {
    override val message = displayName + ": giving up after " +
        retryCount + " failures"
}