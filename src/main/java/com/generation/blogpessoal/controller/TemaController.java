package com.generation.blogpessoal.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.model.Tema;
import com.generation.blogpessoal.repository.TemaRepository;

@RestController
@RequestMapping("/tema")
@CrossOrigin (origins = "*", allowedHeaders = "*")
public class TemaController {
	
	@Autowired // aqui eu fiz a injeção dos dados do banco de dados para utilizar.
	private TemaRepository repository;
	
	@GetMapping
	public ResponseEntity<List<Tema>> getAll (){ // esse comando é para retornar a lista de temas existente no banco de dados.
		return ResponseEntity.ok(repository.findAll());
	}
	
	@GetMapping("/{id}") // esse comando é para da um retorno para requição so usuário.
	public ResponseEntity <Tema> getById (@PathVariable long id){
		return repository.findById(id).map(resp -> ResponseEntity.ok(resp))
				.orElse(ResponseEntity.notFound().build());
	}
	
	@GetMapping("/descricao/{descricao}") //aqui é para retornar o nome
	public ResponseEntity <List<Tema>> getByNome (@PathVariable String nome){
		return ResponseEntity.ok(repository.findAllByDescricaoContainingIgnoreCase(nome));
	}
	
	@PostMapping // esse comando é para adicionar dados no sistema ou no banco de dados.
	public ResponseEntity <Tema> posttema (@RequestBody Tema tema){
		return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(tema));
	}
	
	

	
	
	
	
	
	@PutMapping // comando para atualizar os dados
	public ResponseEntity<Tema>puttema(@Valid @RequestBody Tema tema) {
		return repository.findById(tema.getId()).map(resp -> ResponseEntity.status(HttpStatus.OK).body(
			repository.save(tema))).orElse(ResponseEntity.notFound().build());}
	
	
	
	@DeleteMapping ("/{id}") // comando para deletar um dado de acordo com o id.
	public ResponseEntity<Postagem> deleteTema(@PathVariable Long id) {
		boolean resposta = repository.existsById(id);
		if(resposta) {
			repository.deleteById(id);
			return ResponseEntity.noContent().build();
		}
			else
				return ResponseEntity.notFound().build();}
	}

