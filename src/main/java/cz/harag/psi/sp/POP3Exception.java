package cz.harag.psi.sp;

/**
 * @author Patrik Harag
 * @version 2020-05-16
 */
public class POP3Exception extends RuntimeException {

    public POP3Exception(String response) {
        super(response);
    }
}
