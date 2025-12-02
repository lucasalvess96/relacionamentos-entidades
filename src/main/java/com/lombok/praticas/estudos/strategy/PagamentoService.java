package com.lombok.praticas.estudos.strategy;

import com.lombok.praticas.estudos.comun.ErroRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PagamentoService {

    private final Map<String, PagamentoStrategy> strategies;

    public PagamentoService(List<PagamentoStrategy> strategies) {
        this.strategies = strategies.stream().collect(Collectors.toMap(PagamentoStrategy::tipoPagamento, Function.identity()));
    }

    public PagamentoDto realizarPagamento(String tipoPagamento, PagamentoDto pagamentoDto) {
        PagamentoStrategy strategy = strategies.get(tipoPagamento.toLowerCase().trim());

        if (strategy == null) throw new ErroRequest("Tipo de pagamento não autorizado: " + tipoPagamento);

        return strategy.pagamento(pagamentoDto);
    }
}
