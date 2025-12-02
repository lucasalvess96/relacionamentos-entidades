package com.lombok.praticas.estudos.strategy;

import org.springframework.stereotype.Component;

@Component
public class PagamentoBoletoService implements PagamentoStrategy {

    @Override
    public String tipoPagamento() {
        return "boleto";
    }

    @Override
    public PagamentoDto pagamento(PagamentoDto pagamentoDto) {
        System.out.println("Pagamento via boleto realizado.");
        return pagamentoDto;
    }
}
