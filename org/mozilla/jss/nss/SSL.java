package org.mozilla.jss.nss;

/**
 * This class provides static access to raw NSS calls with the SSL prefix,
 * and handles the usage of NativeProxy objects.
 */

import org.mozilla.jss.pkcs11.PK11Cert;
import org.mozilla.jss.pkcs11.PK11PrivKey;

public class SSL {
    /**
     * Import a file descriptor to create a new SSL file descriptor out of it.
     *
     * See also: SSL_ImportFD in /usr/include/nss3/ssl.h
     */
    public static native PRFDProxy ImportFD(PRFDProxy model, PRFDProxy fd);

    /**
     * Set the value of a SSL option on the specified PRFileDesc.
     *
     * See also: SSL_OptionSet in /usr/include/nss3/ssl.h
     */
    public static native int OptionSet(PRFDProxy fd, int option, int val);

    /**
     * Get the value of a SSL option on the specified PRFileDesc. Note that
     * this raises an exception in the case of an invalid option.
     *
     * See also: SSL_OptionGet in /usr/include/nss3/ssl.h
     */
    public static native int OptionGet(PRFDProxy fd, int option) throws Exception;

    /**
     * Set the hostname of a handshake on the specified PRFileDesc.
     *
     * See also: SSL_SetURL in /usr/include/nss3/ssl.h
     */
    public static native int SetURL(PRFDProxy fd, String url);

    /**
     * Check the security status of a SSL handshake.
     *
     * See also: SSL_SecurityStatus in /usr/include/nss3/ssl.h
     */
    public static native SecurityStatusResult SecurityStatus(PRFDProxy fd);

    /**
     * Reset the handshake status, optionally handshaking as a server.
     *
     * See also: SSL_ResetHandshake in /usr/include/nss3/ssl.h
     */
    public static native int ResetHandshake(PRFDProxy fd, boolean asServer);

    /**
     * Force a handshake to occur if not started, else step one.
     *
     * See also: SSL_ForceHandshake in /usr/include/nss3/ssl.h
     */
    public static native int ForceHandshake(PRFDProxy fd);

    /**
     * Configure the certificate and private key for a server socket.
     *
     * See also: SSL_ConfigSecureServer in /usr/include/nss3/ssl.h
     */
    public static native int ConfigSecureServer(PRFDProxy fd, PK11Cert cert,
        PK11PrivKey key, int kea);

    /**
     * Configure the server's session cache.
     *
     * See also: SSL_ConfigServerSessionIDCache in /usr/include/nss3/ssl.h
     */
    public static native int ConfigServerSessionIDCache(int maxCacheEntries,
        long timeout, long ssl3_timeout, String directory);
}