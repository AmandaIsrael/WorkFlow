package com.WorkFlow.tarefa;

import com.WorkFlow.exception.CategoriaNotFoundException;
import com.WorkFlow.categoria.Categoria;
import com.WorkFlow.categoria.CategoriaService;
import com.WorkFlow.exception.TarefaNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final CategoriaService categoriaService;

    public List<TarefaDTO> findAll() {
        return tarefaRepository.findAll()
                .stream()
                .map(this::toTarefaDTO)
                .toList();
    }

    public Optional<TarefaDTO> findById(Long id) {
        return tarefaRepository.findById(id).map(this::toTarefaDTO);
    }

    public List<TarefaDTO> findByCategoriaId(Long categoriaId) {
        return tarefaRepository.findByCategoriaId(categoriaId)
                .stream()
                .map(this::toTarefaDTO)
                .toList();
    }

    public TarefaDTO save(TarefaDTO newTarefaDTO) {
        Tarefa newTarefa = new Tarefa(newTarefaDTO);
        updateCategoria(newTarefa, newTarefaDTO);

        Tarefa tarefaCreated = tarefaRepository.save(newTarefa);
        return toTarefaDTO(tarefaCreated);
    }

    public Optional<TarefaDTO> put(Long id, TarefaDTO newTarefaDTO) {
        return findById(id).map(existingTarefaDTO -> {
            updateTarefaDTO(existingTarefaDTO, newTarefaDTO);
            return save(existingTarefaDTO);
        });
    }

    public boolean existsById(Long id) {
        return tarefaRepository.existsById(id);
    }

    public boolean delete(Long id) {
        boolean exist = existsById(id);
        if (exist) {
            tarefaRepository.deleteById(id);
        }
        return exist;
    }

    private Categoria getCategoria(Long id) {
        return categoriaService.findById(id)
                .map(Categoria::new)
                .orElseThrow(() -> new CategoriaNotFoundException(id));
    }

    private TarefaDTO toTarefaDTO(Tarefa tarefa) {
        if (tarefa == null) {
            throw new TarefaNotFoundException();
        }
        TarefaDTO tarefaDTO = new TarefaDTO(tarefa);
        updateCategoriaId(tarefa, tarefaDTO);
        return tarefaDTO;
    }

    private void updateTarefaDTO(TarefaDTO existingTarefaDTO, TarefaDTO newTarefaDTO) {
        existingTarefaDTO.setStatus(newTarefaDTO.getStatus());
        existingTarefaDTO.setPrazo(newTarefaDTO.getPrazo());
        existingTarefaDTO.setDescricao(newTarefaDTO.getDescricao());
        existingTarefaDTO.setTitulo(newTarefaDTO.getTitulo());
        existingTarefaDTO.setPrioridade(newTarefaDTO.getPrioridade());
        existingTarefaDTO.setCategoria_id(newTarefaDTO.getCategoria_id());
    }

    private void updateCategoriaId(Tarefa tarefa, TarefaDTO tarefaDTO) {
        Optional.ofNullable(tarefa.getCategoria())
                .map(Categoria::getId)
                .ifPresent(tarefaDTO::setCategoria_id);
    }

    private void updateCategoria(Tarefa tarefa, TarefaDTO tarefaDTO) {
        Optional.ofNullable(tarefaDTO.getCategoria_id())
                .map(this::getCategoria)
                .ifPresent(tarefa::setCategoria);
    }
}
