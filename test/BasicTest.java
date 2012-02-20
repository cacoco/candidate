import org.junit.*;
import java.util.*;
import play.test.*;
import models.*;

public class BasicTest extends UnitTest {

    @Test
    public void createAndRetrieveCandidate() {
        // Create a new Candidate and save it
        String[] urls = {"http://foo.com", "http://bar.com"};
        new Candidate("Bob Smith", "bob@smith.com", "I'm a good guy.", Arrays.toString(urls)).save();
        
        Candidate candidate = Candidate.find("byEmail", "bob@smith.com").first();

        assertNotNull(candidate);
        assertEquals("Bob Smith", candidate.name);
    }

}
