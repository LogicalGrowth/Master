package cr.ac.ucenfotec.fun4fund.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import cr.ac.ucenfotec.fun4fund.web.rest.TestUtil;

public class PasswordHistoryTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PasswordHistory.class);
        PasswordHistory passwordHistory1 = new PasswordHistory();
        passwordHistory1.setId(1L);
        PasswordHistory passwordHistory2 = new PasswordHistory();
        passwordHistory2.setId(passwordHistory1.getId());
        assertThat(passwordHistory1).isEqualTo(passwordHistory2);
        passwordHistory2.setId(2L);
        assertThat(passwordHistory1).isNotEqualTo(passwordHistory2);
        passwordHistory1.setId(null);
        assertThat(passwordHistory1).isNotEqualTo(passwordHistory2);
    }
}
