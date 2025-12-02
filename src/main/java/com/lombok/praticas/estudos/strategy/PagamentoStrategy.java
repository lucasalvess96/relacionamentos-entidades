package com.lombok.praticas.estudos.strategy;

public interface PagamentoStrategy {

    String tipoPagamento();

    PagamentoDto pagamento(PagamentoDto pagamentoDto);
}
