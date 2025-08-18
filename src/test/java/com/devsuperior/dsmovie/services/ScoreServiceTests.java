package com.devsuperior.dsmovie.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.dto.ScoreDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.entities.ScoreEntity;
import com.devsuperior.dsmovie.entities.UserEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.repositories.ScoreRepository;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsmovie.tests.ScoreFactory;

@ExtendWith(SpringExtension.class)
public class ScoreServiceTests {

	@InjectMocks
	private ScoreService service;

	@Mock
	private MovieRepository movieRepository;

	@Mock
	private ScoreRepository scoreRepository;

	@Mock
	private UserService userService;

	private ScoreDTO scoreDTO;
	private MovieEntity movie;
	private UserEntity user;

	@BeforeEach
	void setUp() {
		// Cria uma entidade de Score via factory. Ela já adiciona o score no
		// movie.getScores()
		ScoreEntity scoreEntity = ScoreFactory.createScoreEntity();

		// Pega movie e user da CHAVE COMPOSTA (ScorePK)
		movie = scoreEntity.getId().getMovie();
		user = scoreEntity.getId().getUser();

		// Cria um DTO válido
		scoreDTO = ScoreFactory.createScoreDTO();
	}

	@Test
	public void saveScoreShouldReturnMovieDTO() {
		// Mocks necessários
		when(movieRepository.findById(scoreDTO.getMovieId())).thenReturn(Optional.of(movie));
		when(userService.authenticated()).thenReturn(user);

		// Devolve a própria entidade passada no saveAndFlush
		when(scoreRepository.saveAndFlush(any(ScoreEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

		// Devolve o próprio filme no save (o service salva antes de retornar o DTO)
		when(movieRepository.save(any(MovieEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

		// Executa
		MovieDTO result = service.saveScore(scoreDTO);

		// Verificações
		Assertions.assertNotNull(result);
		Assertions.assertEquals(movie.getId(), result.getId());

		// Garante que as dependências foram chamadas
		verify(movieRepository).findById(scoreDTO.getMovieId());
		verify(scoreRepository).saveAndFlush(any(ScoreEntity.class));
		verify(movieRepository).save(any(MovieEntity.class));
	}

	@Test
	public void saveScoreShouldThrowResourceNotFoundExceptionWhenNonExistingMovieId() {
		// findById retorna vazio
		when(movieRepository.findById(anyLong())).thenReturn(Optional.empty());

		// Executa + Verifica exceção
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.saveScore(scoreDTO);
		});
	}
}