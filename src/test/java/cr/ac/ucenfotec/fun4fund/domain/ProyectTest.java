package cr.ac.ucenfotec.fun4fund.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import cr.ac.ucenfotec.fun4fund.web.rest.TestUtil;

public class ProyectTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Proyect.class);
        Proyect proyect1 = new Proyect();
        proyect1.setId(1L);
        Proyect proyect2 = new Proyect();
        proyect2.setId(proyect1.getId());
        assertThat(proyect1).isEqualTo(proyect2);
        proyect2.setId(2L);
        assertThat(proyect1).isNotEqualTo(proyect2);
        proyect1.setId(null);
        assertThat(proyect1).isNotEqualTo(proyect2);
    }
}
