package scaffolding;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

public class ScorecardLoader {

    public static String load(String filename) {
        InputStream is = ScorecardLoader.class.getResourceAsStream("/scorecards/" + filename);
        if (is == null) {
            throw new RuntimeException("Could not find " + filename);
        }
        try {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

}
