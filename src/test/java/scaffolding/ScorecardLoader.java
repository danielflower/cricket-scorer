package scaffolding;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

public class ScorecardLoader {

    public static String load(String filename) {
        InputStream is = ScorecardLoader.class.getResourceAsStream("/scorecards/" + filename);
        if (is == null) {
            throw new RuntimeException("Could not find " + filename);
        }
        try (InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            char[] buffer = new char[8192];
            int read;
            StringBuilder sb = new StringBuilder();
            while ((read = isr.read(buffer)) > -1) {
                sb.append(buffer, 0, read);
            }
            return sb.toString();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } finally {
            try {
                is.close();
            } catch (IOException ignored) {
            }
        }
    }

}
