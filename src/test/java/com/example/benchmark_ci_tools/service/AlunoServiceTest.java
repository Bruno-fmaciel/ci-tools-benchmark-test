package com.example.benchmark_ci_tools.service;

import com.example.benchmark_ci_tools.model.Aluno;
import com.example.benchmark_ci_tools.repository.AlunoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AlunoServiceTest {

    @InjectMocks
    private AlunoService alunoService;

    @Mock
    private AlunoRepository alunoRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void listarTodosAlunos() {
        Aluno aluno1 = new Aluno("Marina", 25, 10.0, 9.0);
        Aluno aluno2 = new Aluno("Bruno", 27, 8.0, 8.5);

        when(alunoRepository.findAll()).thenReturn(Arrays.asList(aluno1, aluno2));

        var alunos = alunoService.listarTodos();

        assertNotNull(alunos);
        assertEquals(2, alunos.size());
        verify(alunoRepository, times(1)).findAll();
    }

    @Test
    public void buscarPorID() {
        Aluno aluno = new Aluno(1L,"Marina", 25, 10.0, 9.0);

        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));

        Optional<Aluno> resultado = alunoService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Marina", resultado.get().getNome());
        verify(alunoRepository, times(1)).findById(1L);
    }

    @Test
    public void salvar() {
        Aluno aluno = new Aluno("Marina", 25, 10.0, 9.0);

        when(alunoRepository.save(any(Aluno.class))).thenReturn(aluno);

        Aluno resultado = alunoService.salvar(aluno);

        assertNotNull(resultado);
        assertEquals("Marina", resultado.getNome());
        verify(alunoRepository, times(1)).save(aluno);
    }

    @Test
    public void atualizarAluno() {
        Aluno alunoExistente = new Aluno(1L,"Marina", 25, 10.0, 9.0);
        Aluno alunoAtualizado = new Aluno("Bruno", 27, 8.0, 8.5);

        when(alunoRepository.findById(1L)).thenReturn(Optional.of(alunoExistente));
        when(alunoRepository.save(any(Aluno.class))).thenReturn(alunoAtualizado);

        Aluno resultado = alunoService.atualizarAluno(1L, alunoAtualizado);

        assertNotNull(resultado);
        assertEquals("Bruno", resultado.getNome());
        verify(alunoRepository, times(1)).findById(1L);
        verify(alunoRepository, times(1)).save(any(Aluno.class));
    }

    @Test
    public void removerAluno() {
        Aluno aluno = new Aluno(1L,"Marina", 25, 10.0, 9.0);

        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));

        alunoService.excluir(1L);

        verify(alunoRepository, times(1)).deleteById(1L);
    }

    @Test
    public void atualizarAlunoExcecao() {
        Aluno alunoAtualizado = new Aluno(1L,"Marina", 25, 10.0, 9.0);

        when(alunoRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            alunoService.atualizarAluno(1L, alunoAtualizado);
        });

        assertEquals("Aluno não encontrado", thrown.getMessage());

    }
}
