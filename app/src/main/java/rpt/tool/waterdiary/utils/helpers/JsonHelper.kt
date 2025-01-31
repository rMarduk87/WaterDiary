package rpt.tool.waterdiary.utils.helpers

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.util.Log.e
import org.apache.http.HttpResponse
import org.apache.http.HttpVersion
import org.apache.http.NameValuePair
import org.apache.http.StatusLine
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.HttpClient
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.conn.ClientConnectionManager
import org.apache.http.conn.scheme.PlainSocketFactory
import org.apache.http.conn.scheme.Scheme
import org.apache.http.conn.scheme.SchemeRegistry
import org.apache.http.conn.ssl.SSLSocketFactory
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager
import org.apache.http.params.BasicHttpParams
import org.apache.http.params.HttpParams
import org.apache.http.params.HttpProtocolParams
import org.apache.http.protocol.HTTP
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.security.KeyManagementException
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.UnrecoverableKeyException

class JsonHelper
    (var mContext: Context) {
    @SuppressLint("LogNotTimber")
    fun callApi(url: String?, params: List<NameValuePair?>?): String {
        val str = StringBuilder()
        val client: HttpClient = DefaultHttpClient()
        val httpPost = HttpPost(url)
        val response: HttpResponse
        val statusLine: StatusLine

        try {
            httpPost.entity = UrlEncodedFormEntity(params, "utf-8")
            response = client.execute(httpPost)
            statusLine = response.statusLine

            val statusCode = statusLine.statusCode

            if (statusCode == 200)  // Status OK
            {
                val entity = response.entity
                val content = entity.content
                val reader = BufferedReader(InputStreamReader(content, "utf-8"), 8)
                var line: String?
                while ((reader.readLine().also { line = it }) != null) {
                    str.append(line)
                }
            } else {
                e("Log", "Failed to download result..")
            }
        } catch (e: ClientProtocolException) {
            e.message?.let { rpt.tool.waterdiary.utils.log.e(Throwable(e), it) }
            e.printStackTrace()
        } catch (e: IOException) {
            e.message?.let { rpt.tool.waterdiary.utils.log.e(Throwable(e), it) }
            e.printStackTrace()
        }

        return str.toString()
    }

    @SuppressLint("LogNotTimber")
    fun callApi(url: String?): String {
        val str = StringBuilder()
        val client: HttpClient = DefaultHttpClient()
        val httpPost = HttpPost(url)
        val response: HttpResponse
        val statusLine: StatusLine

        try {
            response = client.execute(httpPost)
            statusLine = response.statusLine

            val statusCode = statusLine.statusCode

            if (statusCode == 200)  // Status OK
            {
                val entity = response.entity
                val content = entity.content
                val reader = BufferedReader(InputStreamReader(content, "utf-8"), 8)
                var line: String?
                while ((reader.readLine().also { line = it }) != null) {
                    str.append(line)
                }
            } else {
                e("Log", "Failed to download result..")
            }
        } catch (e: ClientProtocolException) {
            e.message?.let { rpt.tool.waterdiary.utils.log.e(Throwable(e), it) }
            e.printStackTrace()
        } catch (e: IOException) {
            e.message?.let { rpt.tool.waterdiary.utils.log.e(Throwable(e), it) }
            e.printStackTrace()
        }

        return str.toString()
    }

    @SuppressLint("LogNotTimber")
    fun callApi2(url: String?, params: List<NameValuePair?>?): String {
        val str = StringBuilder()
        var client: HttpClient = DefaultHttpClient()
        val httpPost = HttpPost(url)

        try {
            var sslFactory: SSLSocketFactory? = null
            sslFactory = SSLSocketHelper(null)

            sslFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)

            // Enable HTTP parameters
            val hparams: HttpParams = BasicHttpParams()
            HttpProtocolParams.setVersion(hparams, HttpVersion.HTTP_1_1)
            HttpProtocolParams.setContentCharset(hparams, HTTP.UTF_8)

            // Register the HTTP and HTTPS Protocols. For HTTPS, register our custom SSL Factory object.
            val registry = SchemeRegistry()
            registry.register(Scheme("http", PlainSocketFactory.getSocketFactory(), 80))
            registry.register(Scheme("https", sslFactory, 443))

            // Create a new connection manager using the newly created registry and then create a new HTTP client
            // using this connection manager
            val ccm: ClientConnectionManager = ThreadSafeClientConnManager(hparams, registry)
            client = DefaultHttpClient(ccm, hparams)

            httpPost.entity = UrlEncodedFormEntity(params, "utf-8")
            val response = client.execute(httpPost)
            val statusLine = response.statusLine
            val statusCode = statusLine.statusCode
            if (statusCode == 200) { // Status OK
                val entity = response.entity
                val content = entity.content
                val reader = BufferedReader(
                    InputStreamReader(content)
                )
                var line: String?
                while ((reader.readLine().also { line = it }) != null) {
                    str.append(line)
                }
            } else {
                e("Log", "Failed to download result..$statusCode")
            }
        } catch (e: ClientProtocolException) {
            e.message?.let { rpt.tool.waterdiary.utils.log.e(Throwable(e), it) }
            e.printStackTrace()
        } catch (e: IOException) {
            e.message?.let { rpt.tool.waterdiary.utils.log.e(Throwable(e), it) }
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.message?.let { rpt.tool.waterdiary.utils.log.e(Throwable(e), it) }
            e.printStackTrace()
        } catch (e: UnrecoverableKeyException) {
            e.message?.let { rpt.tool.waterdiary.utils.log.e(Throwable(e), it) }
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.message?.let { rpt.tool.waterdiary.utils.log.e(Throwable(e), it) }
            e.printStackTrace()
        } catch (e: KeyStoreException) {
            e.message?.let { rpt.tool.waterdiary.utils.log.e(Throwable(e), it) }
            e.printStackTrace()
        }

        return str.toString()
    }

    @SuppressLint("LogNotTimber")
    fun callApi2(url: String?): String {
        val str = StringBuilder()
        var client: HttpClient = DefaultHttpClient()
        val httpPost = HttpPost(url)

        try {
            var sslFactory: SSLSocketFactory? = null
            sslFactory = SSLSocketHelper(null)

            sslFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)

            // Enable HTTP parameters
            val hparams: HttpParams = BasicHttpParams()
            HttpProtocolParams.setVersion(hparams, HttpVersion.HTTP_1_1)
            HttpProtocolParams.setContentCharset(hparams, HTTP.UTF_8)

            // Register the HTTP and HTTPS Protocols. For HTTPS, register our custom SSL Factory object.
            val registry = SchemeRegistry()
            registry.register(Scheme("http", PlainSocketFactory.getSocketFactory(), 80))
            registry.register(Scheme("https", sslFactory, 443))

            // Create a new connection manager using the newly created registry and then create a new HTTP client
            // using this connection manager
            val ccm: ClientConnectionManager = ThreadSafeClientConnManager(hparams, registry)
            client = DefaultHttpClient(ccm, hparams)

            //httpPost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
            val response = client.execute(httpPost)
            val statusLine = response.statusLine
            val statusCode = statusLine.statusCode
            if (statusCode == 200) { // Status OK
                val entity = response.entity
                val content = entity.content
                val reader = BufferedReader(
                    InputStreamReader(content)
                )
                var line: String?
                while ((reader.readLine().also { line = it }) != null) {
                    str.append(line)
                }
            } else {
                e("Log", "Failed to download result..$statusCode")
            }
        } catch (e: ClientProtocolException) {
            e.message?.let { rpt.tool.waterdiary.utils.log.e(Throwable(e), it) }
            e.printStackTrace()
        } catch (e: IOException) {
            e.message?.let { rpt.tool.waterdiary.utils.log.e(Throwable(e), it) }
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.message?.let { rpt.tool.waterdiary.utils.log.e(Throwable(e), it) }
            e.printStackTrace()
        } catch (e: UnrecoverableKeyException) {
            e.message?.let { rpt.tool.waterdiary.utils.log.e(Throwable(e), it) }
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.message?.let { rpt.tool.waterdiary.utils.log.e(Throwable(e), it) }
            e.printStackTrace()
        } catch (e: KeyStoreException) {
            e.message?.let { rpt.tool.waterdiary.utils.log.e(Throwable(e), it) }
            e.printStackTrace()
        }

        return str.toString()
    }
}