package cr.ac.ucenfotec.fun4fund.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import cr.ac.ucenfotec.fun4fund.web.rest.TestUtil;

public class AdminPreferencesTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdminPreferences.class);
        AdminPreferences adminPreferences1 = new AdminPreferences();
        adminPreferences1.setId(1L);
        AdminPreferences adminPreferences2 = new AdminPreferences();
        adminPreferences2.setId(adminPreferences1.getId());
        assertThat(adminPreferences1).isEqualTo(adminPreferences2);
        adminPreferences2.setId(2L);
        assertThat(adminPreferences1).isNotEqualTo(adminPreferences2);
        adminPreferences1.setId(null);
        assertThat(adminPreferences1).isNotEqualTo(adminPreferences2);
    }
}
