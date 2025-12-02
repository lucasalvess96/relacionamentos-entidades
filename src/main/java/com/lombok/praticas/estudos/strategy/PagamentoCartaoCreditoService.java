package com.lombok.praticas.estudos.strategy;

import org.springframework.stereotype.Component;

@Component
public class PagamentoCartaoCreditoService implements PagamentoStrategy {

    @Override
    public String tipoPagamento() {
        return "cartao-credito";
    }

    @Override
    public PagamentoDto pagamento(PagamentoDto pagamentoDto) {
        if (pagamentoDto.parcelas() <= 0) {
            throw new IllegalArgumentException("Número de parcelas inválido para crédito!");
        }

        System.out.println("Pagamento via cartão de CRÉDITO realizado em " + pagamentoDto.parcelas() + " parcelas. Valor: " + pagamentoDto.valor());
        return pagamentoDto;
    }
}
