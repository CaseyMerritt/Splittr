package cs4750.splitthebill

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import java.util.regex.Matcher
import java.util.regex.Pattern
import cs4750.splitthebill.Person


private const val KEY_NUMOFPEOPLE= "numOfPeople"
private const val KEY_TIPAMOUNT= "tipAmount"
private const val KEY_TAXRESULT= "taxResult"
private const val KEY_SUBTOTALRESULT = "subTotalResult"

class MainActivity : AppCompatActivity()
{    // initialize counters
    //public var numberOfPeople = 0
    public var tipAmount = 0.00
    public var taxResult = 0.00
    public var subtotalResult = 0.00

    private lateinit var numberOfPeopleTextView: TextView
    //private lateinit var peopleSubtractImageView: ImageView
    private lateinit var peopleAddImageView: ImageView

    private lateinit var tipAmountTextView: TextView
    private lateinit var tipSubtractImageView: ImageView
    private lateinit var tipAddImageView: ImageView

    private lateinit var taxResultTextView: TextView
    private lateinit var subtotalResultTextView: TextView
    private lateinit var dollarsignImageView: ImageView
    private lateinit var dollarsignImageView2: ImageView

    private lateinit var personTax: TextView
    private lateinit var personTip: TextView

    lateinit var mainHandler: Handler

    /*private val personListViewModel: PersonListViewModel by lazy{
        ViewModelProviders.of(this).get(PersonListViewModel::class.java)
    }*/
    val personListViewModel = PersonListViewModel.initialize(this)

    override fun onResume() {
        mainHandler.post(updateRecyler)
        super.onResume()
    }


    private val updateRecyler = object : Runnable {
        override fun run() {
            if(numberOfPeopleTextView.text.toString().toInt() != PersonListViewModel.numberOfPeople){
                numberOfPeopleTextView.setText(PersonListViewModel.numberOfPeople.toString())
            }

            mainHandler.postDelayed(this, 100)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        mainHandler = Handler(Looper.getMainLooper())

        super.onCreate(savedInstanceState)
        setContentView(R.layout.bill_page)

        // Fragment for recycler view
        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null){
            val fragment = PersonListFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }

        // saved values of settings values
        var numberOfPeople = savedInstanceState?.getInt(KEY_NUMOFPEOPLE, 0) ?: 0
        PersonListViewModel.numberOfPeople = numberOfPeople
        var tipAmount = savedInstanceState?.getDouble(KEY_TIPAMOUNT, 0.0) ?: 0.0
        PersonListViewModel.tipAmount = tipAmount
        var taxResult = savedInstanceState?.getDouble(KEY_TAXRESULT, 0.0) ?: 0.0
        PersonListViewModel.taxResult = taxResult
        var subtotalResult = savedInstanceState?.getDouble(KEY_SUBTOTALRESULT, 0.0) ?: 0.0
        PersonListViewModel.subtotalResult=subtotalResult




        // Displays settings values
        numberOfPeopleTextView = findViewById(R.id.number_of_people_textView)
        peopleAddImageView = findViewById(R.id.people_add_imageView)

        tipAmountTextView = findViewById(R.id.tip_amount_textView)
        tipSubtractImageView = findViewById(R.id.tip_subtract_imageView)
        tipAddImageView = findViewById(R.id.tip_add_imageView)

        taxResultTextView = findViewById(R.id.tax_result_textView)
        // Limit input value digits before zero to 8 and 2 after zero
        taxResultTextView.setFilters(arrayOf<InputFilter>(DecimalDigitsInputFilter(8, 2)))

        subtotalResultTextView = findViewById(R.id.subtotal_result_textView)
        // Limit input value digits before zero to 8 and 2 after zero
        subtotalResultTextView.setFilters(arrayOf<InputFilter>(DecimalDigitsInputFilter(8, 2)))

        dollarsignImageView = findViewById(R.id.dollarsign_imageView)
        dollarsignImageView2 = findViewById(R.id.dollarsign_imageView2)

        peopleAddImageView.setOnClickListener {
            addPerson()
            Log.i(TAG, "Number of people: ${PersonListViewModel.numberOfPeople}")
        }

        tipSubtractImageView.setOnClickListener {
            decreaseTipAmount()
            checkTipAmount()
        }

        tipAddImageView.setOnClickListener {
            increaseTipAmount()
            checkTipAmount()
        }

        // Tap on the taxResultTextView to edit value
        taxResultTextView.setOnClickListener {

            val userText = taxResultTextView.text
            taxResultTextView.setText(userText)
            taxResultTextView.addTextChangedListener(textWatcher)
            taxResultTextView.clearFocus()
            PersonListViewModel.taxResult = userText.toString().toDouble()
            Log.i("TAG", "new tax result  aaaaa: ${PersonListViewModel.taxResult}")
        }

        // Tap on the subtotalResultTextView to edit value
        subtotalResultTextView.setOnClickListener {

            val userText = subtotalResultTextView.text
            subtotalResultTextView.setText(userText)
            subtotalResultTextView.addTextChangedListener(textWatcher)
            subtotalResultTextView.clearFocus()
            PersonListViewModel.subtotalResult = userText.toString().toDouble()
            Log.i("TAG", "new subtotal result aaaa: ${PersonListViewModel.subtotalResult}")
            //calculateIndividualTip()
        }

        checkTipAmount()

    }   // end onCreate function

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            /*if (!tipAmountTextView.text.isEmpty() && !subtotalResultTextView.text.isEmpty()) {
                Log.i(TAG, "Individual tip called")
                calculateIndividualTip()
                // **** moved updating and calculating individual tip to PersonListFragment.kt ****
            }*/

        }
        override fun beforeTextChanged(s: CharSequence, start: Int,
                                       count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int,
                                   before: Int, count: Int) {

        }
    }

    /*private fun removePerson()
    {
        var
        numberOfPeopleTextView.setText(numberOfPeople.toString())
    }   // end removePerson function*/

    private fun addPerson()
    {
        PersonListViewModel.numberOfPeople++
        numberOfPeopleTextView.setText(PersonListViewModel.numberOfPeople.toString())
    }   // end addPerson function

    private fun decreaseTipAmount()
    {
        tipAmount -= 5
        tipAmountTextView.setText(tipAmount.toString()+"%")
        PersonListViewModel.tipAmount = tipAmount
        Log.i("TAG", "tip amount: ${PersonListViewModel.tipAmount}")
        //calculateIndividualTip()
        //calculateIndividualTax()
        //calculateIndividualSubtotal()
    }   // end removePerson function

    private fun increaseTipAmount()
    {
        tipAmount += 5
        tipAmountTextView.setText(tipAmount.toString()+"%")
        PersonListViewModel.tipAmount = tipAmount
        Log.i("TAG", "tip amount: ${PersonListViewModel.tipAmount}")
        //calculateIndividualTip()
        //calculateIndividualTax()
        //calculateIndividualSubtotal()
    }   // end removePerson function

    private fun checkTipAmount()
    {
        if (tipAmount == 0.00)  // hide subtract button if tip amount is currently 0%
        {
            tipSubtractImageView.setVisibility(View.INVISIBLE)
        }
        else
        {
            tipSubtractImageView.setVisibility(View.VISIBLE)
        }

        if (tipAmount == 100.00)    // hide add button if tip amount is currently 100%
        {
            tipAddImageView.setVisibility(View.INVISIBLE)
        }
        else
        {
            tipAddImageView.setVisibility(View.VISIBLE)
        }

    }   // end checkTipAmount function

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putInt(KEY_NUMOFPEOPLE,  PersonListViewModel.numberOfPeople)
        savedInstanceState.putDouble(KEY_TIPAMOUNT,  PersonListViewModel.tipAmount)
        savedInstanceState.putDouble(KEY_TAXRESULT,  PersonListViewModel.taxResult)
        savedInstanceState.putDouble(KEY_SUBTOTALRESULT,  PersonListViewModel.subtotalResult)

    }

    override fun onStart() {
        super.onStart()
    }

    override fun onPause() {
        super.onPause()
    }

    // Filter to limit tax and subtotal input decimal values
    class DecimalDigitsInputFilter(digitsBeforeZero: Int, digitsAfterZero: Int) :
        InputFilter {
        var mPattern: Pattern
        override fun filter(
            source: CharSequence,
            start: Int,
            end: Int,
            dest: Spanned,
            dstart: Int,
            dend: Int
        ): CharSequence? {
            val matcher: Matcher = mPattern.matcher(dest)
            return if (!matcher.matches()) "" else null
        }

        init {
            mPattern =
                Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?")
        }
    }

}   // end MainActivity class