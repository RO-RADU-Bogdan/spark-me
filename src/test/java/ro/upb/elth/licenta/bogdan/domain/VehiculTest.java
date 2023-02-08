package ro.upb.elth.licenta.bogdan.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ro.upb.elth.licenta.bogdan.web.rest.TestUtil;

public class VehiculTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vehicul.class);
        Vehicul vehicul1 = new Vehicul();
        vehicul1.setId(1L);
        Vehicul vehicul2 = new Vehicul();
        vehicul2.setId(vehicul1.getId());
        assertThat(vehicul1).isEqualTo(vehicul2);
        vehicul2.setId(2L);
        assertThat(vehicul1).isNotEqualTo(vehicul2);
        vehicul1.setId(null);
        assertThat(vehicul1).isNotEqualTo(vehicul2);
    }
}
