package com.WorkFlow.tarefa;

import com.WorkFlow.category.Category;
import com.WorkFlow.exception.CategoryNotFoundException;
import com.WorkFlow.category.CategoryService;
import com.WorkFlow.exception.TarefaNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final CategoryService categoryService;

    public List<TarefaDTO> findAll() {
        return tarefaRepository.findAll()
                .stream()
                .map(this::toTarefaDTO)
                .toList();
    }

    public Optional<TarefaDTO> findById(Long id) {
        return tarefaRepository.findById(id).map(this::toTarefaDTO);
    }

    public List<TarefaDTO> findByCategoryId(Long categoryId) {
        return tarefaRepository.findByCategoryId(categoryId)
                .stream()
                .map(this::toTarefaDTO)
                .toList();
    }

    public TarefaDTO save(TarefaDTO newTarefaDTO) {
        Tarefa newTarefa = new Tarefa(newTarefaDTO);
        updateCategory(newTarefa, newTarefaDTO);

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

    private Category getCategory(Long id) {
        return categoryService.findById(id)
                .map(Category::new)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    private TarefaDTO toTarefaDTO(Tarefa tarefa) {
        if (tarefa == null) {
            throw new TarefaNotFoundException();
        }
        TarefaDTO tarefaDTO = new TarefaDTO(tarefa);
        updateCategoryId(tarefa, tarefaDTO);
        return tarefaDTO;
    }

    private void updateTarefaDTO(TarefaDTO existingTarefaDTO, TarefaDTO newTarefaDTO) {
        existingTarefaDTO.setStatus(newTarefaDTO.getStatus());
        existingTarefaDTO.setDeadline(newTarefaDTO.getDeadline());
        existingTarefaDTO.setDescription(newTarefaDTO.getDescription());
        existingTarefaDTO.setTitle(newTarefaDTO.getTitle());
        existingTarefaDTO.setPriority(newTarefaDTO.getPriority());
        existingTarefaDTO.setCategory_id(newTarefaDTO.getCategory_id());
    }

    private void updateCategoryId(Tarefa tarefa, TarefaDTO tarefaDTO) {
        Optional.ofNullable(tarefa.getCategory())
                .map(Category::getId)
                .ifPresent(tarefaDTO::setCategory_id);
    }

    private void updateCategory(Tarefa tarefa, TarefaDTO tarefaDTO) {
        Optional.ofNullable(tarefaDTO.getCategory_id())
                .map(this::getCategory)
                .ifPresent(tarefa::setCategory);
    }
}
