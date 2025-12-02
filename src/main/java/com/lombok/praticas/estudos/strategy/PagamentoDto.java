package com.lombok.praticas.estudos.strategy;

public record PagamentoDto(Long id, String nome, String descricao, Double valor, Integer tipoPagamentoCartao, Integer tipo,
                           Integer parcelas) {
}
