package com.devsuperior.dsmovie.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

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
	private ScoreService scoreService;

	@Mock
	private MovieRepository movieRepository;

	@Mock
	private ScoreRepository scoreRepository;

	@Mock
	private UserService userService;

	@Test
	public void saveScoreShouldReturnMovieDTO() {
		// Arrange
		ScoreDTO dto = ScoreFactory.createScoreDTO();
		MovieEntity movie = new MovieEntity();
		movie.setId(dto.getMovieId());
		movie.setScore(0.0);
		movie.setCount(0);

		UserEntity user = new UserEntity();
		ScoreEntity score = new ScoreEntity();
		score.setMovie(movie);
		score.setUser(user);
		score.setValue(dto.getScore());

		when(userService.authenticated()).thenReturn(user);
		when(movieRepository.findById(dto.getMovieId())).thenReturn(Optional.of(movie));
		when(scoreRepository.saveAndFlush(any())).thenReturn(score);
		when(movieRepository.save(any())).thenReturn(movie);

		// Act
		MovieDTO result = scoreService.saveScore(dto);

		// Assert
		assertNotNull(result);
		assertEquals(dto.getMovieId(), result.getId());
		verify(scoreRepository).saveAndFlush(any());
		verify(movieRepository).save(any());

	}

	@Test
	public void saveScoreShouldThrowResourceNotFoundExceptionWhenNonExistingMovieId() {

		ScoreDTO dto = ScoreFactory.createScoreDTO();
		when(movieRepository.findById(dto.getMovieId())).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> {
			scoreService.saveScore(dto);
		});

	}
}
