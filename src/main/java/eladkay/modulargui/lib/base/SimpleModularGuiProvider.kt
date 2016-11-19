package eladkay.modulargui.lib.base

import eladkay.modulargui.lib.IModularGuiProvider

abstract class SimpleModularGuiProvider : IModularGuiProvider {
    override fun ignoreEmptyCheck(): Boolean {
        return false
    }
}
