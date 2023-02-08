package ro.upb.elth.licenta.bogdan.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ro.upb.elth.licenta.bogdan.web.rest.TestUtil;

public class IncarcatorTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Incarcator.class);
        Incarcator incarcator1 = new Incarcator();
        incarcator1.setId(1L);
        Incarcator incarcator2 = new Incarcator();
        incarcator2.setId(incarcator1.getId());
        assertThat(incarcator1).isEqualTo(incarcator2);
        incarcator2.setId(2L);
        assertThat(incarcator1).isNotEqualTo(incarcator2);
        incarcator1.setId(null);
        assertThat(incarcator1).isNotEqualTo(incarcator2);
    }
}
