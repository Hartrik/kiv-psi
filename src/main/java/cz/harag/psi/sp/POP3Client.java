package cz.harag.psi.sp;

import java.io.IOException;

/**
 * @author Patrik Harag
 * @version 2020-05-16
 */
public class POP3Client implements AutoCloseable {

    private enum State { NOT_CONNECTED, CONNECTED }

    private final Connection connection;
    private State state = State.NOT_CONNECTED;

    public POP3Client(Connection connection) throws IOException {
        this.connection = connection;
        readResponse();  // should just pass
        this.state = State.CONNECTED;
    }

    public String sendAndExpectSingleLine(String command, String arg) throws IOException {
        send(command, arg);

        return readResponse();
    }

    public String sendAndExpectMultiLine(String command, String arg) throws IOException {
        send(command, arg);

        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while (!(line = readResponse()).equals(".")) {
            line = line.replaceFirst("^\\.\\.", ".");
            stringBuilder.append(line).append('\n');
        }
        return stringBuilder.toString();
    }

    private void send(String command, String arg) throws IOException {
        String data = (arg != null) ? command + " " + arg : command;
        LoggingProvider.logRequest(data);
        connection.getWriter().write(data);
        connection.getWriter().write("\r\n");
        connection.getWriter().flush();
    }

    private String readResponse() throws IOException, POP3Exception {
        String response = connection.getReader().readLine();
        if (response.startsWith("-ERR")) {
            throw new POP3Exception(response);
        }
        LoggingProvider.logResponse(response);
        return response;
    }

    @Override
    public void close() throws Exception {
        try {
            connection.close();
        } finally {
            LoggingProvider.log("Connection closed");
        }
    }
}
