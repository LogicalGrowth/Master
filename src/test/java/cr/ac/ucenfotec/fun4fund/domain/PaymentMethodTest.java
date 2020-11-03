package cr.ac.ucenfotec.fun4fund.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import cr.ac.ucenfotec.fun4fund.web.rest.TestUtil;

public class PaymentMethodTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentMethod.class);
        PaymentMethod paymentMethod1 = new PaymentMethod();
        paymentMethod1.setId(1L);
        PaymentMethod paymentMethod2 = new PaymentMethod();
        paymentMethod2.setId(paymentMethod1.getId());
        assertThat(paymentMethod1).isEqualTo(paymentMethod2);
        paymentMethod2.setId(2L);
        assertThat(paymentMethod1).isNotEqualTo(paymentMethod2);
        paymentMethod1.setId(null);
        assertThat(paymentMethod1).isNotEqualTo(paymentMethod2);
    }
}
