package com.WorkFlow.categoria;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public List<CategoriaDTO> findAll() {
        return categoriaRepository.findAll()
                .stream()
                .map(CategoriaDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<CategoriaDTO> findById(Long id) {
        return categoriaRepository.findById(id).map(CategoriaDTO::new);
    }

    public CategoriaDTO save(CategoriaDTO newCategoriaDTO) {
        Categoria newCategoria = new Categoria(newCategoriaDTO);
        Categoria categoriaCreated = categoriaRepository.save(newCategoria);
        return new CategoriaDTO(categoriaCreated);
    }

    public Optional<CategoriaDTO> put(Long id, CategoriaDTO newCategoriaDTO) {
        return findById(id).map(existingCategoriaDTO -> {
            updateCategoriaDTO(existingCategoriaDTO, newCategoriaDTO);
            return save(existingCategoriaDTO);
        });
    }

    public boolean existsById(Long id) {
        return categoriaRepository.existsById(id);
    }

    public boolean delete(Long id) {
        boolean exist = existsById(id);
        if (exist) {
            categoriaRepository.deleteById(id);
        }
        return exist;
    }

    private void updateCategoriaDTO(CategoriaDTO existingCategoriaDTO, CategoriaDTO newCategoriaDTO) {
        existingCategoriaDTO.setNome(newCategoriaDTO.getNome());
        existingCategoriaDTO.setCor(newCategoriaDTO.getCor());
        existingCategoriaDTO.setIcone(newCategoriaDTO.getIcone());
    }
}
