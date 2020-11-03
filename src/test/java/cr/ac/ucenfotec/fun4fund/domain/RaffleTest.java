package cr.ac.ucenfotec.fun4fund.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import cr.ac.ucenfotec.fun4fund.web.rest.TestUtil;

public class RaffleTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Raffle.class);
        Raffle raffle1 = new Raffle();
        raffle1.setId(1L);
        Raffle raffle2 = new Raffle();
        raffle2.setId(raffle1.getId());
        assertThat(raffle1).isEqualTo(raffle2);
        raffle2.setId(2L);
        assertThat(raffle1).isNotEqualTo(raffle2);
        raffle1.setId(null);
        assertThat(raffle1).isNotEqualTo(raffle2);
    }
}
