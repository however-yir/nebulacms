package io.nebulacms.app.infra.exception;

import java.net.URI;
import org.springframework.web.server.ServerWebInputException;

/**
 * Exception thrown when email is already verified and taken.
 *
 * @author johnniang
 */
public class EmailAlreadyTakenException extends ServerWebInputException {

    public static final URI TYPE = URI.create("https://nebulacms.io/errors/email-already-taken");

    public EmailAlreadyTakenException(String reason) {
        super(reason);
        setType(TYPE);
    }

}
