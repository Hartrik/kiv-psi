package cz.harag.psi.sp;

import java.util.Locale;

import javax.net.ssl.SSLSocketFactory;

/**
 * Vstupní třída.
 *
 * @version 2020-05-16
 * @author Patrik Harag
 */
public class Main {

    /**
     * Vstupní metoda.
     *
     * @param args argumenty
     */
    public static void main(String[] args) throws Exception {
        Locale.setDefault(Locale.US);

        if (args.length < 2) {
            System.err.println("Usage: java -jar <name>.jar <host> <port> [--ssl]");
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);
        boolean ssl = (args.length > 2) && args[2].contains("--ssl");

        Connection connection = (ssl)
                ? new SocketConnection(host, port, (SSLSocketFactory) SSLSocketFactory.getDefault())
                : new SocketConnection(host, port);

        LoggingProvider.log(String.format("Connection established: host=%s, port=%d", host, port));

        POP3Client client = new POP3Client(connection);

        client.close();
    }

}