package ro.upb.elth.licenta.bogdan.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ro.upb.elth.licenta.bogdan.web.rest.TestUtil;

public class RezervareTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Rezervare.class);
        Rezervare rezervare1 = new Rezervare();
        rezervare1.setId(1L);
        Rezervare rezervare2 = new Rezervare();
        rezervare2.setId(rezervare1.getId());
        assertThat(rezervare1).isEqualTo(rezervare2);
        rezervare2.setId(2L);
        assertThat(rezervare1).isNotEqualTo(rezervare2);
        rezervare1.setId(null);
        assertThat(rezervare1).isNotEqualTo(rezervare2);
    }
}
