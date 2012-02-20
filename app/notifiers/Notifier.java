package notifiers;

import models.Candidate;
import play.Play;
import play.mvc.Mailer;

public class Notifier extends Mailer {

    private static final String RECIPIENT_EMAIL_ADDRESS =
            Play.configuration.getProperty("application.email.address");

    public static void candidate(final Candidate candidate, final boolean previouslySubmitted) {
        setSubject("A new job candidate has applied!");
        addRecipient(RECIPIENT_EMAIL_ADDRESS);
        setFrom("feedback@flite.com");
        send(candidate, previouslySubmitted);
    }
}
