package be.jwtregistry.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import be.jwtregistry.web.rest.TestUtil;

public class IndustriesTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Industries.class);
        Industries industries1 = new Industries();
        industries1.setId(1L);
        Industries industries2 = new Industries();
        industries2.setId(industries1.getId());
        assertThat(industries1).isEqualTo(industries2);
        industries2.setId(2L);
        assertThat(industries1).isNotEqualTo(industries2);
        industries1.setId(null);
        assertThat(industries1).isNotEqualTo(industries2);
    }
}
