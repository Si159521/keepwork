package com.example.a23_hf069

import android.app.ProgressDialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class P_loginFragment : Fragment() { //개인로그인
    private var IP_ADDRESS = "3.34.126.115" // 본인 IP주소를 넣으세요.

    private var TAG = "phptest" // phptest log 찍으려는 용도
    private lateinit var id_text_input_edit_text: EditText // id
    private lateinit var password_text_input_edit_text: EditText // password
    private lateinit var id: String // 사용자 아이디
    lateinit var login : Button
    lateinit var signUp : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        return inflater.inflate(R.layout.fragment_p_login, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        login = view.findViewById<Button>(R.id.login_btn)
        signUp = view.findViewById<Button>(R.id.signUp_btn)

        id_text_input_edit_text = view.findViewById<EditText>(R.id.id_text)
        password_text_input_edit_text = view.findViewById<EditText>(R.id.pw_text)

        login.setOnClickListener() {

            id = id_text_input_edit_text.text.toString().trim()
            val password = password_text_input_edit_text.text.toString().trim()

            if (id.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "정보를 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                val task = SelectData()
                task.execute("http://$IP_ADDRESS/android_login_php.php", id, password)
            }
        }

        signUp.setOnClickListener() {
            // signUp버튼을 클릭하면 CorporateSignUpActivity로 전환
            val intent = Intent(getActivity(), PersonalSignUpActivity::class.java)
            startActivity(intent)
        }
    }

    inner class SelectData : AsyncTask<String, Void, String>() {
        private var progressDialog: ProgressDialog? = null
        override fun doInBackground(vararg params: String): String {
            val serverURL = params[0]
            val userid = params[1]
            val userpw = params[2]

            val postParameters = "personal_id=$userid&personal_password=$userpw"

            try {
                val url = URL(serverURL)
                val httpURLConnection = url.openConnection() as HttpURLConnection

                httpURLConnection.readTimeout = 5000
                httpURLConnection.connectTimeout = 5000
                httpURLConnection.requestMethod = "POST"
                httpURLConnection.connect()

                val outputStream = httpURLConnection.outputStream
                outputStream.write(postParameters.toByteArray(charset("UTF-8")))
                outputStream.flush()
                outputStream.close()

                val responseStatusCode = httpURLConnection.responseCode
                Log.d(TAG, "POST response code - $responseStatusCode")

                val inputStream: InputStream
                inputStream = if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    httpURLConnection.inputStream
                } else {
                    httpURLConnection.errorStream
                }

                val inputStreamReader = InputStreamReader(inputStream, "UTF-8")
                val bufferedReader = BufferedReader(inputStreamReader)
                val sb = StringBuilder()
                var line: String? = null

                while (bufferedReader.readLine().also { line = it } != null) {
                    sb.append(line)
                }

                bufferedReader.close()
                Log.d("php 값 :", sb.toString())
                return sb.toString()
            } catch (e: Exception) {
                Log.d(TAG, "SelectData: Error", e)
                return "Error " + e.message
            }
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)

            if (result == "success") {
                // 로그인 성공
                Toast.makeText(view?.context, "로그인 성공", Toast.LENGTH_SHORT).show()
                // 로그인 성공 시 homeactivity로 전환
                val intent = Intent(getActivity(), HomeActivity::class.java)
                intent.putExtra("userId", id) // 아이디를 Intent에 추가 (사용자 아이디를 HomeActivity로 넘김)
                startActivity(intent)
            } else {
                // 로그인 실패
                Toast.makeText(view?.context, "로그인 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }
}