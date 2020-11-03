package cr.ac.ucenfotec.fun4fund.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import cr.ac.ucenfotec.fun4fund.web.rest.TestUtil;

public class RecommendationTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Recommendation.class);
        Recommendation recommendation1 = new Recommendation();
        recommendation1.setId(1L);
        Recommendation recommendation2 = new Recommendation();
        recommendation2.setId(recommendation1.getId());
        assertThat(recommendation1).isEqualTo(recommendation2);
        recommendation2.setId(2L);
        assertThat(recommendation1).isNotEqualTo(recommendation2);
        recommendation1.setId(null);
        assertThat(recommendation1).isNotEqualTo(recommendation2);
    }
}
