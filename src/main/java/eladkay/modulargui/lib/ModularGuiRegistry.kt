package eladkay.modulargui.lib

import com.google.common.collect.Lists
import com.mojang.realmsclient.gui.ChatFormatting
import com.palechip.hudpixelmod.util.plus
import eladkay.modulargui.lib.base.EmptyModularGuiProvider
import eladkay.modulargui.lib.base.NameModularGuiProvider
import eladkay.modulargui.lib.base.StringAbstractModularGuiProvider
import java.util.logging.Logger

/**
 * This class is meant to serve as a registry to the Modular GUI Lib.

 * @author Eladkay
 * *
 * @since 1.6
 */
object ModularGuiRegistry {

    //Should register example elements?
    var shouldRegisterExampleElements = false

    //List of all elements in the Modular GUI
    var allElements = Lists.newArrayList<Element>()
    /**
     * Example elements.
     * You should keep a constant of your elements in some sort of registry class.
     */
    val TITLE = Element("", StringAbstractModularGuiProvider(ChatFormatting.AQUA + "Modular" + ChatFormatting.GOLD + "Gui"))
    val NAME = Element("IGN", NameModularGuiProvider())
    val GROUPER = Element("", EmptyModularGuiProvider(), true)

    /**
     * Register an element to the modular GUI.

     * @param element The element to register
     */
    fun registerElement(element: Element) {
        if (!allElements.contains(element) || element.shouldAllowDuplicates() ?: false)
            allElements.add(element)
        else
            Logger.getLogger("modulargui").warning("Tried to register element " + element.name +
                    " and it was already registered!")
    }

    init {

        if (shouldRegisterExampleElements) { //if it should register example elements...
            //register the example elements
            registerElement(TITLE)
            registerElement(GROUPER)
            registerElement(GROUPER)
            registerElement(NAME)
        }
    }

    /**
     * Instances of this class represent seperate lines in the Modular GUI HUD.
     * This class is immutable.

     * @author Eladkay
     * *
     * @since 1.6
     */
    class Element {


        override fun toString(): String {
            return "Element{name='$name', provider=$provider}"
        }

        /**
         * Default constructor.

         * @param name     The name to be displayed before the content of the element in the modular GUI HUD
         * *
         * @param provider The IModularGuiProvider that provides the content for the element
         */
        constructor(name: String, provider: IModularGuiProvider) {
            this.name = name
            this.provider = provider
        }

        /**
         * Should allow duplicate elements of this type?

         * @param name           The name to be displayed before the content of the element in the modular GUI HUD
         * *
         * @param provider       The IModularGuiProvider that provides the content for the element
         * *
         * @param allowDuplicate should duplicate elements of this type be allowed?
         */
        constructor(name: String, provider: IModularGuiProvider, allowDuplicate: Boolean) {
            this.name = name
            this.provider = provider
            this.allowDuplicate = allowDuplicate
        }

        //the name of the element
        val name: String
        //the provider of the element
        val provider: IModularGuiProvider
        //should this elements be allowed to be registered more than once?
        private var allowDuplicate: Boolean? = false

        fun shouldAllowDuplicates(): Boolean? {
            return allowDuplicate
        }
    }
}
