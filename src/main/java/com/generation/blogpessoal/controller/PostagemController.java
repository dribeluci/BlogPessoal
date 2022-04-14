package com.generation.blogpessoal.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
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
import com.generation.blogpessoal.repository.PostagemRepository;
import com.generation.blogpessoal.repository.TemaRepository;

import ch.qos.logback.core.status.Status;

@RestController
@RequestMapping("/postagens")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PostagemController {
	
	@Autowired //injeção de dependência . Para funcionar em qualquer classe, tenho que colocar  bin
	private PostagemRepository repository;
	@Autowired TemaRepository temarepository;
	// usei o list porque eu tenho que retornar muitos valores, por isso foi usado o GetAll também.
	@GetMapping
	public ResponseEntity<List<Postagem>> GetAll (){
		return ResponseEntity.ok(repository.findAll());
	}
	
	@GetMapping("/{id}")//id entre colchetes é o valor como sendo variável. só posso utilizar essa estratégia com valores númericos
	public ResponseEntity <Postagem> GetById (@PathVariable long id){
	return repository.findById(id)
			.map(resp -> ResponseEntity.ok(resp))
			.orElse(ResponseEntity.notFound().build());
	// o map significa que todos os termos presente em uma lista
	// será analisado pela função, que neste caso será todos os id
	// existentes no banco de dados.
	//esse método acima é conhecido como lamba um método sem função que devo personalizar. 
	//todo esse comando é equivalente a um if como vemos no exemplo abaixo		
//	Optional <Postagem> resposta = repository.findById(id);
//	if (resposta.isPresent()) {
//		return ResponseEntity.ok(resposta);
//	} else {
//		return ResponseEntity.notFound().build();
	}
	@GetMapping("/titulo/{titulo}")
	public ResponseEntity<List<Postagem>> GetByTitulo(@PathVariable String titulo){
		return ResponseEntity.ok(repository.findAllByTituloContainingIgnoreCase(titulo));
	}
	// esse método abaixo é uma requsição do tipo post, ou seja adiciona algo
	@PostMapping
	public ResponseEntity<Postagem> postPostagem (@Valid @RequestBody Postagem postagem, Tema tema){
		if(temarepository.existsById(postagem.getTema().getId())) {
			return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(postagem));}
		else {
			return ResponseEntity.notFound().build();}
	}
	//O RequestBody serve para retornar o corpo, normalmente, dro front end.
	// o responseEnity retorna um código de resposta que são 200,300,etc.
	//body foi usado porque a resposta vai vir do corpo da requisição.
	// o método save cria um novo dado na tabela do banco de dados.
	// basicamente esse método acima funciona como o insert que fazemos no mysql.
	// @valid é um método específico do validation e retorna o erro mais especificamente.

	@PutMapping 
	public ResponseEntity<Postagem>putPostagem(@Valid @RequestBody Postagem postagem  ) {
		if(temarepository.existsById(postagem.getTema().getId())) {
		return repository.findById(postagem.getId()).map(resp -> ResponseEntity.status(HttpStatus.OK).body(
				repository.save(postagem))).orElse(ResponseEntity.notFound().build());}
	else {
		return ResponseEntity.notFound().build();}
}
	
	
// o comando acima evita que o put adicione dados, caso o id digitado não exista.	
	@DeleteMapping("/{id}") //MÉTODO QUE NÃO RETORNA NADA
	public ResponseEntity<Postagem> deletePostagem(@PathVariable Long id) {
		// essa é a forma que faz usando uma condição sendo que o comando existByID
		// retorna verdadeorp caso ID exista e caso não retorna falso.
		boolean resposta = repository.existsById(id);
		if(resposta) {
			repository.deleteById(id);
			return ResponseEntity.noContent().build();
		}
			else
				return ResponseEntity.notFound().build();}
	// poderia ter feito tudo que fiz em cima usando lambda que seria assim:
	/*
	 * @DeleteMapping("/{id}")
	public ResponseEntity<?> deletePostagem (@PathVariable Long id) {
		return repository.findById(id)
				.map(resposta ->{
					repository.deleteById(id);
					return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
				}})
		.orElse(ResponseEntity.notFound().build());
	 */
}
	
