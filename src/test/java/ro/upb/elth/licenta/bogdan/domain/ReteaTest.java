package ro.upb.elth.licenta.bogdan.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ro.upb.elth.licenta.bogdan.web.rest.TestUtil;

public class ReteaTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Retea.class);
        Retea retea1 = new Retea();
        retea1.setId(1L);
        Retea retea2 = new Retea();
        retea2.setId(retea1.getId());
        assertThat(retea1).isEqualTo(retea2);
        retea2.setId(2L);
        assertThat(retea1).isNotEqualTo(retea2);
        retea1.setId(null);
        assertThat(retea1).isNotEqualTo(retea2);
    }
}
