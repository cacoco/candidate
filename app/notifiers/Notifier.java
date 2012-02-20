package notifiers;

import models.Candidate;
import play.Play;
import play.mvc.Mailer;

public class Notifier extends Mailer {

    private static final String RECIPIENT_EMAIL_ADDRESS =
            Play.configuration.getProperty("application.email.to.address");
    private static final String FROM_EMAIL_ADDRESS = 
            Play.configuration.getProperty("application.email.from.address");

    public static void candidate(final Candidate candidate, final boolean previouslySubmitted) {
        setSubject("A new job candidate has applied!");
        addRecipient(RECIPIENT_EMAIL_ADDRESS);
        setFrom(FROM_EMAIL_ADDRESS);
        send(candidate, previouslySubmitted);
    }
}
