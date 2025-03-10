package org.example.events.moks;

import org.example.events.entity.ViaCep;

public class MockCep {

    public ViaCep mockViaCep() {
        ViaCep address = new ViaCep();
        address.setCep("95948-000");
        address.setLogradouro("");
        address.setComplemento("");
        address.setUnidade("");
        address.setBairro("");
        address.setLocalidade("Travesseiro");
        address.setUf("RS");
        address.setEstado("Rio Grande do Sul");
        address.setRegiao("Sul");
        address.setIbge("4321626");
        address.setGia("");
        address.setDdd("51");
        address.setSiafi("6037");
        return address;
    }
}
