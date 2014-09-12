package com.joyrec.util.http

import java.net.{URLEncoder, InetAddress, InetSocketAddress}
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager
import java.security.cert.X509Certificate
import org.apache.commons.httpclient.HttpClient
import org.apache.commons.httpclient.HttpMethod
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager
import org.apache.commons.httpclient.cookie.CookiePolicy
import org.apache.commons.httpclient.methods.PostMethod
import org.apache.commons.httpclient.params.HttpClientParams
import org.apache.commons.httpclient.params.HttpConnectionParams
import org.apache.commons.httpclient.protocol.Protocol
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory
import org.apache.commons.httpclient.auth.AuthScope
import org.apache.commons.httpclient.UsernamePasswordCredentials
import org.apache.commons.httpclient.methods.GetMethod
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity
import org.apache.commons.httpclient.methods.multipart.StringPart
import org.apache.commons.httpclient.methods.multipart.FilePart
import java.io.File
import org.apache.commons.httpclient.methods.multipart.Part
import com.ggfos.util.Retry

object ConnMsg {
  protected val defaultMaxConnectionsPerHost = 200
  protected val maxTotalConns = 500
  protected val connTimeOut = 10000
  protected val soTimeOut = 10000
  lazy val defaultMsg =
    new MultiThreadedHttpConnectionManager {
      val params = this.getParams
      params.setDefaultMaxConnectionsPerHost(defaultMaxConnectionsPerHost)
      params.setMaxTotalConnections(maxTotalConns)
      params.setConnectionTimeout(connTimeOut)
      params.setSoTimeout(soTimeOut)
    }
}

trait Httpable {

  protected def connectionManager = ConnMsg.defaultMsg

  private val clientParams = new HttpClientParams {
    setCookiePolicy(CookiePolicy.IGNORE_COOKIES)
  }

  Protocol.registerProtocol("https", new Protocol("https", new ProtocolSocketFactory {

    private val sslcontext: SSLContext = {
      val v = SSLContext.getInstance("SSL")
      v.init(null, Array(new X509TrustManager {
        def checkClientTrusted(chain: Array[X509Certificate], authType: String) = {}

        def checkServerTrusted(chain: Array[X509Certificate], authType: String) = {}

        def getAcceptedIssuers = Array[X509Certificate]()
      }), new java.security.SecureRandom())
      v
    }

    override def createSocket(host: String, port: Int) =
      sslcontext.getSocketFactory().createSocket(host, port)

    override def createSocket(host: String, port: Int,
                              clientHost: InetAddress, clientPort: Int) = sslcontext.getSocketFactory().createSocket(host, port, clientHost,
      clientPort)

    override def createSocket(host: String, port: Int,
                              localAddress: InetAddress, localPort: Int,
                              params: HttpConnectionParams) = {
      if (params == null) {
        throw new IllegalArgumentException(
          "Parameters may not be null");
      }
      val timeout = params.getConnectionTimeout();
      val socketfactory = sslcontext.getSocketFactory();
      if (timeout == 0) {
        socketfactory.createSocket(host, port, localAddress, localPort);
      } else {
        val socket = socketfactory.createSocket();
        val localaddr = new InetSocketAddress(localAddress, localPort)
        val remoteaddr = new InetSocketAddress(host, port)
        socket.bind(localaddr);
        socket.connect(remoteaddr, timeout);
        socket;
      }
    }
  }, 443))

  private def apply[R](fn: HttpClient => R, proxy: (String, Int) = null, credentials: (String, String) = null) = {
    val client = new HttpClient(clientParams, connectionManager)
    proxy match {
      case (ip, port) if (ip != null && ip.nonEmpty && port > 0) =>
        client.getHostConfiguration().setProxy(ip, port)
      case _ =>
    }
    credentials match {
      case (name, pwd) if (name != null && pwd != null) =>
        client.getParams().setAuthenticationPreemptive(true)
        client.getState().setCredentials(AuthScope.ANY,
          new UsernamePasswordCredentials(name, pwd))
      case _ =>
    }
    fn(client)
  }

  private def execUntilSuccuss[T <: HttpMethod, R](method: T, proxy: (String, Int) = null, credentials: (String, String) = null)(otherMethodAction: T => R) =
    Retry(
    exec(method, proxy, credentials) {
      otherMethodAction
    }, {
      println(_)
    })()

  private def exec[T <: HttpMethod, R](method: T, proxy: (String, Int) = null, credentials: (String, String) = null)(otherMethodAction: T => R) =
    apply({
      c =>
        try {
          c.executeMethod(method)
          otherMethodAction(method)
        } finally {
          method.releaseConnection
        }
    }, proxy, credentials)

  private def buildBasePostMethod[T <: Httpable, R](baseUrl: String, params: Seq[(Any, Any)], headers: Iterable[(String, String)])(fn: PostMethod => R) = {
    new PostMethod(baseUrl) {
      if (headers.nonEmpty)
        headers.foreach {
          case (k, v) =>
            setRequestHeader(k, v)
        }
      fn(this)
      getParams().setContentCharset("UTF-8")
    }
  }

  private def postMethod(baseUrl: String, params: Seq[(Any, Any)], headers: Iterable[(String, String)]) =
    buildBasePostMethod(baseUrl, params, headers) {
      post =>
        params.foreach {
          case (k, v) =>
            require(k != null && v != null)
            post.addParameter(k.toString, v.toString)
        }
    }

  private def postMultipartMethod(baseUrl: String, params: Seq[(Any, Any)], multis: Seq[(Any, Any)], headers: Iterable[(String, String)]) =
    buildBasePostMethod(baseUrl, params, headers) {
      post =>
        val fileParts: Array[Part] = multis.map {
          case (k, v) =>
            new FilePart(k.toString, new File(v.toString))
        }.toArray
        val stringParts: Array[Part] = params.map {
          case (k, v) => new StringPart(k.toString, v.toString, "UTF-8")
        }.toArray
        post.setRequestEntity(new MultipartRequestEntity(stringParts ++ fileParts, post.getParams()))
    }

  private def postBodyMethod(baseUrl: String, params: Seq[(Any, Any)], bodyJsonStr: String, headers: Iterable[(String, String)]) =
    buildBasePostMethod(baseUrl, params, headers) {
      post =>
        post.setRequestHeader("Content-Type", "application/json")
        post.setRequestBody(bodyJsonStr)
    }

  private def getMethod(baseUrl: String, params: Seq[(Any, Any)], headers: Iterable[(String, String)]) =
    new GetMethod(baseUrl + (if (params.nonEmpty)
      "?" + params.filter {
        case (k, v) => k != null && v != null
      }.map {
        case (k, v) =>
          s"$k=${v}"
      }.mkString("&")
    else "")) {
      getParams().setContentCharset("UTF-8")
      if (headers.nonEmpty)
        headers.foreach {
          case (k, v) =>
            setRequestHeader(k, v)
        }
    }

  def postMulti(baseUrl: String, params: (Any, Any)*)(multis: (Any, Any)*)(proxy: (String, Int) = null, credentials: (String, String) = null, headers: Iterable[(String, String)] = Nil) =
    exec(postMultipartMethod(baseUrl: String, params: Seq[(Any, Any)], multis: Seq[(Any, Any)], headers: Iterable[(String, String)]), proxy, credentials) {
      m =>
        m.getResponseBodyAsString
    }

  def post(baseUrl: String, params: (Any, Any)*)(proxy: (String, Int) = null, credentials: (String, String) = null, headers: Iterable[(String, String)] = Nil) =
    exec(postMethod(baseUrl: String, params: Seq[(Any, Any)], headers: Iterable[(String, String)]), proxy, credentials) {
      m =>
        m.getResponseBodyAsString
    }

  def postRes(baseUrl: String, params: (Any, Any)*)(proxy: (String, Int) = null, credentials: (String, String) = null, headers: Iterable[(String, String)] = Nil) =
    execUntilSuccuss(postMethod(baseUrl: String, params: Seq[(Any, Any)], headers: Iterable[(String, String)]), proxy, credentials) {
      m =>
        m.getResponseBodyAsString
    }

  def postBody(baseUrl: String, params: (Any, Any)*)(body: String)(proxy: (String, Int) = null, credentials: (String, String) = null, headers: Iterable[(String, String)] = Nil) =
    exec(postBodyMethod(baseUrl: String, params: Seq[(Any, Any)], body, headers: Iterable[(String, String)]), proxy, credentials) {
      m =>
        m.getStatusText()
    }

  def get(baseUrl: String, params: (Any, Any)*)(proxy: (String, Int) = null, credentials: (String, String) = null, headers: Iterable[(String, String)] = Nil) =
    exec(getMethod(baseUrl: String, params: Seq[(Any, Any)], headers: Iterable[(String, String)]), proxy, credentials) {
      m =>
        m.getResponseBodyAsString
    }

  def getRes(baseUrl: String, params: (Any, Any)*)(proxy: (String, Int) = null, credentials: (String, String) = null, headers: Iterable[(String, String)] = Nil) =
    execUntilSuccuss(getMethod(baseUrl: String, params: Seq[(Any, Any)], headers: Iterable[(String, String)]), proxy, credentials) {
      m =>
        m.getResponseBodyAsString
    }
}

object HttpParameterUtil {
  def encode(param: String) = URLEncoder.encode(param, "UTF-8").replace("+", "%20").replace("*", "%2A")
    .replace("%7E", "~")

  def urlEncode(param: String) = URLEncoder.encode(param, "UTF-8")

}