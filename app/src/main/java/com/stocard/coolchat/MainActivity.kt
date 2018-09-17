package com.stocard.coolchat

import android.app.ProgressDialog
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*

class MainActivity : AppCompatActivity() {

    // private val chatListView: ListView  by lazy { this.findViewById<ListView>(R.id.chat_list) }

    private var pd: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        fetchKotlinMessages()
    }

    private fun fetchKotlinMessages(): AsyncTask<String, String, String> {
        return JsonTask().execute("https://android-hackschool.herokuapp.com")
    }

    /**
     * Borrowed form https://stackoverflow.com/a/37525989/570168.
     */
    private inner class JsonTask : AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            super.onPreExecute()

            pd = ProgressDialog(this@MainActivity)
                    .apply {
                        this.setMessage("Please wait")
                        this.setCancelable(false)
                        this.show()
                    }
        }

        override fun doInBackground(vararg params: String): String? {

            var connection: HttpURLConnection? = null
            var reader: BufferedReader? = null

            try {
                Thread.sleep(1000) // for some more drama on fast networks ;-)
                val url = URL(params[0])
                connection = url.openConnection() as HttpURLConnection
                connection.connect()


                val stream = connection.inputStream

                reader = BufferedReader(InputStreamReader(stream))

                val buffer = StringBuffer()

                for (line in reader.lines()) {
                    buffer.append(line + "\n")
                    Log.d("Response: ", "> $line")
                }

                return buffer.toString()

            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } finally {
                connection?.disconnect()
                try {
                    reader?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            return null
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            pd?.dismiss()
            pd = null

            val moshi = Moshi.Builder().build()
            val type = Types.newParameterizedType(List::class.java, Message::class.java)
            val jsonAdapter = moshi.adapter<List<Message>>(type)
            var messages: List<Message>? = null
            try {
                messages = jsonAdapter.fromJson(result)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            if (messages != null) {
                showKotlinMessages(messages)
            }

        }
    }

    private fun showKotlinMessages(messages: List<Message>) {
        val messageItems = ArrayList<String>()
        for (message in messages) {
            messageItems.add(message.name + ": " + message.message)
        }
        chat_list.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, messageItems)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add("Refresh").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        fetchKotlinMessages()
        return true
    }
}
