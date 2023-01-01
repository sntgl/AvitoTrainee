package ru.tagilov.avitotrainee.city.ui.viewmodel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.util.concurrent.ThreadLocalRandom


private val random: ThreadLocalRandom
    get() = ThreadLocalRandom.current()

fun randomString(): String {
    return "test_" + randomInt()
}

fun randomInt(): Int {
    return random.nextInt(Int.MAX_VALUE)
}


fun randomDouble(): Double {
    return random.nextDouble()
}

@ExperimentalCoroutinesApi
class MainDispatcherRule(val dispatcher: TestDispatcher = StandardTestDispatcher()) :
    TestWatcher() {

    override fun starting(description: Description) = Dispatchers.setMain(dispatcher)

    override fun finished(description: Description) = Dispatchers.resetMain()

}