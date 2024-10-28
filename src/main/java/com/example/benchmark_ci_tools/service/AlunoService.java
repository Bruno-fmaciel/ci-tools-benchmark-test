package com.example.benchmark_ci_tools.service;

import com.example.benchmark_ci_tools.model.Aluno;
import com.example.benchmark_ci_tools.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {
    @Autowired
    private AlunoRepository alunoRepository;

    public List<Aluno> listarTodos() {
        return alunoRepository.findAll();
    }

    public Optional<Aluno> buscarPorId(Long id) {
        return alunoRepository.findById(id);
    }

    public Aluno salvar(Aluno aluno) {
        return alunoRepository.save(aluno);
    }

    public Aluno atualizarAluno(Long id, Aluno alunoAtualizado) {
        Optional<Aluno> alunoOptional = alunoRepository.findById(id);

        if (alunoOptional.isPresent()) {
            Aluno alunoExistente = alunoOptional.get();
            alunoExistente.setNome(alunoAtualizado.getNome());
            alunoExistente.setIdade(alunoAtualizado.getIdade());
            alunoExistente.setNota1(alunoAtualizado.getNota1());
            alunoExistente.setNota2(alunoAtualizado.getNota2());
            return alunoRepository.save(alunoExistente);
        } else {
            throw new RuntimeException("Aluno não encontrado");
        }
    }

    public void excluir(Long id) {
        alunoRepository.deleteById(id);
    }
}
