package com.example.benchmark_ci_tools.controller;

import com.example.benchmark_ci_tools.model.Aluno;
import com.example.benchmark_ci_tools.repository.AlunoRepository;
import com.example.benchmark_ci_tools.service.AlunoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;


import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AlunoControllerTest {

    @InjectMocks
    private AlunoController alunoController;

    @Mock
    private AlunoService alunoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void criarAluno() {
        Aluno aluno = new Aluno("Bruno", 27, 8.0, 8.5);
        when(alunoService.salvar(any(Aluno.class))).thenReturn(aluno);

        Aluno resultado = alunoController.criarAluno(aluno);

        assertEquals(aluno.getNome(), resultado.getNome());
        verify(alunoService, times(1)).salvar(aluno);
    }

    @Test
    public void listarTodosAlunos() {
        Aluno aluno1 = new Aluno("Bruno", 27, 8.0, 8.5);
        Aluno aluno2 = new Aluno("Marina", 25, 10.0, 9.5);

        when(alunoService.listarTodos()).thenReturn(Arrays.asList(aluno1, aluno2));

        var resultado = alunoController.listarAlunos();

        assertEquals(2, resultado.size());
        verify(alunoService, times(1)).listarTodos();
    }

    @Test
    public void buscarAlunoPorIdInexistente() {
        when(alunoService.buscarPorId(1L)).thenReturn(Optional.empty());

        ResponseEntity<Aluno> resultado = alunoController.buscarAlunoPorID(1L);

        assertEquals(ResponseEntity.notFound().build(), resultado);
        verify(alunoService, times(1)).buscarPorId(1L);
    }

    @Test
    public void atualizarAluno() {
        Aluno alunoExistente = new Aluno("Bruno", 27, 8.0, 8.5);
        Aluno alunoAtualizado = new Aluno("Marina", 25, 10.0, 9.5);

        when(alunoService.buscarPorId(1L)).thenReturn(Optional.of(alunoExistente));
        when(alunoService.atualizarAluno(1L, alunoAtualizado)).thenReturn(alunoAtualizado);

        ResponseEntity<Aluno> resultado = alunoController.atualizarAluno(1L, alunoAtualizado);

        assertEquals(ResponseEntity.ok(alunoAtualizado), resultado);
        verify(alunoService, times(1)).atualizarAluno(1L, alunoAtualizado);
    }

    @Test
    public void deletarAlunoInexistente() {
        when(alunoService.buscarPorId(1L)).thenReturn(Optional.of(new Aluno()));

        ResponseEntity<Void> resultado = alunoController.deletarAluno(1L);

        assertEquals(ResponseEntity.noContent().build(), resultado);
        verify(alunoService, times(1)).excluir(1L);

    }

    @Test
    public void deletarAluno() {
        when(alunoService.buscarPorId(1L)).thenReturn(Optional.empty());
        ResponseEntity<Void> resultado = alunoController.deletarAluno(1L);

        assertEquals(ResponseEntity.notFound().build(), resultado);
        verify(alunoService, never()).excluir(anyLong());
    }
}
