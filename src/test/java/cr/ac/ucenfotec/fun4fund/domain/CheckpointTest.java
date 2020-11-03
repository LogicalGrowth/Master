package cr.ac.ucenfotec.fun4fund.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import cr.ac.ucenfotec.fun4fund.web.rest.TestUtil;

public class CheckpointTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Checkpoint.class);
        Checkpoint checkpoint1 = new Checkpoint();
        checkpoint1.setId(1L);
        Checkpoint checkpoint2 = new Checkpoint();
        checkpoint2.setId(checkpoint1.getId());
        assertThat(checkpoint1).isEqualTo(checkpoint2);
        checkpoint2.setId(2L);
        assertThat(checkpoint1).isNotEqualTo(checkpoint2);
        checkpoint1.setId(null);
        assertThat(checkpoint1).isNotEqualTo(checkpoint2);
    }
}
