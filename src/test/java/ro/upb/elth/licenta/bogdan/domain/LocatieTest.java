package ro.upb.elth.licenta.bogdan.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ro.upb.elth.licenta.bogdan.web.rest.TestUtil;

public class LocatieTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Locatie.class);
        Locatie locatie1 = new Locatie();
        locatie1.setId(1L);
        Locatie locatie2 = new Locatie();
        locatie2.setId(locatie1.getId());
        assertThat(locatie1).isEqualTo(locatie2);
        locatie2.setId(2L);
        assertThat(locatie1).isNotEqualTo(locatie2);
        locatie1.setId(null);
        assertThat(locatie1).isNotEqualTo(locatie2);
    }
}
