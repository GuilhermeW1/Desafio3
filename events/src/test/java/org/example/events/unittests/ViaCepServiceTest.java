package org.example.events.unittests;

import org.example.events.entity.ViaCep;
import org.example.events.moks.MockCep;
import org.example.events.service.ViaCepService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class ViaCepServiceTest {

    @Mock
    ViaCepService viaCepService;

    @Test
    void getCep() {
        ViaCep address = new MockCep().mockViaCep();

        when(viaCepService.getCepInfo("95948000")).thenReturn(address);

        var result = viaCepService.getCepInfo("95948000");
        assertNotNull(result);
        assertEquals(address.getCep(), result.getCep());
        assertEquals(address.getLogradouro(), result.getLogradouro());
        assertEquals(address.getComplemento(), result.getComplemento());
        assertEquals(address.getUnidade(), result.getUnidade());
        assertEquals(address.getBairro(), result.getBairro());
        assertEquals(address.getLocalidade(), result.getLocalidade());
        assertEquals(address.getUf(), result.getUf());
        assertEquals(address.getEstado(), result.getEstado());
        assertEquals(address.getRegiao(), result.getRegiao());
        assertEquals(address.getIbge(), result.getIbge());
        assertEquals(address.getGia(), result.getGia());
        assertEquals(address.getDdd(), result.getDdd());
        assertEquals(address.getSiafi(), result.getSiafi());
    }
}
