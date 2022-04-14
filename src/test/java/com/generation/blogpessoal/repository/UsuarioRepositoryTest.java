package com.generation.blogpessoal.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.generation.blogpessoal.model.Usuario;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
// a anotação de cima é utilizada para mostrar que é um teste e o que está dentro dos parênteses
// é usado para não utilizar a porta padrão do spring que no caso seria a 8080.

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
/* a anoatação acima indica que o Ciclo de vida da Classe de Teste será
por Classe, ou seja, será utilizado somente nessa classe.
 */
public class UsuarioRepositoryTest {
    
	@Autowired
	private UsuarioRepository usuarioRepository;
	// injetando o repositório de usuário para utilizar as informações para testar.
	
	@BeforeAll
	void start(){
    // a anotação beforeall A anotação @BeforeAll indica que o método deve ser executado
	//uma única vez antes de todos os métodos da classe, para criar
	//algumas pré-condições necessárias para todos os testes (criar
    //objetos, por exemplo).
		 
		usuarioRepository.deleteAll();// apaga tudo do banco de dados da parte de usuário.
		// aqui estão registrando 4 novos  usuários no nosso banco de dados.
		usuarioRepository.save(new Usuario(0L, "João da Silva", "joao@email.com.br", "13465278", "https://i.imgur.com/FETvs2O.jpg"));
		
		usuarioRepository.save(new Usuario(0L, "Manuela da Silva", "manuela@email.com.br", "13465278", "https://i.imgur.com/NtyGneo.jpg"));
		
		usuarioRepository.save(new Usuario(0L, "Adriana da Silva", "adriana@email.com.br", "13465278", "https://i.imgur.com/mB3VM2N.jpg"));

        usuarioRepository.save(new Usuario(0L, "Paulo Antunes", "paulo@email.com.br", "13465278", "https://i.imgur.com/JR7kUFU.jpg"));

	}

	@Test // sempre que for criar um novo teste eu tenho que usar essa anotação para o Spring saber disso.
	@DisplayName("Retorna 1 usuario") // essa anotaçaõ é para dar o nome a um teste, neste caso ele tem que 
	// retornar um usuário.
	public void deveRetornarUmUsuario() {
		Optional<Usuario> usuario = usuarioRepository.findByUsuario("joao@email.com.br");


		assertTrue(usuario.get().getUsuario().equals("joao@email.com.br"));
		/*  o método acima vai ver se a afirmação é verdadeira, se for verdadeira o teste passa
		 * se não tem algum problema.
	
	}

	@Test
	@DisplayName("Retorna 3 usuarios")
	public void deveRetornarTresUsuarios() {

		/**
		 Executa o método findAllByNomeContainingIgnoreCase para buscar todos os usuarios com silva no nome. 
		 */
		List<Usuario> listaDeUsuarios = usuarioRepository.findAllByNomeContainingIgnoreCase("Silva");

		// O método assertEquals verifica se dois valores são iguais.
		assertEquals(3, listaDeUsuarios.size());

	
		// esses 3 "métodos" abaixo vão garantir se o teste encontrou esses nomes com sIlva,
		// caso não de certo significa que o teste falhou se funcionar significa que está tudo ok.
		assertTrue(listaDeUsuarios.get(0).getNome().equals("João da Silva"));

		assertTrue(listaDeUsuarios.get(1).getNome().equals("Manuela da Silva"));

		assertTrue(listaDeUsuarios.get(2).getNome().equals("Adriana da Silva"));
		
	}

	@AfterAll // essa anotação significa indica que todos os testes devem ser executados antes.
	public void end() {
		usuarioRepository.deleteAll(); // essa função end vai apagar todos os dados registrados no banco
		// de dados durante esse teste.
	}
	
}
