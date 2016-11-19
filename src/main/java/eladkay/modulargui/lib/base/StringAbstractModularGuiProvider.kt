package eladkay.modulargui.lib.base

import eladkay.modulargui.lib.IModularGuiProvider

/**
 * This class is meant to show a correct implementation of IModularGuiProvider.
 * This is an abstract implementation: it can be used in more than one element and still, possibly
 * have different content.

 * @author Eladkay
 * *
 * @since 1.6
 */
class StringAbstractModularGuiProvider
/**
 * This is the default constructor.

 * @param s the content to display
 */
(//the content to display.
        private val content: String) : IModularGuiProvider {
    override fun getAfterstats(): String {
        throw UnsupportedOperationException()
    }

    override fun showElement(): Boolean {
        return true
    } //elements using this provider will always be shown.

    override fun content(): String {
        return content
    } //display the content

    override fun ignoreEmptyCheck(): Boolean {
        return false
    }

}

