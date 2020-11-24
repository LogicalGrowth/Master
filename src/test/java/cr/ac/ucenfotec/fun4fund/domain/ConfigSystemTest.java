package cr.ac.ucenfotec.fun4fund.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import cr.ac.ucenfotec.fun4fund.web.rest.TestUtil;

public class ConfigSystemTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigSystem.class);
        ConfigSystem configSystem1 = new ConfigSystem();
        configSystem1.setId(1L);
        ConfigSystem configSystem2 = new ConfigSystem();
        configSystem2.setId(configSystem1.getId());
        assertThat(configSystem1).isEqualTo(configSystem2);
        configSystem2.setId(2L);
        assertThat(configSystem1).isNotEqualTo(configSystem2);
        configSystem1.setId(null);
        assertThat(configSystem1).isNotEqualTo(configSystem2);
    }
}
