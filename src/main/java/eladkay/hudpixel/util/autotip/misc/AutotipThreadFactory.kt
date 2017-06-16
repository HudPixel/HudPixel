package eladkay.hudpixel.util.autotip.misc

import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

object AutotipThreadFactory : ThreadFactory {

    private val threadNumber = AtomicInteger(1)

    override fun newThread(r: Runnable): Thread {
        return Thread(r, "Autotip" + threadNumber.getAndIncrement())
    }

}