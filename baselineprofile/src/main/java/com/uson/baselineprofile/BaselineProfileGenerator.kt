package com.uson.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {
    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun generate() = baselineProfileRule.collect(
        packageName = "team.retum.jobis",
        stableIterations = 2,
        maxIterations = 8,
    ) {
        pressHome()
        startActivityAndWait()

        device.wait(Until.hasObject(By.pkg("team.retum.jobis")), 10_000)
        device.waitForIdle()

        Thread.sleep(1000)
    }
}
