package cr.ac.ucenfotec.fun4fund.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import cr.ac.ucenfotec.fun4fund.web.rest.TestUtil;

public class DonationHistoryTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DonationHistory.class);
        DonationHistory donationHistory1 = new DonationHistory();
        donationHistory1.setId(1L);
        DonationHistory donationHistory2 = new DonationHistory();
        donationHistory2.setId(donationHistory1.getId());
        assertThat(donationHistory1).isEqualTo(donationHistory2);
        donationHistory2.setId(2L);
        assertThat(donationHistory1).isNotEqualTo(donationHistory2);
        donationHistory1.setId(null);
        assertThat(donationHistory1).isNotEqualTo(donationHistory2);
    }
}
