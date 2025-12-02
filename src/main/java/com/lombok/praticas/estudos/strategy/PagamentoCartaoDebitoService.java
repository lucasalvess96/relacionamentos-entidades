package com.lombok.praticas.estudos.strategy;

import org.springframework.stereotype.Component;

@Component
public class PagamentoCartaoDebitoService implements PagamentoStrategy {

    @Override
    public String tipoPagamento() {
        return "cartao-debito";
    }

    @Override
    public PagamentoDto pagamento(PagamentoDto pagamentoDto) {
        System.out.println("Pagamento via cartão de DÉBITO realizado. Valor: " + pagamentoDto.valor());
        return pagamentoDto;
    }
}
