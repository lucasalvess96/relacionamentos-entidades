package com.lombok.praticas.estudos.strategy;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/pagamento")
@CrossOrigin
public class PagamentoRecurso {

    private final PagamentoService service;

    public PagamentoRecurso(PagamentoService service) {
        this.service = service;
    }

    @PostMapping("/pagar/{tipoPagamento}")
    @Transactional
    public ResponseEntity<PagamentoDto> pagar(@PathVariable String tipoPagamento, @RequestBody @Valid PagamentoDto pagamentoDto) {
        return ResponseEntity.created(URI.create("/pagamento/pagar")).body(service.realizarPagamento(tipoPagamento, pagamentoDto));
    }
}
