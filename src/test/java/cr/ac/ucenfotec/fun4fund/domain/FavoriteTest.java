package cr.ac.ucenfotec.fun4fund.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import cr.ac.ucenfotec.fun4fund.web.rest.TestUtil;

public class FavoriteTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Favorite.class);
        Favorite favorite1 = new Favorite();
        favorite1.setId(1L);
        Favorite favorite2 = new Favorite();
        favorite2.setId(favorite1.getId());
        assertThat(favorite1).isEqualTo(favorite2);
        favorite2.setId(2L);
        assertThat(favorite1).isNotEqualTo(favorite2);
        favorite1.setId(null);
        assertThat(favorite1).isNotEqualTo(favorite2);
    }
}
