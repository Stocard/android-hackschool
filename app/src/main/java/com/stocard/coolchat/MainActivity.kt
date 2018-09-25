package com.stocard.coolchat

import android.app.ProgressDialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


class MainActivity : AppCompatActivity() {

    private var progressDialog: ProgressDialog? = null
    private val prefs by lazy { PreferenceManager.getDefaultSharedPreferences(this) }
    private var name: String? = null

    private val adapter: ArrayAdapter<String> by lazy {
        ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mutableListOf())
    }

    private val messageParser: JsonAdapter<List<Message>> by lazy {
        val type = Types.newParameterizedType(List::class.java, Message::class.java)
        Moshi.Builder().build().adapter<List<Message>>(type)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        chat_list.adapter = adapter

        send_button.setOnClickListener {
            val input = message_input.text.toString()
            send(input)
            message_input.text = null
        }
    }

    override fun onResume() {
        super.onResume()
        fetchMessages()

        name = prefs.getString("name", null)
        if (name == null) {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun send(message: String) {
        PostMessageTask().execute("https://android-hackschool.herokuapp.com/message", name, message)
    }

    private fun fetchMessages() {
        GetMessagesTask().execute("https://android-hackschool.herokuapp.com")
    }

    private fun showMessages(messages: List<Message>) {
        val messageItems = messages.map { it.name + ": " + it.message }
        adapter.clear()
        adapter.addAll(messageItems)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(R.string.refresh).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        fetchMessages()
        return true
    }

    /**
     * Borrowed form https://stackoverflow.com/a/37525989/570168.
     */
    private inner class GetMessagesTask : AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            super.onPreExecute()

            progressDialog = ProgressDialog(this@MainActivity).apply {
                setMessage(getString(R.string.getting_messages_dialog_message))
                setCancelable(false)
                show()
            }
        }

        override fun doInBackground(vararg params: String): String? {

            var connection: HttpURLConnection? = null

            try {
                Thread.sleep(100) // for some more drama on fast networks ;-)
                val url = URL(params[0])
                connection = url.openConnection() as HttpURLConnection
                connection.connect()

                val stream = connection.inputStream

                val response = BufferedReader(InputStreamReader(stream)).use {
                    it.readLines().joinToString("\n")
                }
                Log.d("Response: ", "> $response")
                return response

            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } finally {
                connection?.disconnect()
            }
            return null
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            progressDialog?.dismiss()

            var messages: List<Message>? = null
            try {
                messages = messageParser.fromJson(result)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            if (messages != null) {
                showMessages(messages)
            }
        }
    }

    private inner class PostMessageTask : AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            super.onPreExecute()

            progressDialog = ProgressDialog(this@MainActivity).apply {
                setMessage(getString(R.string.posting_messages_dialog_message))
                setCancelable(false)
                show()
            }
        }

        override fun doInBackground(vararg params: String): String? {

            var connection: HttpURLConnection? = null

            try {
                Thread.sleep(100) // for some more drama on fast networks ;-)
                val url = URL(params[0])
                connection = (url.openConnection() as HttpURLConnection).apply {
                    requestMethod = "POST"
                    setRequestProperty("Content-Type", "application/json")
                    setRequestProperty("Accept", "application/json")
                    doOutput = true
                    doInput = true
                }
                connection.connect()

                val jsonParam = JSONObject().apply {
                    put("name", params[1])
                    put("message", params[2])
                }

                DataOutputStream(connection.outputStream).use { outputStream ->
                    outputStream.writeBytes(jsonParam.toString())
                    outputStream.flush()
                    outputStream.close()
                }

                Log.i("STATUS", connection.responseCode.toString())
                Log.i("MSG", connection.responseMessage)

            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } catch (e: JSONException) {
                e.printStackTrace()
            } finally {
                connection?.disconnect()
            }
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            progressDialog?.dismiss()
            fetchMessages()
        }
    }
}
