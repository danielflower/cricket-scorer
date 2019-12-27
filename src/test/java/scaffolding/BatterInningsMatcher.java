package scaffolding;

import com.danielflower.crickam.scorer.BatterInnings;
import com.danielflower.crickam.scorer.Player;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class BatterInningsMatcher extends BaseMatcher<BatterInnings> {
    private final Player player;

    private BatterInningsMatcher(Player player) {
        this.player = player;
    }

    public static BatterInningsMatcher withBatter(Player player) {
        return new BatterInningsMatcher(player);
    }

    @Override
    public boolean matches(Object actual) {
        return actual instanceof BatterInnings && ((BatterInnings) actual).player().equals(player);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(player + " batting");
    }
}
