package edu.hm.cs.w4

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import edu.hm.cs.w4.database.TimerDAO
import edu.hm.cs.w4.database.TimerDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.edit_timer.view.*
import kotlinx.coroutines.*


class MainActivity :
    AppCompatActivity(), TimerAdapter.TimerCallback {

    // Coroutine stuff
    private var databaseJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + databaseJob)

    private lateinit var timersFromDB: TimerDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager(this)
        val timerAdapter = TimerAdapter(emptyList(), this)
        recyclerView.adapter = timerAdapter

        timersFromDB = TimerDatabase.getInstance(application).timerDAO
        val timers = timersFromDB.getAllTimers();
        //observe when timers are loaded from DB
        timers.observe(this, Observer {
            timerAdapter.timers = it
            timerAdapter.notifyDataSetChanged()
        })

        fab.setOnClickListener {
            val timerIdx = timers.value!!.size + 1
            val name = "Timer ${timerIdx}"
            //use coroutines to insert timer to DB
            uiScope.launch {
                withContext(Dispatchers.IO) {
                    timersFromDB.insert(Timer(0L, 10 * timerIdx, name))
                }
            }
        }
    }

    override fun startTimer(timer: Timer) {
        timer.startTimer(this);
    }

    override fun deleteTimer(timer: Timer) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                timersFromDB.delete(timer)
            }
        }
    }

    override fun editTimer(timer: Timer) {
        val view: View = LayoutInflater.from(this).inflate(R.layout.edit_timer, null);
        view.timer_name.setText(timer.name)
        view.timer_seconds.setText(timer.seconds.toString())

        val builder = AlertDialog.Builder(this)
            .setTitle(R.string.edit_timer)
            .setView(view)
            .setPositiveButton("OK")
            { _, _ ->
                timer.name = view.timer_name.text.toString()
                timer.seconds = view.timer_seconds.text.toString().toInt()
                uiScope.launch {
                    withContext(Dispatchers.IO) {
                        timersFromDB.update(timer)
                    }
                }
            }
            .setNegativeButton("Cancel") { _, _ -> }
            .show()

        builder.show()


    }

}
