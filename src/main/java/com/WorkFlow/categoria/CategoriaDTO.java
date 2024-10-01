package com.WorkFlow.categoria;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoriaDTO {

    public CategoriaDTO(Categoria categoria) {
        this.id = categoria.getId();
        this.cor = categoria.getCor();
        this.nome = categoria.getNome();
        this.icone = categoria.getIcone();
    }

    private Long id;
    @NotNull(message = "The 'nome' field cannot be null.")
    private String nome;
    private String cor;
    private String icone;
}
