package ro.upb.elth.licenta.bogdan.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ro.upb.elth.licenta.bogdan.web.rest.TestUtil;

public class StatieTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Statie.class);
        Statie statie1 = new Statie();
        statie1.setId(1L);
        Statie statie2 = new Statie();
        statie2.setId(statie1.getId());
        assertThat(statie1).isEqualTo(statie2);
        statie2.setId(2L);
        assertThat(statie1).isNotEqualTo(statie2);
        statie1.setId(null);
        assertThat(statie1).isNotEqualTo(statie2);
    }
}
