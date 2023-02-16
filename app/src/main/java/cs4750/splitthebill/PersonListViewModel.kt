package cs4750.splitthebill

import android.content.Context
import androidx.lifecycle.ViewModel

/*class PersonListViewModel: ViewModel() {

    // List of Persons
    val persons = mutableListOf<Person>(
        Person(),
        Person(),
    )

    // Number of persons initialized to 2
    var numberOfPeople = 0

    // Numerical Values initialized to 0
    var tipAmount = 0.00
    var taxResult = 0.00
    var subtotalResult = 0.00

    init {

    }
}*/

class PersonListViewModel private constructor(context: Context) {

    companion object {
        // Number of persons initialized to 2
        var numberOfPeople = 0

        // List of Persons
        val persons = mutableListOf<Person>()

        // Numerical Values initialized to 0
        var tipAmount = 0.00
        var taxResult = 0.00
        var subtotalResult = 0.00

        private var INSTANCE: PersonListViewModel? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = PersonListViewModel(context)
            }
        }

        fun get(): PersonListViewModel {
            return INSTANCE ?:
            throw IllegalStateException("PersonListViewModel must be initialized")
        }

        fun getPeopleSize(): Int {
            return numberOfPeople
        }

        fun getTip(): Double {
            return tipAmount
        }

        fun getTax(): Double {
            return taxResult
        }

        fun getSubtotal(): Double {
            return subtotalResult
        }

    }
}