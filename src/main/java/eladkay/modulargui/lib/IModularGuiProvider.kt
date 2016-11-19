package eladkay.modulargui.lib

/**
 * Classes implementing this interface will be able to serve as providers for elements of the Modular GUI

 * @author Eladkay
 * *
 * @since 1.6
 */
interface IModularGuiProvider {
    fun showElement(): Boolean  //should this element be shown?

    fun content(): String  //content of the element

    fun ignoreEmptyCheck(): Boolean  //return true if this element should be displayed even if the content is empty

    fun getAfterstats(): String
}
