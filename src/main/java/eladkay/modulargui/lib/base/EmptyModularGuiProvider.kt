package eladkay.modulargui.lib.base

import eladkay.modulargui.lib.IModularGuiProvider

/**
 * This class is meant to show a correct implementation of IModularGuiProvider.
 * This is a static implementation: all elements using it will always have the same value.
 * Using this provider on an element will make sure it's always completely empty, so it can be used as a grouper.

 * @author Eladkay
 * *
 * @since 1.6
 */
class EmptyModularGuiProvider : IModularGuiProvider {
    override fun getAfterstats(): String {
        throw UnsupportedOperationException()
    }

    override fun showElement(): Boolean {
        return true
    }

    override fun content(): String {
        return ""
    }

    override fun ignoreEmptyCheck(): Boolean {
        return true
    }
}
