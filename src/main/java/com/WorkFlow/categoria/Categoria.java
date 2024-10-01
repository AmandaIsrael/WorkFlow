package com.WorkFlow.categoria;

import com.WorkFlow.tarefa.Tarefa;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "categoria")
public class Categoria {

    public Categoria(CategoriaDTO categoriaDTO) {
        this.id = categoriaDTO.getId();
        this.nome = categoriaDTO.getNome();
        this.cor = categoriaDTO.getCor();
        this.icone = categoriaDTO.getIcone();
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String cor;
    private String icone;

    @OneToMany(mappedBy = "categoria")
    private List<Tarefa> tarefaList;
}
