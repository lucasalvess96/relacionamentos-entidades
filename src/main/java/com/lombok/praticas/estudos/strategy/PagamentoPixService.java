package com.lombok.praticas.estudos.strategy;

import org.springframework.stereotype.Component;

@Component
public class PagamentoPixService implements PagamentoStrategy {

    @Override
    public String tipoPagamento() {
        return "pix";
    }

    @Override
    public PagamentoDto pagamento(PagamentoDto pagamentoDto) {
        System.out.println("Pagamento via pix realizado.");
        return pagamentoDto;
    }
}
