package com.lombok.praticas.estudos.strategy;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
//@Entity
//@Table(name = "PAGAMENTO", schema = "CRUD")
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String nome;

    String descricao;

    Double valor;

    Integer tipoPagamentoCartao;

    Integer tipo;

    Integer parcelas;
}
