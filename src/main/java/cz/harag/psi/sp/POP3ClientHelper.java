package cz.harag.psi.sp;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Patrik Harag
 * @version 2020-05-22
 */
public class POP3ClientHelper {

    public static List<String> list(POP3Client client) throws IOException {
        String list = client.sendAndExpectMultiLine("LIST", null);

        return Pattern.compile("\n").splitAsStream(list)
                .skip(1)
                .map(line -> line.split("\\s+")[0])
                .collect(Collectors.toList());
    }

    public static String rawMail(POP3Client client, String id) throws IOException {
        String mail = client.sendAndExpectMultiLine("RETR", id);
        int index = mail.indexOf('\n');
        return mail.substring(index + 1);
    }

}