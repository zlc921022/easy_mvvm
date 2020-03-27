package com.xiaochen.data

import java.io.IOException
import java.io.InputStream
import java.security.KeyManagementException
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.UnrecoverableKeyException
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate

import javax.net.ssl.HostnameVerifier
import javax.net.ssl.KeyManager
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

/**
 * Https 证书工具类
 */
class SSLUtil {

    class SSLParams {
        var sSLSocketFactory: SSLSocketFactory? = null
        var trustManager: X509TrustManager? = null
    }

    private inner class UnSafeHostnameVerifier : HostnameVerifier {
        override fun verify(hostname: String, session: SSLSession): Boolean {
            return true
        }
    }

    private class UnSafeTrustManager : X509TrustManager {
        @Throws(CertificateException::class)
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
        }

        @Throws(CertificateException::class)
        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
        }

        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return arrayOf()
        }
    }


    private class MyTrustManager @Throws(NoSuchAlgorithmException::class, KeyStoreException::class)
    constructor(private val localTrustManager: X509TrustManager) : X509TrustManager {
        private val defaultTrustManager: X509TrustManager?

        init {
            val var4 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            var4.init(null as KeyStore?)
            defaultTrustManager = chooseTrustManager(var4.trustManagers)
        }


        @Throws(CertificateException::class)
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {

        }

        @Throws(CertificateException::class)
        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
            try {
                defaultTrustManager!!.checkServerTrusted(chain, authType)
            } catch (ce: CertificateException) {
                localTrustManager.checkServerTrusted(chain, authType)
            }

        }


        override fun getAcceptedIssuers(): Array<X509Certificate?> {
            return arrayOfNulls(0)
        }
    }

    companion object {

        fun getSslSocketFactory(
            certificates: Array<InputStream>?,
            bksFile: InputStream?,
            password: String?
        ): SSLParams {
            val sslParams = SSLParams()
            try {
                val trustManagers = certificates?.let { prepareTrustManager(*it) }
                val keyManagers = prepareKeyManager(bksFile, password)
                val sslContext = SSLContext.getInstance("TLS")
                var trustManager: X509TrustManager? = null
                if (trustManagers != null) {
                    trustManager = chooseTrustManager(trustManagers)?.let { MyTrustManager(it) }
                } else {
                    trustManager = UnSafeTrustManager()
                }
                sslContext.init(keyManagers, trustManager?.let { arrayOf<TrustManager>(it) }, null)
                sslParams.sSLSocketFactory = sslContext.socketFactory
                sslParams.trustManager = trustManager
                return sslParams
            } catch (e: NoSuchAlgorithmException) {
                throw AssertionError(e)
            } catch (e: KeyManagementException) {
                throw AssertionError(e)
            } catch (e: KeyStoreException) {
                throw AssertionError(e)
            }

        }

        private fun prepareTrustManager(vararg certificates: InputStream?): Array<TrustManager>? {
            if (certificates == null || certificates.size <= 0) return null
            try {

                val certificateFactory = CertificateFactory.getInstance("X.509")
                val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
                keyStore.load(null)
                var index = 0
                for (certificate in certificates) {
                    val certificateAlias = Integer.toString(index++)
                    keyStore.setCertificateEntry(
                        certificateAlias,
                        certificateFactory.generateCertificate(certificate)
                    )
                    try {
                        certificate?.close()
                    } catch (e: IOException) {
                    }

                }
                var trustManagerFactory: TrustManagerFactory? = null

                trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
                trustManagerFactory!!.init(keyStore)

                return trustManagerFactory.trustManagers
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            } catch (e: CertificateException) {
                e.printStackTrace()
            } catch (e: KeyStoreException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return null

        }

        private fun prepareKeyManager(
            bksFile: InputStream?,
            password: String?
        ): Array<KeyManager>? {
            try {
                if (bksFile == null || password == null) return null

                val clientKeyStore = KeyStore.getInstance("BKS")
                clientKeyStore.load(bksFile, password.toCharArray())
                val keyManagerFactory =
                    KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
                keyManagerFactory.init(clientKeyStore, password.toCharArray())
                return keyManagerFactory.keyManagers

            } catch (e: KeyStoreException) {
                e.printStackTrace()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            } catch (e: UnrecoverableKeyException) {
                e.printStackTrace()
            } catch (e: CertificateException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return null
        }

        private fun chooseTrustManager(trustManagers: Array<TrustManager>): X509TrustManager? {
            for (trustManager in trustManagers) {
                if (trustManager is X509TrustManager) {
                    return trustManager
                }
            }
            return null
        }
    }
}