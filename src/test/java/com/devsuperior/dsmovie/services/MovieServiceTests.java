package com.devsuperior.dsmovie.services;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsmovie.tests.MovieFactory;

@ExtendWith(SpringExtension.class)
public class MovieServiceTests {
	
	@InjectMocks
	private MovieService service;
	
	@Mock
	private MovieRepository repository;
	
	private long existingMovieId, nonExistingMovieId;
	private String title;
	private MovieEntity movie;
	private MovieDTO movieDTO;
	//private PageImpl<MovieEntity> page;
	
	
	@BeforeEach
	void setUp() throws Exception{
		existingMovieId = 1L;
		nonExistingMovieId = 2L;
		title = "The Witcher";
		
		movie = MovieFactory.createMovieEntity();
		movieDTO = new MovieDTO(movie);
		
		Mockito.when(repository.findById(existingMovieId)).thenReturn(Optional.of(movie));
		Mockito.when(repository.findById(nonExistingMovieId)).thenReturn(Optional.empty());
		
	}
	
	@Test
	public void findAllShouldReturnPagedMovieDTO() {
	}
	
	@Test
	public void findByIdShouldReturnMovieDTOWhenIdExists() {
		
		MovieDTO result = service.findById(existingMovieId);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), existingMovieId);
		Assertions.assertEquals(result.getTitle(), movie.getTitle());
		
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class,() ->{
			service.findById(nonExistingMovieId);
		});
	}
	
	@Test
	public void insertShouldReturnMovieDTO() {
	}
	
	@Test
	public void updateShouldReturnMovieDTOWhenIdExists() {
	}
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
	}
	
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
	}
}
