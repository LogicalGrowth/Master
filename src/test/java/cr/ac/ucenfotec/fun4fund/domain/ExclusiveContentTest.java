package cr.ac.ucenfotec.fun4fund.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import cr.ac.ucenfotec.fun4fund.web.rest.TestUtil;

public class ExclusiveContentTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExclusiveContent.class);
        ExclusiveContent exclusiveContent1 = new ExclusiveContent();
        exclusiveContent1.setId(1L);
        ExclusiveContent exclusiveContent2 = new ExclusiveContent();
        exclusiveContent2.setId(exclusiveContent1.getId());
        assertThat(exclusiveContent1).isEqualTo(exclusiveContent2);
        exclusiveContent2.setId(2L);
        assertThat(exclusiveContent1).isNotEqualTo(exclusiveContent2);
        exclusiveContent1.setId(null);
        assertThat(exclusiveContent1).isNotEqualTo(exclusiveContent2);
    }
}
