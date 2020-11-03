package cr.ac.ucenfotec.fun4fund.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import cr.ac.ucenfotec.fun4fund.web.rest.TestUtil;

public class AppLogTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppLog.class);
        AppLog appLog1 = new AppLog();
        appLog1.setId(1L);
        AppLog appLog2 = new AppLog();
        appLog2.setId(appLog1.getId());
        assertThat(appLog1).isEqualTo(appLog2);
        appLog2.setId(2L);
        assertThat(appLog1).isNotEqualTo(appLog2);
        appLog1.setId(null);
        assertThat(appLog1).isNotEqualTo(appLog2);
    }
}
