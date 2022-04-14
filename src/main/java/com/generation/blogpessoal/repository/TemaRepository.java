package com.generation.blogpessoal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.blogpessoal.model.Tema;

// podemos perceber que o Jpa está trazendo a tabela tema criada no model, mas especificamente o Id, que é o único dado long.
public interface TemaRepository extends JpaRepository<Tema, Long> {
	
	public List<Tema> findAllByDescricaoContainingIgnoreCase( String descricao);

}