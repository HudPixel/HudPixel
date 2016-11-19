package net.unaussprechlich.managedgui.lib.util.storage

/**
 * Created by kecka on 05.09.2016.
 */
class StorageFourSide {

    var LEFT: Short = 0
    var RIGHT: Short = 0
    var TOP: Short = 0
    var BOTTOM: Short = 0

    constructor() {
        this.LEFT = 0
        this.RIGHT = 0
        this.TOP = 0
        this.BOTTOM = 0
    }


    constructor(LEFT: Short, RIGHT: Short, TOP: Short, BOTTOM: Short) {
        this.LEFT = LEFT
        this.RIGHT = RIGHT
        this.TOP = TOP
        this.BOTTOM = BOTTOM
    }
}
