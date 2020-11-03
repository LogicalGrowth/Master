package cr.ac.ucenfotec.fun4fund.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import cr.ac.ucenfotec.fun4fund.web.rest.TestUtil;

public class PartnerRequestTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PartnerRequest.class);
        PartnerRequest partnerRequest1 = new PartnerRequest();
        partnerRequest1.setId(1L);
        PartnerRequest partnerRequest2 = new PartnerRequest();
        partnerRequest2.setId(partnerRequest1.getId());
        assertThat(partnerRequest1).isEqualTo(partnerRequest2);
        partnerRequest2.setId(2L);
        assertThat(partnerRequest1).isNotEqualTo(partnerRequest2);
        partnerRequest1.setId(null);
        assertThat(partnerRequest1).isNotEqualTo(partnerRequest2);
    }
}
