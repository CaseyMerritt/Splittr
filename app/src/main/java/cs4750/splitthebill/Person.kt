package cs4750.splitthebill

import android.util.Log
import android.widget.TextView
import java.util.*
import kotlin.collections.ArrayList

class Person {

    // Name of person
    var name: String = "Person"

    // List of items a person is paying for
    val items = mutableListOf<Item>(
        Item("Item", 0.00),
    )

    //is the list supposed to be visible or not
    var isVisible = true

    // Total amount a person is paying
    var total: Double = 0.00

    // tip amount for person
    var tip: Double = 0.00

    // tax amount for person (based on % of total subtotal they spent)
    var tax: Double = 0.00

    // Adds blank item to item list
    fun addItem() {
        var item= Item()
        items+=item
    }

    // Calculates total owed by person
    fun calculateTotal(num:Double){
        total+=num
    }

}




